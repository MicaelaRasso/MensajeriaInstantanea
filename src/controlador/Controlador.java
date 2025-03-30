package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import modelo.Contacto;
import modelo.Mensaje;
import modelo.Sistema;
import modelo.Usuario;
import vista.VentanaAgregarContacto;
import vista.VentanaError;
import vista.VentanaInicio;
import vista.VentanaPrincipal;

public class Controlador implements ActionListener {
	private VentanaInicio vInicio;
	private VentanaPrincipal vPrincipal;
	private VentanaAgregarContacto vContacto;
	private VentanaError vError;
	private Sistema sistema;
	private Contacto contactoActual = null;
	
	
	public Controlador(VentanaInicio vInicio, VentanaPrincipal vPrincipal, VentanaAgregarContacto vContacto, VentanaError vError
) {
		super();
		this.vInicio = vInicio;
		this.vPrincipal = vPrincipal;
		this.vContacto = vContacto;
		this.vError = vError;
		this.vInicio.getBtnIngresar().addActionListener(this);
		this.vPrincipal.getBtnConversacion().addActionListener(this);
		this.vPrincipal.getBtnEnviar().addActionListener(this);
		this.vPrincipal.getBtnContacto().addActionListener(this);
		this.vContacto.getBtnAgregar().addActionListener(this);
		this.vError.getBtnVolver().addActionListener(this);
		
	}

	public static void main(String[] args) {
		
		VentanaInicio inicio = new VentanaInicio();
		VentanaPrincipal principal = new VentanaPrincipal();
		VentanaAgregarContacto contacto = new VentanaAgregarContacto();
		VentanaError vError = new VentanaError();


		Controlador contr = new Controlador(inicio, principal, contacto, vError);

		inicio.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		//VENTANA INICIO
		if (vInicio.getBtnIngresar().equals(e.getSource())) {
			registroInicial();
		}else {

			//VENTANA AGREGAR CONTACTO - Crea contacto e inicia la conversación (en sistema)
			if(vContacto.getBtnAgregar().equals(e.getSource())) {
				agregarContacto();					
			}else {
				
				//VENTANA PRINCIPAL
				if(vPrincipal.getBtnConversacion().equals(e.getSource())) {
					cargarMensajes();

			        vPrincipal.revalidate();
			        vPrincipal.repaint();
					
				}else {
					
					//VENTANA PRINCIPAL - Presiona para enviar un mensaje
					if (vPrincipal.getBtnEnviar().equals(e.getSource())) {
						String m;
						m = vPrincipal.getTxtrEscribirMensaje().getText();
						try {
							sistema.enviarMensaje(m, contactoActual);
						} catch (IOException e1) {
							mostrarError("No se pudo establecer la conexión con el socket");
							e1.printStackTrace();
							
						}
						
					}else {
						//VENTANA PRINCIPAL - Abre la ventana de agregar contacto
						if (vPrincipal.getBtnContacto().equals(e.getSource())) {
							vPrincipal.setVisible(false);
							vContacto.setVisible(true);	
						}else {
							if (vError.getBtnVolver().equals(e.getSource())) {
								vError.setVisible(false);
							}
						}
					}
				}
			}
		}		
	}

	private void registroInicial() {
		String nombre = vInicio.getTfNombre().getText();
		String puerto = vInicio.getTfPuerto().getText();
		if (!(nombre.equals("") || puerto.equals(""))) {

			int p = Integer.valueOf(puerto);
			
			Usuario usuario = new Usuario(nombre, p); 
			this.sistema = new Sistema(usuario, this);
			
			try {
				sistema.iniciarServidor(nombre, p);
			} catch (IOException e) {
				mostrarError("El servidor no pudo iniciar la conexion");
				e.printStackTrace();
			}
			
			vInicio.setVisible(false);
			vPrincipal.setVisible(true);
		}
	}
	
	private void agregarContacto() {
		String nombre = vContacto.getTfNombre().getText();
		String ip = vContacto.getTfIP().getText();
		String puerto = vContacto.getTfPuerto().getText();
		if (!(nombre.equals("") || ip.equals("") || puerto.equals(""))) {
			int p = Integer.valueOf(puerto);
			try {
				InetAddress.getByName(ip);
				
				Contacto c = new Contacto(nombre, ip, p);
				sistema.agregarContacto(c);
				
				cargarContactos();
				
				vContacto.setVisible(false);
				
		        vPrincipal.revalidate();
		        vPrincipal.repaint();
				vPrincipal.setVisible(true);
				
			} catch (UnknownHostException e1) {
				mostrarError("IP inválida, ingresar una IP válida");
			}
		}
	}

	private void cargarContactos() {
        DefaultListModel<String> modelo = new DefaultListModel<>();
       
        if (sistema!=null && !sistema.getAgenda().isEmpty()) {
	        Iterator it = sistema.getAgenda().values().iterator();
	        
	        while(it.hasNext()) {
				Contacto c =  (Contacto) it.next();
				modelo.addElement(c.toString());
			}
	        
	        JList<String> listaContactos = new JList<>(modelo);
	        listaContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	
	        listaContactos.addListSelectionListener(e -> {
	            if (!e.getValueIsAdjusting()) {
	                String seleccionado = listaContactos.getSelectedValue();
	                contactoActual = sistema.getContacto(seleccionado);
	            }
	        });
	
	        vPrincipal.getSpContactos().setViewportView(listaContactos);
        }	
	
	}
	
	public void nuevoMensaje() {
		cargarMensajes();
		//desde aca podria manejar las notificaciones
	}
	
	private void cargarMensajes() {
	    if (contactoActual != null) {
	        vPrincipal.getLblNombre().setText(contactoActual.getNombre());

	        
	        JPanel messagePanel = new JPanel();
	        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

	        // Iterar sobre los mensajes del contacto actual
	        Iterator<Mensaje> it = sistema.getConversacion(contactoActual).getMensajes().iterator();
	        while (it.hasNext()) {
	            Mensaje m = it.next();
	            JLabel label = new JLabel();
	            label.setText(m.toString());
	            messagePanel.add(label);
	        }

            messagePanel.revalidate();
            messagePanel.repaint();
	        SwingUtilities.invokeLater(() -> {
	            vPrincipal.getSpConversacion().setViewportView(messagePanel);
	        });
	    } else {
	        mostrarError("Debe seleccionar un contacto antes de iniciar la conversación");
	    }
	}
	
	private void mostrarError(String s) {
		vError.getTxtrMensajeDeError().setText(s);
		vError.setVisible(true);
	}
	
	public Sistema getSistema() {
		return sistema;
	}

	public Contacto getContactoActual() {
		return contactoActual;
	}
}
