package modelo;

import java.io.*;
import java.net.*;

public class Conexion {
    private Socket socket;
    private PrintWriter out;
    private String IP;
    private int puerto;

    public Conexion(String IP, int puerto, String nombreUsuario) throws IOException {
        this.IP = IP;
        this.puerto = puerto;
        conectar(nombreUsuario);
    }

    
    private void conectar(String nombreUsuario) throws IOException {
        try {
            this.socket = new Socket(IP, puerto);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(nombreUsuario); // Enviamos el nombre
            String respuesta = in.readLine(); // Esperamos la respuesta del servidor

            if ("1".equals(respuesta)) {
                System.out.println("Conexión establecida con éxito como " + nombreUsuario);
            } else {
                System.err.println("Nombre de usuario en uso. Conexión rechazada.");
                socket.close();
                throw new IOException("Conexión rechazada por el servidor.");
            }
        } catch (IOException e) {
            System.err.println("Error al conectar con " + IP + ":" + puerto + " - " + e.getMessage());
            throw e;
        }
    }

    
    public void enviarMensaje(Mensaje mensaje) throws IOException {
        conectar(mensaje.getUsuario().getNombre());
    	if (socket == null || socket.isClosed()) {
            throw new IOException("No hay conexión activa con el servidor.");
        }

        if (out == null) {
            throw new IOException("OutputStream no disponible para enviar mensaje.");
        }
        

        out.println(mensaje.toString());  // Envía el mensaje como string formateado
        out.flush();

        System.out.println("Mensaje enviado: " + mensaje.toString());

        cerrarConexion(); // Si querés cerrar luego de enviar. Opcional.
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
