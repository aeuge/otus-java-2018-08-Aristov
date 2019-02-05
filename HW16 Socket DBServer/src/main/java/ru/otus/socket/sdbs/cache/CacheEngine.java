package ru.otus.socket.sdbs.cache;

public interface CacheEngine<K, V> {

    void put(CacheElement<K, V> element);

    CacheElement<K, V> get(K key);

    int getHitCount();

    int getMissCount();

    void dispose();

    int getCount();
}
