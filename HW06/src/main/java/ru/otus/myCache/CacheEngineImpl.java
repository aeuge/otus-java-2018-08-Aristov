package ru.otus.myCache;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

public class CacheEngineImpl<K, V> implements CacheEngine<K, V> {
    private static final int TIME_THRESHOLD_MS = 100;

    private final int maxElements;
    private final long lifeTimeMs;
    private final long idleTimeMs;
    private final boolean isEternal;

    private final Map<K, MyElement<K, V>> elements = new LinkedHashMap<>();
    private final Timer timer = new Timer();

    private int hit = 0;
    private int miss = 0;

    CacheEngineImpl(int maxElements, long lifeTimeMs, long idleTimeMs, boolean isEternal) {
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
        this.isEternal = lifeTimeMs == 0 && idleTimeMs == 0 || isEternal;
        if (!isEternal) {
            if (lifeTimeMs != 0) {
                TimerTask lifeTimerTask = getTimerTask(lifeElement -> lifeElement.getCreationTime() + lifeTimeMs);
                timer.schedule(lifeTimerTask, lifeTimeMs,lifeTimeMs);
            }
            if (idleTimeMs != 0) {
                TimerTask idleTimerTask = getTimerTask(idleElement -> idleElement.getLastAccessTime() + idleTimeMs);
                timer.schedule(idleTimerTask, idleTimeMs, idleTimeMs);
            }
        }
    }

    public void put(MyElement<K, V> element) {
        if (elements.size() == maxElements) {
            K firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
        }

        K key = element.getKey();
        elements.put(key, element);

    }

    public MyElement<K, V> get(K key) {
        MyElement<K, V> element = elements.get(key);
        if (element != null) {
            hit++;
            element.setAccessed();
        } else {
            miss++;
        }
        return element;
    }

    public int getHitCount() {
        return hit;
    }

    public int getMissCount() {
        return miss;
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    private TimerTask getTimerTask(Function<MyElement<K, V>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                elements.keySet().removeIf((K)->{
                    MyElement<K, V> element = elements.get(K);
                    if (element == null || isT1BeforeT2(timeFunction.apply(element), System.currentTimeMillis())) {
                        System.out.println("deleting element with key "+K.toString());
                        return true;
                    } else {
                        System.out.println("cache element with key "+K.toString()+" is alive");
                        return false;
                    }
                });
                /**DateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
                formatter.setTimeZone(TimeZone.getTimeZone("MSK"));
                elements.forEach((K,V)->{MyElement<K, V> element = elements.get(K);
                    if (element == null || isT1BeforeT2(timeFunction.apply(element), System.currentTimeMillis())) {
                        //System.out.println("last access time "+formatter.format(element.getLastAccessTime()));
                        System.out.println("deleting element with key "+K.toString());
                        //elements.remove(K);
                        //this.cancel();
                    }
                });
                 */
            }
        };
    }


    private boolean isT1BeforeT2(long t1, long t2) {
        /**DateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
        formatter.setTimeZone(TimeZone.getTimeZone("MSK"));
        System.out.println("comparing "+formatter.format(t1)+" and"+formatter.format(t2));
        */
        return t1 < t2 + TIME_THRESHOLD_MS;
    }
}
