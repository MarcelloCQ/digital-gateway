package com.marcello.digitalgateway.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class IsoMessage {
    private String mti;
    private List<Integer> fieldsPresent = new ArrayList<>();
    private Map<Integer, String> fields = new HashMap();

    public String getField(int de) {
        return this.fields.get(de);
    }

    public void putField(int de, String value) {
        if (value != null) {
            this.fields.put(de, value);
            if (!fieldsPresent.contains(de)) {
                fieldsPresent.add(de);
            }
        }
    }

}
