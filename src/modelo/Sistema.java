package modelo;

import java.util.ArrayList;

public class Sistema {
	private Usuario usuario;
	private ArrayList<Usuario> agenda = new ArrayList<Usuario>();
	private ArrayList<Conversacion> conversaciones = new ArrayList<Conversacion>();

	public Sistema(Usuario usuario) {
		this.usuario = usuario;
	}

	//Metodos
	/*
	public Cliente agregarContacto(Contacto contacto) {
		agenda.add(contacto);
		
		return new Cliente(contacto);
	}
*/
	//Getters
	
	public Usuario getUsuario() {
		return usuario;
	}

	public ArrayList<Usuario> getAgenda() {
		return agenda;
	}

	public ArrayList<Conversacion> getConversaciones() {
		return conversaciones;
	}
}
