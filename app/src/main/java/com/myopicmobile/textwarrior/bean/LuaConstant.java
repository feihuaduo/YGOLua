package com.myopicmobile.textwarrior.bean;

/**
 * TODO
 *
 * @author 千年纹 1799426163@qq.com
 * @version 1.0.0
 */
public class LuaConstant extends LuaMessage {

    public LuaConstant() {
        setType(TYPE_CONSTANT);
    }

    public LuaConstant(String value, String name, String doc) {
        this();
        setValue(value);
        setName(name);
        setDoc(doc);
    }


    @Override
    public String toString() {
        return getName() + "=" + getValue() + "\n" + getDoc();
    }
}
