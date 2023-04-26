import java.util.Scanner;

import services.Indexer;

import java.io.*;

public class BaseApplication {

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        String command = "";

        System.out.println("\nEnter command (-c: create index, -l: list records, -s: search): ");
        System.err.println(
                "Usage: <-flag> <input_file or data file> <output_file or index file> <key_length> <optional: key value>");

        command = scanner.nextLine();

        String[] arguments = null;
        arguments = command.split(" ");

        if (arguments.length < 4) {
            System.err.println(
                    "Correct Usage: <-flag> <input_file or data file> <output_file or index file> <key_length> <optional: key value>");
            System.exit(1);
        }

        String indexCommand = arguments[0].toLowerCase();

        Indexer indexService = new Indexer();

        switch (indexCommand) {
            case "-c":
                String inputFileName = arguments[1].toLowerCase();
                String outputFileName = arguments[2].toLowerCase();
                Integer keyLength = Integer.parseInt(arguments[3]);
                indexService.createIndex(inputFileName, outputFileName, keyLength);
                break;
            case "-l":
                String dataFileName = arguments[1].toLowerCase();
                String indexFileName = arguments[2].toLowerCase();
                Integer lengthOfKey = Integer.parseInt(arguments[3]);
                indexService.listRecords(dataFileName, indexFileName, lengthOfKey);
                break;
            case "-s":
                dataFileName = arguments[1].toLowerCase();
                indexFileName = arguments[2].toLowerCase();
                lengthOfKey = Integer.parseInt(arguments[3]);
                String keyValue = arguments[4].trim(); 
                indexService.searchRecords(dataFileName, indexFileName, lengthOfKey, keyValue);
                break;
            default:
                System.out.println("Invalid command, please try again.");
                break;
        }

    }

}
