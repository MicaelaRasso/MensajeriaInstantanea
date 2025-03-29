package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

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
	private Sistema sistema;
	private Contacto contactoActual = null;
	
	
	public Controlador(VentanaInicio vInicio, VentanaPrincipal vPrincipal, VentanaAgregarContacto vContacto) {
		super();
		this.vInicio = vInicio;
		this.vPrincipal = vPrincipal;
		this.vContacto = vContacto;
		this.vInicio.getBtnIngresar().addActionListener(this);
		this.vPrincipal.getBtnConversacion().addActionListener(this);
		this.vPrincipal.getBtnEnviar().addActionListener(this);
		this.vPrincipal.getBtnContacto().addActionListener(this);
		this.vContacto.getBtnAgregar().addActionListener(this);
		
	}


	public static void main(String[] args) {
		
		VentanaInicio inicio = new VentanaInicio();
		VentanaPrincipal principal = new VentanaPrincipal();
		VentanaAgregarContacto contacto = new VentanaAgregarContacto();

		Controlador contr = new Controlador(inicio, principal, contacto);

		inicio.setVisible(true);
		

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		//VENTANA INICIO
		if (vInicio.getBtnIngresar().equals(e.getSource())) {
			String nombre = vInicio.getTfNombre().getText();
			String puerto = vInicio.getTfPuerto().getText();
			if (!(nombre.equals("") || puerto.equals(""))) {

				int p = Integer.valueOf(puerto);
				
				Usuario usuario = new Usuario(nombre, p); 
				this.sistema = new Sistema(usuario);
				
				sistema.iniciarServidor(nombre, p);
				
				vInicio.setVisible(false);
				vPrincipal.setVisible(true);
			}
		}else {

			//VENTANA AGREGAR CONTACTO - Crea contacto e inicia la conversación (en sistema)
			if(vContacto.getBtnAgregar().equals(e.getSource())) {
				String nombre = vContacto.getTfNombre().getText();
				String ip = vContacto.getTfIP().getText();
				String puerto = vContacto.getTfPuerto().getText();
				if (!(nombre.equals("") || ip.equals("") || puerto.equals(""))) {
					int p = Integer.valueOf(puerto);
					
					Contacto c = new Contacto(nombre, ip, p);
					sistema.agregarContacto(c);
					
					cargarContactos();
					
					vContacto.setVisible(false);
					

			        vPrincipal.revalidate();
			        vPrincipal.repaint();
					vPrincipal.setVisible(true);
				}
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
						sistema.enviarMensaje(m, contactoActual);
						
					}else {
						
						//VENTANA PRINCIPAL - Abre la ventana de agregar contacto
						if (vPrincipal.getBtnContacto().equals(e.getSource())) {
							vPrincipal.setVisible(false);
							vContacto.setVisible(true);	
						}
					}
				}
			}
		}		
	}

	private void cargarContactos() {
		
		 // Modelo de la lista
        DefaultListModel<String> contactos = new DefaultListModel<>();
        Iterator<Contacto> it = sistema.getAgenda().iterator();
		while(it.hasNext()) {
			Contacto c =  it.next();
			contactos.addElement(c.toString());
		}

		// Lista de contactos
        JList<String> contactList = new JList<>(contactos);
        contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Permite selección única
        contactList.setVisibleRowCount(10);
       
        vPrincipal.getSpContactos().setViewportView(contactList);
	}
	
	private void cargarMensajes() {
		if(contactoActual != null) {
		//if(sistema.getAgenda().contains(contactoActual)) {
			vPrincipal.getLblNombre().setText(contactoActual.getNombre());
			
			 // Panel contenedor de los mensajes
	        JPanel messagePanel = new JPanel();
	        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

	        Iterator<Mensaje> it = sistema.getConversacion(contactoActual).getMensajes().iterator();
			while(it.hasNext()) {
				Mensaje m = (Mensaje) it.next();
				JLabel label = new JLabel();
				label.setText(m.toString());			
				messagePanel.add(label);
			}
			
			vPrincipal.getSpConversacion().setViewportView(messagePanel);

		}else {
			mostrarError("Debe seleccionar un contacto antes de iniciar la conversación");
		}
	}
	
	private void mostrarError(String s) {
		VentanaError vError = new VentanaError();
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
