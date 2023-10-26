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

    public void connect() throws IOException, IllegalArgumentException {
        try {
            this.clientSocket = new Socket(this.address, this.port);
            this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            throw new IOException("TCP Error: Could not open tcp client socket on port " + this.port);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("TCP Error: Invalid port number " + this.port);
        } catch (Exception e) {
            throw new IOException("TCP Error: Client connection failed");
        }

        try {
            this.in = new BufferedReader(
                    new java.io.InputStreamReader(this.clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new IOException("InputStream Error: Could not get inputstream from socket on port " + this.port);
        }
    }

    public void close() throws IOException {
        try {
            this.clientSocket.close();
            this.out.close();
            this.in.close();

        } catch (IOException e) {
            throw new IOException("TCP Error: Could not close tcp client socket on port " + this.port);
        }
    }

    public PrintWriter getOut() {
        return this.out;
    }

    public BufferedReader getIn() {
        return this.in;
    }
}
