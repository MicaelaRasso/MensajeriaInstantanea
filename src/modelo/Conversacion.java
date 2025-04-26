package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Conversacion {
	private Contacto contacto;
	private ArrayList<Mensaje> mensajes = new ArrayList<Mensaje>();
	private boolean tieneNotificacion = false;
	
	public Conversacion(Contacto contacto) {
		this.contacto = contacto;
	}
	
	//Metodos
	
	public boolean tieneNotificacion() {
	    return tieneNotificacion;
	}

	public void setNotificacion(boolean notificacion) {
	    this.tieneNotificacion = notificacion;
	}

	
	public void recibirMensaje(String contenido, LocalDateTime fechaYHoraStr, Contacto c) {
	    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;  // El formato que tiene es ISO 8601
        LocalDateTime fechaYHora = LocalDateTime.parse(fechaYHoraStr, formatter);
        mensajes.add(new Mensaje(c, contenido, fechaYHora));
	}

	public void agregarMensaje(String mensaje, LocalDateTime fechaYHora, Contacto c) {
		mensajes.add(new Mensaje(c, mensaje, fechaYHora));
	}
	
	//Getters
	public ArrayList<Mensaje> getMensajes() {
		return mensajes;
	}

	public Contacto getContacto() {
		return contacto;
	}
	
}
