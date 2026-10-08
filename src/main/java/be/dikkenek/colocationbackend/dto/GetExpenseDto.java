package be.dikkenek.colocationbackend.dto;

import java.util.Date;


public class GetExpenseDto {

    private String libele;

    private float price;

    private String date_expense;

    private String coloc;

    public GetExpenseDto(String libele, float price, String date_expense, String coloc)
    {
        setLibele(libele);
        setPrice(price);
        setDate_expense(date_expense);
        setColoc(coloc);
    }

    public String getLibele() {
        return libele;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDate_expense() {
        return date_expense;
    }

    public void setDate_expense(String date_expense) {
        this.date_expense = date_expense;
    }

    public String getColoc() {
        return coloc;
    }

    public void setColoc(String coloc) {
        this.coloc = coloc;
    }

}
