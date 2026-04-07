package com.marcello.digitalgateway.iso8583.parser;

import com.marcello.digitalgateway.model.IsoMessage;

public interface ISO8583Parser {
    IsoMessage parse(String message);
}
