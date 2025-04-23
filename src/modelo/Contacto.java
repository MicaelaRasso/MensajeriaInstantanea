package modelo;

import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Contacto{
	private String nombre;
    private Conversacion conversacion;

    public Contacto(String nombre) {
        this.setNombre(nombre);
    }

    public Conversacion getConversacion() {
        return conversacion;
    }

    public void setConversacion(Conversacion conversacion) {
        this.conversacion = conversacion;
    }

    @Override
    public String toString() {
        return super.toString();
    }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
