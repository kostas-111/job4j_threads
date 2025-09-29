package ru.job4j.threads.synch;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
Нужно создать коллекцию, которая будет корректно работать в многопоточный среде
 */
@ThreadSafe
public class SingleLockList<T> implements Iterable<T> {

	@GuardedBy("this")
	private final List<T> list;

	public SingleLockList(List<T> list) {
		this.list = copy(list);
	}

	public synchronized void add(T value) {
		list.add(value);
	}

	public synchronized T get(int index) {
		return list.get(index);
	}

	/*
	Этот итератор будет работать в режиме fail-safe -
	все изменения после получения коллекции не будут отображаться в итераторе.
	Защита от ConcurrentModificationException
	 */
	@Override
	public synchronized Iterator<T> iterator() {
		return copy(list).iterator();
	}

	private List<T> copy(List<T> origin) {
		return new ArrayList<>(origin);
	}
}
