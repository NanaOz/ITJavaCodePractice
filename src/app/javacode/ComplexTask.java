package app.javacode;

import java.util.Random;

public class ComplexTask {
    private final int id;
    private int result;

    public ComplexTask(int id) {
        this.id = id;
    }

    public void execute() {
        System.out.println(Thread.currentThread().getName() + " выполнение задачи " + id);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        result = new Random().nextInt(100);

        System.out.println(Thread.currentThread().getName() + " завершенная задача " + id + " с результатом " + result);
    }

    public int getResult() {
        return result;
    }
}
