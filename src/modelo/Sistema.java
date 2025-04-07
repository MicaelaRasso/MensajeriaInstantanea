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

	    if (contacto.getCliente() == null) {
	        try {
	            Cliente cliente = new Cliente(contacto.getIP(), contacto.getPuerto());
	            contacto.setCliente(cliente);
	        } catch (IOException e) {
	            contactoSinConexion("No se pudo conectar con el contacto: " + contacto.getNombre());
	            return;
	        }
	    }

	    contacto.enviarMensaje(mensaje); // suficiente
	    // contacto.getCliente().enviarMensaje(mensaje); ⛔ eliminar esta línea
	}


	public void recibirMensaje(String s, String ip) {
	    try {
	        String[] partes = s.split("//");

	        if (partes.length == 4) {
	            String nombre = partes[0].replace(":", "").trim();
	            String contenido = partes[1].trim();
	            String fechaYHoraStr = partes[2].trim();
	            int puerto = Integer.parseInt(partes[3].trim());
	            System.out.println("Yo soy: " + usuario.getNombre() + ":" + usuario.getPuerto());
	            System.out.println("[DEBUG] Mensaje recibido: " + s + " desde " + ip);  // <- AGREGÁ ESTO

	            // 🛑 Evitar procesar mensajes que vienen de uno mismo
	            if (nombre.equals(usuario.getNombre()) && puerto == usuario.getPuerto()) {
	                System.out.println("Mensaje de uno mismo ignorado.");
	                return;
	            }

	            Contacto cont;
	            if (agenda.containsKey(nombre)) {
	                cont = agenda.get(nombre);
	            } else {
	                cont = new Contacto(nombre, ip, puerto);
	                agenda.put(nombre, cont);
	                Conversacion conv = new Conversacion(cont);
	                cont.setConversacion(conv);
	                conversaciones.add(conv);
	                SwingUtilities.invokeLater(() -> controlador.contactoAgregado(cont));
	                try {
	                    Cliente cliente = new Cliente(ip, puerto);
	                    cont.setCliente(cliente);
	                } catch (IOException e) {
	                    System.err.println("No se pudo establecer conexión saliente con " + nombre);
	                }

	            }

	            cont.recibirMensaje(contenido, fechaYHoraStr);
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
		controlador.contactoSinConexion(s);		
	}

	public void notificarDesconexion() {
	    for (Contacto contacto : agenda.values()) {
	        try {
	            // Si ya hay cliente, usarlo, si no, crearlo
	            if (contacto.getCliente() == null) {
	                Cliente cliente = new Cliente(contacto.getIP(), contacto.getPuerto());
	                contacto.setCliente(cliente);
	            }

	            Mensaje msg = new Mensaje(usuario, "[DESCONECTADO]", LocalDateTime.now());
	            contacto.getCliente().enviarMensaje(msg);
	        } catch (IOException e) {
	            System.out.println("No se pudo notificar a " + contacto.getNombre());
	        }
	    }

	    // Detener servidor (si querés)
	    if (servidor != null) {
	        try {
				servidor.terminar();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  // suponiendo que tenés un método así
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
