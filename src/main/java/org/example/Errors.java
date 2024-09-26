package org.example;

public enum Errors {
    INPUT("Введены неверные данные. Попробуй ещё раз");

    private String massage;
    Errors(String code){
        this.massage = code;
    }
    public String getMassage(){ return massage;}
}
