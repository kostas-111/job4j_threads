package ru.job4j.threads.buffer;

/*
Механизм остановки потребителя, когда производитель закончил свою работу
Когда первая нить заканчивает свою работу, потребители переходят в режим wait
 */
public class ParallelSearch {

	public static void main(String[] args) {
		SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(3);
		final Thread consumer = new Thread(
			() -> {
				while (!Thread.currentThread().isInterrupted()) {
					try {
						System.out.println(queue.poll());
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						break;
					}
				}
			});

		consumer.start();

		new Thread(
			() -> {
				for (int i = 0; i != 3; i++) {
					try {
						queue.offer(i);
						Thread.sleep(500);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
				consumer.interrupt();
			}
		).start();
	}
}
