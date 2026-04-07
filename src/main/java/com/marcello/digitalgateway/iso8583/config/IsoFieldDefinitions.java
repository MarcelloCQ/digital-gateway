package com.marcello.digitalgateway.iso8583.config;

import com.marcello.digitalgateway.model.FieldDefinition;
import com.marcello.digitalgateway.model.FieldType;

import java.util.Map;
import static java.util.Map.entry;

public class IsoFieldDefinitions {

    public static final Map<Integer, FieldDefinition> DEFINITIONS = Map.ofEntries(
            entry(2, new FieldDefinition(FieldType.LLVAR, 19)),
            entry(3, new FieldDefinition(FieldType.N, 6)),
            entry(4, new FieldDefinition(FieldType.N, 12)),
            entry(7, new FieldDefinition(FieldType.N, 10)),
            entry(11, new FieldDefinition(FieldType.N, 6)),
            entry(12, new FieldDefinition(FieldType.N, 6)),
            entry(13, new FieldDefinition(FieldType.N, 4)),
            entry(18, new FieldDefinition(FieldType.N, 4)),
            entry(22, new FieldDefinition(FieldType.N, 3)),
            entry(25, new FieldDefinition(FieldType.N, 2)),
            entry(37, new FieldDefinition(FieldType.ANS, 12)),
            entry(38, new FieldDefinition(FieldType.ANS, 6)),
            entry(39, new FieldDefinition(FieldType.ANS, 2)),
            entry(41, new FieldDefinition(FieldType.ANS, 8)),
            entry(42, new FieldDefinition(FieldType.ANS, 15)),
            entry(49, new FieldDefinition(FieldType.N, 3)),
            entry(102, new FieldDefinition(FieldType.LLVAR, 28)),
            entry(103, new FieldDefinition(FieldType.LLVAR, 28))
    );

}
