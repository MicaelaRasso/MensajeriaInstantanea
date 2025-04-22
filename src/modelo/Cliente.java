package modelo;

import java.io.*;
import java.net.*;

public class Cliente {
    private Socket socket;
    private PrintWriter out;
    private String IP;
    private int puerto;

    public Cliente(String IP, int puerto) throws IOException {
        this.IP = IP;
        this.puerto = puerto;
        conectar();
    }

    private void conectar() throws IOException {
        try {
            this.socket = new Socket(IP, puerto);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Conectado con " + IP + ":" + puerto);
        } catch (IOException e) {
            System.err.println("Error al conectar con " + IP + ":" + puerto + " - " + e.getMessage());
            throw e;
        }
    }

    public void enviarMensaje(Mensaje mensaje) throws IOException {
        if (socket == null || socket.isClosed()) {
            try {
                conectar(); // Intentar reconectar si el socket está cerrado
            } catch (IOException e) {
                throw new IOException("No se pudo reconectar con " + IP + ":" + puerto, e);
            }
        }

        if (out == null) {
            throw new IOException("No hay conexión establecida con " + IP + ":" + puerto);
        }

        out.println(mensaje.toString());
        out.flush();
        System.out.println("Enviando mensaje: " + mensaje);
        System.out.println("Mensaje enviado a " + IP + ":" + puerto);
        cerrarConexion();
    }


    public void cerrarConexion() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Conexión cerrada con " + IP + ":" + puerto);
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar la conexión con " + IP + ":" + puerto);
        }
    }
}
