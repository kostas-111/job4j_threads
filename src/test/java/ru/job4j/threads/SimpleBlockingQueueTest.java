package ru.job4j.threads;

import org.junit.jupiter.api.Test;
import ru.job4j.threads.buffer.SimpleBlockingQueue;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SimpleBlockingQueueTest {

	@Test
	void producerConsumerWithJoin() throws InterruptedException {

		SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(2);
		final int[] producedCount = new int[1];
		final int[] consumedCount = new int[1];
		final int[] lastValue = new int[1];

		/*
		создаем потребителя (запустится и будет ждать)
		 */
		Thread consumer = new Thread(() -> {
			try {
				System.out.println("Потребитель начал работу и ждет данные...");
				Integer value = queue.poll();
				consumedCount[0] = 1;
				lastValue[0] = value;
				System.out.println("Потребитель получил: " + value);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}, "Consumer");

		/*
		создаем производителя
		 */
		Thread producer = new Thread(() -> {
			try {
				System.out.println("Производитель начал работу");
				Thread.sleep(500);
				int value = 50;
				queue.offer(value);
				producedCount[0] = 1;
				System.out.println("Производитель отправил: " + value);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}, "Producer");

		/*
		Запускаем потребителя первым - он заблокируется в poll()
		 */
		consumer.start();

		/*
		Даем время потребителю перейти в wait()
		 */
		Thread.sleep(100);

		/*
		Запускаем производителя
		 */
		producer.start();

		/*
		Добиваемся последовательного выполнения через join()
		 */
		producer.join();
		System.out.println("Производитель завершил работу");

		consumer.join();
		System.out.println("Потребитель завершил работу");

		assertEquals(1, producedCount[0]);
		assertEquals(1, consumedCount[0]);
		assertEquals(50, lastValue[0]);
	}

	@Test
	public void whenFetchAllThenGetIt() throws InterruptedException {

		final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
		final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(5);

		Thread producer = new Thread(
			() -> IntStream.range(0, 5).forEach(i -> {
				try {
					queue.offer(i);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			})
		);
		producer.start();

		Thread consumer = new Thread(
			() -> {
				while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
					try {
						buffer.add(queue.poll());
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
		);

		consumer.start();
		producer.join();
		consumer.interrupt();
		consumer.join();

		assertThat(buffer).containsExactly(0, 1, 2, 3, 4);
	}

	@Test
	public void whenFetchAllWithTimeoutThenGetAllElements() throws InterruptedException {
		final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
		final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(5);

		Thread producer = new Thread(() -> {
			try {
				for (int i = 0; i < 5; i++) {
					queue.offer(i);
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		});

		Thread consumer = new Thread(() -> {
			int received = 0;
			while (received < 5) {
				try {
					Integer value = queue.poll();
					if (value != null) {
						buffer.add(value);
						received++;
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}
		});

		producer.start();
		consumer.start();

		producer.join();
		consumer.join();

		assertThat(buffer).containsExactly(0, 1, 2, 3, 4);
	}

}