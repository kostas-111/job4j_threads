package ru.job4j.cash;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/*
Методы в классе используют ConcurrentHashMap, который обеспечивает собственную потокобезопасность
без необходимости внешней синхронизации по this, за исключением метода transfer().
В transfer() добавил синхронизацию по всему объекту AccountStorage.
Это обеспечит атомарность, так как блокирует весь объект даже для операций с разными аккаунтами.
 */
@ThreadSafe
public class AccountStorage {

    private final ConcurrentHashMap<Integer, Account> accounts = new ConcurrentHashMap<>();

    public boolean add(Account account) {
        return accounts.putIfAbsent(account.id(), account) == null;
    }

    public boolean update(Account account) {
        return accounts.replace(account.id(), account) != null;
    }

    public void delete(int id) {
        accounts.remove(id);
    }

    public Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    /*
    Атомарность гарантируется, так как все операции выполняются внутри synchronized (this)
     */
    @GuardedBy("this")
    public boolean transfer(int fromId, int toId, int amount) {
        synchronized (this) {
            Optional<Account> fromAccount = getById(fromId);
            Optional<Account> toAccount = getById(toId);
            if (fromAccount.isEmpty() || toAccount.isEmpty() || amount <= 0) {
                return false;
            }

            int sumFromId = fromAccount.get().amount();
            int sumToId = toAccount.get().amount();

            if (sumFromId < amount) {
                return false;
            }

            Account updateFrom = new Account(fromId, sumFromId - amount);
            Account updateTo = new Account(toId, sumToId + amount);
            return update(updateFrom) && update(updateTo);
        }
    }
}
