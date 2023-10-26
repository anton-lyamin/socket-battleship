package comms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClientSocket {
    int port;
    InetAddress address;
    Socket clientSocket;
    PrintWriter out;
    BufferedReader in;

    public TcpClientSocket(InetAddress address, int port) throws UnknownHostException {
        this.port = port;
        this.address = address;
    }

    public boolean connect() throws IOException, IllegalArgumentException {
        this.clientSocket = new Socket(this.address, this.port);

        this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(
                new java.io.InputStreamReader(this.clientSocket.getInputStream()));
        return true;
    }

    public boolean close() {
        try {
            this.clientSocket.close();
            this.out.close();
            this.in.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public PrintWriter getOut() {
        return this.out;
    }

    public BufferedReader getIn() {
        return this.in;
    }
}
