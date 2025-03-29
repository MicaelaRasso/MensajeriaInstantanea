package modelo;

import java.util.ArrayList;

public class Sistema {
	private Usuario usuario;
	private ArrayList<Contacto> agenda = new ArrayList<Contacto>();
	private ArrayList<Conversacion> conversaciones = new ArrayList<Conversacion>();
	private Servidor servidor;

	public Sistema(Usuario usuario) {
		this.usuario = usuario;
		this.servidor = new Servidor(usuario.getPuerto());
	}

	//Metodos

	public void iniciarServidor(String nombre, int puerto) {
		this.usuario = new Usuario(nombre, puerto);
		this.servidor = new Servidor(puerto);
	}

	public void agregarContacto(Contacto contacto) {
		agenda.add(contacto);
		contacto.setConversacion(new Conversacion());
	}

	public Conversacion getConversacion(Contacto c) {
		return c.getConversacion();
	}
	
	
	
	//Getters
	
	public Usuario getUsuario() {
		return usuario;
	}

	public Servidor getServidor() {
		return servidor;
	}

	public ArrayList<Contacto> getAgenda() {
		return agenda;
	}

	public ArrayList<Conversacion> getConversaciones() {
		return conversaciones;
	}

}
