package edu.brown.cs.student.main.cache;

import java.util.Optional;

/**
 * This interface represents a generic cache for storing key-value pairs.
 *
 * @param <K> The type of keys stored in the cache.
 * @param <V> The type of values stored in the cache.
 */
public interface Cache<K, V> {
  void put(K key, V value);

  Optional<V> getIfPresent(K key);

  void invalidate(K key);

  void invalidateAll();
}
