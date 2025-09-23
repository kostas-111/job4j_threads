package ru.job4j.threads;

import ru.job4j.CountBarrier;

public class CountBarrierDemo {
	public static void main(String[] args) throws InterruptedException {

		/*
		Создаем барьер на 3 вызова count()
		 */
		CountBarrier barrier = new CountBarrier(3);

		System.out.println("=== CountBarrier Demo ===");
		System.out.println("Target count: " + 3);

		/*
		Поток, который будет ждать барьер
		 */
		Thread waiterThread = new Thread(() -> {
			System.out.println(Thread.currentThread().getName() + " started and calling await()");

			barrier.await();

		}, "WaiterThread");

		/*
		Запускаем поток ожидания
		 */
		waiterThread.start();

		/*
		Даем время потоку начать ожидание
		 */
		Thread.sleep(100);

		/*
		Имитируем вызовы count() с задержкой
		 */
		for (int i = 1; i <= 3; i++) {
			Thread.sleep(1000);
			System.out.println("Calling count() #" + i);
			barrier.count();
		}

		/*
		Ждем завершения потока
		 */
		waiterThread.join();
		System.out.println("=== Demo completed ===");
	}
}
