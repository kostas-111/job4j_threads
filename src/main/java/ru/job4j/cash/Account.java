package ru.job4j.cash;

public record Account(int id, int amount) {
    public Account {
        if (id <= 0) {
            throw new IllegalArgumentException("Идентификатор счета долен быть больше нуля");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Баланс счета не может быть отрицательным");
        }
    }
}