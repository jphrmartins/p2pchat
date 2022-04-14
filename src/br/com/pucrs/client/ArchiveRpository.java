package br.com.pucrs.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ArchiveRpository {
    private final Map<String, String> hashCodes;

    public ArchiveRpository(String directoryToRead) {
        this.hashCodes = readArchives(directoryToRead);
    }

    private Map<String, String> readArchives(String directory) {
        Map<String, String> hashCodes = new HashMap<>();
        File dir = new File(directory);
        File[] files = Objects.requireNonNull(dir.listFiles());
        StringBuilder message = new StringBuilder();
        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    message.append(line);
                }
            } catch (IOException e) {
                System.err.println("Error while reading archive. " + e);
            }

            hashCodes.put("" + message.toString().hashCode(), message.toString()); //Todo hash tem que ser md5
            message = new StringBuilder();
        }
        return hashCodes;
    }

    public String getFileContent(String hash) {
        return hashCodes.get(hash);
    }

}
