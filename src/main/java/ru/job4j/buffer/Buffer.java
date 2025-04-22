package ru.job4j.buffer;

/*
Монитор:
В случае синхронизированного метода монитором является экземпляр этого класса.
Таким образом, монитором выступает экземпляр класса Buffer.

Критическая секция:
Код внутри методов add и toString, так как они помечены как synchronized.
Это означает, что только один поток может выполнять любой из этих методов
в Buffer в определённый момент времени.

Таким образом, монитор (Buffer) обеспечивает синхронизацию,
а код внутри synchronized-методов является критическими секциями,
защищёнными от одновременного доступа несколькими потоками.
 */
public class Buffer {
    private StringBuilder buffer = new StringBuilder();

    private synchronized void add(int value) {
        System.out.println(value);
        buffer.append(value);
    }

    @Override
    public synchronized String toString() {
        return buffer.toString();
    }
}
