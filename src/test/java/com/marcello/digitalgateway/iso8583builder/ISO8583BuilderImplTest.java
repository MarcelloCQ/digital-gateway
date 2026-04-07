package com.marcello.digitalgateway.iso8583builder;

import com.marcello.digitalgateway.iso8583.builder.ISO8583Builder;
import com.marcello.digitalgateway.iso8583.builder.ISO8583BuilderImpl;
import com.marcello.digitalgateway.model.IsoMessage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ISO8583BuilderImplTest {

    @Test
    void ISO8583BuilderImplTest(){
        IsoMessage msg = new IsoMessage();
        msg.setMti("0200");
        msg.getFieldsPresent().add(2);
        msg.putField(2, "4111111111111111");
        msg.getFieldsPresent().add(4);
        msg.putField(4, "50000");
        msg.getFieldsPresent().add(37);
        msg.putField(37, "000001");

        ISO8583Builder builder = new ISO8583BuilderImpl();
        String isoMessageStr = builder.build(msg);

        System.out.println("Mensaje ISO generado:\n" + isoMessageStr);

        assertTrue(isoMessageStr.contains("0200"));
        assertTrue(isoMessageStr.contains("4111111111111111"));
        assertTrue(isoMessageStr.contains("000000050000"));
        assertTrue(isoMessageStr.contains("000001"));
    }

}
