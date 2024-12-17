import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ProcessManager {

    public static void main(String[] args) {
        String os = System.getProperty("os.name").toLowerCase(); // Detectăm SO
        Scanner scanner = new Scanner(System.in);

        System.out.println("Operating System Detected: " + os);
        System.out.println("1. List Processes\n2. Kill Process\n3. Exit");

        try {
            while (true) {
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consumăm linia rămasă

                if (choice == 1) {
                    listProcesses(os);
                } else if (choice == 2) {
                    System.out.print("Enter PID to terminate: ");
                    String pid = scanner.nextLine();
                    killProcess(os, pid);
                } else if (choice == 3) {
                    System.out.println("Exiting...");
                    break;
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    // Listează procesele în funcție de SO
    private static void listProcesses(String os) {
        try {
            ProcessBuilder processBuilder;

            if (os.contains("win")) {
                processBuilder = new ProcessBuilder("tasklist");
            } else if (os.contains("mac") || os.contains("nux")) {
                processBuilder = new ProcessBuilder("ps", "-aux");
            } else {
                System.out.println("Unsupported OS");
                return;
            }

            Process process = processBuilder.start();

            // Citirea rezultatului comenzii
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            FileWriter writer = new FileWriter("processes.log");

            System.out.println("Processes:");
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                writer.write(line + "\n");
            }

            writer.close();
            reader.close();

            System.out.println("Process list saved to processes.log");

        } catch (IOException e) {
            System.out.println("Error listing processes: " + e.getMessage());
        }
    }

    // Oprește un proces după PID
    private static void killProcess(String os, String pid) {
        try {
            ProcessBuilder processBuilder;

            if (os.contains("win")) {
                processBuilder = new ProcessBuilder("taskkill", "/PID", pid, "/F");
            } else if (os.contains("mac") || os.contains("nux")) {
                processBuilder = new ProcessBuilder("kill", pid);
            } else {
                System.out.println("Unsupported OS");
                return;
            }

            Process process = processBuilder.start();
            process.waitFor();

            System.out.println("Process " + pid + " terminated.");

        } catch (Exception e) {
            System.out.println("Error terminating process: " + e.getMessage());
        }
    }
}
