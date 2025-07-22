package com.akazaki.api.domain.ports.out;

import com.akazaki.api.domain.model.Order;

public interface EmailSenderPort {
    void sendInvoiceEmail(Order order, String pdfPath);
}
