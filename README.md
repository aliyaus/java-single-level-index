# Java Single Level Index 

# Key Concepts 
* Random access files: The program uses RandomAccessFile to read and write to files with random access, which allows the program to read and write to specific locations in the file rather than just reading from the beginning.
* Indexing: The program creates an index file that maps keys to the locations of the corresponding records in the data file. This allows for faster searching of the data file, as the program can use binary search on the index file to quickly find the location of a record in the data file.
* Binary search: The program uses binary search to find the location of a record in the data file based on its key. Binary search is an efficient algorithm for finding an item in a sorted list by repeatedly dividing the search interval in half.
* Byte buffers: The program uses a ByteBuffer to combine the bytes of the key and the offset into a single byte array that is written to the index file.
* Sorting: The program sorts the index records by key using Collections.sort(). Sorting is an important technique for organizing data and allows for more efficient searching and processing of the data.

### to create an index file 
`-c input.txt output.idx 4` or `c input2.txt output.idx 4` 

### to list all records 
`-l input.txt output.idx 4` or `-l input2.txt output.idx 4`

### to search records 
`-s input.txt output.idx 4 AAAA` or `-s input2.txt output.idx 4 0000`