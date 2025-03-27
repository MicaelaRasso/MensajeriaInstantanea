package prueba;

import modelo.Cliente;
import modelo.Contacto;
import modelo.Servidor;
import modelo.Sistema;
import modelo.Usuario;

public class Prueba {

	public static void main(String[] args) {
		
		Usuario usuario = new Usuario("micaela",3080);
		Sistema sistema = new Sistema(usuario);
		
		
		Servidor servidor = new Servidor(sistema.getUsuario().getPuerto());
		
		
	}

}
