package edu.brown.cs.student.main.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import java.util.concurrent.TimeUnit;

public class ACSDataCache {

  private LoadingCache<String, Object> cache;

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

  public Object getIfPresent(String key) {
      Object value = cache.getIfPresent(key);
      return value;
  }

  public void put(String key, Object value) {
      cache.put(key, value);
  }
}
