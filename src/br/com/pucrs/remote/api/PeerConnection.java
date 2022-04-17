package br.com.pucrs.remote.api;

public class PeerConnection {
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
}
