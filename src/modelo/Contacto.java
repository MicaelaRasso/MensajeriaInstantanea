package modelo;
import java.io.IOException;
public class Contacto extends Usuario {
    private String ip;
    private Conversacion conversacion;
    private Cliente cliente;

    public Contacto(String nombre, String IP, int puerto) {
        super(nombre, puerto);
        this.ip = IP;
        this.cliente = null; // Se inicializa en null, se conecta solo al enviar
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
