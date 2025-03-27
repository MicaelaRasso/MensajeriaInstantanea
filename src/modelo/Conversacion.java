package modelo;

import java.util.ArrayList;

public class Conversacion {
	private Usuario emisor;
	private Usuario receptor;
	private ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();
	
	public Conversacion(Usuario emisor, Usuario receptor, ArrayList<Mensaje> mensajes) {
		super();
		this.emisor = emisor;
		this.receptor = receptor;
		this.mensajes = mensajes;
	}
	
	//Metodos
	
	public void recibirMensaje(Mensaje mensaje) {
		mensajes.add(mensaje);
	}
	
	//Getters
	
	public Usuario getEmisor() {
		return emisor;
	}
	public Usuario getReceptor() {
		return receptor;
	}
	public ArrayList<Mensaje> getMensajes() {
		return mensajes;
	}


	
}
