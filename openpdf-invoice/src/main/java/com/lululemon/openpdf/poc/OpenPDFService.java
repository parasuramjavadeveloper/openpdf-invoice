package com.lululemon.openpdf.poc;

import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.*;
import com.lowagie.text.alignment.HorizontalAlignment;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class OpenPDFService {

    private void writeTableHeader(final PdfPTable table) {

        final var font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(10);
        font.setColor(Color.BLACK);
        final var cell = new PdfPCell();
        cell.setBorder(PdfPCell.BOTTOM);
        cell.setBorderWidth(1);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(new Phrase("Dealer Item No.", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Item Description", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Crowd", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Unit price", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Total Amount", font));
        table.addCell(cell);
    }

    private void writeTableData(final PdfPTable table) {
        final var font = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE);
        font.setSize(8);
        font.setColor(255, 117, 24);
        final var cell = new PdfPCell();
        cell.setBorder(PdfPCell.BOTTOM);
        cell.setBorderWidth(1);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(new Phrase(getOrderAPIData().getOrderItemSku(), font));
        table.addCell(cell);
        cell.setPhrase(new Phrase(getOrderAPIData().getInterfaceArticleName(), font));
        table.addCell(cell);
        cell.setPhrase(new Phrase(getOrderAPIData().getMessageQuantity(), font));
        table.addCell(cell);
        cell.setPhrase(new Phrase(getOrderAPIData().getUnitPrice(), font));
        table.addCell(cell);
        cell.setPhrase(new Phrase(getOrderAPIData().getTotalPrice(), font));
        table.addCell(cell);
    }

    private Order getOrderAPIData() {
        final var order = new Order();
        order.setInvoiceName("Invoice");
        order.setPartnerAddress("Chicago,USA");
        order.setCustomerName("Ram");
        order.setPostAdressSupplement("Stadshusgangen, Stockholm");
        order.setStreet("Upper Drottninggatan, ");
        order.setHouseNumber("12 A/4");
        order.setPostCode("103 ");
        order.setCity("Stockholm");
        order.setPartnerInvoiceNumber("311AJ2B4");
        order.setZalandoOrderNumber("8823519");
        order.setZalandoOrderDate("8-12-2022");
        order.setDateOfDeliveryMessage("16-12-2022");
        order.setBillingDate("8-12-2022");
        order.setPage("www.abcxyz.com");
        order.setAmountVat("4.05 EUR");
        order.setAmountNetto("15.95 EUR");
        order.setTotalAmount("20.00 EUR");
        order.setPartnerCompanyName("Expera Enterprises");
        order.setShopNameOnZalando("Expera");
        order.setAddress("6 B/13, Lower Drottninggatan, Stadshusgangen, Stockholm, 113 12");
        order.setManagingDirector("Gustaf Skarsgard, ");
        order.setRegistryCourt("abc123, ");
        order.setRegistrationNumber("P11AR3, ");
        order.setVatIDNumber("VAT11334, ");
        order.setOrderItemSku("100300000");
        order.setInterfaceArticleName("T-Shirt");
        order.setMessageQuantity("5");
        order.setUnitPrice("50");
        order.setTotalPrice("250");
        return order;
    }

    public void generateInvoicePDF() throws DocumentException, IOException {
        final Order order = getOrderAPIData();
        final var path = "openPdfInvoice.pdf";
        final var logoPath = new ClassPathResource("images/LuluLogo.png");
        final var logo = Image.getInstance(logoPath.getFile().toString());
        final var partnerLogoPath = new ClassPathResource("images/zalandoPartner.png");
        final var partnerLogo = Image.getInstance(partnerLogoPath.getFile().toString());
        Image image1 = Image.getInstance(logo);
        Image image2 = Image.getInstance(partnerLogo);
        final var document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(path));
        document.open();
        final var logoTable = new PdfPTable(1);
        logoTable.setWidthPercentage(100);
        final var pLogo = new Paragraph();
        image1.scalePercent(75);
        image2.scalePercent(75);
        pLogo.add(new Chunk(image1, 0, 0, true));
        pLogo.add(new Chunk(image2, 0, 0, true));
        final var logoCell = new PdfPCell();
        logoCell.addElement(pLogo);
        logoTable.addCell(logoCell);
        document.add(logoTable);
        final var font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLACK);
        final var p = new Paragraph(order.getInvoiceName(), font);
        p.setAlignment(Element.ALIGN_LEFT);
        document.add(p);
        final var newLine = new Paragraph("\n");
        document.add(newLine);
        final var addressFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE);
        addressFont.setSize(10);
        addressFont.setColor(255, 117, 24);
        final var apTable = new Table(1);
        apTable.getDefaultCell().setBorder(0);
        apTable.setBorder(Table.BOTTOM);
        apTable.setBorderWidth(1.5f);
        apTable.setPadding(1);
        apTable.setWidth(35);
        apTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
        final var addressPartner = new Paragraph(order.getPartnerAddress(), addressFont);
        addressPartner.setAlignment(Element.ALIGN_LEFT);
        final var aPCell = new Cell();
        aPCell.add(addressPartner);
        apTable.addCell(aPCell);
        document.add(apTable);
        document.add(getCustomerTable());
        document.add(getTable());
        document.add(newLine);
        document.add(newLine);
        final var table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.5f, 3.5f, 3.0f, 3.0f, 1.5f});
        table.setSpacingBefore(10);
        writeTableHeader(table);
        writeTableData(table);
        document.add(table);
        final var tabFont = FontFactory.getFont(FontFactory.HELVETICA);
        tabFont.setSize(8);
        final var tabFont2 = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE);
        tabFont2.setSize(8);
        tabFont2.setColor(255, 117, 24);
        final var tabFont3 = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        tabFont3.setSize(8);

        final var vat = new Paragraph("VAT.20%:", tabFont);
        final var vatAmt = new Paragraph(order.getAmountVat(), tabFont2);
        vat.add(new Paragraph(vatAmt));
        document.add(vat);

        final var netAmt = new Paragraph("(NetAmt:", tabFont);
        final var netAmtVal = new Paragraph(order.getAmountNetto(), tabFont2);
        netAmt.add(new Paragraph(netAmtVal));
        document.add(netAmt);

        final var invoiceAmt = new Paragraph("Invoice Amount EUR \t\t\t", tabFont3);
        invoiceAmt.add(new Paragraph(order.getTotalAmount(), tabFont2));
        document.add(invoiceAmt);

        final var noteParaFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        noteParaFont.setSize(10);
        noteParaFont.setColor(Color.BLACK);
        final var notesParagraph = new Paragraph("Note: ", noteParaFont);
        Font noteContentFont = FontFactory.getFont(FontFactory.HELVETICA);
        noteContentFont.setSize(10);
        noteContentFont.setColor(Color.BLACK);
        final var notesContent = new Paragraph("If you have chosen the invoice payment method, Zalando has already" +
                " sent you the order confirmation by e-mail with a reference to the total amount to be paid.", noteContentFont);
        notesParagraph.add(new Paragraph(notesContent));
        document.add(newLine);
        document.add(notesParagraph);
        document.add(newLine);
        final var noteQuestionFont = FontFactory.getFont(FontFactory.HELVETICA);
        noteQuestionFont.setSize(10);
        noteQuestionFont.setColor(Color.BLACK);
        final var questions = new Paragraph("Do you have any questions about your order? Visit our help pages at "
                + "www.zalando.at/faq - you can also contact us there.", noteQuestionFont);
        document.add(questions);
        final var zalandoAmtFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        zalandoAmtFont.setSize(10);
        zalandoAmtFont.setColor(Color.BLACK);
        final var zalandoAmt = new Paragraph("\n Please transfer outstanding amounts to Zalando only.", zalandoAmtFont);
        document.add(zalandoAmt);
        document.add(newLine);
        document.add(getFooterTable());
        document.close();
    }

    private Table getTable() {
        final var table = new Table(1);
        table.getDefaultCell().setBorder(0);
        table.setBorderWidth(1);
        table.setBorderColor(new Color(200, 200, 200));
        table.setBackgroundColor(new Color(240, 240, 240));
        table.setPadding(1);
        table.setWidth(60);
        table.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        final var cellFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        cellFont.setSize(10);
        final var cellFont2 = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE);
        cellFont2.setSize(10);
        cellFont2.setColor(255, 170, 51);

        final var partnerInvoiceOne = new Paragraph("Partner Invoice Number:", cellFont);
        partnerInvoiceOne.setAlignment(Element.ALIGN_LEFT);
        final var partnerInvoiceTwo = new Paragraph(getOrderAPIData().getPartnerInvoiceNumber(), cellFont2);
        partnerInvoiceTwo.setAlignment(Element.ALIGN_RIGHT);
        partnerInvoiceOne.add(new Paragraph(partnerInvoiceTwo));
        final var cell1 = new Cell();
        cell1.add(partnerInvoiceOne);
        table.addCell(cell1);
        final var zOrderNo = new Paragraph("Zalando Order Number:", cellFont);
        zOrderNo.setAlignment(Element.ALIGN_LEFT);
        final var zOrderNo2 = new Paragraph(getOrderAPIData().getZalandoOrderNumber(), cellFont2);
        zOrderNo2.setAlignment(Element.ALIGN_RIGHT);
        zOrderNo.add(new Paragraph(zOrderNo2));
        final var cell2 = new Cell();
        cell2.add(zOrderNo);
        table.addCell(cell2);

        final var zOrderDate = new Paragraph("Zalando Order Date:", cellFont);
        zOrderDate.setAlignment(Element.ALIGN_LEFT);
        final var zOrderDate2 = new Paragraph(getOrderAPIData().getZalandoOrderDate(), cellFont2);
        zOrderDate2.setAlignment(Element.ALIGN_RIGHT);
        zOrderDate.add(new Paragraph(zOrderDate2));
        final var cell3 = new Cell();
        cell3.add(zOrderDate);
        table.addCell(cell3);

        final var dateDelMsg = new Paragraph("Date Of Delivery Message:", cellFont);
        dateDelMsg.setAlignment(Element.ALIGN_LEFT);
        final var dateDelMsg2 = new Paragraph(getOrderAPIData().getDateOfDeliveryMessage(), cellFont2);
        dateDelMsg2.setAlignment(Element.ALIGN_RIGHT);
        dateDelMsg.add(new Paragraph(dateDelMsg2));
        final var cell4 = new Cell();
        cell4.add(dateDelMsg);
        table.addCell(cell4);

        final var billingDate = new Paragraph("Billing Date:", cellFont);
        billingDate.setAlignment(Element.ALIGN_LEFT);
        final var billingDate2 = new Paragraph(getOrderAPIData().getBillingDate(), cellFont2);
        billingDate2.setAlignment(Element.ALIGN_RIGHT);
        billingDate.add(new Paragraph(billingDate2));
        final var cell5 = new Cell();
        cell5.add(billingDate);
        table.addCell(cell5);

        final var page = new Paragraph("Page:", cellFont);
        page.setAlignment(Element.ALIGN_LEFT);
        final var page2 = new Paragraph(getOrderAPIData().getPage(), cellFont2);
        page2.setAlignment(Element.ALIGN_RIGHT);
        page.add(new Paragraph(page2));
        final var cell6 = new Cell();
        cell6.add(page);
        table.addCell(cell6);
        return table;
    }


    private Table getCustomerTable() {

        final var customerTable = new Table(1);
        customerTable.getDefaultCell().setBorder(0);
        customerTable.setBorder(Table.NO_BORDER);
        customerTable.setPadding(1);
        customerTable.setWidth(35);
        customerTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
        final var fontOne = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE);
        fontOne.setSize(8);
        fontOne.setColor(255, 117, 24);
        final var fontTwo = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE);
        fontTwo.setSize(10);
        fontTwo.setColor(255, 117, 24);
        Cell cellObj = new Cell();
        cellObj.setBorder(Cell.NO_BORDER);

        final var pName = new Paragraph(getOrderAPIData().getCustomerName(), fontOne);
        pName.setAlignment(Element.ALIGN_LEFT);
        cellObj.add(pName);
        final var postAddr = new Paragraph(getOrderAPIData().getPostAdressSupplement(), fontOne);
        postAddr.setAlignment(Element.ALIGN_LEFT);
        cellObj.add(postAddr);
        final var streetHouseNumber = new Paragraph(getOrderAPIData().getStreet().concat(getOrderAPIData().getHouseNumber()), fontTwo);
        streetHouseNumber.setAlignment(Element.ALIGN_LEFT);
        cellObj.add(streetHouseNumber);
        final var postCodeCity = new Paragraph(getOrderAPIData().getPostCode().concat(getOrderAPIData().getCity()), fontTwo);
        postCodeCity.setAlignment(Element.ALIGN_LEFT);
        cellObj.add(postCodeCity);
        customerTable.addCell(cellObj);
        return customerTable;
    }

    private Table getFooterTable() {

        final var footerTable = new Table(1);
        footerTable.getDefaultCell().setBorder(0);
        footerTable.setBorder(Table.TOP);
        footerTable.setBorderWidth(1);
        footerTable.setPadding(1);
        footerTable.setWidth(100);
        footerTable.setHorizontalAlignment(HorizontalAlignment.LEFT);
        final var cell = new Cell();
        cell.setBorder(Cell.NO_BORDER);
        final var font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setSize(6);
        font.setColor(255, 117, 24);
        final var partnerCompanyName = new Paragraph(getOrderAPIData().getPartnerCompanyName(), font);
        partnerCompanyName.setAlignment(Element.ALIGN_LEFT);
        cell.add(partnerCompanyName);
        final var shopNameOnZalando = new Paragraph(getOrderAPIData().getShopNameOnZalando(), font);
        shopNameOnZalando.setAlignment(Element.ALIGN_LEFT);
        cell.add(shopNameOnZalando);
        final var address = new Paragraph(getOrderAPIData().getAddress(), font);
        address.setAlignment(Element.ALIGN_LEFT);
        cell.add(address);
        final var manageDir = new Paragraph(getOrderAPIData().getManagingDirector().concat(getOrderAPIData().getRegistryCourt()).concat(getOrderAPIData().getRegistrationNumber()).concat(getOrderAPIData().getVatIDNumber()), font);
        manageDir.setAlignment(Element.ALIGN_LEFT);
        cell.add(manageDir);
        footerTable.addCell(cell);
        return footerTable;
    }

}
