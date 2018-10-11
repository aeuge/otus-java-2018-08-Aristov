package ru.otus.myCache;

/**
 * Created by Aeuge.
 */
public class MainC {

    public static void main(String[] args) throws InterruptedException {
        new MainC().CacheExample();
    }

    private void CacheExample() throws InterruptedException {
        int size = 6_500_000;
        CacheEngine<Integer, String> cache = new CacheEngineImpl<>(size, 3000000, 0, false);

        for (int i = 0; i < size; i++) {
            cache.put(new MyElement<>(i, "String: " + i));
        }

        for (int i = 0; i < size; i++) {
            MyElement<Integer, String> element = cache.get(i);
            //System.out.println("String for " + i + ": " + (element != null ? element.getValue() : "null"));
        }

        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());

        Thread.sleep(1000);
        MyElement<Integer, String> elementtemp = cache.get(2);

        System.gc();
        Thread.sleep(30000);

        for (int i = 0; i < size; i++) {
            MyElement<Integer, String> element = cache.get(i);
            //System.out.println("String for " + i + ": " + (element != null ? element.getValue() : "null"));
        }

        System.out.println("Cache hits: " + cache.getHitCount());
        System.out.println("Cache misses: " + cache.getMissCount());

        cache.dispose();
    }

}
