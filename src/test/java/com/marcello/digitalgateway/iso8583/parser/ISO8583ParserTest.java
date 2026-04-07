package com.marcello.digitalgateway.iso8583.parser;

import com.marcello.digitalgateway.model.IsoMessage;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ISO8583ParserTest {

    @Test
    void shouldHandleEmptyOrInvalidMessage() {

        ISO8583Parser parser = new ISO8583ParserImpl();

        String emptyMessage = "";
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> parser.parse(emptyMessage));
        assertEquals("Message cannot be null or empty", ex1.getMessage());

        String nullMessage = null;
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> parser.parse(nullMessage));
        assertEquals("Message cannot be null or empty", ex2.getMessage());

        String shortMessage = "0200\nF23AC48128E08000"; // falta secondary bitmap
        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, () -> parser.parse(shortMessage));
        assertEquals("Invalid ISO8583 message format", ex3.getMessage());
    }
}
