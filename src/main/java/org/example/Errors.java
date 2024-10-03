package org.example;

public enum Errors {
    INPUT("Введены неверные данные. Попробуй ещё раз");

    private String errorMessage;
    Errors(String code){
        this.errorMessage = code;
    }
    public String getErrorMessage(){ return errorMessage;}
}
