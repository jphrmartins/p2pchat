package br.com.pucrs.client;

import br.com.pucrs.remote.api.ResourceInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ArchiveRpository {
    private final Map<String, ArchiveContent> hashCodes;
    private String directory;

    public ArchiveRpository(String directoryToRead) {
        this.hashCodes = readArchives(directoryToRead);
        this.directory = directoryToRead;
    }

    public byte[] getFileContent(String hash) {
        return hashCodes.get(hash).getContent();
    }

    public List<ResourceInfo> getAllResources() {
        return hashCodes.entrySet().stream()
                .map(it -> new ResourceInfo(it.getValue().getName(), it.getKey()))
                .collect(Collectors.toList());
    }

    private Map<String, ArchiveContent> readArchives(String directory) {
        Map<String, ArchiveContent> hashCodes = new HashMap<>();
        File dir = new File(directory);
        File[] files = Objects.requireNonNull(dir.listFiles());
        for (File file : files) {
            try {
                byte[] content = Files.readAllBytes(file.toPath());
                hashCodes.put(md5(content), new ArchiveContent(file.getName(), content));
            } catch (IOException e) {
                System.err.println("Error while reading archive. " + e);
            }
        }
        return hashCodes;
    }

    private String md5(byte[] content) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashedContent = md.digest(content);
            StringBuilder hexHash = new StringBuilder();
            for (byte bte : hashedContent) {
                hexHash.append(Integer.toHexString(0xFF & bte));
            }
            return hexHash.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDirectory() {
        return directory;
    }
}
