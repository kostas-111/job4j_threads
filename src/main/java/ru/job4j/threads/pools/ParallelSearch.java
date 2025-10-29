package ru.job4j.threads.pools;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelSearch<T> extends RecursiveTask<Integer> {

  private static final int VALUE = 10;
  private final T[] array;
  private final T target;
  private final int start;
  private final int end;

  public ParallelSearch(T[] array, T target, int start, int end) {
    this.array = array;
    this.target = target;
    this.start = start;
    this.end = end;
  }

  @Override
  protected Integer compute() {

    int length = end - start;

    /*
    Базовый случай
     */
    if (length <= VALUE) {
      return linearSearch();
    }

    /*
    Разделяем задачу на две подзадачи
     */
    int middle = start + length / 2;

    ParallelSearch<T> leftSearch = new ParallelSearch<>(array, target, start, middle);
    ParallelSearch<T> rightSearch = new ParallelSearch<>(array, target, middle + 1, end);

    /*
    Запускаем подзадачи параллельно
     */
    leftSearch.fork();
    rightSearch.fork();

    Integer left = leftSearch.join();
    Integer right = rightSearch.join();

    return left != -1 ? left : right;
  }

  private int linearSearch() {
    for (int i = 0; i < array.length; i++) {
      if (target.equals(array[i])) {
        return i;
      }
    }
    return -1;
  }

  public static <T> int parallelSearch(T[] array, T target) {

    if (array == null || array.length == 0) {
      return -1;
    }

    ForkJoinPool pool = ForkJoinPool.commonPool();
    return pool.invoke(new ParallelSearch<>(array, target, 0, array.length - 1));
  }
}
