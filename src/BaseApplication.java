import java.util.Scanner;

import services.Indexer;

import java.io.*;

public class BaseApplication {

    /**
     * 
     * A command-line application that allows users to create an index file for a
     * given data file,
     * list records from a data file based on the index file, and search for a
     * specific record based on the index file.
     * The application takes a command as input (-c: create index, -l: list records,
     * -s: search) followed by necessary
     * arguments. The Indexer class is responsible for creating and manipulating the
     * index file, while the main method
     * acts as a controller that directs user input to the appropriate methods of
     * the Indexer class.
     */
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
