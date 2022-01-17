package com.myopicmobile.textwarrior.bean;

import java.util.Objects;

/**
 * Create By feihua  On 2022/1/16
 */
public class LuaMessage {

    public static final int TYPE_FUNCTION = 0;
    public static final int TYPE_CONSTANT = 1;
    public static final int TYPE_KEY = 2;

    private int type;
    private String name;
    private String doc;
    private String value;
    private String returnValue;
    private String args;
    private String ascription;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getAscription() {
        return ascription;
    }

    public void setAscription(String ascription) {
        this.ascription = ascription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LuaMessage that = (LuaMessage) o;
        return type == that.type && Objects.equals(name, that.name) && Objects.equals(ascription, that.ascription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, ascription);
    }
}
