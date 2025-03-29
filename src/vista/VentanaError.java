package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.DropMode;

public class VentanaError extends JFrame {

	private JPanel contentPane;
	private JTextArea txtrMensajeDeError;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaError frame = new VentanaError();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VentanaError() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 250, 250);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtrMensajeDeError = new JTextArea();
		txtrMensajeDeError.setWrapStyleWord(true);
		txtrMensajeDeError.setDropMode(DropMode.INSERT);
		txtrMensajeDeError.setLineWrap(true);
		txtrMensajeDeError.setText("Mensaje de error - Mensaje de error - Mensaje de error - Mensaje de error - Mensaje de error");
		txtrMensajeDeError.setToolTipText("");
		txtrMensajeDeError.setFont(new Font("Segoe UI Semibold", Font.BOLD, 13));
		txtrMensajeDeError.setEditable(false);
		txtrMensajeDeError.setBounds(20, 20, 190, 170);
		contentPane.add(txtrMensajeDeError);
	}

	public JTextArea getTxtrMensajeDeError() {
		return txtrMensajeDeError;
	}
	
	
}
