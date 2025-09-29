package ru.job4j.threads.buffer;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

/*
Реализация шаблона Producer Consumer
 */
@ThreadSafe
public class SimpleBlockingQueue<T> {

	@GuardedBy("this")
	private Queue<T> queue = new LinkedList<>();

	/*
	Максимальный размер очереди
	 */
	private final int capacity;

	public SimpleBlockingQueue(int capacity) {
		this.capacity = capacity;
	}

	public synchronized void offer(T value) throws InterruptedException {

		/*
		Ждем пока не появится свободное место
		 */
		while (queue.size() >= capacity) {
			wait();
		}
		queue.offer(value);

		/*
		Будим все ожидающие потоки
		 */
		notifyAll();
	}

	public synchronized T poll() throws InterruptedException {
		while (queue.isEmpty()) {

			/*
			Освобождаем монитор
			 */
			wait();
		}
		T result = queue.poll();

		/*
		Уведомляем производителей о свободном месте
		 */
		notifyAll();
		return result;
	}

	public synchronized boolean isEmpty() {
		return queue.isEmpty();
	}
}
