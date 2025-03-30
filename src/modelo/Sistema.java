package modelo;

import java.io.IOException;
import java.time.LocalDateTime;
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
		Conversacion conv = new Conversacion();
		contacto.setConversacion(conv);
		conversaciones.add(conv);		
	}

	public Conversacion getConversacion(Contacto c) {
		return c.getConversacion();
	}
	

	public void enviarMensaje(String m, Contacto contacto) throws IOException {
		Mensaje mensaje = new Mensaje(usuario, m, LocalDateTime.now());
		contacto.getConversacion().recibirMensaje(mensaje);
		contacto.getCliente().enviarMensaje(mensaje);
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
