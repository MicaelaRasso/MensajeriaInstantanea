package modelo;

import java.io.*;
import java.net.*;

public class Conexion {
    private Socket socket;
    private boolean connected = false;
    private PrintWriter out;
    private String IP;
	private Sistema sistema;

    public Conexion(String IP, String nombreUsuario, Sistema sistema) throws IOException {
        this.IP = IP;
        this.sistema = sistema;
        try {        	
        	this.conectar();
            System.out.println("Conexion establecida con el servidor");
        }catch (IOException e) {
            System.err.println("Error al conectar con el servidor" + e.getMessage());
            throw e;
        }
    }
    
    public void conectar() throws IOException {
    	try {    		
    		this.socket = new Socket(ConfigLoader.host, ConfigLoader.port);
    		this.out = new PrintWriter(socket.getOutputStream(), true);
    		this.recibirRequest(socket);
    		this.connected = true;
    	}catch(IOException e){
    		throw new IOException();
    	}
    }

   
    public void enviarRequest(Request request) throws IOException {
    	
    	if(connected == false) {
    		System.out.println("entra");
    		conectar();
    	}
    	String Json = JsonConverter.toJson(request);
    	if (socket == null || socket.isClosed()) {
            throw new IOException("No hay conexión activa con el servidor.");
        }

        if (out == null) {
            throw new IOException("OutputStream no disponible para enviar mensaje.");
        }
        
        out.println(Json);  // Envía el mensaje como Json
        out.flush();
        System.out.println("Mensaje enviado: " + Json);
    }
  
    
    public void recibirRequest(Socket socket) {
        new Thread(() -> {  // Hacerlo en un hilo separado para que pueda recibir varios mensajes
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String requestRecibido;
                while ((requestRecibido = in.readLine()) != null) {
                    System.out.println("Recibiendo una respuesta...");
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
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start(); // Importante: en un hilo aparte
    }

    public void cerrarConexion() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Conexión cerrada con " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar la conexión con " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
        }
    }
    
}
