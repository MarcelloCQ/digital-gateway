package com.marcello.digitalgateway.iso8583.builder;

import com.marcello.digitalgateway.model.IsoMessage;

public interface ISO8583Builder {
    String build(IsoMessage message);
}
