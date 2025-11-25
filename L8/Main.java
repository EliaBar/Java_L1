import java.util.Random;

public class Main{
    private static final long ITERATIONS = 1_000_000_000L;
    private static long totalInside = 0;

    public static void main(String[] args) throws InterruptedException {

        if (args.length != 1) {
            System.out.println("Usage: java ParallelMonteCarloPi <threads>");
            return;
        }

        int threads = Integer.parseInt(args[0]);
        Thread[] workers = new Thread[threads];

        long startTime = System.nanoTime();
        Object lock = new Object();
        long iterationsPerThread = ITERATIONS / threads;

        for (int i = 0; i < threads; i++) {
            workers[i] = new Thread(() -> {

                Random rand = new Random();
                long inside = 0;

                for (long j = 0; j < iterationsPerThread; j++) {

                    double x = rand.nextDouble();
                    double y = rand.nextDouble();

                    if (x * x + y * y <= 1.0) {
                        inside++;
                    }
                }

                synchronized (lock) {
                    totalInside += inside;
                }
            });

            workers[i].start();
        }

        for (Thread t : workers) {
            t.join();
        }

        long endTime = System.nanoTime();
        double timeMs = (endTime - startTime) / 1_000_000.0;

        double pi = 4.0 * totalInside / ITERATIONS;
        
        System.out.printf("PI is %.5f\n", pi);
        System.out.printf("THREADS %d\n", threads);
        System.out.printf("ITERATIONS %,d\n", ITERATIONS);
        System.out.printf("TIME %.2fms\n", timeMs);
    }
}
