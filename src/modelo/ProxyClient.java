package modelo;

import java.io.*;
import java.net.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Encapsula la lógica de conexión con el proxy/monitor en puerto 60000,
 * envío de requests y recepción de respuestas con ACK + payload JSON,
 * y reintentos (fail-over).
 */
public class ProxyClient extends Thread {
    private static final String PROXY_HOST = ConfigLoader.host;
    private static final int PROXY_PORT = ConfigLoader.port;
    private static final int MAX_RETRIES = 3;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Sistema sistema;

    // Cola bloqueante para respuestas (una a la vez, FIFO)
    private final BlockingQueue<Request> responseQueue = new ArrayBlockingQueue<>(1);

    public ProxyClient(Sistema s) {
        this.sistema = s;
    }

    private void connect() throws IOException {
        socket = new Socket(PROXY_HOST, PROXY_PORT);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        System.out.println("Conectado al Proxy en " + PROXY_HOST + ":" + PROXY_PORT);
    }

    @Override
    public void run() {
        try {
            connect();

            String header;
            while ((header = in.readLine()) != null) {
                System.out.println("[RECV HEADER] " + header);
                String payload = in.readLine(); // JSON o string plano
                System.out.println("[RECV PAYLOAD] " + payload);

                String op = parseField(header, "OPERACION");
                System.out.println(op);

                if ("GET_MESSAGE".equalsIgnoreCase(op)) {
                    // Mensaje espontáneo: notificar al sistema
                    Request req = JsonConverter.fromJson(payload);
                    sistema.recibirMensaje(req);
                } else if ("CLIENT_REQ".equalsIgnoreCase(op)) {
                    // Respuesta a un send(): colocar en la cola
                    Request resp;
                    if (payload.equals("registrado") || payload.equals("en uso") || payload.equals("enviado")) {
                        resp = new Request();
                        resp.setContenido(payload);
                    } else {
                        resp = JsonConverter.fromJson(payload);
                    }

                    // Intentar poner en la cola sin bloquear indefinidamente
                    if (!responseQueue.offer(resp, 5, TimeUnit.SECONDS)) {
                        System.err.println("No se pudo entregar la respuesta al hilo que hizo send()");
                    }
                } else {
                    System.err.println("Operación desconocida: " + op);
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Request send(Request req) throws IOException, InterruptedException {
        String header = String.format(
                "OPERACION:%s;USER:%s",
                "CLIENT_REQ",
                req.getEmisor().getNombre()
        );
        String payload = JsonConverter.toJson(req);

        IOException lastEx = null;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                if (socket == null || socket.isClosed()) {
                    System.out.println("Reconectando...");
                    connect();
                }

                // Enviar header y payload
                out.println(header);
                out.println(payload);

                // Leer ACK (bloqueantemente pero sincronizado)
                String ack = in.readLine();
                if (!"ACK".equals(ack)) {
                    throw new IOException("ACK inválido: " + ack);
                }

                // Esperar respuesta del hilo lector (hasta 5 segundos)
                Request resp = responseQueue.poll(5, TimeUnit.SECONDS);
                if (resp == null) {
                    throw new IOException("Timeout esperando respuesta del servidor");
                }

                return resp;

            } catch (IOException e) {
                lastEx = e;
                close();
                Thread.sleep(1000); // Espera antes de reintentar
                System.out.println("Reintentando conexión (" + attempt + "/" + MAX_RETRIES + ")");
            }
        }
        throw lastEx;
    }

    public void close() {
        try {
            if (socket != null) socket.close();
            System.out.println("Conexión cerrada con el Proxy.");
        } catch (IOException ignored) {}
    }

    private String parseField(String header, String key) {
        for (String part : header.split(";")) {
            if (part.trim().toUpperCase().startsWith(key.toUpperCase() + ":")) {
                return part.substring(key.length() + 1).trim();
            }
        }
        return "";
    }
}
