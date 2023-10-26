public class Listener implements Runnable {
    public void run() {
        System.out.println("I am the listener");
        while (true) {
            System.out.println("Listening...");
        }
    }
}
