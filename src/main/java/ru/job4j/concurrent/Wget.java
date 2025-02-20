package ru.job4j.concurrent;

/*
В методе main идет симулирование процесса загрузки.
В теле метода создан цикл от 0 до 100.
Через 1 секунду выводится на консоль информация о загрузке.
Метод print печатает символы в строку без перевода каретки.
Символ \r указывает, что каретку каждый раз нужно вернуть в начало строки.
Это позволяет через промежуток времени обновить строчку.
 */
public class Wget {
    public static void main(String[] args) {
        Thread thread = new Thread(
                () -> {
                    try {
                        for (int i = 0; i <= 100; i++) {
                            System.out.print("\rLoading : " + i + "%");
                            Thread.sleep(1000);
                        }
                        System.out.println("");
                        System.out.println("Loaded.");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
    );
        thread.start();
    }
}