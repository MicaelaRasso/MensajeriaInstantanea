package modelo;

import java.io.PrintWriter;

public class Sender {
    private final PrintWriter out;

    public Sender(PrintWriter out) {
        this.out = out;
    }

    public synchronized void sendRequest(Request req) {
        String header = String.format("OPERACION:CLIENT_REQ;USER:%s", req.getEmisor().getNombre());
        String payload = JsonConverter.toJson(req);
        out.println(header);
        out.println(payload);
    }
}