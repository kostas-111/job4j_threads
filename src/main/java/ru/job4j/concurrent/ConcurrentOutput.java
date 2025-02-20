package ru.job4j.concurrent;

/*
Метод Thread#start() указывает виртуальной машине,
что операторы описанные в конструкторе нужно запустить в отдельной нити.
 */
public class ConcurrentOutput {
    public static void main(String[] args) {
        Thread another = new Thread(
                () -> System.out.println(Thread.currentThread().getName())
        );
        another.start();
        System.out.println(Thread.currentThread().getName());

        Thread second = new Thread(
                () -> System.out.println(Thread.currentThread().getName())
        );
        second.start();
    }
}
