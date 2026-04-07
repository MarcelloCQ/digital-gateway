package com.marcello.digitalgateway.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FieldDefinition {

    private FieldType type;
    private int length;
}
