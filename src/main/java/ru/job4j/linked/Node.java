package ru.job4j.linked;

/*
Класс Immutable, так как:
- поля помечены final
- состояние объекта после создания меняться не будет (есть конструктор, сеттеры удалены)
 */
public class Node<T> {
    private final Node<T> next;
    private final T value;

    public Node(Node<T> next, T value) {
        this.next = next;
        this.value = value;
    }

    public Node<T> getNext() {
        return next;
    }

    public T getValue() {
        return value;
    }
}