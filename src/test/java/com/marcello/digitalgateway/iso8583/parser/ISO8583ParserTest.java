package com.marcello.digitalgateway.iso8583.parser;

import com.marcello.digitalgateway.model.IsoMessage;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ISO8583ParserTest {

    @Test
    void shouldParserMtiAndBitMapsCorrectly() {

        String message = """
        0200
        F23AC48128E08000
        0000000000000000
        0210
        4111111111111111
        000000050000
        240405
        000001
        120000
        0405
        6011
        101
        BANCOMDIGITAL
        BANCOM001
        000000000050000
        604
        """;

        ISO8583Parser parser = new ISO8583ParserImpl();
        IsoMessage result = parser.parse(message);

        assertEquals("0200", result.getMti());

        List<Integer> fields = result.getFieldsPresent();

        assertNotNull(fields);
        assertFalse(fields.isEmpty());

        assertTrue(fields.contains(2), "Debe contener DE-2 (PAN)");
        assertTrue(fields.contains(3), "Debe contener DE-3 (Processing Code)");
        assertTrue(fields.contains(4), "Debe contener DE-4 (Amount)");
        assertTrue(fields.contains(7), "Debe contener DE-7 (Transmission Date)");
        assertTrue(fields.contains(11), "Debe contener DE-11 (STAN)");
        assertTrue(fields.contains(12), "Debe contener DE-12 (Local Time)");
    }

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
