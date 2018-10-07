package ru.otus.myGCwatch;

import java.lang.management.ManagementFactory;
import java.util.*;

class Benchmark implements BenchmarkMBean {
    private volatile int size = 0;
    private int TimeInterval = 10;//как часто выводить метрику
    private int TimeOOM = 40;//когда упасть с ООМ
    private Timer mTimer = new Timer();
    private Timer mTimerOOM = new Timer();
    private List<String> OOMlist = new ArrayList<>();
    private long freeMemBegin;
    private long freeMemCurrent;
    private Long freeMemDelta;
    private long sleepTime;
    private Long sleepTimeDelta;
    private long timeBegin;
    private long timeCurrent;

    public long getWorkingTime(){
        return timeCurrent-timeBegin;
    };

    @SuppressWarnings("InfiniteLoopStatement")
    void run() throws InterruptedException {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {GC.printGCMetrics();}
        };
        mTimer.scheduleAtFixedRate( timerTask,0, TimeInterval*1000);//запускаем таймер и раз в TimeInterval показываем статтистику по GC
        TimerTask timerTaskOOM = new TimerTask() {
            @Override
            public void run() {
                Object[] array = new Object[500000000];
                for (int i = 0; i < 500000000; i++) {
                    array[i] = new String(new char[0]);
                }
            }
        };
        mTimerOOM.schedule( timerTaskOOM, TimeOOM*1000);//запускаем таймер и раз в TimeInterval показываем статтистику по GC
        timeBegin = System.currentTimeMillis();
        while (true) {
            int local = size;
            //организуем утчечку памяти
            freeMemBegin = Runtime.getRuntime().freeMemory();
            Thread.sleep(200);
            for (int i = 0; i < local; i++) {
                OOMlist.add("OOM");
            }
            /** Не заработало оценивать оставшееся время и свободное место и регулировать длину Sleep
             * показатель свободного места живет своей жизнью
             * Thread.sleep(200);
            freeMemCurrent = Runtime.getRuntime().freeMemory();
            timeCurrent = System.currentTimeMillis();
            freeMemDelta = freeMemBegin-freeMemCurrent;
            sleepTimeDelta = TimeOOM*1000/(timeCurrent-timeBegin);
            System.out.println(freeMemCurrent+"--"+freeMemBegin);
            System.out.println(freeMemDelta+"//"+sleepTimeDelta);

            System.out.println("Working Time:"+getWorkingTime()+" ms");
             */
            Thread.sleep(500);

        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }
}