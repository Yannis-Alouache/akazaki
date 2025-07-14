package com.akazaki.api.infrastructure.persistence.GenerateInvoice;

import com.akazaki.api.domain.model.Order;
import com.akazaki.api.domain.model.OrderItem;
import com.akazaki.api.domain.ports.out.PdfWriterInvoice;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Component
public class PdfInvoiceWriterAdapter implements PdfWriterInvoice {

    @Value("${invoice.path}")
    private String invoicePath;

    @Override
    public String writeInvoice(Order order) throws FileNotFoundException {
        String formattedDate = order.getDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String fileName = invoicePath + "facture_" + formattedDate + "_" + order.getId() + ".pdf";

        try (PdfWriter pdfWriter = new PdfWriter(fileName);
             PdfDocument pdf = new PdfDocument(pdfWriter);
             Document document = new Document(pdf)) {

            document.add(new Paragraph("FACTURE")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("N° de commande : " + order.getId()));
            document.add(new Paragraph("Date : " + order.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            document.add(new Paragraph("Client : " + order.getUser().getFirstName() + " " + order.getUser().getLastName()));
            document.add(new Paragraph("Email : " + order.getUser().getEmail()));
            document.add(new Paragraph("Adresse : " +
                    order.getShippingAddress().getStreetNumber() + " " +
                    order.getShippingAddress().getStreet() + ", " +
                    (order.getShippingAddress().getAddressComplement() != null ? order.getShippingAddress().getAddressComplement() + ", " : "") +
                    order.getShippingAddress().getCity() + " " +
                    order.getShippingAddress().getPostalCode() + ", " +
                    order.getShippingAddress().getCountry()));
            document.add(new Paragraph("\n"));

            float[] columnWidths = {4, 1, 2, 2};
            Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();

            String[] titles = {"Produit", "Qté", "PU (€)", "Total (€)"};
            for (String t : titles) {
                table.addHeaderCell(new Paragraph(t)
                        .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                        .setBold());
            }

            for (OrderItem item : order.getItems()) {
                table.addCell(item.getProduct().getName());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell(String.format("%.2f", item.getPrice()));
                table.addCell(String.format("%.2f", item.getQuantity() * item.getPrice()));
            }

            document.add(table);
            document.add(new Paragraph("\n"));

            double total = order.calculateTotalPrice();
            document.add(new Paragraph("Total de la commande : " + String.format("%.2f", total) + " €")
                    .setBold()
                    .setTextAlignment(TextAlignment.RIGHT));

        } catch (IOException e) {
            throw new FileNotFoundException("Erreur lors de la génération du PDF : " + e.getMessage());
        }
        return fileName;
    }

}
