package com.marcello.digitalgateway.core;

import com.marcello.digitalgateway.exception.CoreTimeoutException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class CoreTcpClient {

    @Value("${core.tcp.client.host}")
    private String host;

    @Value("${core.tcp.client.port}")
    private int port;

    private static final int CONNECT_TIMEOUT_MS = 5000;
    private static final int READ_TIMEOUT_MS = 10000;

    public String sendMessage(String isoMessage) {
        String response;
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), CONNECT_TIMEOUT_MS);
            socket.setSoTimeout(READ_TIMEOUT_MS);

            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII));
                 BufferedReader reader = new BufferedReader(
                         new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII))) {

                logMessage("Enviando al CORE:", isoMessage);

                writer.write(isoMessage);
                writer.flush();

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null && !line.isBlank()) {
                    sb.append(line).append("\n");
                }
                response = sb.toString().trim();

                logMessage("Respuesta del CORE:", response);

            }

        } catch (IOException e) {
            throw new CoreTimeoutException("Timeout o error al conectar con el CORE: " + e.getMessage());
        }

        return response;
    }

    private void logMessage(String prefix, String message) {
        String masked = message.replaceAll("(\\d{6})\\d+(\\d{4})", "$1XXXXXX$2");
        System.out.println(prefix + "\n" + masked);
    }

//    public static String generateStan() {
//        return String.format("%06d", ThreadLocalRandom.current().nextInt(0, 1_000_000));
//    }
}