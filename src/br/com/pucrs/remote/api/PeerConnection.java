package br.com.pucrs.remote.api;

import java.io.Serializable;
import java.util.Objects;

public class PeerConnection implements Serializable {
    private final String address;
    private final String port;

    private final String userName;

    public PeerConnection(String address, String port, String userName) {
        this.address = address;
        this.port = port;
        this.userName = userName;
    }

    public String getAddress() {
        return address;
    }

    public String getPort() {
        return port;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "PeerConnection{" +
                "address='" + address + '\'' +
                ", port='" + port + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeerConnection that = (PeerConnection) o;
        return Objects.equals(getAddress(), that.getAddress()) &&
                Objects.equals(getPort(), that.getPort()) &&
                Objects.equals(getUserName(), that.getUserName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAddress(), getPort(), getUserName());
    }
}
