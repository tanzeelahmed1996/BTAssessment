package LogFileProcessor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String filepath = null;

        if (args.length > 0 && args[0].endsWith(".txt")) {
            filepath = args[0];
        }
        
         // If no valid argument is provided, prompt the user for a filepath
         while (filepath == null || filepath.isEmpty() || !filepath.endsWith(".txt")) {
            System.out.println("Please enter a valid .txt filepath:");
            filepath = scanner.nextLine();
        }

        LogProcessor logProcessor = new LogProcessor();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                logProcessor.processLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        logProcessor.finaliseSessions();
        logProcessor.printReport();
    }
}

