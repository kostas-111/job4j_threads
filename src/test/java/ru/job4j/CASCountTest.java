package ru.job4j;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CASCountTest {

	@Test
	void whenIncrementTwiceThenGetTwo() {
		CASCount casCount = new CASCount();
		casCount.increment();
		casCount.increment();
		assertEquals(2, casCount.get());
	}

	@Test
	void whenIncrementInTwoThreadsThenGetTwo() throws InterruptedException {
		CASCount casCount = new CASCount();

		Thread thread1 = new Thread(casCount::increment);
		Thread thread2 = new Thread(casCount::increment);

		thread1.start();
		thread2.start();

		thread1.join();
		thread2.join();

		assertEquals(2, casCount.get());
	}
}