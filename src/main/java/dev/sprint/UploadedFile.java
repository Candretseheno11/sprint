package main.java.dev.sprint;

public class UploadedFile {
    private final String originalName;
    private final String storedName;
    private final long size;
    private final String contentType;

    public UploadedFile(String originalName, String storedName, long size, String contentType) {
        this.originalName = originalName;
        this.storedName = storedName;
        this.size = size;
        this.contentType = contentType;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getStoredName() {
        return storedName;
    }

    public long getSize() {
        return size;
    }

    public String getContentType() {
        return contentType;
    }
}
