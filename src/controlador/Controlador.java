package controlador;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import modelo.Contacto;
import modelo.Conversacion;
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
		
		// Deshabilita el botón de enviar si el área de texto está vacía
		this.vPrincipal.getBtnEnviar().setEnabled(false);  // estado inicial

		this.vPrincipal.getTxtrEscribirMensaje().getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
		    private void actualizarEstadoBoton() {
		        String texto = vPrincipal.getTxtrEscribirMensaje().getText().trim();
		        vPrincipal.getBtnEnviar().setEnabled(!texto.isEmpty());
		    }

		    @Override
		    public void insertUpdate(javax.swing.event.DocumentEvent e) {
		        actualizarEstadoBoton();
		    }

		    @Override
		    public void removeUpdate(javax.swing.event.DocumentEvent e) {
		        actualizarEstadoBoton();
		    }

		    @Override
		    public void changedUpdate(javax.swing.event.DocumentEvent e) {
		        actualizarEstadoBoton();
		    }
		});

		this.vContacto = vContacto;
		this.vInicio.getBtnIngresar().addActionListener(this);
		this.vPrincipal.getBtnConversacion().addActionListener(this);
		this.vPrincipal.getBtnEnviar().addActionListener(this);
		this.vPrincipal.getBtnContacto().addActionListener(this);
		this.vContacto.getBtnAgregar().addActionListener(this);
		this.vContacto.getBtnVolver().addActionListener(this);
	}

	public static void main(String[] args) {
		
		VentanaInicio inicio = new VentanaInicio();
		VentanaPrincipal principal = new VentanaPrincipal(null);
		VentanaAgregarContacto contacto = new VentanaAgregarContacto();

		Controlador contr = new Controlador(inicio, principal, contacto);

		inicio.setVisible(true);

		//inicio.getContentPane().setVisible(true);
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
				vContacto.getTfNombre().setText("");
				vContacto.getTfIP().setText("");
				vContacto.getTfPuerto().setText("");
			}else {
				//VENTANA PRINCIPAL
				if(vPrincipal.getBtnConversacion().equals(e.getSource())) {
					DefaultListModel<String> modelo = new DefaultListModel<>();
				       
			        if (sistema!=null && !sistema.getAgenda().isEmpty()) {
				        Iterator<Contacto> it = sistema.getAgenda().values().iterator();
				        
				        while(it.hasNext()) {
							Contacto c =  (Contacto) it.next();
							modelo.addElement(c.toString());
						}
				        
				        JList<String> listaContactos = new JList<>(modelo);
				        listaContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				
				        listaContactos.addListSelectionListener(f -> {
				            if (!f.getValueIsAdjusting()) {
				                String seleccionado = listaContactos.getSelectedValue();
				                contactoActual = sistema.getContacto(seleccionado);
				                crearConversacion(contactoActual);
				                }
				            });
			        }
					cargarContactos();
					cargarConversaciones();

			        vPrincipal.revalidate();
			        vPrincipal.repaint();
					
				}else {
					
					//VENTANA PRINCIPAL - Presiona para enviar un mensaje
					if (vPrincipal.getBtnEnviar().equals(e.getSource())) {
						if (contactoActual == null) {
							JOptionPane.showMessageDialog(
								    null,
								    "Debés seleccionar una conversación antes de enviar un mensaje.",
								    "ERROR 003",
								    JOptionPane.WARNING_MESSAGE
								);

							return;
						}

						String m = vPrincipal.getTxtrEscribirMensaje().getText().trim();
						vPrincipal.getTxtrEscribirMensaje().setText("");
						enviarMensaje(m, contactoActual);

						SwingUtilities.invokeLater(() -> {
							cargarMensajes();
						});	
					}else {
						//VENTANA PRINCIPAL - Abre la ventana de agregar contacto
						if (vPrincipal.getBtnContacto().equals(e.getSource())) {
							vPrincipal.setVisible(false);
							vContacto.setVisible(true);	
						}else {
							//VENTANA AGREGAR CONTACTO - Volver a la ventana principal
							if(vContacto.getBtnVolver().equals(e.getSource())) {
								vPrincipal.setVisible(true);
								vContacto.setVisible(false);	
							}
						}
					}
				}
			}
		}		
	}

	private void registroInicial() {
		String nombre = vInicio.getTfNombre().getText();
		String IP = vInicio.getTfIP().getText(); //HAY QUE AGREGAR EL CONTENEDOR
		IP = "127.0.0.1";
		String puerto = vInicio.getTfPuerto().getText();
		if (!(nombre.equals("") || puerto.equals(""))) {
			try {
				int p = Integer.valueOf(puerto);
				if (p < 0 || p > 65535) {
					JOptionPane.showMessageDialog(
						    null,
						    "Puerto fuera de rango (0-65535)",
						    "ERROR 002",
						    JOptionPane.WARNING_MESSAGE
						);
				}else {
					if (!Sistema.isPortAvailable(p)) { //HAY QUE ARREGLAR ESTA FUNCION, POR AHORA, SIEMPRE DEVUELVE TRUE
						JOptionPane.showMessageDialog(
							    null,
							    "El puerto no es valido",
							    "ERROR 003",
							    JOptionPane.WARNING_MESSAGE
							);
					} else {
						Usuario usuario = new Usuario(nombre, IP, p); 
						this.sistema = new Sistema(usuario, this);

						vInicio.setVisible(false);
						vPrincipal.setVisible(true);
					}
				}
			}catch(NumberFormatException e){
				JOptionPane.showMessageDialog(
					    null,
					    "El puerto solo puede contener números",
					    "ERROR 005",
					    JOptionPane.WARNING_MESSAGE
					);
			}
		}else {
			JOptionPane.showMessageDialog(
				    null,
				    "Debe completar todos los campos",
				    "ERROR 001",
				    JOptionPane.WARNING_MESSAGE
				);
		}
	}
	
	private void agregarContacto() {
		String nombre = vContacto.getTfNombre().getText();
		if (!(nombre.equals(""))) {
			if(sistema.getAgenda().containsKey(nombre)){
				//Los nicknames son unicos, no debería suceder
				JOptionPane.showMessageDialog(
					    null,
					    "Se ha intentado agregar a un contacto ya agendado",
					    "ERROR 006",
					    JOptionPane.WARNING_MESSAGE
					);
			}else {
				sistema.agregarContacto(nombre);
			
				cargarContactos();
				
				vContacto.setVisible(false);
				
		        vPrincipal.revalidate();
		        vPrincipal.repaint();
				vPrincipal.setVisible(true);
				JOptionPane.showMessageDialog(
					    null,
					    "Se ha agregado el contacto " + nombre,
					    "Contacto agregado",
					    JOptionPane.WARNING_MESSAGE
					);
					
				}
		}else {
			JOptionPane.showMessageDialog(
				    null,
				    "Debe ingresar un nombre de contacto",
				    "ERROR 001",
				    JOptionPane.WARNING_MESSAGE
				);
			}
	}
	
	public void crearConversacion(Contacto contacto) {
		//revisa array de conversaciones, si ya existe, no la crea
		
		sistema.crearConversacion(contacto);
	}
	
	public void enviarMensaje(String m, Contacto contactoActual){
		try {
			sistema.enviarMensaje(m, contactoActual);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void cargarContactos() {
        DefaultListModel<String> modelo = new DefaultListModel<>();
       
        if (sistema!=null && !sistema.getAgenda().isEmpty()) {
	        Iterator<Contacto> it = sistema.getAgenda().values().iterator();
	        
	        while(it.hasNext()) {
				Contacto c =  (Contacto) it.next();
				modelo.addElement(c.toString());
			}
	        
	        JList<String> listaContactos = new JList<>(modelo);
	        listaContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	
	        /*listaContactos.addListSelectionListener(e -> {
	            if (!e.getValueIsAdjusting()) {
	                String seleccionado = listaContactos.getSelectedValue();
	                if(seleccionado.contains("*")) {
	                    seleccionado = seleccionado.replace("*", "");
	                }
	                //contactoActual = sistema.getContacto(seleccionado);
	            }
	        });*/
	
	        vPrincipal.getSpContactos().setViewportView(listaContactos);
        }	
	
	}
	
	public void notificarMensaje(Contacto cont) {
		cargarConversaciones();  // Recargamos la lista de conversaciones con el asterisco

		if (!cont.equals(contactoActual)) {
			JList<String> listaConversaciones = (JList<String>) vPrincipal.getSpConversacion().getViewport().getView();
			DefaultListModel<String> modelo = (DefaultListModel<String>) listaConversaciones.getModel();

			for (int i = 0; i < modelo.size(); i++) {
				String nombre = modelo.getElementAt(i);
				if (nombre.equals(cont.getNombre()) || nombre.equals(cont.getNombre() + " *")) {
					if (!nombre.endsWith("*")) {
						modelo.set(i, cont.getNombre() + " *");  // Agrega notificación
					}
					break;
				}
			}
		}
	}
	
	public void nuevoMensaje() {
	    SwingUtilities.invokeLater(() -> {
	        cargarMensajes();
	    });
	}
	
	private JPanel crearPanelMensaje(Mensaje m) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	    String fechaFormateada = m.getFechaYHora().format(formatter);

	    boolean esMio = m.getUsuario().equals(sistema.getUsuario());

	    // Colores personalizados
	    Color cFondo = new Color(232, 218, 239); // lila claro
	    Color cBoton = new Color(210, 180, 222); // lila oscuro
	    Color colorFondoMensaje = esMio ? cFondo : cBoton;

	    JPanel panelMensaje = new JPanel();
	    panelMensaje.setLayout(new BoxLayout(panelMensaje, BoxLayout.Y_AXIS));
	    panelMensaje.setBorder(BorderFactory.createCompoundBorder(
	        BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
	        BorderFactory.createEmptyBorder(5, 10, 5, 10)
	    ));
	    panelMensaje.setBackground(colorFondoMensaje);
	    panelMensaje.setAlignmentX(esMio ? Component.RIGHT_ALIGNMENT : Component.LEFT_ALIGNMENT);

	    JLabel lblUsuario = new JLabel(m.getUsuario().getNombre() + ":");
	    lblUsuario.setFont(new Font("Segoe UI Semibold", Font.ITALIC, 12));
	    lblUsuario.setForeground(new Color(50, 50, 50));

	    JTextArea txtrContenido = new JTextArea(m.getContenido());
	    txtrContenido.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
	    txtrContenido.setLineWrap(true);
	    txtrContenido.setWrapStyleWord(true);
	    txtrContenido.setEditable(false);
	    txtrContenido.setBackground(colorFondoMensaje);
	    txtrContenido.setBorder(null);
	    txtrContenido.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));

	    JLabel lblFecha = new JLabel(fechaFormateada);
	    lblFecha.setFont(new Font("Segoe UI", Font.ITALIC, 10));
	    lblFecha.setForeground(new Color(100, 100, 100));

	    panelMensaje.add(lblUsuario);
	    panelMensaje.add(txtrContenido);
	    panelMensaje.add(lblFecha);

	    JPanel wrapper = new JPanel(new FlowLayout(esMio ? FlowLayout.RIGHT : FlowLayout.LEFT));
	    wrapper.setOpaque(false);
	    wrapper.add(panelMensaje);

	    return wrapper;
	}

	
	private void cargarMensajes() {
        vPrincipal.getLblNombre().setText(contactoActual.getNombre());

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

        // Iterar sobre los mensajes del contacto actual de forma invertida, para que el ultimo aparezca arriba

        ArrayList<Mensaje> invertida = new ArrayList<>(sistema.getConversacion(contactoActual).getMensajes());

        Collections.reverse(invertida);
        Iterator<Mensaje> it = invertida.iterator();
        while (it.hasNext()) {
            Mensaje m = it.next();  
            JPanel panelMensaje = crearPanelMensaje(m);
            messagePanel.add(panelMensaje);
        }

        messagePanel.revalidate();
        messagePanel.repaint();
        
        // Actualizar la vista
        SwingUtilities.invokeLater(() -> {
            vPrincipal.getSpMensajes().setViewportView(messagePanel);
        });
	}

	public void cargarConversaciones() {
        DefaultListModel<String> modelo = new DefaultListModel<>();
       
        if (sistema!=null && !sistema.getConversaciones().isEmpty()) {
	        Iterator<Conversacion> it = sistema.getConversaciones().iterator();
	        
	        while (it.hasNext()) {
	        	Conversacion c = it.next();
	        	modelo.addElement(c.getContacto().getNombre()); // No le pongas * acá
	        }
	        
	        JList<String> listaConversaciones = new JList<>(modelo);
	        listaConversaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	
	        listaConversaciones.addListSelectionListener(e -> {
	        	if (!e.getValueIsAdjusting()) {
	        		String seleccionado = listaConversaciones.getSelectedValue();

	        		if (seleccionado.contains(" *")) {
	        			seleccionado = seleccionado.replace(" *", "");  // Quitamos asterisco
	        		}

	        		if (sistema.getAgenda().containsKey(seleccionado)) {
	        			contactoActual = sistema.getContacto(seleccionado);
	        			cargarMensajes();

	        			// Habilita o deshabilita el botón enviar según el contenido del campo de texto
	        			String texto = vPrincipal.getTxtrEscribirMensaje().getText().trim();
	        			vPrincipal.getBtnEnviar().setEnabled(!texto.isEmpty());

	        			// Limpia el asterisco en la lista de conversaciones
	        			DefaultListModel<String> modeloLista = (DefaultListModel<String>) listaConversaciones.getModel();
	        			for (int i = 0; i < modeloLista.size(); i++) {
	        				String nombre = modeloLista.get(i);
	        				if (nombre.equals(contactoActual.getNombre() + " *")) {
	        					modeloLista.set(i, contactoActual.getNombre());
	        					break;
	        				}
	        			}
	        		}else {
	        			JOptionPane.showMessageDialog(
	        				    null,
	        				    "Error al seleccionar la conversación",
	        				    "ERROR 008",
	        				    JOptionPane.WARNING_MESSAGE
	        				);
	        		}
	        	}
	        });
	
	        vPrincipal.getSpConversacion().setViewportView(listaConversaciones);
        }	
	
	}
	
	public void contactoAgregado(Contacto c) {
	    SwingUtilities.invokeLater(() -> {
	        cargarContactos();
	        cargarConversaciones();  // también para que aparezca la nueva conversación
	        JOptionPane.showMessageDialog(
				    null,
				    "Se ha agregado el contacto " + c.getNombre(),
				    "Contacto agregado",
				    JOptionPane.WARNING_MESSAGE
				);
	    });
	}

	public void contactoSinConexion(String s, String e) {
	    JOptionPane.showMessageDialog(
			    null,
			    s,
			    e,
			    JOptionPane.WARNING_MESSAGE
			);
	}

	public Sistema getSistema() {
		return sistema;
	}

	public Contacto getContactoActual() {
		return contactoActual;
	}
	
	public void notificarDesconexion() {
	    sistema.notificarDesconexion();
	}

}
