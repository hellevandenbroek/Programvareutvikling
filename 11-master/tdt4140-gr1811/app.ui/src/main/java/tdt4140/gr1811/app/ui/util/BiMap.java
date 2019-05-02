package tdt4140.gr1811.app.ui.util;

import java.util.HashMap;
import java.util.Map;

/**
 * BBidirectional map backed internally by two maps
 * 
 * @param <K>
 * @param <V>
 */
public class BiMap<K, V> {
	
	private Map<K, V> map1 = new HashMap<>();
	private Map<V, K> map2 = new HashMap<>();
	
	public void put(K a, V b) {
		map1.put(a, b);
		map2.put(b, a);
	}
	
	public V getByK(K item) {
		return map1.get(item);
	}
	
	public K getByV(V item) {
		return map2.get(item);
	}
	
	public void removeByA(K item) {
		V value = map1.remove(item);
		map2.remove(value);
	}
	
	public void removeByB(V item) {
		K value = map2.remove(item);
		map1.remove(value);
	}
}
