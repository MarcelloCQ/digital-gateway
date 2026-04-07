package com.marcello.digitalgateway.core;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Component
public class MockCoreServer {

    @Value("${core.mock.port}")
    private int port;

    private static final int READ_TIMEOUT_MS = 10_000;

    @PostConstruct
    public void start() {
        new Thread(this::runServer, "MockCoreServer-Thread").start();
    }

    private void runServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("CORE Mock TCP listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout(READ_TIMEOUT_MS);
                new Thread(() -> handleClient(clientSocket), "ClientHandler-" + clientSocket.getPort()).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket socket) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII));
             BufferedWriter writer = new BufferedWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII))) {

            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[1024];
            int read;
            while ((read = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, read);
                if (!reader.ready()) break;
            }

            String message = sb.toString().trim();
            if (message.isEmpty()) return;

            String[] lines = message.split("\n");
            String pan = lines.length > 3 ? lines[3].trim() : "0000000000000000";
            String maskedPan = maskPan(pan);

            System.out.println("Mensaje recibido. MTI: " + lines[0] + ", PAN: " + maskedPan);

            String response = buildResponse(lines);

            System.out.println("Enviando respuesta:\n" + response);

            writer.write(response);
            writer.newLine();
            writer.flush();

        } catch (IOException e) {
            System.err.println("Error manejando cliente: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    private String buildResponse(String[] lines) {
        String mti = lines[0];
        String pan = lines.length > 3 ? lines[3].trim() : "0000000000000000";
        String stan = lines.length > 7 ? lines[7].trim() : "000001";
        String amountStr = lines.length > 5 ? lines[5].trim() : "0";

        long amount;
        try {
            amount = Long.parseLong(amountStr);
        } catch (NumberFormatException e) {
            amount = 0;
        }

        String responseMti;
        String rc;

        if ("0400".equals(mti)) {
            responseMti = "0410";
            rc = "00";
        } else {
            responseMti = "0200".equals(mti) ? "0210" : mti;
            if (amount > 99_999_900L) {
                rc = "51";
            } else if ((pan.charAt(pan.length() - 1) - '0') % 2 == 0) {
                rc = "00";
            } else {
                rc = "05";
            }
        }

        String rrn = stan;
        String approvalCode = String.format("%06d", new Random().nextInt(1_000_000));

        StringBuilder response = new StringBuilder();
        response.append(responseMti).append("\n");

        if (lines.length > 1) response.append(lines[1]).append("\n");
        if (lines.length > 2) response.append(lines[2]).append("\n");

        response.append("DE-37 (RRN):").append(rrn).append("\n");
        response.append("DE-38 (AuthCode):").append(approvalCode).append("\n");
        response.append("DE-39 (RC):").append(rc).append("\n");

        return response.toString();
    }

    private String maskPan(String pan) {
        if (pan == null || pan.length() < 10) {
            return pan;
        }
        return pan.substring(0, 6) + "XXXXXX" + pan.substring(pan.length() - 4);
    }
}