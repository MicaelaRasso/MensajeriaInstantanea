package modelo;

import java.util.ArrayList;

public class Conversacion {
	private ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();
	
	public Conversacion() {}
	
	//Metodos
	
	public void recibirMensaje(Mensaje mensaje) {
		mensajes.add(mensaje);
	}
	
	//Getters
	public ArrayList<Mensaje> getMensajes() {
		return mensajes;
	}


	
}
