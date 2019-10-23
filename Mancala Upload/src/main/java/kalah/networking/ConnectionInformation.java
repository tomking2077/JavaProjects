package kalah.networking;

public class ConnectionInformation {
    private String serverAddress;
    private Integer socketNumber;

    public ConnectionInformation(String serverAddress, int socketNumber) {
        this.serverAddress = serverAddress;
        this.socketNumber = socketNumber;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public Integer getSocketNumber() {
        return socketNumber;
    }

    public void setSocketNumber(int socketNumber) {
        this.socketNumber = socketNumber;
    }
}
