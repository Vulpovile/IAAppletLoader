package com.androdome.iadventure.utils;

import java.util.ArrayList;
import java.util.List;

public class ArrayListBinding<K, V> implements Binding<K, V> {

	private ArrayList<K> keys = new ArrayList<K>();
	private ArrayList<V> values = new ArrayList<V>();
	@Override
	public BindingObject<K,V> getBindingObject(int i) {
		if(i < 0 || i > keys.size()-1)
			return null;
		else return new BindingObject<K,V>(keys.get(i), values.get(i));
	}
	
	@Override
	public List<K> getKeyList() {
		return keys;
	}

	@Override
	public synchronized boolean addBinding(K key, V value) {
		for(int i = 0; i < keys.size(); i++)
		{
			if(keys.get(i) == key && values.get(i) == value)
				return false;
		}
		keys.add(key);
		values.add(value);
		return true;
	}
	
	@Override
	public synchronized boolean removeBinding(K key, V value) {
		for(int i = 0; i < keys.size(); i++)
		{
			if(keys.get(i) == key && values.get(i) == value)
			{
				keys.remove(i);
				values.remove(i);
				return true;
			}
		}
		return false;
	}

	@Override
	public List<V> getValueList() {
		return values;
	}

	@Override
	public boolean removeBinding(int i) {
		if(i < 0 || i > keys.size()-1)
			return false;
		keys.remove(i);
		values.remove(i);
		return true;
	}

}
