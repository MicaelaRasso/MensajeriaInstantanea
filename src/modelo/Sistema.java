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

	public void consultaPorContacto(String nombreContacto) {
		try {
			//armo el paquete
			conexion.consultaContacto(paquete);
	}
		
	public void recibirConsulta(Request paquete) {
		if (!paquete.getContenido().equals("")) {
			Contacto c = new Contacto(paquete.getContenido());
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

	public void enviarMensaje(String m, Contacto contacto) throws IOException {
	    Mensaje mensaje = new Mensaje(contacto, m, LocalDateTime.now());
	    
	    conexion.enviarMensaje(mensaje);
	    
	    Conversacion conv = contacto.getConversacion();
	    conv.enviarMensaje(mensaje, contacto);
	        
	}


	public void recibirMensaje(String s) {
	    try {
	        String[] partes = s.split("//");

	        if (partes.length == 4) {
	            String nombre = partes[0].replace(":", "").trim();
	            String contenido = partes[1].trim();
	            String fechaYHoraStr = partes[2].trim();
	            int puerto = Integer.parseInt(partes[3].trim());
	            System.out.println("Mensaje recibido: " + s);

	            if (nombre.equals(usuario.getNombre()) && puerto == usuario.getPuerto()) {
	                System.out.println("Mensaje de uno mismo ignorado.");
	                return;
	            }

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
	            
	            //cont.recibirMensaje(contenido, fechaYHoraStr);
	            SwingUtilities.invokeLater(() -> {
	                controlador.nuevoMensaje();
	                controlador.notificarMensaje(cont);
	            });

	        } else {
	            System.out.println("Mensaje mal formado: " + s);
	        }
	    } catch (NumberFormatException e) {
	        System.err.println("Error al parsear el puerto: " + e.getMessage());
	    } catch (Exception e) {
	        System.err.println("Error al recibir mensaje: " + e.getMessage());
	        e.printStackTrace();
	    }
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
