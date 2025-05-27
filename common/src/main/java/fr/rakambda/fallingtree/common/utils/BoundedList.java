package fr.rakambda.fallingtree.common.utils;

import java.util.LinkedList;

public class BoundedList<T> extends LinkedList<T> {
	private final int maxSize;
	
	public BoundedList(int maxSize) {
		this.maxSize = maxSize;
	}
	
	@Override
	public boolean add(T element) {
		while (size() > maxSize) {
			super.removeFirst(); // Evict the oldest element
		}
		return super.add(element);
	}
	
	@Override
	public void add(int index, T element) {
		while (size() > maxSize) {
			super.removeFirst(); // Still evict from the beginning to maintain "oldest"
		}
		super.add(index, element);
	}
	
	@Override
	public boolean addAll(java.util.Collection<? extends T> c) {
		while (size() > maxSize) {
			super.removeFirst();
		}
		return super.addAll(c);
	}
	
	@Override
	public boolean addAll(int index, java.util.Collection<? extends T> c) {
		while (size() > maxSize) {
			super.removeFirst();
		}
		return super.addAll(index, c);
	}
}