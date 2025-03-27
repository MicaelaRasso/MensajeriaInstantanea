package modelo;

import java.time.LocalDateTime;

public class Mensaje {
	private String contenido;
	private LocalDateTime fechaYHora;

	public Mensaje(String contenido, LocalDateTime fechaYHora) {
		this.contenido = contenido;
		this.fechaYHora = fechaYHora;
	}

	public String getContenido() {
		return contenido;
	}

	public LocalDateTime getFechaYHora() {
		return fechaYHora;
	}

}
