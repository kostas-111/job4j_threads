package ru.job4j;

/*
В примере из урока используется шаблон Singleton с двойной проверкой блокировки
Проблема: этот подход не гарантирует потокобезопасности.
Возможна ситуация, когда другой поток увидит частично инициализированный объект instance
до завершения его полной инициализации.
Чтобы исправить эту проблему, нужно использовать ключевое слово volatile,
которое гарантирует корректное поведение потоков относительно порядка записи и чтения
переменной instance.
Ключевое слово volatile предотвращает кэширование значения переменной instance в регистрах процессора
или локальных кэшах потока. Это гарантирует, что все потоки будут видеть обновленное значение instance
 после завершения инициализации объекта.
 */
public class DCLSingleton {

    private static volatile DCLSingleton instance;

    public static DCLSingleton getInstance() {
        if (instance == null) {
            synchronized (DCLSingleton.class) {
                if (instance == null) {
                    instance = new DCLSingleton();
                }
            }
        }
        return instance;
    }

    private DCLSingleton() {
    }
}
