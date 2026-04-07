package com.marcello.digitalgateway.service;

import com.marcello.digitalgateway.core.CoreTcpClient;
import org.springframework.stereotype.Service;

@Service
public class CoreGatewayService {
    private final CoreTcpClient tcpClient;

    public CoreGatewayService(CoreTcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    public String sendToCore(String isoMessage) {
        return tcpClient.sendMessage(isoMessage);
    }
}
