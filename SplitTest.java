public class SplitTest {
    public static void main(String[] args) {
        String command = "ERROR";
        String[] commandParts = command.split(":");
        System.out.println(commandParts[0]);
    }

}
