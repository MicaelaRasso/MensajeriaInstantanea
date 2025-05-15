package modelo;

public class Usuario {
	private String nombre;
	private String IP;
	
	public Usuario() {
		super();
		this.nombre = "";
		this.IP = "";
	}
	
	public Usuario(String nombre,String IP) {
		super();
		this.nombre = nombre;
		this.setIP(IP);
	}
	
	public String getNombre() {
		return nombre;
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

	public void setNombre(String value) {
		// TODO Auto-generated method stub
		this.nombre = value;
	}
	
}
