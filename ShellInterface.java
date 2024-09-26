import java.util.Scanner;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ShellInterface {

    private static HashMap<String, String> commandTable = new HashMap<String, String>();
    static {
        commandTable.put("ls", "dir");
        commandTable.put("pwd", "cd");
        commandTable.put("cp", "copy");
        commandTable.put("rm", "del");
        commandTable.put("cat", "type");
        commandTable.put("man", "help");
        commandTable.put("nano", "notepad");
        commandTable.put("grep", "find");
        commandTable.put("uname", "ver");
        commandTable.put("date", "date");
        commandTable.put("which", "where");
        commandTable.put("env", "set");
        commandTable.put("whoami", "whoami");
        commandTable.put("ifconfig", "ipconfig");
        commandTable.put("exit", "exit");
        commandTable.put("last", "last");
    }

    public ShellInterface() {

    }

    public void start() {
        boolean running = true;
        Scanner input = new Scanner(System.in);
        String lastInput = "";
        while (running) {
            try {
                // read input from the keyboard - input is a command
                //System.out.println(System.getProperty("user.dir")); <- Was used to show current directory, but cd is not being used.
                System.out.print("Bash--->");
                String[] inputLineArray = input.nextLine().toLowerCase().split(" ");
                String command = inputLineArray[0];
                String arguements = "";
                if (inputLineArray.length > 1) {
                    arguements += " " + String.join(" ", Arrays.copyOfRange(inputLineArray, 1, inputLineArray.length));
                }

                // If command is not an accepted windows command, attempt to convert
                if (!commandTable.containsValue(command)) {
                    command = commandTable.get(inputLineArray[0]);
                }

                // Command was not found, prompt user with accceptable commands
                if (command == null) {
                    for (Map.Entry<String, String> entry : commandTable.entrySet()) {
                        String unixCommand = entry.getKey();
                        String dosCommand = entry.getValue();
                        System.out.printf("Unix: %-10s MS-DOS: %s%n", unixCommand, dosCommand);
                    }

                    
                    System.out.println("\nCommand " + "\"" + inputLineArray[0] + "\" "
                            + "is not supported, a list of supported commands can be found above.");
                    lastInput = inputLineArray[0] + arguements;
                    continue;
                }

                if (command.equals("exit")) {
                    running = false;
                    break;
                }

                if (command.equals("last")){
                    System.out.println(lastInput);
                    lastInput = "last";
                    continue;
                }

                // create a new process using ProcessBuilder
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", command + arguements);
                // start the process
                Process process = pb.start();

                // Obtain the input stream
                Scanner inputfromprocess = new Scanner(process.getInputStream());
                // display the information from the process
                while (inputfromprocess.hasNext()) {
                    System.out.println(inputfromprocess.nextLine());
                }

                // wait for the process to finish
                process.waitFor();
                inputfromprocess.close();
                lastInput = inputLineArray[0] + arguements;
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
        input.close();
    }
}
