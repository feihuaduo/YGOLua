package com.myopicmobile.textwarrior.bean;

/**
 * TODO
 *
 * @author 千年纹 1799426163@qq.com
 * @version 1.0.0
 */
public class LuaKey extends LuaMessage {

    public LuaKey() {
        setType(TYPE_KEY);
    }

    public LuaKey(String name) {
        this();
        setName(name);
    }

    @Override
    public String toString() {
        return getName();
    }
}
