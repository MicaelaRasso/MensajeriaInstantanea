package modelo;

import java.time.LocalDateTime;

public class Mensaje {
	private String contenido;
	private Usuario usuario;
	private LocalDateTime fechaYHora;

	public Mensaje(Usuario usuario, String contenido, LocalDateTime fechaYHora) {
		this.usuario = usuario;
		this.contenido = contenido;
		this.fechaYHora = fechaYHora;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public String getContenido() {
		return contenido;
	}

	public LocalDateTime getFechaYHora() {
		return fechaYHora;
	}

	@Override
	public String toString() {
		return usuario.getNombre() + ": // " + contenido + " // " + fechaYHora + " // " + usuario.getPuerto();
	}
	
	public String paraMostrar() {
		String salto = System.lineSeparator();
		return usuario.toString() + ": " + salto + contenido + salto + fechaYHora;
	}

}
