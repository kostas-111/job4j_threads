package ru.job4j.cash;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@ThreadSafe
public class AccountStorage {

    private final ConcurrentHashMap<Integer, Account> accounts = new ConcurrentHashMap<>();

    @GuardedBy("this")
    public boolean add(Account account) {
        return accounts.putIfAbsent(account.id(), account) == null;
    }

    @GuardedBy("this")
    public boolean update(Account account) {
        return accounts.replace(account.id(), account) != null;
    }

    @GuardedBy("this")
    public void delete(int id) {
        accounts.remove(id);
    }

    @GuardedBy("this")
    public Optional<Account> getById(int id) {
        return Optional.ofNullable(accounts.get(id));
    }

    @GuardedBy("this")
    public boolean transfer(int fromId, int toId, int amount) {
        int sumFromId = accounts.get(fromId).amount();
        int sumToId = accounts.get(toId).amount();
        if (amount <= 0 || sumFromId < amount) {
            return false;
        }
        Account updateFrom = new Account(fromId, sumFromId - amount);
        Account updateTo = new Account(toId, sumToId + amount);
        return update(updateFrom) && update(updateTo);

    }
}
