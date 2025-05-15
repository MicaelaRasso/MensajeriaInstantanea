package modelo;

public class InvalidUserException extends Exception {

    public InvalidUserException() {
        super("Usuario inválido");
    }

    public InvalidUserException(String mensaje) {
        super(mensaje);
    }

    public InvalidUserException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public InvalidUserException(Throwable causa) {
        super(causa);
    }
}