import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

class Account {
    private static final AtomicInteger idGenerator = new AtomicInteger(0);
    private final int id;
    private int balance;

    public Account(int initialBalance) {
        this.id = idGenerator.getAndIncrement();
        this.balance = initialBalance;
    }

    public int getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    public boolean withdraw(int amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void deposit(int amount) {
        balance += amount;
    }
}

class Bank {
    public void transfer(Account from, Account to, int amount) {
        Account firstLock = from.getId() < to.getId() ? from : to;
        Account secondLock = from.getId() < to.getId() ? to : from;

        synchronized (firstLock) {
            synchronized (secondLock) {
                if (from.withdraw(amount)) {
                    to.deposit(amount);
                }
            }
        }
    }

    public long getTotalBalance(List<Account> accounts) {
        long total = 0;
        for (Account acc : accounts) {
            total += acc.getBalance();
        }
        return total;
    }
}

public class Task1Main {
    public static void main(String[] args) throws InterruptedException {
        Bank bank = new Bank();
        List<Account> accounts = new ArrayList<>();
        int numberOfAccounts = 100;
        int initialBalance = 1000;
        
        for (int i = 0; i < numberOfAccounts; i++) {
            accounts.add(new Account(initialBalance));
        }

        long startTotalBalance = bank.getTotalBalance(accounts);
        System.out.println("Total money before: " + startTotalBalance);

        int numberOfThreads = 2000;
        ExecutorService executor = Executors.newFixedThreadPool(20); 
        CountDownLatch latch = new CountDownLatch(numberOfThreads); 
        Random random = new Random();

        for (int i = 0; i < numberOfThreads; i++) {
            executor.submit(() -> {
                try {
                    Account from = accounts.get(random.nextInt(numberOfAccounts));
                    Account to = accounts.get(random.nextInt(numberOfAccounts));
                    if (from != to) {
                        int amount = random.nextInt(50); 
                        bank.transfer(from, to, amount);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        long endTotalBalance = bank.getTotalBalance(accounts);
        System.out.println("Total money after:  " + endTotalBalance);

        if (startTotalBalance == endTotalBalance) {
            System.out.println("SUCCESS: Balance is consistent.");
        } else {
            System.err.println("FAILURE: Balance mismatch!");
        }
    }
}