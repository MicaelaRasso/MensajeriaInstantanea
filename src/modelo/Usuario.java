package modelo;

public class Usuario {
	private String nombre;
	private String IP;
	private int puerto;
	
	public Usuario() {
		super();
		this.nombre = "";
		this.IP = "";
		this.puerto = 0;
	}
	
	public Usuario(String nombre,String IP, int puerto) {
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

	public void setPuerto(int int1) {
		// TODO Auto-generated method stub
		this.puerto = int1;
		
	}

	public void setNombre(String value) {
		// TODO Auto-generated method stub
		this.nombre = value;
	}
	
}
