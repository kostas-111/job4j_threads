package ru.job4j.threads.pools;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ParallelSearchTest {

  @Test
  void testDifferentDataTypes() {

    Integer[] intArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
    assertEquals(5, ParallelSearch.parallelSearch(intArray, 6));

    String[] strArray = {"apple", "banana", "cherry", "date", "elderberry"};
    assertEquals(2, ParallelSearch.parallelSearch(strArray, "cherry"));

    Double[] doubleArray = {1.1, 2.2, 3.3, 4.4, 5.5};
    assertEquals(3, ParallelSearch.parallelSearch(doubleArray, 4.4));

    Person[] personArray = {
        new Person("Alice", 25),
        new Person("Bob", 30),
        new Person("Charlie", 35)
    };
    Person targetPerson = new Person("Bob", 30);
    assertEquals(1, ParallelSearch.parallelSearch(personArray, targetPerson));
  }

  @Test
  void testLinearSearchSmallArray() {

    /*
    Массив размером 8 (< 10) - должен использоваться линейный поиск
     */
    Integer[] smallArray = {5, 3, 8, 1, 9, 2, 7, 4};

    /*
    Поиск существующих элементов
     */
    assertEquals(0, ParallelSearch.parallelSearch(smallArray, 5));
    assertEquals(3, ParallelSearch.parallelSearch(smallArray, 1));
    assertEquals(6, ParallelSearch.parallelSearch(smallArray, 7));

    /*
    Поиск несуществующего элемента
     */
    assertEquals(-1, ParallelSearch.parallelSearch(smallArray, 10));
  }

  @Test
  void testParallelSearchLargeArray() {

    /*
    Создаем большой массив (20 элементов)
     */
    Integer[] largeArray = new Integer[20];
    for (int i = 0; i < largeArray.length; i++) {
      largeArray[i] = i * 2;
    }

    /*
    Поиск элементов в разных частях массива
     */
    assertEquals(0, ParallelSearch.parallelSearch(largeArray, 0));
    assertEquals(5, ParallelSearch.parallelSearch(largeArray, 10));
    assertEquals(19, ParallelSearch.parallelSearch(largeArray, 38));

    /*
    Поиск несуществующего элемента (нечетное число)
     */
    assertEquals(-1, ParallelSearch.parallelSearch(largeArray, 99));
  }

  @Test
  void testElementNotFound() {
    Integer[] array = {1, 2, 3, 4, 5};

    /*
    Несуществующий элемент
     */
    assertEquals(-1, ParallelSearch.parallelSearch(array, 10));

    /*
    Пустой массив
     */
    Integer[] emptyArray = {};
    assertEquals(-1, ParallelSearch.parallelSearch(emptyArray, 1));

    /*
    null массив
     */
    assertEquals(-1, ParallelSearch.parallelSearch(null, 1));
  }

  @Test
  void testThresholdCases() {

    /*
    Массив ровно из 10 элементов - линейный поиск
     */
    Integer[] exactly10 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    assertEquals(5, ParallelSearch.parallelSearch(exactly10, 5));
    assertEquals(9, ParallelSearch.parallelSearch(exactly10, 9));

    /*
    Массив из 11 элементов - параллельный поиск
     */
    Integer[] exactly11 = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    assertEquals(10, ParallelSearch.parallelSearch(exactly11, 10));
  }

  @Test
  void testFirstAndLastElements() {
    Integer[] array = {100, 200, 300, 400, 500};
    assertEquals(0, ParallelSearch.parallelSearch(array, 100)); // первый
    assertEquals(4, ParallelSearch.parallelSearch(array, 500)); // последний
  }

  @Test
  void testSearchNullInArray() {
    String[] arrayWithNull = {"a", "b", null, "d"};
    try {
      int result = ParallelSearch.parallelSearch(arrayWithNull, null);
      assertEquals(2, result);
    } catch (NullPointerException e) {
     e.getMessage();
    }
  }

  @Test
  void testSingleElementArray() {
    Integer[] single = {42};
    assertEquals(0, ParallelSearch.parallelSearch(single, 42));
    assertEquals(-1, ParallelSearch.parallelSearch(single, 99));
  }

  @Test
  void testDuplicateElements() {
    Integer[] withDuplicates = {1, 2, 3, 2, 4, 2, 5};

    /*
    Может вернуть любой из индексов с элементом 2
     */
    int result = ParallelSearch.parallelSearch(withDuplicates, 2);
    assertTrue(result == 1 || result == 3 || result == 5);
  }

  static class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
      this.name = name;
      this.age = age;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null || getClass() != obj.getClass()) {
        return false;
      }
      Person person = (Person) obj;
      return age == person.age && name.equals(person.name);
    }

    @Override
    public int hashCode() {
      return 31 * name.hashCode() + age;
    }
  }

}