package com.marcello.digitalgateway.controller;

import com.marcello.digitalgateway.service.CoreGatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gateway")
public class GatewayController {

    private final CoreGatewayService coreGatewayService;

    public GatewayController(CoreGatewayService coreGatewayService) {
        this.coreGatewayService = coreGatewayService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendIsoMessage(@RequestBody String isoMessage) {
        try {
            String response = coreGatewayService.sendToCore(isoMessage);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(504).body("Error al comunicarse con el CORE: " + e.getMessage());
        }
    }
}
