package modelo;

import java.io.IOException;

public class Usuario {
	private String nombre;
	private int puerto;
	private Conexion conexion;
	
	public Usuario(String nombre, String IP, int puerto) {
		super();
		this.nombre = nombre;
		this.puerto = puerto;
		try {
			this.conexion = new Conexion(IP, puerto, nombre);
		} catch (IOException e) {
			System.out.println("No pudo generarse la conexion");
			e.printStackTrace();
		}
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public Conexion getConexion() {
		return conexion;
	}

	public int getPuerto() {
		return puerto;
	}

	@Override
	public String toString() {
		return nombre;
	}
	
}
