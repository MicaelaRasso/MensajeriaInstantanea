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
	private Servidor servidor;
	private Controlador controlador;

	public Sistema(Usuario usuario, Controlador controlador) {
		this.usuario = usuario;
		this.controlador = controlador;
	}

	//Metodos

	public void iniciarServidor(String nombre, int puerto) throws IOException {
		this.usuario = new Usuario(nombre, puerto);
		this.servidor = new Servidor(puerto, this);
		this.servidor.iniciar();
	}

	public void agregarContacto(Contacto contacto) {
		agenda.put(contacto.getNombre(), contacto);
		Conversacion conv = new Conversacion();
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

	public void recibirMensaje(String s) {
	    System.out.println(s); //1
	    try {
	        String[] partes = s.split("\n");
	        if(partes.length == 3) {
	            String nombre = partes[0].replace(":", "").trim();
	            String contenido = partes[1].trim();  // Contenido del mensaje
	            String fechaYHoraStr = partes[2].trim();  // Fecha y hora

	            // Verifica que 'cont' no sea null
	            Contacto cont = agenda.get(nombre);
	            if (cont == null) {
	                System.out.println("No se encontró el contacto: " + nombre);
	                return; // Termina el método si el contacto no existe
	            }

	            cont.recibirMensaje(contenido, fechaYHoraStr);  // Esto puede lanzar excepciones

	            controlador.nuevoMensaje();
	            
	            System.out.println("Se recibió el mensaje"); //2
	        }
	    } catch (Exception e) {
	        e.printStackTrace();  // Registra la excepción para investigar el error
	        System.out.println("Hubo un error al procesar el mensaje.");
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

	


}
