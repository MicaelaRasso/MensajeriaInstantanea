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
    }

	private void conectar() throws IOException {
        try {
            this.socket = new Socket("127.0.0.1", 5000);
            this.out = new PrintWriter(socket.getOutputStream(), true);

        } catch (IOException e) {
            System.err.println("Error al conectar con el servidor" + e.getMessage());
            throw e;
        }
    }
   
    public void enviarRequest(Request request) throws IOException {
    	String Json = JsonConverter.toJson(request);
        conectar();
    	if (socket == null || socket.isClosed()) {
            throw new IOException("No hay conexión activa con el servidor.");
        }

        if (out == null) {
            throw new IOException("OutputStream no disponible para enviar mensaje.");
        }
        
        out.println(Json);  // Envía el mensaje como Json
        out.flush();

        System.out.println("Mensaje enviado: " + request.getContenido());

        cerrarConexion(); // Si querés cerrar luego de enviar. Opcional.
    }
    
    public void recibirRequest() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String requestRecibido = in.readLine();
			if (requestRecibido != null) {
				System.out.println("Request recibido: " + requestRecibido);
				Request requestObj = JsonConverter.fromJson(requestRecibido);
				if (requestObj.getOperacion().equals("mensaje")) {
					sistema.recibirMensaje(requestObj);
				} else if (requestObj.getOperacion().equals("consulta")) {
					sistema.recibirConsulta(requestObj);
				} else if (requestObj.getOperacion().equals("registro")) {
					//sistema.recibirRegistro(rObj);
				}
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
