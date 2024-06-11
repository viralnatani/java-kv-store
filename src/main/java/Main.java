import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible
        // when running tests.
        System.out.println("Logs from your program will appear here!");

        // Uncomment this block to pass the first stage
        ServerSocket serverSocket = null;
        int port = 6379;
        try {
            serverSocket = new ServerSocket(port);
            // Since the tester restarts your program quite often, setting SO_REUSEADDR
            // ensures that we don't run into 'Address already in use' errors
            serverSocket.setReuseAddress(true);

            while (true) {
                Socket socket = serverSocket.accept();
                Thread thread1 = new Thread(new SocketConcurrent(socket));
                thread1.start();
            }

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}

class SocketConcurrent implements Runnable {

    private Socket serverSocket;

    SocketConcurrent(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {

        try {
            System.out.println(Thread.currentThread().getName() + "executing");
            // Wait for connection from client.
            PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            String input;
            while ((input = in.readLine()) != null) {
                if (input.equalsIgnoreCase("PING")) {
                    out.println("+PONG\r");
                }
            }

        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }

    }
}
