package modelo;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import controlador.Controlador;

public class Sistema {
    private Usuario usuario;
    private ProxyClient proxyClient;
    private HashMap<String, Contacto> agenda = new HashMap<>();
    private ArrayList<Conversacion> conversaciones = new ArrayList<>();
    private Controlador controlador;

    public Sistema(Usuario usuario, Controlador controlador) {
        this.usuario = usuario;
        this.proxyClient = new ProxyClient(this);;
        this.controlador = controlador;
        proxyClient.start();
    }

    public void iniciarConexion() throws IOException {
        registrarUsuario();
    }

    public void registrarUsuario() throws IOException {
        Request request = crearRequest();
        request.setOperacion("registro");
        request.setEmisor(this.usuario);
        request.setReceptor(new Usuario());
        try {
			try {
				Request result = proxyClient.send(request);
				if(result != null && result.getContenido().equals("en uso")) {
					throw new IOException("en uso");
				}
			}catch(IOException e){
				throw new IOException();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void enviarMensaje(String mensaje, Contacto contacto) throws IOException {
        Request request = crearRequest();
        request.setOperacion("mensaje");
        request.setNombreReceptor(contacto.getNombre());
        request.setContenido(mensaje);

        Conversacion conv = contacto.getConversacion();
        conv.agregarMensaje(mensaje, request.getFechaYHora(), usuario);

        try {
        	proxyClient.send(request);
        }catch(IOException e){
        	throw new IOException();
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public synchronized void recibirMensaje(Request request) {
        String nombre = request.getEmisor().getNombre();
        String contenido = request.getContenido();
        LocalDateTime fechaYHoraStr = request.getFechaYHora();

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
        }
        conv.recibirMensaje(contenido, fechaYHoraStr, cont);
    }

    public void consultaPorContacto(String nombreContacto) throws IOException {
        Request request = crearRequest();
        request.setOperacion("consulta");
        request.setEmisor(this.usuario);
        request.setContenido(nombreContacto);

        Request respuesta;
		try {
			respuesta = proxyClient.send(request);
	        if (!respuesta.getContenido().equals("")) {
	            if (!agenda.containsKey(respuesta.getContenido())) {
	                Contacto c = new Contacto(respuesta.getContenido());
	                agenda.put(c.getNombre(), c);
	                this.controlador.NotificarRespuestaServidor("El contacto ha sido agregado exitosamente", true);
	            }
	        }else {
	        	this.controlador.NotificarRespuestaServidor("El contacto no existe", false);
	        }
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

    public void crearConversacion(Contacto contacto) {
        if (!conversaciones.contains(contacto.getConversacion())) {
            Conversacion conv = new Conversacion(contacto);
            contacto.setConversacion(conv);
            conversaciones.add(conv);
        }
    }

    public ArrayList<Mensaje> cargarMensajesDeConversacion(Contacto contacto) {
        Conversacion conv = contacto.getConversacion();
        if (conv != null) {
            return conv.getMensajes();
        }
        return new ArrayList<>();
    }

    public Request crearRequest() {
        Request request = new Request();
        request.setEmisor(this.usuario);
        request.setReceptor(new Usuario());
        request.setFechaYHora(LocalDateTime.now());
        return request;
    }

    public HashMap<String, Contacto> getAgenda() {
        return agenda;
    }
    
    public Contacto getContacto(String nombre) {
		return agenda.get(nombre);
	}

    public ArrayList<Conversacion> getConversaciones() {
        return conversaciones;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
