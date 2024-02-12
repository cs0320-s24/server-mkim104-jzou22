package edu.brown.cs.student.main.cache;

import java.util.Optional;

public interface Cache<K, V> {
  void put(K key, V value);

  Optional<V> getIfPresent(K key);

  void invalidate(K key);

  void invalidateAll();
}
