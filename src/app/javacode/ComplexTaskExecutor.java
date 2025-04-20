package app.javacode;

import java.util.concurrent.*;

public class ComplexTaskExecutor {
    private final CyclicBarrier barrier;
    private final ExecutorService executor;

    public ComplexTaskExecutor(int numberOfTasks) {
        this.barrier = new CyclicBarrier(numberOfTasks, () -> {
            System.out.println("All tasks completed. Merging results in " + Thread.currentThread().getName());
        });
        this.executor = Executors.newFixedThreadPool(numberOfTasks);
    }

    public void executeTasks(int numberOfTasks) {
        for (int i = 0; i < numberOfTasks; i++) {
            int finalI = i;
            executor.submit(() -> {
                ComplexTask complexTask = new ComplexTask(finalI);
                complexTask.execute();

                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                }

                if (finalI == numberOfTasks - 1) {
                    executor.shutdown();
                }

            });
        }
    }
}
