package modelo;

import java.util.ArrayList;

public class Chat {
	private Usuario emisor;
	private Usuario receptor;
	private ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();
	
	public Chat(Usuario emisor, Usuario receptor, ArrayList<Mensaje> mensajes) {
		super();
		this.emisor = emisor;
		this.receptor = receptor;
		this.mensajes = mensajes;
	}

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
