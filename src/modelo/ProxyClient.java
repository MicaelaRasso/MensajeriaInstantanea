package modelo;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

/**
 * Encapsula la lógica de conexión con el proxy/monitor en puerto 60000,
 * envío de requests y recepción de respuestas con ACK + payload JSON,
 * y reintentos (fail-over).
 */
public class ProxyClient {
    private static final String PROXY_HOST = "127.0.0.1";
    private static final int PROXY_PORT = 60000;
    private static final int MAX_RETRIES = 3;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * Abre un socket con el proxy y los streams
     */
    private void connect() throws IOException {
        socket = new Socket(PROXY_HOST, PROXY_PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Conectado al Proxy en " + PROXY_HOST + ":" + PROXY_PORT);
    }

    /**
     * Envía un Request y devuelve la respuesta JSON ya convertida a Request.
     * @param req Request a enviar.
     * @return Request recibido en la respuesta.
     * @throws IOException si hay problemas en la conexión o reintentos fallidos.
     */
    public Request send(Request req) throws IOException {
        String header = String.format(
            "OPERACION:%s;USER:%s",
            req.getOperacion(),
            req.getEmisor().getNombre()
        );
        String payload = JsonConverter.toJson(req);

        IOException lastEx = null;
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                if (socket == null || socket.isClosed()) {
                    connect();
                }

                // 1) enviamos header + payload
                out.println(header);
                out.println(payload);

                // 2) leemos ACK
                String ack = in.readLine();
                if (!"ACK".equals(ack)) {
                    throw new IOException("ACK inválido: " + ack);
                }

                // 3) leemos respuesta JSON
                String jsonResp = in.readLine();
                Request resp = JsonConverter.fromJson(jsonResp);
                return resp;
            } catch (IOException e) {
                lastEx = e;
                close();
                sleep(200); // Espera antes de reintentar
                System.out.println("Reintentando conexión (" + attempt + "/" + MAX_RETRIES + ")");
            }
        }
        throw lastEx;
    }

    /**
     * Cierra el socket y los streams.
     */
    public void close() {
        try {
            if (socket != null) socket.close();
            System.out.println("Conexión cerrada con el Proxy.");
        } catch (IOException ignored) {}
    }

    /**
     * Pausa el hilo actual por un tiempo determinado.
     */
    private void sleep(long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException ignored) {}
    }
}
