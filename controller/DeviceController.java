package controller;

import types.Readable;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class DeviceController {
    private PrintWriter out;
    private Readable in;
    private Socket clientSocket;

    private boolean terminated = false;

    public abstract void start();

    public void setOutputWriter(PrintWriter x) {
        out = x;
    }

    public void setInputReader(types.Readable x) {
        in = x;
    }

    public void setClientSocket(Socket x) {
        clientSocket = x;
    }

    public PrintWriter getOutputWriter() {
        return out;
    }

    public Readable getInputReader() {
        return in;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void sendData(String data) {
        getOutputWriter().println(data);
    }

    public String readData() throws IOException {
        return getInputReader().getInput();
    }

    public String readDataNonBlocking() throws IOException {
        return getInputReader().getInputNonBlocking();
    }

    public boolean isTerminated() {
        return terminated;
    }

    public void terminate() {
        terminated = true;
        if (clientSocket != null && !clientSocket.isClosed()) {
            try {
                sendData("END");
                clientSocket.close();
            } catch (IOException e) {

            }
        }
    }
}
