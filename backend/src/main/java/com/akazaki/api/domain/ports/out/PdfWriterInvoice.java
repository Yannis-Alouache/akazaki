package com.akazaki.api.domain.ports.out;

import com.akazaki.api.domain.model.Order;

import java.io.FileNotFoundException;

public interface PdfWriterInvoice {
    void writeInvoice(Order order) throws FileNotFoundException;
}
