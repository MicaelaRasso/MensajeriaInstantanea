package modelo;

public class Contacto extends Usuario {
	private String ip;
	
	public Contacto(String nombre, String puerto, String IP) {
		super(nombre, puerto);
		this.ip = IP;
	}

	public String getIP() {
		return ip;
	}
	
}