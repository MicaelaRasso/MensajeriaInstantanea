package modelo;

import java.io.*;
import java.net.*;

public class Servidor {

    private static final int PORT = 12345; // Change this if needed

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress());

                // Create I/O streams
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String msg;
                while ((msg = in.readLine()) != null) { // Read multiple messages
                    System.out.println("Client: " + msg);
                    out.println("Server received: " + msg); // Send response
                }

                socket.close();
                System.out.println("Client disconnected.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
