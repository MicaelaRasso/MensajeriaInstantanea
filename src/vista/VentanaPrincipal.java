package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.Color;

public class VentanaPrincipal extends JFrame {

	private JPanel contentPane;
	private JButton btnEnviar;
	private JButton btnConversacion;
	private JTextArea txtrEscribirMensaje;
	private JScrollPane spContactos;
	private JScrollPane spConversacion;	
	private JLabel lblNombre;
	private JButton btnContacto;
	

	public VentanaPrincipal() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		spContactos = new JScrollPane();
		spContactos.setBounds(20, 50, 150, 250);
		contentPane.add(spContactos);
		
		JLabel lblContactos = new JLabel("Contactos");
		lblContactos.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
		lblContactos.setHorizontalAlignment(SwingConstants.CENTER);
		lblContactos.setBounds(20, 15, 112, 25);
		contentPane.add(lblContactos);
		
		btnConversacion = new JButton("Iniciar conversación");
		btnConversacion.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		btnConversacion.setBounds(20, 310, 150, 25);
		contentPane.add(btnConversacion);
		
		lblNombre = new JLabel("Nombre de usuario");
		lblNombre.setHorizontalAlignment(SwingConstants.CENTER);
		lblNombre.setFont(new Font("Segoe UI Semibold", Font.BOLD | Font.ITALIC, 12));
		lblNombre.setBounds(200, 29, 350, 14);
		contentPane.add(lblNombre);
		
		spConversacion = new JScrollPane();
		spConversacion.setBounds(200, 50, 350, 250);
		contentPane.add(spConversacion);
		
		txtrEscribirMensaje = new JTextArea();
		txtrEscribirMensaje.setToolTipText("Escribir mensaje...");
		txtrEscribirMensaje.setTabSize(3);
		txtrEscribirMensaje.setForeground(new Color(0, 0, 0));
		txtrEscribirMensaje.setFont(new Font("Segoe UI Semilight", Font.PLAIN, 12));
		txtrEscribirMensaje.setBounds(200, 310, 290, 40);
		contentPane.add(txtrEscribirMensaje);
		
		btnEnviar = new JButton("");
		btnEnviar.setBounds(500, 310, 50, 40);
		contentPane.add(btnEnviar);
		
		btnContacto = new JButton("+");
		btnContacto.setFont(new Font("Segoe UI Semibold", Font.BOLD, 12));
		btnContacto.setBounds(130, 14, 40, 30);
		contentPane.add(btnContacto);
	}
	
	public JPanel getContentPane() {
		return contentPane;
	}

	public JButton getBtnEnviar() {
		return btnEnviar;
	}

	public JButton getBtnConversacion() {
		return btnConversacion;
	}

	public JTextArea getTxtrEscribirMensaje() {
		return txtrEscribirMensaje;
	}

	public JScrollPane getSpContactos() {
		return spContactos;
	}

	public JScrollPane getSpConversacion() {
		return spConversacion;
	}

	public JLabel getLblNombre() {
		return lblNombre;
	}
	
	public JButton getBtnContacto() {
		return btnContacto;
	}
}
