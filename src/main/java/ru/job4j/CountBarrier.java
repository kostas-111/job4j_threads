package ru.job4j;

/*
Класс, который блокирует выполнение по условию счетчика
 */
public class CountBarrier {
	private final Object monitor = this;

	private final int total;

	private int count = 0;

	public CountBarrier(int total) {
		this.total = total;
	}

	/*
	Метод count изменяет состояние программы
	Переменная total содержит количество вызовов метода count()
	 */
	public void count() {
		synchronized (monitor) {
			count++;
			monitor.notifyAll();

		}
	}

	/*
	Нити, которые выполняют метод await, могут начать работу если поле count >= total.
	Если оно не равно, то нужно перевести нить в состояние wait.
	 */
	public void await() {
		synchronized (monitor) {
			while (count < total) {
				try {
					monitor.wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}
}
