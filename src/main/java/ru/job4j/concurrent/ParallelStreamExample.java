package ru.job4j.concurrent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/*
Чтобы создать из этого потока параллельный поток, используется метод parallelStream().
Данный метод вернет параллельный поток, если это возможно.
Если какие-то операции нельзя выполнить параллельно,
то вернется обычный последовательный поток Stream.

Метод parallelStream() создает поток из элементов Integer,
который может быть выполнен в многопоточной среде.

Затем проверяем поток на параллельность с помощью метода isParallel().
Данный метод определен в интерфейсе BaseStream, который является родительским
по отношению к интерфейсу Stream.

В выводе видим, что метод isParallel() вернул true.
Параллельность потока в данном случае не влияет на результат.
Возможность таким удобным и безопасным образом создать многопоточность в Stream
защищает программиста от ошибок, которые можно допустить при ручной организации многопоточного окружения.
 */
public class ParallelStreamExample {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Stream<Integer> stream = list.parallelStream();
        System.out.println(stream.isParallel());
        Optional<Integer> multiplication = stream
                .reduce((left, right) -> left * right);
        System.out.println(multiplication.get());
    }
}