package ru.job4j.threads.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailNotification {

  /*
  Создаем пул нитей по количеству доступных процессоров
   */
  private final ExecutorService pool = Executors.newFixedThreadPool(
      Runtime.getRuntime().availableProcessors());

  public void emailTo(User user) {
    String subject = String.format("Notification %s to email %s",
        user.getName(), user.getEmail());
    String body = String.format("Add a ew event to %s", user.getName());

    /*
    Через ExecutorService создаем задачу, которая будет создавать
    данные для пользователя и передавать их в метод send
     */
    pool.submit(() -> send(subject, body, user.getEmail()));
  }

  /*
  Закрываем пул и ждем пока все задачи завершатся
   */
  public void close() {
    pool.shutdown();
    while (!pool.isTerminated()) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public void send(String subject, String body, String email) {
  }

}
