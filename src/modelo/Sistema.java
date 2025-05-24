package modelo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import controlador.Controlador;

public class Sistema {
    private Usuario usuario;
    private ProxyClient proxyClient;
    private HashMap<String, Contacto> agenda = new HashMap<>();
    private ArrayList<Conversacion> conversaciones = new ArrayList<>();
    private final Observable<Request> responseObservable = new Observable<>();
    private final Observable<Request> messageObservable = new Observable<>();
    private Sender sender;
    private Controlador controlador;
    private BufferedReader in;
    private PrintWriter out;

    public Sistema(Usuario usuario, Controlador controlador) {
    	this.usuario = usuario;
        this.proxyClient = new ProxyClient(this,responseObservable,messageObservable);
        try {
        	this.proxyClient.connect();
        	out = this.proxyClient.getOut();
        	in = this.proxyClient.getIn();
        }catch(IOException e){
        	System.out.println("Error al conectar");
        }
        this.controlador = controlador;

        // Observador de mensajes
        messageObservable.addObserver(req -> recibirMensaje(req));
    }

    public void iniciarConexion() throws IOException {
        registrarUsuario();
    }

    public void registrarUsuario() throws IOException {
        Request request = crearRequest();
        request.setOperacion("registro");
        request.setEmisor(this.usuario);
        request.setReceptor(new Usuario());
        
        Observer<Request> respuestaRegistro = new Observer<>() {
            @Override
            public void update(Request response) {
                if ("en uso".equals(response.getContenido())) {
                    System.out.println("Nombre en uso");
                } else {
                    System.out.println("Usuario registrado");
                }
                responseObservable.removeObserver(this); // auto-remover
            }
        };
        responseObservable.addObserver(respuestaRegistro);
        try {
			proxyClient.send(request);
		} catch (InterruptedException | IOException e) {
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
        
        Observer<Request> respuestaMensaje = new Observer<>() {
            @Override
            public void update(Request response) {
                if ("en uso".equals(response.getContenido())) {
                    System.out.println("Nombre en uso");
                } else {
                    System.out.println("Usuario registrado");
                }
                responseObservable.removeObserver(this); // auto-remover
            }
        };
        responseObservable.addObserver(respuestaMensaje);
        try {
			proxyClient.send(request);
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public synchronized void recibirMensaje(Request request) {
        String nombre = request.getEmisor().getNombre();
        String contenido = request.getContenido();
        LocalDateTime fechaYHoraStr = request.getFechaYHora();
        System.out.println("Mensaje recibido");

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
            System.out.println("Nueva conversación creada para: " + nombre);
        }
        conv.recibirMensaje(contenido, fechaYHoraStr, cont);
        controlador.nuevoMensaje();
        controlador.cargarContactos();
        controlador.cargarConversaciones();
    }

    public void consultaPorContacto(String nombreContacto) throws IOException {
        Request request = crearRequest();
        request.setOperacion("consulta");
        request.setEmisor(this.usuario);
        request.setContenido(nombreContacto);
		try {
			Observer<Request> respuestaMensaje = new Observer<>() {
	            @Override
	            public void update(Request response) {
	            	if (!response.getContenido().equals("")) {
	    	            if (!agenda.containsKey(response.getContenido())) {
	    	                Contacto c = new Contacto(response.getContenido());
	    	                agenda.put(c.getNombre(), c);
	    	                controlador.NotificarRespuestaServidor("El contacto ha sido agregado exitosamente", true);
	    	            }
	    	        }else {
	    	        	System.out.println("el contacto no existe");
	    	        	controlador.NotificarRespuestaServidor("El contacto no existe", false);
	    	        }
	            	responseObservable.removeObserver(this); // auto-remover
	            }
	        };
	        responseObservable.addObserver(respuestaMensaje);
	        proxyClient.send(request);
	        /*if (!respuesta.getContenido().equals("")) {
	            if (!agenda.containsKey(respuesta.getContenido())) {
	                Contacto c = new Contacto(respuesta.getContenido());
	                agenda.put(c.getNombre(), c);
	                this.controlador.NotificarRespuestaServidor("El contacto ha sido agregado exitosamente", true);
	            }
	        }else {
	        	this.controlador.NotificarRespuestaServidor("El contacto no existe", false);
	        }*/
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
