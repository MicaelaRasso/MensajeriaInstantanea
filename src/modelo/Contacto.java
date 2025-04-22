package modelo;

import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Contacto extends Usuario {
    private String ip;
    private Conversacion conversacion;

    public Contacto(String nombre, String IP, int puerto) {
        super(nombre, IP, puerto);
    }
    
 /*   public void recibirMensaje(String c, String fyh) {
        conversacion.recibirMensaje(c, fyh, this);
    }*/
/*
    public void enviarMensaje(Mensaje mensaje) {
        try {
        	mensaje.getUsuario().getConexion().enviarMensaje(mensaje); //deberia hacerlo desde usuario? 

            // Si llegamos hasta acá, se envió correctamente => lo agregamos a la conversación
            conversacion.enviarMensaje(mensaje, this);

        } catch (IOException e) {
            System.err.println("No se pudo enviar mensaje a " + getNombre());

            // Mostrar notificación en la GUI
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(
                    null,
                    "No se pudo enviar el mensaje porque '" + getNombre() + "' no está conectado.",
                    "Error de conexión",
                    JOptionPane.WARNING_MESSAGE
                );
            });
        }
    }*/

    // Getters
    public String getIP() {
        return ip;
    }

    public Conversacion getConversacion() {
        return conversacion;
    }

    public void setConversacion(Conversacion conversacion) {
        this.conversacion = conversacion;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
