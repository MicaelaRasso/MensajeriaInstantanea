package modelo;

public class Contacto extends Usuario {
	private String ip;
	private Conversacion conversacion;
	
	public Contacto(String nombre, String IP, int puerto) {
		super(nombre, puerto);
		this.ip = IP;
	}

	public void recibirMensaje(Mensaje mensaje) {
		conversacion.recibirMensaje(mensaje);
	}
	

	//getters
	public String getIP() {
		return ip;
	}

	public Conversacion getConversacion() {
		return conversacion;
	}
}