package ru.job4j;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

/*
 Неблокирующий счетчик
 */
@ThreadSafe
public class CASCount {

	private final AtomicInteger count = new AtomicInteger();

	public void increment() {
		int newCount;
		int old;
		do {
			old = count.get();
			newCount = old + 1;
		} while (!count.compareAndSet(old, newCount));
	}

	public int get() {
		return count.get();
	}
}