package modelo;

import java.io.IOException;

public class Usuario {
	private String nombre;
	private String IP;
	private int puerto;
	
	public Usuario(String nombre, String IP, int puerto) {
		super();
		this.nombre = nombre;
		this.setIP(IP);
		this.puerto = puerto;
	}
	
	public String getNombre() {
		return nombre;
	}

	public int getPuerto() {
		return puerto;
	}

	@Override
	public String toString() {
		return nombre;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}
	
}
