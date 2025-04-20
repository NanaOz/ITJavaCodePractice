package app.javacode;


public class Main {
    public static void main(String[] args) {
        BlockingQueue<String> queue = new BlockingQueue<>(10);

        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    String item = "item" + i;
                    queue.enqueue(item);
                    System.out.println("enqueue: " + item);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    String item = queue.dequeue();
                    System.out.println("dequeue: " + item);
                    Thread.sleep(1800);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }
}