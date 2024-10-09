package com.andre.dojo.Models;

public class CustomJSON {
    private Organization organization;
    private Customer customer;
    private Invoice invoice;

    private Bank bank;

    public CustomJSON(){

    }

    public CustomJSON(Organization organization, Customer customer, Invoice invoice, Bank bank) {
        this.organization = organization;
        this.customer = customer;
        this.invoice = invoice;
        this.bank = bank;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
