package com.akazaki.api.application.commands.SendInvoice;

import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.ports.out.EmailSenderPort;
import com.akazaki.api.domain.ports.out.OrderRepository;
import com.akazaki.api.infrastructure.exceptions.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendInvoiceByEmailCommandHandler {
    private final OrderRepository orderRepository;
    private final EmailSenderPort emailSenderPort;

    public void handle(Long orderId, String pdfPath) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        emailSenderPort.sendInvoiceEmail(order, pdfPath);
    }
}
