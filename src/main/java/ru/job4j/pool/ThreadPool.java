package ru.job4j.pool;

import ru.job4j.threads.buffer.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;

public class ThreadPool {

	private final List<Thread> threads = new LinkedList<>();
	private final SimpleBlockingQueue<Runnable> tasks;

	public ThreadPool(int queueCapacity) {
		tasks = new SimpleBlockingQueue<>(queueCapacity);

		/*
		Создаем и запускаем нити
		 */
		int size = Runtime.getRuntime().availableProcessors();
		for (int i = 0; i < size; i++) {
			threads.add(new Thread(() -> {
				while (!Thread.currentThread().isInterrupted()) {
					try {
						Runnable task = tasks.poll();
						task.run();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						break;
					}
				}
			}));
		}
		threads.forEach(Thread::start);
	}

	public void work(Runnable job) throws InterruptedException {
		tasks.offer(job);
	}

	public void shutdown() {
		threads.forEach(Thread::interrupt);
		threads.forEach(thread -> {
			try {
				thread.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		});
	}
}
