package com.androdome.iadventure.utils;

import java.util.List;

public interface Binding<K, V> {

	public boolean addBinding(K key, V value);
	public List<K> getKeyList();
	public List<V> getValueList();
	public BindingObject<K, V> getBindingObject(int i);
	public boolean removeBinding(K key, V value);
	public boolean removeBinding(int i);
}
