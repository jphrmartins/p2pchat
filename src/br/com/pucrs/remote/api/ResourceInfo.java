package br.com.pucrs.remote.api;

public class ResourceInfo {
    private final String fileName;
    private final String hash;

    public ResourceInfo(String fileName, String hash) {
        this.fileName = fileName;
        this.hash = hash;
    }

    public String getFileName() {
        return fileName;
    }

    public String getHash() {
        return hash;
    }
}
