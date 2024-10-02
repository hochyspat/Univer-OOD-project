package org.example;

public enum Errors {
    INPUT("Введены неверные данные. Попробуй ещё раз");

    private String errorMassage;
    Errors(String code){
        this.errorMassage = code;
    }
    public String getErrorMassage(){ return errorMassage;}
}
