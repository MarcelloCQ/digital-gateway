package com.marcello.digitalgateway.iso8583.parser;

import com.marcello.digitalgateway.iso8583.config.IsoFieldDefinitions;
import com.marcello.digitalgateway.iso8583.util.BitmapUtils;
import com.marcello.digitalgateway.model.FieldDefinition;
import com.marcello.digitalgateway.model.IsoMessage;

import java.util.ArrayList;
import java.util.List;

public class ISO8583ParserImpl implements ISO8583Parser {
    @Override
    public IsoMessage parse(String message) {

        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message cannot be null or empty");
        }

        String[] lines = message.split("\\n");

        if(lines.length < 3) {
            throw new IllegalArgumentException("Invalid ISO8583 message format");
        }

        IsoMessage isoMessage = new IsoMessage();

        String mti = lines[0].trim();
        isoMessage.setMti(mti);

        String primaryBitmapHex = lines[1].trim();
        String secondaryBitmapHex = lines[2].trim();

        String primaryBitmapBin = BitmapUtils.hexToBinary(primaryBitmapHex);
        String secondaryBitmapBin = BitmapUtils.hexToBinary(secondaryBitmapHex);

        List<Integer> fieldsPresent = new ArrayList<>();
        fieldsPresent.addAll(BitmapUtils.extractFields(primaryBitmapBin));
        if(primaryBitmapBin.charAt(0) == '1') {
            fieldsPresent.addAll(BitmapUtils.extractFields(secondaryBitmapBin).stream().map(i -> i + 64).toList());
        }

        int lineIndex = 3;
        for (Integer de : fieldsPresent) {
            if(de == 1) continue;
            if(lineIndex >= lines.length) {
                throw new IllegalArgumentException("Mensaje ISO incompleto: falta el valor del DE-" + de);
            }

            String rawValue = lines[lineIndex].trim();
            FieldDefinition definition = IsoFieldDefinitions.DEFINITIONS.get(de);
            if(definition == null) {
                isoMessage.putField(de, rawValue);
            } else {
                switch(definition.getType()) {
                    case N, ANS -> {
                        if(rawValue.length() > definition.getLength()) {
                            throw new IllegalArgumentException("DE-" + de + " excede longitud máxima: " + rawValue);
                        }
                        isoMessage.putField(de, rawValue);
                    }

                    case LLVAR -> {
                        int len = rawValue.length();
                        if (len > definition.getLength()) {
                            throw new IllegalArgumentException("DE-" + de + " LLVAR excede longitud máxima: " + len);
                        }
                        isoMessage.putField(de, rawValue);
                    }

                    case LLLVAR -> {
                        int len = rawValue.length();
                        if (len > definition.getLength()) {
                            throw new IllegalArgumentException("DE-" + de + " LLLVAR excede longitud máxima: " + len);
                        }
                        isoMessage.putField(de, rawValue);
                    }
                }
            }
            lineIndex++;
        }

        return  isoMessage;
    }
}
