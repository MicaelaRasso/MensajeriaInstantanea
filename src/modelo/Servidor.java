package modelo;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.SwingUtilities;  // Importar SwingUtilities

public class Servidor {
    private final int puerto;
    private ServerSocket serverSocket;
    private static Sistema sistema = null;
    
    public Servidor(int puerto, Sistema sistema) {
        this.puerto = puerto;
        this.sistema = sistema;
    }

    // Método para iniciar el servidor en un hilo separado
    public void iniciar() {
        Runnable servidorRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // Crear un ServerSocket para escuchar conexiones
                    serverSocket = new ServerSocket(puerto);
                    System.out.println("Servidor escuchando en el puerto " + puerto);

                    // Aceptar conexiones de clientes
                    while (true) {
                        Socket socket = serverSocket.accept();
                        new ClientHandler(socket).start(); // Manejar cada cliente en un hilo
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        
        // Iniciar el hilo del servidor
        Thread servidorThread = new Thread(servidorRunnable);
        servidorThread.start(); // Esto inicia el servidor en un hilo separado
    }
    
    public void terminar() throws IOException {
        if (serverSocket != null) {
            serverSocket.close(); // Cerrar el ServerSocket cuando el servidor termine
        }
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
                    if ("exit".equalsIgnoreCase(message)) {
                        System.out.println("Cliente desconectado.");
                        break; // Salir del bucle si el cliente envía "exit"
                    }
                    try {
						sistema.recibirMensaje(message);
					} catch (Exception e) {
						e.printStackTrace();
					}                    
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
}
