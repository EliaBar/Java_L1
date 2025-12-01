import java.util.concurrent.TimeUnit;

class RingBuffer<T> {
    private final T[] buffer;
    private int head = 0; 
    private int tail = 0; 
    private int count = 0; 
    private final int capacity;

    @SuppressWarnings("unchecked")
    public RingBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = (T[]) new Object[capacity];
    }

    public synchronized void put(T item) throws InterruptedException {
        while (count == capacity) {
            wait();
        }
        
        buffer[tail] = item;
        tail = (tail + 1) % capacity; 
        count++;
        notifyAll(); 
    }

    public synchronized T take() throws InterruptedException {
        while (count == 0) {
            wait();
        }

        T item = buffer[head];
        head = (head + 1) % capacity;
        count--;

        notifyAll(); 
        return item;
    }
}

public class Task2Main {
    public static void main(String[] args) {
        RingBuffer<String> buffer1 = new RingBuffer<>(10);
        RingBuffer<String> buffer2 = new RingBuffer<>(10);

        for (int i = 1; i <= 5; i++) {
            final int threadNum = i;
            Thread producer = new Thread(() -> {
                int msgCount = 1;
                try {
                    while (true) {
                        String msg = "Потік № " + threadNum + " згенерував повідомлення " + msgCount++;
                        buffer1.put(msg);
                        TimeUnit.MILLISECONDS.sleep(100); 
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            producer.setDaemon(true);
            producer.start();
        }

        for (int i = 1; i <= 2; i++) {
            final int threadNum = i;
            Thread transfer = new Thread(() -> {
                try {
                    while (true) {
                        String received = buffer1.take();
                        String newMsg = "Потік № " + threadNum + " переклав повідомлення: [" + received + "]";
                        buffer2.put(newMsg);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            transfer.setDaemon(true); 
            transfer.start();
        }

        System.out.println("Основний потік почав вичитувати дані...");
        try {
            for (int i = 1; i <= 100; i++) {
                String finalMsg = buffer2.take();
                System.out.println("Main отримав (" + i + "/100): " + finalMsg);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Роботу завершено. Основний потік зупиняється, демони гинуть автоматично.");
    }
}