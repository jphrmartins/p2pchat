package br.com.pucrs.remote.api;

import java.io.Serializable;
import java.util.Objects;

public class ResourceInfo implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceInfo that = (ResourceInfo) o;
        return Objects.equals(getFileName(), that.getFileName()) &&
                Objects.equals(getHash(), that.getHash());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFileName(), getHash());
    }
}
