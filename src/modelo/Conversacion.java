package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Conversacion {
	private ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();
	
	public Conversacion() {}
	
	//Metodos
	
	public void recibirMensaje(String contenido, String fechaYHoraStr, Contacto c) {
	    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;  // El formato que tienes es ISO 8601
        LocalDateTime fechaYHora = LocalDateTime.parse(fechaYHoraStr, formatter);
        mensajes.add(new Mensaje(c, contenido, fechaYHora));
	}

	public void enviarMensaje(Mensaje mensaje, Contacto contacto) {
		mensajes.add(mensaje);
	}
	
	//Getters
	public ArrayList<Mensaje> getMensajes() {
		return mensajes;
	}
}
