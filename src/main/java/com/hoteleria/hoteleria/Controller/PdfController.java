package com.hoteleria.hoteleria.Controller;

import com.hoteleria.hoteleria.DTO.PurchaseDTO;
import com.hoteleria.hoteleria.Service.impl.PurchaseService;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.element.Table;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    private final PurchaseService purchaseService;

    public PdfController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }
    @GetMapping("/payments/{userId}/pdf")
    public ResponseEntity<byte[]> generatePaymentHistoryPdf(@PathVariable Integer userId) {
        List<PurchaseDTO> paymentHistory = purchaseService.getPurchaseHistoryByUserId(userId);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);


            document.add(new Paragraph("Payment History for User ID: " + userId)
                    .setBold()
                    .setFontSize(14)
                    .setMarginBottom(10));


            float[] columnWidths = {100F, 100F, 100F, 100F};
            Table table = new Table(columnWidths);
            table.addHeaderCell(new Cell().add(new Paragraph("Purchase ID").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Date").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Total").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Status").setBold()));


            for (PurchaseDTO purchase : paymentHistory) {
                table.addCell(new Cell().add(new Paragraph(purchase.getId().toString())));
                table.addCell(new Cell().add(new Paragraph(purchase.getCreateAt().toString())));
                table.addCell(new Cell().add(new Paragraph("$" + purchase.getTotal().toString())));
                table.addCell(new Cell().add(new Paragraph(purchase.getPaymentStatus().toString())));
            }


            document.add(table);


            document.close();


            byte[] pdfBytes = baos.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=payment-history.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}