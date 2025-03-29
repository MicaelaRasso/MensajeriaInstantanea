package modelo;

public class Usuario {
	private String nombre;
	private int puerto;
	
	public Usuario(String nombre, int puerto) {
		super();
		this.nombre = nombre;
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
	
}
