package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;

import modelo.Contacto;
import modelo.Mensaje;
import modelo.Sistema;
import modelo.Usuario;
import vista.VentanaAgregarContacto;
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

			//VENTANA AGREGAR CONTACTO
			if(vContacto.getBtnAgregar().equals(e.getSource())) {
				String nombre = vContacto.getTfNombre().getText();
				String ip = vContacto.getTfIP().getText();
				String puerto = vContacto.getTfPuerto().getText();
				if (!(nombre.equals("") || puerto.equals("") || puerto.equals(""))) {
					int p = Integer.valueOf(puerto);
					
					Contacto c = new Contacto(nombre, ip, p);
					sistema.agregarContacto(c);
					
					vContacto.setVisible(false);
					vPrincipal.setVisible(true);
				}
			}else {
				
				//VENTANA PRINCIPAL
				if(vPrincipal.getBtnConversacion().equals(e.getSource())) {
					//seleccionar un contacto para iniciar conversacion
					/*
					//c va a ser extraido de la seleccion del jScrollPanel, todavia no se como
					Contacto c = new Contacto("","",1);
					sistema.getConversacion(c);
					*/					
				}else {
					
					//VENTANA PRINCIPAL
					if (vPrincipal.getBtnEnviar().equals(e.getSource())) {

						String m;
						/*
						contactoActual = //algo del jScrollPanel
						
						 */
						m = vPrincipal.getTxtrEscribirMensaje().getText();
						Mensaje mensaje = new Mensaje(m, LocalDateTime.now());
						sistema.getConversacion(contactoActual).recibirMensaje(mensaje);
					}else {
						
						//VENTANA PRINCIPAL
						if (vPrincipal.getBtnContacto().equals(e.getSource())) {
							vPrincipal.setVisible(false);
							vContacto.setVisible(true);	
						}
					}
				}
			}
		}		
	}

	
	
}
