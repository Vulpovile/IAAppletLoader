package com.androdome.iadventure.utils;

public class BindingObject<K, V> {
	public final K key;
	public final V value;
	BindingObject(K key,V value)
	{
		this.key = key;
		this.value = value;
	}
	
}
