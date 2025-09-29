package ru.job4j.threads.synch;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;

class SingleLockListTest {

	@Test
	void whenIt() {
		var init = new ArrayList<Integer>();
		SingleLockList<Integer> list = new SingleLockList<>(init);
		list.add(1);
		var it = list.iterator();
		list.add(2);
		assertThat(it.next()).isEqualTo(1);
	}

	@Test
	void whenAdd() throws InterruptedException {
		var init = new ArrayList<Integer>();
		SingleLockList<Integer> list = new SingleLockList<>(init);
		Thread first = new Thread(() -> list.add(1));
		Thread second = new Thread(() -> list.add(2));
		first.start();
		second.start();
		first.join();
		second.join();
		Set<Integer> rsl = new TreeSet<>();
		list.iterator().forEachRemaining(rsl::add);
		assertThat(rsl).hasSize(2);
		assertThat(rsl).containsAll(List.of(1, 2));
	}

}