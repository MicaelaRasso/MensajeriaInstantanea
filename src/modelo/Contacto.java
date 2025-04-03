package modelo;
import java.io.IOException;
public class Contacto extends Usuario {
    private String ip;
    private Conversacion conversacion;
    private Cliente cliente;

    public Contacto(String nombre, String IP, int puerto) {
        super(nombre, puerto);
        this.ip = IP;
        try {
            this.cliente = new Cliente(IP, puerto);
        } catch (IOException e) {
            System.err.println("No se pudo conectar con el contacto: " + nombre);
            this.cliente = null;  // Se deja null para manejar la reconexión si es necesario
        }
    }

    public void recibirMensaje(String c, String fyh) {
        conversacion.recibirMensaje(c, fyh, this);
    }

    public void enviarMensaje(Mensaje mensaje) {
        conversacion.enviarMensaje(mensaje, this);
    }

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

    public Cliente getCliente() {
        return cliente;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
