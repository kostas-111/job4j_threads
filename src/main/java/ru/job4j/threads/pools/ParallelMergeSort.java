package ru.job4j.threads.pools;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelMergeSort extends RecursiveTask<int[]> {

  private final int[] array;
  private final int from;
  private final int to;

  public ParallelMergeSort(int[] array, int from, int i) {
    this.array = array;
    this.from = from;
    to = i;
  }

  @Override
  protected int[] compute() {

    if (from == to) {
      return new int[] {array[from]};
    }

    int middle = (from + to) / 2;

    /*
    Создаем задачи для сортировки частей
     */
    ParallelMergeSort leftSort = new ParallelMergeSort(array, from, middle);
    ParallelMergeSort rightSort = new ParallelMergeSort(array, middle + 1, to);

    /*
    Производим деление.
    Оно будет происходить, пока в частях не останется по одному элементу
    метод fork() организует асинхронное выполнение новой задачи.
    Это аналогично тому, что мы запустили бы рекурсивный метод еще раз
     */
    leftSort.fork();
    rightSort.fork();

    /*
    Объединяем полученные результаты
    метод join(). Этот метод ожидает завершения задачи и возвращает результат
    её выполнения, но во время ожидания поток не блокируется,
    а может начать выполнение других задач
     */
    int[] left = leftSort.join();
    int[] right = rightSort.join();
    return MergeSort.merge(left, right);
  }

  public static int[] sort(int[] array) {
    ForkJoinPool forkJoinPool = new ForkJoinPool();

    /*
     метод invoke(). Этот метод служит для запуска выполнения
     */
    return forkJoinPool.invoke(new ParallelMergeSort(array, 0, array.length - 1));
  }
}
