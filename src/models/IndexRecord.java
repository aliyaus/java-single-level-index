package models;

public class IndexRecord implements Comparable<IndexRecord> {
    String key;
    long pointer;

    public IndexRecord(String key, long pointer) {
        this.key = key;
        this.pointer = pointer;
    }

    public int compareTo(IndexRecord other) {
        return this.key.compareTo(other.key);
    }

    public String getKey() {
        return this.key;
    }
    
    public Long getPointer() {
        return this.pointer;
    }
}