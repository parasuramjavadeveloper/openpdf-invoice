package com.lululemon.openpdf.poc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/V1")
public class OpenPDFController {
    @GetMapping("/create/pdf/")
    public String generateInvoicePDF() throws IOException {
        OpenPDFService openPDFService = new OpenPDFService();
        openPDFService.generateInvoicePDF();
        return "PDF Generated Successfully";
    }
}
