public class ShellDriver {
    ShellInterface d = new ShellInterface();
    public static void main(String[] args) {
        ShellInterface sInterface = new ShellInterface();
        sInterface.start();
        System.out.println("Program Exited Successfully.");
    }
}