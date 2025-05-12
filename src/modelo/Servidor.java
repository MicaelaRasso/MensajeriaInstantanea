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
        Servidor.sistema = sistema;
    }

    public static boolean isPortAvailable(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Método para iniciar el servidor en un hilo separado
    public void iniciar() throws IOException {
        Runnable servidorRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (!isPortAvailable(puerto)) {
                        throw new IOException("Error: Puerto " + puerto + " no disponible.");
                    }
                    
                    serverSocket = new ServerSocket(puerto);
                    System.out.println("Servidor escuchando en el puerto " + puerto);

                    while (true) {
                        Socket socket = serverSocket.accept();
                        new ClientHandler(socket).start(); // Manejar cada cliente en un hilo
                    }

                } catch (IOException e) {
                    try {
                        throw e;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };
        new Thread(servidorRunnable).start();
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
            	try {
            	    String message = in.readLine(); // Lee solo un mensaje
            	    if (message != null && !"exit".equalsIgnoreCase(message)) {
            	        System.out.println("Recibiendo mensaje: " + message);
            	        sistema.recibirMensaje(message);
            	    }
            	} catch (Exception e) {
            	    e.printStackTrace();
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
