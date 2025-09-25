package ru.job4j.threads;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SimpleBlockingQueueTest {

	@Test
	void producerConsumerWithJoin() throws InterruptedException {

		SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>();
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

}