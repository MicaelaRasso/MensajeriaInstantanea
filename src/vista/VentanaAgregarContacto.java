package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaAgregarContacto extends JFrame {

	private JPanel contentPane;
	private JTextField tfNombre;
	private JTextField tfIP;
	private JTextField tfPuerto;
	private JButton btnAgregar;
	private JButton btnVolver;

	public VentanaAgregarContacto() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 360);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNombre = new JLabel("Nombre de usuario");
		lblNombre.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
		lblNombre.setHorizontalAlignment(SwingConstants.CENTER);
		lblNombre.setBounds(40, 35, 200, 20);
		contentPane.add(lblNombre);
		
		tfNombre = new JTextField();
		tfNombre.setColumns(10);
		tfNombre.setBounds(40, 60, 200, 30);
		contentPane.add(tfNombre);
		
		JLabel lblIP = new JLabel("IP");
		lblIP.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
		lblIP.setHorizontalAlignment(SwingConstants.CENTER);
		lblIP.setBounds(40, 105, 200, 20);
		contentPane.add(lblIP);
		
		tfIP = new JTextField();
		tfIP.setColumns(10);
		tfIP.setBounds(40, 130, 200, 30);
		contentPane.add(tfIP);
		
		JLabel lblPuerto = new JLabel("Puerto");
		lblPuerto.setHorizontalAlignment(SwingConstants.CENTER);
		lblPuerto.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
		lblPuerto.setBounds(40, 180, 200, 20);
		contentPane.add(lblPuerto);
		
		tfPuerto = new JTextField();
		tfPuerto.setColumns(10);
		tfPuerto.setBounds(40, 205, 200, 30);
		contentPane.add(tfPuerto);
		
		btnAgregar = new JButton("Agregar");
		btnAgregar.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		btnAgregar.setBounds(150, 259, 90, 23);
		contentPane.add(btnAgregar);
		
		btnVolver = new JButton("Volver");
		btnVolver.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
		btnVolver.setBounds(40, 260, 90, 23);
		contentPane.add(btnVolver);
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public JTextField getTfNombre() {
		return tfNombre;
	}

	public JTextField getTfIP() {
		return tfIP;
	}

	public JTextField getTfPuerto() {
		return tfPuerto;
	}

	public JButton getBtnAgregar() {
		return btnAgregar;
	}
	
	public JButton getBtnVolver() {
		return btnVolver;
	}
}
