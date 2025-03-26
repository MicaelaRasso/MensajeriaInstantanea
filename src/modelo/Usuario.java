package modelo;

public class Usuario {
	private String nombre;
	private String puerto;
	
	public Usuario(String nombre, String puerto) {
		super();
		this.nombre = nombre;
		this.puerto = puerto;
	}
	
	public String getNombre() {
		return nombre;
	}
	public String getPuerto() {
		return puerto;
	}
	
}
