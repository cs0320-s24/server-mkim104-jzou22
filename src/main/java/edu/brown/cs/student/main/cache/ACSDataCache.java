package edu.brown.cs.student.main.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import java.util.concurrent.TimeUnit;
/**
 * This class represents a cache for storing ACS data.
 */
public class ACSDataCache {

  private LoadingCache<String, Object> cache;
    /**
     * Constructs an ACSDataCache with the specified maximum size, expiration duration, and time unit.
     *
     * @param maxSize                The maximum size of the cache.
     * @param expireAfterWriteDuration The duration after which entries expire after being written.
     * @param unit                   The time unit for the expiration duration.
     */
  public ACSDataCache(long maxSize, long expireAfterWriteDuration, TimeUnit unit) {
    cache =
        CacheBuilder.newBuilder()
            .maximumSize(maxSize)
            .expireAfterWrite(expireAfterWriteDuration, unit)
            .removalListener(
                (RemovalListener<String, Object>)
                    notification ->
                        System.out.println(
                            "Removed: "
                                + notification.getKey()
                                + ", cause: "
                                + notification.getCause()))
            .build(
                new CacheLoader<String, Object>() {
                  @Override
                  public Object load(String key) {
                    throw new UnsupportedOperationException("Loading not supported");
                  }
                });
  }
    /**
     * Retrieves the value associated with the specified key, if present in the cache.
     *
     * @param key The key whose associated value is to be retrieved.
     * @return The value associated with the specified key, or null if no mapping exists for the key.
     */
  public Object getIfPresent(String key) {
      Object value = cache.getIfPresent(key);
      return value;
  }
    /**
     * Associates the specified value with the specified key in the cache.
     *
     * @param key   The key with which the specified value is to be associated.
     * @param value The value to be associated with the specified key.
     */
  public void put(String key, Object value) {
      cache.put(key, value);
  }
}
