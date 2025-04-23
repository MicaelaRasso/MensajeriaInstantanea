package modelo;

import java.io.*;
import java.net.*;

public class Conexion {
    private Socket socket;
    private PrintWriter out;
    private String IP;
    private int puerto;
	private Sistema sistema;

    public Conexion(String IP, int puerto, String nombreUsuario, Sistema sistema) throws IOException {
        this.IP = IP;
        this.puerto = puerto;
        this.sistema = sistema;
        conectar(nombreUsuario);
        cerrarConexion();
    }

	private void conectar(String nombreUsuario) throws IOException {
        try {
            this.socket = new Socket("127.0.0.1", 5000);
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
	
	public boolean agregarContacto(String nombreContacto) throws IOException {
		//pregunta a al servidor si existe este usuario
		return true;
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
    
    public void recibirMensaje() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String mensajeRecibido = in.readLine();
			if (mensajeRecibido != null) {
				System.out.println("Mensaje recibido: " + mensajeRecibido);
				sistema.recibirMensaje(mensajeRecibido);
			}
		} catch (IOException e) {
			System.err.println("Error al recibir el mensaje: " + e.getMessage());
		}
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
