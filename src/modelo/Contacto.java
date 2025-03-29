package modelo;

public class Contacto extends Usuario {
	private String ip;
	private Conversacion conversacion;
	private Cliente cliente;
	
	public Contacto(String nombre, String IP, int puerto) {
		super(nombre, puerto);
		this.ip = IP;
		this.cliente = new Cliente(IP,puerto);
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

	public void setConversacion(Conversacion conversacion) {
		this.conversacion = conversacion;
	}

	public Cliente getCliente() {
		return cliente;
	}
	
	
}