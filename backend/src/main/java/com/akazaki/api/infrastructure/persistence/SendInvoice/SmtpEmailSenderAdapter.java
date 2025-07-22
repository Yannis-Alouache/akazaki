package com.akazaki.api.infrastructure.persistence.SendInvoice;

import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.ports.out.EmailSenderPort;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
public class SmtpEmailSenderAdapter implements EmailSenderPort {

    private final JavaMailSender mailSender;

    @Value("${invoice.sender.email}")
    private String fromEmail;

    @Override
    public void sendInvoiceEmail(Order order, String pdfPath) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(order.getUser().getEmail());
            helper.setSubject("Votre facture – Commande #" + order.getId());
            helper.setText(
                    "Bonjour " + order.getUser().getFirstName() + ",\n\n" +
                            "Veuillez trouver ci-joint la facture de votre commande n°" + order.getId() + ".\n\n" +
                            "Merci pour votre achat et à bientôt !"
            );

            FileSystemResource file = new FileSystemResource(new File(pdfPath));
            helper.addAttachment(new File(pdfPath).getName(), file);

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l’envoi de l’email : " + e.getMessage(), e);
        }
    }
}
