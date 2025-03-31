package modelo;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Cliente {
    private int puerto;
    private String IP;

    public Cliente(String IP, int puerto) {
		this.puerto = puerto;
		this.IP = IP;
	}

	public void enviarMensaje(Mensaje mensaje) throws IOException {
		try (Socket socket = new Socket(IP, puerto)) {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			    String mensajeStr = mensaje.getUsuario().getNombre() + "//" +
			                        mensaje.getContenido() + "//" +
			                        mensaje.getFechaYHora().toString();
			out.println(mensajeStr);
		}        
	}
}
