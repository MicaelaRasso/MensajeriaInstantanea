package modelo;

import java.time.LocalDateTime;

public class Mensaje {
	private String contenido;
	private Contacto contacto;
	private LocalDateTime fechaYHora;

	public Mensaje(Contacto contacto, String contenido, LocalDateTime fechaYHora) {
		this.contacto = contacto;
		this.contenido = contenido;
		this.fechaYHora = fechaYHora;
	}

	public Contacto getUsuario() {
		return contacto;
	}

	public String getContenido() {
		return contenido;
	}

	public LocalDateTime getFechaYHora() {
		return fechaYHora;
	}

	@Override
	public String toString() {
		return contacto.getNombre() + ": // " + contenido + " // " + fechaYHora;
	}
	
	public String paraMostrar() {
		String salto = System.lineSeparator();
		return contacto.toString() + ": " + salto + contenido + salto + fechaYHora;
	}

}
