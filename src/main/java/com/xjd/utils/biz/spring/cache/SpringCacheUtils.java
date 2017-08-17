package com.xjd.utils.biz.spring.cache;

import java.util.*;

import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * Spring Cache 工具类
 * 依赖Slf4j
 *
 * @author elvis.xu
 * @since 2017-08-16 11:14
 */
@Slf4j
public abstract class SpringCacheUtils {


	public static <T> T getCachedObject(CacheManager cacheManager, String cacheName, Object key, Class<T> clazz) {
		if (cacheManager == null) {
			log.debug("cacheManager is null");
			return null;
		}
		return getCachedObject(cacheManager.getCache(cacheName), key, clazz);
	}

	/**
	 * 从Cache中获取key对应的值对象
	 * @param cache
	 * @param key
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> T getCachedObject(Cache cache, Object key, Class<T> clazz) {
		if (cache == null) {
			log.debug("cache is null");
			return null;
		}
		return cache.get(key, clazz);
	}

	public static <T> T getCachedObject(CacheManager cacheManager, String cacheName, Object key, Class<T> clazz, ValueLoader<Object, T> valueLoader) {
		if (cacheManager == null) {
			log.debug("cacheManager is null");
		}
		return getCachedObject(cacheManager == null ? null : cacheManager.getCache(cacheName), key, clazz, valueLoader);
	}

	/**
	 * 从Cache中获取key对应的值对象, 如果获取不到则使用valueLoader加载值对象返回, 并将值对象放入cache中
	 * @param cache
	 * @param key
	 * @param clazz
	 * @param valueLoader
	 * @param <T>
	 * @return
	 */
	public static <T> T getCachedObject(Cache cache, Object key, Class<T> clazz, ValueLoader<Object, T> valueLoader) {
		T t = null;
		if (cache == null) {
			log.debug("cache is null");
		} else {
			t = cache.get(key, clazz);
		}
		if (t == null) {
			if (valueLoader == null) {
				log.debug("valueLoader is null");
			} else {
				t = valueLoader.load(key);
				if (t != null && cache != null) {
					cache.put(key, t);
				}
			}
		}
		return t;
	}

	public static <T> Collection<T> getCachedObjects(CacheManager cacheManager, String cacheName, Collection<Object> keys, Class<T> clazz, ValueLoader<Object, T> valueLoader) {
		if (cacheManager == null) {
			log.debug("cacheManager is null");
		}
		return getCachedObjects(cacheManager == null ? null : cacheManager.getCache(cacheName), keys, clazz, valueLoader);

	}

	/**
	 * 从Cache中获取keys对应的值对象列表, 对于获取不到的key使用valueLoader加载值对象返回, 并将值对象放入cache中
	 * @param cache
	 * @param keys
	 * @param clazz
	 * @param valueLoader
	 * @param <T>
	 * @return
	 */
	public static <T> Collection<T> getCachedObjects(Cache cache, Collection<Object> keys, Class<T> clazz, ValueLoader<Object, T> valueLoader) {
		if (keys == null || keys.isEmpty()) {
			log.debug("keys are empty");
			return new ArrayList<T>(0);
		}
		Set<Object> noRepeatKeys = new HashSet<>(keys);
		List<T> results = new ArrayList<T>(noRepeatKeys.size());
		noRepeatKeys.forEach(key -> {
			T t = getCachedObject(cache, key, clazz, valueLoader);
			if (t != null) {
				results.add(t);
			}
		});
		return results;
	}


	public static <T> Collection<T> getCachedObjects(CacheManager cacheManager, String cacheName, Collection<Object> keys, Class<T> clazz) {
		if (cacheManager == null) {
			log.debug("cacheManager is null");
		}
		return getCachedObjects(cacheManager == null ? null : cacheManager.getCache(cacheName), keys, clazz);
	}

	/**
	 * 从Cache中获取keys对应的值对象列表
	 * @param cache
	 * @param keys
	 * @param clazz
	 * @param <T>
	 * @return
	 */
	public static <T> Collection<T> getCachedObjects(Cache cache, Collection<Object> keys, Class<T> clazz) {
		return getCachedObjects(cache, keys, clazz, null, null);
	}

	public static <T> Collection<T> getCachedObjects(CacheManager cacheManager, String cacheName, Collection<Object> keys, Class<T> clazz, ValuesLoader<Object, T> valuesLoader, KeyParser<T> keyParser) {
		if (cacheManager == null) {
			log.debug("cacheManager is null");
		}
		return getCachedObjects(cacheManager == null ? null : cacheManager.getCache(cacheName), keys, clazz, valuesLoader, keyParser);
	}

	/**
	 * 从Cache中获取keys对应的值对象列表, 对于获取不到的key列表, 使用valuesLoader进行加载返回, 并把加载的值对象列表通过keyParser提取出键放入缓存中
	 * @param cache
	 * @param keys
	 * @param clazz
	 * @param valuesLoader
	 * @param keyParser
	 * @param <T>
	 * @return
	 */
	public static <T> Collection<T> getCachedObjects(Cache cache, Collection<Object> keys, Class<T> clazz, ValuesLoader<Object, T> valuesLoader, KeyParser<T> keyParser) {
		if (keys == null || keys.isEmpty()) {
			log.debug("keys are empty");
			return new ArrayList<T>(0);
		}

		if (cache == null) {
			log.debug("cache is null");
			if (valuesLoader == null) {
				log.debug("valuesLoader is null");
				return new ArrayList<T>(0);
			} else {
				return valuesLoader.load(new HashSet<>(keys));
			}
		}

		Set<Object> noRepeatKeys = new HashSet<>(keys);
		List<Object> noHitKeys = new LinkedList<>();
		List<T> results = new ArrayList<T>(noRepeatKeys.size());

		noRepeatKeys.forEach(key -> {
			T t = cache.get(key, clazz);
			if (t != null) {
				results.add(t);
			} else {
				noHitKeys.add(key);
			}
		});

		if (!noHitKeys.isEmpty()) {
			if (valuesLoader == null) {
				log.debug("valuesLoader is null");
			} else {
				Collection<T> loadResults = valuesLoader.load(noHitKeys);
				if (loadResults != null && !loadResults.isEmpty()) {
					results.addAll(loadResults);

					if (keyParser == null) {
						log.debug("keyParser is null");
					} else {
						loadResults.forEach(r -> {
							cache.put(keyParser.parseKey(r), r);
						});
					}
				}
			}
		}
		return results;
	}

	public static void put(CacheManager cacheManager, String cacheName, Object key, Object value) {
		if (cacheManager == null) {
			log.debug("cacheManager is null");
			return;
		}
		put(cacheManager.getCache(cacheName), key, value);
	}

	public static void put(Cache cache, Object key, Object value) {
		if (cache == null) {
			log.debug("cache is null");
			return;
		}
		cache.put(key, value);
	}

	public static Object putIfAbsent(CacheManager cacheManager, String cacheName, Object key, Object value) {
		if (cacheManager == null) {
			log.debug("cacheManager is null");
			return null;
		}
		return putIfAbsent(cacheManager.getCache(cacheName), key, value);
	}

	public static Object putIfAbsent(Cache cache, Object key, Object value) {
		if (cache == null) {
			log.debug("cache is null");
			return null;
		}
		Cache.ValueWrapper valueWrapper = cache.putIfAbsent(key, value);
		if (valueWrapper != null) {
			return valueWrapper.get();
		}
		return null;
	}

	public static void evict(CacheManager cacheManager, String cacheName, Object key) {
		if (cacheManager == null) {
			log.debug("cacheManager is null");
			return;
		}
		evict(cacheManager.getCache(cacheName), key);
	}

	public static void evict(Cache cache, Object key) {
		if (cache == null) {
			log.debug("cache is null");
			return;
		}
		cache.evict(key);
	}

	public static void clear(CacheManager cacheManager, String cacheName) {
		if (cacheManager == null) {
			log.debug("cacheManager is null");
			return;
		}
		clear(cacheManager.getCache(cacheName));
	}

	public static void clear(Cache cache) {
		if (cache == null) {
			log.debug("cache is null");
			return;
		}
		cache.clear();
	}


	public static interface ValueLoader<K, V> {
		V load(K k);
	}

	public static interface ValuesLoader<K, V> extends ValueLoader<Collection<K>, Collection<V>> {

	}

	public static interface KeyParser<E> {
		Object parseKey(E e);
	}

}
