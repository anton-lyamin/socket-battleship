public class Killer implements Runnable {
    private Boolean kill;

    public Killer(Boolean kill) {
        this.kill = kill;
    }

    public void run() {
        System.out.println("I am the killer");
        this.kill = true;
    }
}
