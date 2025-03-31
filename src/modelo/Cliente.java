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
/*    
	public static void main(String[] args) throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
			// Pedir IP y puerto al usuario
			System.out.print("Ingresa la direccion IP del servidor: ");
			String ip = scanner.nextLine();

			System.out.print("Ingresa el puerto del servidor: ");
			int puerto = scanner.nextInt();

			// Iniciar cliente con los valores ingresados
			Cliente cliente = new Cliente(ip, puerto);
			//cliente.iniciar();
			///hola a ver si rompo todo
		}
    }
*/
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

