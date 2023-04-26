package services;

import models.IndexRecord;

import java.io.*;
import java.util.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Indexer {

    /**
     * Creates an index file for the given input file. The index file contains the
     * key and position of each record in the input file. The key is the first
     * `keyLength`
     * characters of each record. The method reads the input file, extracts the key
     * and
     * position of each record, and sorts the records by key. Then, it writes the
     * sorted records to the output file in binary format, with each record
     * consisting of
     * the key (encoded in ASCII) followed by its position (as a long value). It
     * also writes
     * the records to a text file for verification. If the output file already
     * exists, it is overwritten.
     *
     * @param inputFileName  the name of the input file to be indexed
     * @param outputFileName the name of the output file to which the index will be
     *                       written
     * @param keyLength      the length of the key to be used for indexing
     * @throws IOException if there is an error reading the input file or writing
     *                     the index files
     */
    public void createIndex(String inputFileName, String outputFileName, int keyLength) throws IOException {
        RandomAccessFile inputFile = new RandomAccessFile(inputFileName, "r");
        FileOutputStream outputFile = new FileOutputStream(outputFileName, false);
        PrintWriter textOutputFile = new PrintWriter(outputFileName + "-print");

        List<IndexRecord> records = new ArrayList<>();

        String line = inputFile.readLine();
        long position = 0;
        while (line != null) {
            String key = line.substring(0, Math.min(line.length(), keyLength));
            records.add(new IndexRecord(key, position));
            position = inputFile.getFilePointer();

            line = inputFile.readLine();
        }

        // Sort index records by key
        Collections.sort(records, Comparator.comparing(IndexRecord::getKey));

        for (IndexRecord record : records) {
            // write to binary file
            byte[] keyBytes = record.getKey().getBytes(StandardCharsets.US_ASCII);
            ByteBuffer buffer = ByteBuffer.allocate(keyBytes.length + Long.BYTES);
            buffer.put(keyBytes);
            buffer.putLong(record.getPointer());
            outputFile.write(buffer.array());
            // write newline character to binary file
            byte[] newline = { '\n' }; // use only the line feed character
            outputFile.write(newline);

            // write to a textual format to verify
            String outputLine = String.format("%s[%d]%n", record.getKey(), record.getPointer());

            textOutputFile.write(outputLine);
        }

        inputFile.close();
        outputFile.close();
        textOutputFile.close();

    }

    /**
     * 
     * This method reads the index file and corresponding data file and lists all
     * records by seeking to the correct position in the data file, reading the data
     * record,
     * and printing the combined record.
     * 
     * @param dataFileName  The name of the data file to be read.
     * @param indexFileName The name of the index file to be read.
     * @param keyLength     The length of the key value in each record.
     * @throws IOException If an I/O error occurs while reading the files.
     */
    public void listRecords(String dataFileName, String indexFileName, int keyLength) throws IOException {
        // Open index file
        RandomAccessFile indexFile = new RandomAccessFile(indexFileName, "r");
        // Open data file
        RandomAccessFile dataFile = new RandomAccessFile(dataFileName, "r");

        // Read each record from index file, seek to corresponding position in data
        // file, and print record
        while (indexFile.getFilePointer() < indexFile.length()) {
            // Read index record
            byte[] keyBytes = new byte[keyLength];
            indexFile.read(keyBytes);
            long pointer = indexFile.readLong();

            indexFile.readLine();

            // Seek to position in data file
            dataFile.seek(pointer);

            // Read data record
            byte[] dataBuffer = new byte[1000];
            int numBytesRead = dataFile.read(dataBuffer);

            // Check for end of file
            if (numBytesRead == -1) {
                break;
            }

            // Construct and print record
            String dataRecord = new String(dataBuffer, 0, Math.min(numBytesRead, 1000), StandardCharsets.US_ASCII);
            int newlineIndex = dataRecord.indexOf('\n');
            if (newlineIndex >= 0) {
                dataRecord = dataRecord.substring(0, newlineIndex);
            }
            String keyString = new String(keyBytes, StandardCharsets.US_ASCII);
            System.out.println(keyString + dataRecord.substring(keyLength));
        }

        indexFile.close();
        dataFile.close();
    }

    /**
     * 
     * Searches for a specific record in a data file using an index file and binary search algorithm.
     * 
     * @param dataFileName  the name of the data file to be searched
     * @param indexFileName the name of the index file to be used
     * @param keyLength     the length of the key field in bytes
     * @param keyValue      the key value of the record to be searched
     * @throws IOException if an I/O error occurs while reading the files
     */
    public void searchRecords(String dataFileName, String indexFileName, int keyLength, String keyValue)
            throws IOException {

        // Open data file
        RandomAccessFile inputFile = new RandomAccessFile(dataFileName, "r");
        // Open index file
        RandomAccessFile indexFile = new RandomAccessFile(indexFileName, "r");

        // Determine the length of each record in the index file
        int recordLength = keyLength + Long.BYTES + 1;

        // Determine the number of records in the index file
        long indexFileSize = indexFile.length();
        int numRecords = (int) (indexFileSize / recordLength);

        // Perform binary search on the records list
        int left = 0;
        int right = numRecords - 1;
        while (left <= right) {
            int mid = (left + right) / 2;

            // Seek to the start of the record in the index file
            long offset = (long) mid * recordLength;
            indexFile.seek(offset);

            // Read the key from the index file
            byte[] keyBytes = new byte[keyLength];

            indexFile.read(keyBytes);
            String key = new String(keyBytes, StandardCharsets.US_ASCII);

            // Compare the search key to the key in the current record
            int comparison = keyValue.compareTo(key);
            if (comparison == 0) {
                // match found

                // Read the pointer from the index file
                long pointer = indexFile.readLong();

                // Seek to the position in the input file and read the corresponding line
                inputFile.seek(pointer);
                String result = inputFile.readLine();
                System.out.println(result);
                return;
            } else if (comparison < 0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        // Close the files and return null if the search key is not found
        inputFile.close();
        indexFile.close();
        System.out.println("Record not found");
    }

}
