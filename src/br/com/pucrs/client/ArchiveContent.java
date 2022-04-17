package br.com.pucrs.client;

public class ArchiveContent {
    private final String name;
    private final byte[] content;

    public ArchiveContent(String name, byte[] content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public byte[] getContent() {
        return content;
    }
}
