package modelo;

public class Usuario {
	private String nombre;
	private int puerto;
	
	public Usuario(String nombre, int i) {
		super();
		this.nombre = nombre;
		this.puerto = i;
	}
	
	public String getNombre() {
		return nombre;
	}
	public int getPuerto() {
		return puerto;
	}
	
}
