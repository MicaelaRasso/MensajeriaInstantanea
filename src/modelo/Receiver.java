package modelo;

import java.io.BufferedReader;
import java.io.IOException;

public class Receiver extends Thread {
    private final BufferedReader in;
    private final Observable<Request> responseObservable;
    private final Observable<Request> messageObservable;

    public Receiver(BufferedReader in, Observable<Request> responseObservable, Observable<Request> messageObservable) {
        this.in = in;
        this.responseObservable = responseObservable;
        this.messageObservable = messageObservable;
    }

    @Override
    public void run() {
        try {
            String header;
            while ((header = in.readLine()) != null) {
                String payload = in.readLine();
                System.out.println("[Cliente] Header: " + header);
                System.out.println("[Cliente] Payload: " + payload);
                
                Request req = JsonConverter.fromJson(payload);
                String op = parseField(header, "OPERACION");

                switch (op) {
                    case "GET_MESSAGE":
                        messageObservable.notifyObservers(req);
                        break;
                    case "RESPUESTA":
                        responseObservable.notifyObservers(req);
                        break;
                    default:
                        System.out.println("[Receiver] Operación desconocida: " + op);
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private String parseField(String header, String key) {
        for (String part : header.split(";")) {
            if (part.startsWith(key + ":")) {
                return part.substring(key.length() + 1).trim();
            }
        }
        return "";
    }
}