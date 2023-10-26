public class ThreadPractice {
    public static void main(String args[]) {
        int broadcastPort = 1234;
        Runnable runnable = () -> {
            System.out.println("Hello from " + Thread.currentThread().getName() + "listening on port " + broadcastPort);
        };
        Thread thread = new Thread(runnable, "Thread 1");
        Thread thread2 = new Thread(runnable, "Thread 2");
        thread.start();
        thread2.start();
    }
}
