package comms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.ServerSocket;

public class TcpServerSocket {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public TcpServerSocket(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public boolean accept(int timeout) throws SocketException, IllegalArgumentException, IOException {
        this.serverSocket.setSoTimeout(timeout);
        try {
            this.clientSocket = serverSocket.accept();
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(
                    new java.io.InputStreamReader(clientSocket.getInputStream()));
            return true;
        } catch (SocketTimeoutException e) {
            return false;
        } catch (SocketException e) {
            throw new SocketException(
                    "TCP Error: Could not open tcp server socket on port " + this.serverSocket.getLocalPort());
        } catch (IOException e) {
            throw new IOException("TCP Error: Could not accept connection on port " + this.serverSocket.getLocalPort());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("TCP Error: Invalid port number " + this.serverSocket.getLocalPort());
        }
    }

    public PrintWriter getOut() {
        return this.out;
    }

    public BufferedReader getIn() {
        return this.in;
    }

    public boolean close() {
        try {
            this.serverSocket.close();
            this.clientSocket.close();
            this.out.close();
            this.in.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
