package com.myopicmobile.textwarrior.bean;

/**
 * Create By feihua  On 2022/1/15
 */
public class Symbol {
    private String name;
    private String symbol;

    public Symbol(String name) {
        this.name = name;
        this.symbol = name;
    }

    public Symbol(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
