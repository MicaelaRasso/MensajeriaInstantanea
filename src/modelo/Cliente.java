package modelo;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Cliente {
   // private Contacto contacto;
    private int puerto;
    private String IP;
/*
    public Cliente(Contacto contacto) {
		super();
		this.contacto = contacto;	
	}
*/
    public Cliente(String IP, int puerto) {
		this.puerto = puerto;
		this.IP = IP;
	}

	public void iniciar() throws IOException {
	        try (//Socket socket = new Socket(contacto.getIP(), contacto.getPuerto());
	        	Socket socket = new Socket(IP, puerto);
	            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	            Scanner scanner = new Scanner(System.in)) {

	            //System.out.println("Conectado a " + contacto.getIP() + ":" + contacto.getPuerto());
	            System.out.println("Conectado a " + IP + ":" + puerto);
	        	System.out.println("Escribe un mensaje (o 'salir' para cerrar):");

	            while (true) {
	                System.out.print("> ");
	                String message = scanner.nextLine();

	                if (message.equalsIgnoreCase("salir")) {
	                    System.out.println("Desconectando...");
	                    break;
	                }

	                out.println(message);
	            }
	        }
    }
    
	public static void main(String[] args) throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
			// Pedir IP y puerto al usuario
			System.out.print("Ingresa la direccion IP del servidor: ");
			String ip = scanner.nextLine();

			System.out.print("Ingresa el puerto del servidor: ");
			int puerto = scanner.nextInt();

			// Iniciar cliente con los valores ingresados
			Cliente cliente = new Cliente(ip, puerto);
			cliente.iniciar();
		}
    }
}

