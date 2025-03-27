package modelo;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Servidor {
    private final int puerto;
    private ServerSocket serverSocket;
    
    public Servidor(int puerto) {
    	this.puerto = puerto;
    }
    
    public void iniciar() throws IOException {
    	try (ServerSocket serverSocket = new ServerSocket(puerto)) {
    		this.serverSocket = serverSocket;
            System.out.println("Servidor escuchando en el puerto " + puerto);
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
         }
    }
    
    public void terminar() throws IOException {
		this.serverSocket.close();
    }
    
    private static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String message;
                while ((message = in.readLine()) != null) {
                    if ("exit".equalsIgnoreCase(message)) { // Comparar correctamente
                        System.out.println("Cliente desconectado.");
                        break; // Salir del bucle si el cliente envía "exit"
                    }
                    System.out.println("Mensaje recibido: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close(); // Asegurarse de cerrar el socket
                    System.out.println("Conexion cerrada.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
			System.out.print("Ingresa el puerto del servidor: ");
			int puerto = scanner.nextInt();

	        new Servidor(puerto).iniciar();
		}
    }
}
