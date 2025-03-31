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
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaError extends JFrame {

	private JPanel contentPane;
	private JTextArea txtrMensajeDeError;
	private JButton btnVolver;

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
		txtrMensajeDeError.setBounds(20, 20, 190, 138);
		contentPane.add(txtrMensajeDeError);
		
		btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnVolver.setBounds(121, 177, 89, 23);
		contentPane.add(btnVolver);
	}

	public JTextArea getTxtrMensajeDeError() {
		return txtrMensajeDeError;
	}

	public JButton getBtnVolver() {
		return btnVolver;
	}
}
