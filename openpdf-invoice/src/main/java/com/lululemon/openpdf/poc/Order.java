package com.lululemon.openpdf.poc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Order {
    private String invoiceName;
    private String partnerAddress;
    private String customerName;
    private String postAdressSupplement;
    private String street;
    private String houseNumber;
    private String postCode;
    private String city;
    private String partnerInvoiceNumber;
    private String zalandoOrderNumber;
    private String zalandoOrderDate;
    private String dateOfDeliveryMessage;
    private String billingDate;
    private String page;
    private String amountVat;
    private String amountNetto;
    private String totalAmount;
    private String partnerCompanyName;
    private String shopNameOnZalando;
    private String address;
    private String managingDirector;
    private String registryCourt;
    private String registrationNumber;
    private String vatIDNumber;
    private String orderItemSku;
    private String interfaceArticleName;
    private String messageQuantity;
    private String unitPrice;
    private String totalPrice;
}
