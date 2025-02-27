package ru.job4j.concurrent;

/*
Чтобы получить состояние нити можно воспользоваться методом - getState().
Этот метод возвращает перечисления Thread.State.
Тело цикла while выполняется произвольное количество раз.
За управление нитями в Java отвечает планировщик задач.
Он решает, сколько времени отвести на выполнение одной задачи.
 */
public class ThreadState {
    public static void main(String[] args) {
        Thread first = new Thread(
                () -> System.out.println(Thread.currentThread().getName())
        );
        Thread second = new Thread(
                () -> System.out.println(Thread.currentThread().getName())
        );
        first.start();
        second.start();
        while (first.getState() != Thread.State.TERMINATED || second.getState() != Thread.State.TERMINATED) {
            System.out.println(first.getState());
            System.out.println(second.getState());
        }
        System.out.println("Работа завершена");
    }
}