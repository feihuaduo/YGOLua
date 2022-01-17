package com.myopicmobile.textwarrior.bean;

/**
 * TODO
 *
 * @author 千年纹 1799426163@qq.com
 * @version 1.0.0
 */
public class LuaFunction extends LuaMessage {

    public LuaFunction() {
        setType(TYPE_FUNCTION);
    }

    public LuaFunction(String name) {
        this();
        setName(name);
    }

    public LuaFunction(String returnValue, String name, String args, String doc, String ascription) {
        this();
        setReturnValue(returnValue);
        setName(name);
        setArgs(args);
        setDoc(doc);
        setAscription(ascription);
    }

    @Override
    public String toString() {
        return getReturnValue() + " " + getAscription() + "." + getName() + "(" + getArgs() + ")\n" + getDoc();
    }
}
