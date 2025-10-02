package ru.job4j.threads.cache;

public class OptimisticException extends RuntimeException {
	public OptimisticException(String message) {
		super(message);
	}
}
