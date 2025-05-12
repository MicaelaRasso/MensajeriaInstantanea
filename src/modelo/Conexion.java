package modelo;

import java.io.*;
import java.net.*;

public class Conexion {
    private Socket socket;
    private ServerSocket serverSocket;
    private PrintWriter out;
    private String IP;
    private int puerto;
	private Sistema sistema;

    public Conexion(String IP, int puerto, String nombreUsuario, Sistema sistema) throws IOException {
        this.IP = IP;
        this.puerto = puerto;
        this.sistema = sistema;
        iniciar();
    }
    
    public void iniciar() throws IOException {
        Runnable servidorRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(puerto);
                    System.out.println("Usuario escuchando en el puerto " + puerto);

                    while (true) {
                        Socket socket = serverSocket.accept();
                        System.out.println("Nueva conexión desde el servidor " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
                        recibirRequest(socket);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(servidorRunnable).start();
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
        cerrarConexion();
    }
    
    /*public void recibirRequest(Socket serverSocket) {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
			String requestRecibido = in.readLine();
			System.out.println("Recibiendo una respuesta...");
			if (requestRecibido != null) {
				System.out.println("Request recibido: " + requestRecibido);
				Request requestObj = JsonConverter.fromJson(requestRecibido);
				System.out.println(requestObj.getContenido());
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
	}*/
    
    public void recibirRequest(Socket serverSocket) {
        new Thread(() -> {  // Hacerlo en un hilo separado para que pueda recibir varios mensajes
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
                String requestRecibido;
                while ((requestRecibido = in.readLine()) != null) {
                    System.out.println("Recibiendo una respuesta...");
                    System.out.println("Request recibido: " + requestRecibido);
                    Request requestObj = JsonConverter.fromJson(requestRecibido);
                    System.out.println(requestObj.getContenido());

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
                    serverSocket.close();
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
