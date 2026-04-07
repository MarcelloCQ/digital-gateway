package com.marcello.digitalgateway.iso8583.builder;

import com.marcello.digitalgateway.iso8583.config.IsoFieldDefinitions;
import com.marcello.digitalgateway.model.FieldDefinition;
import com.marcello.digitalgateway.model.FieldType;
import com.marcello.digitalgateway.model.IsoMessage;

import java.util.List;
import java.util.Map;

public class ISO8583BuilderImpl implements ISO8583Builder {
    @Override
    public String build(IsoMessage message) {
        if (message == null || message.getMti() == null) {
            throw new IllegalArgumentException("IsoMessage o MTI no puede ser nulo");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(message.getMti()).append("\n");

        List<Integer> fieldsPresent = message.getFieldsPresent();
        Map<Integer, String> fields = message.getFields();

        for (int de : fieldsPresent) {
            FieldDefinition def = IsoFieldDefinitions.DEFINITIONS.get(de);
            if (def == null) {
                throw new IllegalArgumentException("No hay definición para el campo DE-" + de);
            }

            String value = fields.get(de);
            if (value == null) continue;

            String formatted = formatField(value, def, de);
            sb.append(formatted).append("\n");
        }

        return sb.toString().trim();
    }

    private String formatField(String value, FieldDefinition def, int de) {
        int maxLength = def.getLength();
        FieldType type = def.getType();

        switch (type) {
            case N:
                if (value.length() > maxLength) {
                    throw new IllegalArgumentException("DE-" + de + " excede longitud máxima: " + value);
                }
                return String.format("%" + maxLength + "s", value).replace(' ', '0');

            case ANS:
                if (value.length() > maxLength) {
                    throw new IllegalArgumentException("DE-" + de + " excede longitud máxima: " + value);
                }
                return String.format("%-" + maxLength + "s", value);

            case LLVAR:
                if (value.length() > maxLength) {
                    throw new IllegalArgumentException("DE-" + de + " excede longitud máxima: " + value);
                }
                return value;

            default:
                throw new IllegalArgumentException("Tipo de campo no soportado: " + type);
        }
    }
}
