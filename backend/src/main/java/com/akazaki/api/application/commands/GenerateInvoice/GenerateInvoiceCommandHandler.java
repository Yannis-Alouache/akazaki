package com.akazaki.api.application.commands.GenerateInvoice;

import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.ports.out.OrderRepository;
import com.akazaki.api.domain.ports.out.PdfWriterInvoice;
import com.akazaki.api.infrastructure.exceptions.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;

@Component
@RequiredArgsConstructor
public class GenerateInvoiceCommandHandler {

    private final OrderRepository orderRepository;
    private final PdfWriterInvoice pdfWriterInvoice;

    public void handle(Long orderId) throws FileNotFoundException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        pdfWriterInvoice.writeInvoice(order);
    }
}
