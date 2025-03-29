package vista;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Font;

public class VentanaInicio extends JFrame{

	private JPanel contentPane;
	private JTextField tfNombre;
	private JTextField tfPuerto;
	private JButton btnIngresar;

	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaInicio frame = new VentanaInicio();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public VentanaInicio() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 300);
		setLocationRelativeTo(null);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tfNombre = new JTextField();
		tfNombre.setBounds(40, 60, 200, 30);
		contentPane.add(tfNombre);
		tfNombre.setColumns(10);
		
		JLabel lblNombre = new JLabel("Nombre de usuario");
		lblNombre.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
		lblNombre.setHorizontalAlignment(SwingConstants.CENTER);
		lblNombre.setBounds(40, 35, 200, 20);
		contentPane.add(lblNombre);
		
		JLabel lblPuerto = new JLabel("Puerto");
		lblPuerto.setFont(new Font("Segoe UI Semibold", Font.BOLD, 16));
		lblPuerto.setHorizontalAlignment(SwingConstants.CENTER);
		lblPuerto.setBounds(40, 105, 200, 20);
		contentPane.add(lblPuerto);
		
		tfPuerto = new JTextField();
		tfPuerto.setColumns(10);
		tfPuerto.setBounds(40, 130, 200, 30);
		contentPane.add(tfPuerto);
		
		btnIngresar = new JButton("Ingresar");
		btnIngresar.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnIngresar.setBounds(90, 190, 90, 23);
		contentPane.add(btnIngresar);
	}

	public JTextField getTfNombre() {
		return tfNombre;
	}

	public JTextField getTfPuerto() {
		return tfPuerto;
	}

	public JButton getBtnIngresar() {
		return btnIngresar;
	}

}
