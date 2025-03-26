package modelo;

import java.io.*;
import java.net.*;

public class Cliente {
    private static final String IP = "127.0.0.1"; // Server IP (localhost for testing)
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(IP, PORT);
            System.out.println("Connected to server!");

            // Create I/O streams
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            String message;
            while (true) {
                System.out.print("Enter message (or 'exit' to quit): ");
                message = userInput.readLine();
                if (message.equalsIgnoreCase("exit")) break; // Stop if user types "exit"

                out.println(message); // Send message
                System.out.println("Server Response: " + in.readLine()); // Receive response
            }

            socket.close();
            System.out.println("Disconnected from server.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
