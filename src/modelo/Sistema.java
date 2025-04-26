package modelo;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.SwingUtilities;

import controlador.Controlador;

public class Sistema {
	private Usuario usuario;
	private Conexion conexion;
	private HashMap<String,Contacto> agenda = new HashMap<String,Contacto>();
	private ArrayList<Conversacion> conversaciones = new ArrayList<Conversacion>();
	private Controlador controlador;

	public Sistema(Usuario usuario, Controlador controlador) {
		this.usuario = usuario;
		this.controlador = controlador;
		try {
			this.conexion = new Conexion(usuario.getIP(), usuario.getPuerto(), usuario.getNombre(), this);
		} catch (IOException e) {
			System.out.println("No pudo generarse la conexion");
			e.printStackTrace();
		}
	}

	//Metodos

	public void consultaPorContacto(String nombreContacto) throws IOException {
		Request request = new Request();
		request.setOperacion("consulta");
		request.setEmisor(this.usuario);
		request.setReceptor(new Usuario());
		request.setContenido(nombreContacto);
		conexion.enviarRequest(request);
	}
	
	public void recibirConsulta(Request request) {
		if (!request.getContenido().equals("")) {
			Contacto c = new Contacto(request.getContenido());
			agenda.put(c.getNombre(), c);
		}
	}

	public void enviarMensaje(String mensaje, Contacto contacto) throws IOException {
		Request request = crearRequest();
		request.setOperacion("mensaje");
		request.setEmisor(this.usuario);
		request.setNombreReceptor(contacto.getNombre());
		request.setContenido(mensaje);
	    
	    Conversacion conv = contacto.getConversacion();
	    conv.agregarMensaje(mensaje, request.getFechaYHora(), contacto);
	    
	    conexion.enviarRequest(request);
	}


	public void recibirMensaje(Request request) {
	    try {
            String nombre = request.getEmisor().getNombre();
            String contenido = request.getContenido();
            LocalDateTime fechaYHoraStr = request.getFechaYHora();
            System.out.println("Mensaje recibido: " + contenido);

	        Contacto cont;
	        Conversacion conv;
            if (agenda.containsKey(nombre)) {
                cont = agenda.get(nombre);
                conv = cont.getConversacion();
            } else {
                cont = new Contacto(nombre);
                agenda.put(nombre, cont);
                conv = new Conversacion(cont);
                cont.setConversacion(conv);
                conversaciones.add(conv);
                SwingUtilities.invokeLater(() -> controlador.contactoAgregado(cont));
            }
	            conv.recibirMensaje(contenido, fechaYHoraStr, cont);
	            SwingUtilities.invokeLater(() -> {
	                controlador.nuevoMensaje();
	                controlador.notificarMensaje(cont);
	            });
	    } catch (NumberFormatException e) {
	        System.err.println("Error al parsear el puerto: " + e.getMessage());
	    } catch (Exception e) {
	        System.err.println("Error al recibir mensaje: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
		
	public void crearConversacion(Contacto contacto) {
		//creo la conversacion y la guardo en el contacto
		Conversacion conv = new Conversacion(contacto);
		contacto.setConversacion(conv);
		conversaciones.add(conv);
	}

	public Conversacion getConversacion(Contacto c) {
		return c.getConversacion();
	}
	
	public Request crearRequest() {
		Request request = new Request();
		request.setEmisor(this.usuario);
		request.setReceptor(new Usuario());
		return request;
	}

	public void contactoSinConexion(String s) {
		controlador.contactoSinConexion(s, "ERROR 009");		
	}

	public void notificarDesconexion() {
		//Una vez definido el funcionamiento del nuevo servidor, rehacer esta función
	   
	}
	
	public Contacto getContacto(String seleccionado) {
		return agenda.get(seleccionado);
	}
	
	//Getters
	
	public Usuario getUsuario() {
		return usuario;
	}

	public ArrayList<Conversacion> getConversaciones() {
		return conversaciones;
	}

	public HashMap<String, Contacto> getAgenda() {
		return agenda;
	}

	public static boolean isPortAvailable(int p) {
		//Una vez definido el funcionamiento del nuevo servidor, rehacer esta función
		return true;
	}

}
