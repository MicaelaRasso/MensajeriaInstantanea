package modelo;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.SwingUtilities;

import controlador.Controlador;

public class Sistema {
	private Usuario usuario;
	private HashMap<String,Contacto> agenda = new HashMap<String,Contacto>();
	private ArrayList<Conversacion> conversaciones = new ArrayList<Conversacion>();
	private static Servidor servidor;
	private Controlador controlador;

	public Sistema(Usuario usuario, Controlador controlador) {
		this.usuario = usuario;
		this.controlador = controlador;
	}

	//Metodos

	public void iniciarServidor(String nombre, int puerto) throws IOException {
		this.usuario = new Usuario(nombre, puerto);
		Sistema.servidor = new Servidor(puerto, this);
		Sistema.servidor.iniciar();
	}

	public void agregarContacto(Contacto contacto) {
		agenda.put(contacto.getNombre(), contacto);
		Conversacion conv = new Conversacion(contacto);
		contacto.setConversacion(conv);
		conversaciones.add(conv);		
	}

	public Conversacion getConversacion(Contacto c) {
		return c.getConversacion();
	}
	

	public void enviarMensaje(String m, Contacto contacto) throws IOException {
		Mensaje mensaje = new Mensaje(usuario, m, LocalDateTime.now());
		contacto.enviarMensaje(mensaje);
		contacto.getCliente().enviarMensaje(mensaje);
	}	

	public void recibirMensaje(String s, String ip, int puerto) {
	    try {
	        String[] partes = s.split("//");

	        if (partes.length == 3) {
	            String nombre = partes[0].replace(":", "").trim();
	            String contenido = partes[1].trim();
	            String fechaYHoraStr = partes[2].trim();
	            Contacto cont;
	            if(agenda.containsKey(nombre)) {
	            	cont = agenda.get(nombre);
	            } else {
	            	// Si el contacto no existe, lo agregamos a la agenda
	            	cont = new Contacto(nombre, ip, puerto);
	            	agenda.put(nombre, cont);
	            	Conversacion conv = new Conversacion(cont);
	            	cont.setConversacion(conv);
	            	conversaciones.add(conv);
	            }
	            

	            cont.recibirMensaje(contenido, fechaYHoraStr);
	            SwingUtilities.invokeLater(() -> 
	            		{
	            			controlador.nuevoMensaje();
	        	            controlador.notificarMensaje(cont);
	            		});


	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public Contacto getContacto(String seleccionado) {
		return agenda.get(seleccionado);
	}
	
	//Getters
	
	public Usuario getUsuario() {
		return usuario;
	}

	public Servidor getServidor() {
		return servidor;
	}


	public ArrayList<Conversacion> getConversaciones() {
		return conversaciones;
	}

	public HashMap<String, Contacto> getAgenda() {
		return agenda;
	}

	public static boolean isPortAvailable(int p) {
		
		return Servidor.isPortAvailable(p);
	}

	


}
