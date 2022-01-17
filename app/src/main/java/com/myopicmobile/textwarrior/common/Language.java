/*
 * Copyright (c) 2013 Tah Wei Hoon.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License Version 2.0,
 * with full text available at http://www.apache.org/licenses/LICENSE-2.0.html
 *
 * This software is provided "as is". Use at your own risk.
 */
package com.myopicmobile.textwarrior.common;

import com.myopicmobile.textwarrior.bean.LuaConstant;
import com.myopicmobile.textwarrior.bean.LuaFunction;
import com.myopicmobile.textwarrior.bean.LuaKey;
import com.myopicmobile.textwarrior.bean.LuaMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Base class for programming language syntax.
 * By default, C-like symbols and operators are included, but not keywords.
 */
public abstract class Language {
    public final static char EOF = '\uFFFF';
    public final static char NULL_CHAR = '\u0000';
    public final static char NEWLINE = '\n';
    public final static char BACKSPACE = '\b';
    public final static char TAB = '\t';
    public final static String GLYPH_NEWLINE = "\u21b5";
    public final static String GLYPH_SPACE = "\u00b7";
    public final static String GLYPH_TAB = "\u00bb";


    private final static char[] BASIC_C_OPERATORS = {
            '(', ')', '{', '}', '.', ',', ';', '=', '+', '-',
            '/', '*', '&', '!', '|', ':', '[', ']', '<', '>',
            '?', '~', '%', '^'
    };

    //关键字
    protected HashMap<LuaKey, Integer> _keywordsMap = new HashMap<>(0);
    //函数
    protected HashMap<LuaFunction, Integer> _namesMap = new HashMap<>(0);
    protected HashMap<LuaConstant, Integer> _constantMap = new HashMap<>(0);

    protected HashMap<String, String[]> _basesMap = new HashMap<>(0);

    protected HashMap<String, Integer> _usersMap = new HashMap<>(0);//userWord是那种用户自己定义的标识符
    protected HashMap<Character, Integer> _operatorsMap = generateOperators(BASIC_C_OPERATORS);

    private ArrayList<LuaMessage> _userCache = new ArrayList<>();
    private LuaMessage[] _userWords = new LuaMessage[0];
    private LuaKey[] _keyword;
    private LuaFunction[] _name;
    private LuaConstant[] _constant;

    public void updateUserWord() {
        // TODO: Implement this method
        LuaMessage[] uw = new LuaMessage[_userCache.size()];
        _userWords = _userCache.toArray(uw);
    }

    public LuaMessage[] getUserWord() {
        return _userWords;
    }

    public LuaFunction[] getNames() {
        return _name;
    }

    public void setNames(String[] names) {
        ArrayList<LuaFunction> buf = new ArrayList<>();
        _namesMap = new HashMap<>(names.length);
        for (int i = 0; i < names.length; ++i) {
            if (!buf.contains(new LuaFunction(names[i])))
                buf.add(new LuaFunction(names[i]));
            _namesMap.put(new LuaFunction(names[i]), Lexer.NAME);
        }
        _name = new LuaFunction[buf.size()];
        buf.toArray(_name);
    }

    public void setNames(List<LuaFunction> names) {
        ArrayList<LuaFunction> buf = new ArrayList<>();
        _namesMap = new HashMap<>(names.size());
        for (int i = 0; i < names.size(); ++i) {
            if (!buf.contains(names.get(i)))
                buf.add(names.get(i));
            _namesMap.put(names.get(i), Lexer.NAME);
        }
        _name = new LuaFunction[buf.size()];
        buf.toArray(_name);
    }

    public String[] getBasePackage(String name) {
        return _basesMap.get(name);
    }

    public LuaKey[] getKeywords() {
        return _keyword;
    }

	public LuaConstant[] getConstant() {
		return _constant;
	}

	public void setKeywords(String[] keywords) {
//        _keyword = new String[keywords.length];
//        for (int i = 0; i < keywords.length; i++) {
//            _keyword[i] = keywords[i] + "[keyword]";
//        }
//
//        _keywordsMap = new HashMap<String, Integer>(keywords.length);
//        for (int i = 0; i < keywords.length; ++i) {
//            _keywordsMap.put(keywords[i], Lexer.KEYWORD);
//        }
        ArrayList<LuaKey> buf = new ArrayList<>();
        _keywordsMap = new HashMap<>(keywords.length);
        for (int i = 0; i < keywords.length; ++i) {
            if (!buf.contains(new LuaKey(keywords[i])))
                buf.add(new LuaKey(keywords[i]));
            _keywordsMap.put(new LuaKey(keywords[i]), Lexer.KEYWORD);
        }
        _keyword = new LuaKey[buf.size()];
        buf.toArray(_keyword);
    }

    public void setConstants(List<LuaConstant> data) {
        ArrayList<LuaConstant> buf = new ArrayList<>();
        _constantMap = new HashMap<>(data.size());
        for (int i = 0; i < data.size(); ++i) {
            if (!buf.contains(data.get(i)))
                buf.add(data.get(i));
            _constantMap.put(data.get(i), Lexer.CONSTANT);
        }
        _constant = new LuaConstant[buf.size()];
        buf.toArray(_constant);
    }

    public void addBasePackage(String name, String[] names) {
        _basesMap.put(name, names);
    }

    public void clearUserWord() {
        _userCache.clear();
        _usersMap.clear();
    }

    public void addUserWord(String name) {
        LuaFunction luaFunction=new LuaFunction(name);
        if (!_userCache.contains(luaFunction) && !_namesMap.containsKey(luaFunction))
            _userCache.add(luaFunction);
        _usersMap.put(name, Lexer.NAME);
    }

    protected void setOperators(char[] operators) {
        _operatorsMap = generateOperators(operators);
    }

    private HashMap<Character, Integer> generateOperators(char[] operators) {
        HashMap<Character, Integer> operatorsMap = new HashMap<Character, Integer>(operators.length);
        for (int i = 0; i < operators.length; ++i) {
            operatorsMap.put(operators[i], Lexer.OPERATOR);
        }
        return operatorsMap;
    }

    public final boolean isOperator(char c) {
        return _operatorsMap.containsKey(Character.valueOf(c));
    }

    public final boolean isKeyword(String s) {
        return _keywordsMap.containsKey(s);
    }

    public final boolean isConstant(LuaConstant s) {
        return _constantMap.containsKey(s);
    }

    public final boolean isName(String s) {
        return _namesMap.containsKey(s);
    }

    public final boolean isBasePackage(String s) {
        return _basesMap.containsKey(s);
    }

    public final boolean isBaseWord(String p, String s) {
        String[] pkg = _basesMap.get(p);
        for (String n : pkg) {
            if (n.equals(s))
                return true;
        }
        return false;
    }

    public final boolean isUserWord(String s) {
        return _usersMap.containsKey(s);
    }

    private boolean contains(String[] a, String s) {
        for (String n : a) {
            if (n.equals(s))
                return true;
        }
        return false;
    }

    private boolean contains(ArrayList<String> a, String s) {
        for (String n : a) {
            if (n.equals(s))
                return true;
        }
        return false;
    }

    /**
     * 空白符
     *
     * @param c
     * @return
     */
    public boolean isWhitespace(char c) {
        return (c == ' ' || c == '\n' || c == '\t' ||
                c == '\r' || c == '\f' || c == EOF);
    }

    /**
     * 点运算符
     *
     * @param c
     * @return
     */
    public boolean isSentenceTerminator(char c) {
        return (c == '.');
    }

    /**
     * 斜杠
     *
     * @param c
     * @return
     */
    public boolean isEscapeChar(char c) {
        return (c == '\\');
    }

    /**
     * Derived classes that do not do represent C-like programming languages
     * should return false; otherwise return true
     */
    public boolean isProgLang() {
        return true;
    }

    /**
     * Whether the word after c is a token
     */
    public boolean isWordStart(char c) {
        return false;
    }

    /**
     * Whether cSc is a token, where S is a sequence of characters that are on the same line
     * 字符串引号
     */
    public boolean isDelimiterA(char c) {
        return (c == '"');
    }

    /**
     * Same concept as isDelimiterA(char), but Language and its subclasses can
     * specify a second type of symbol to use here
     * 单个字符引号
     */
    public boolean isDelimiterB(char c) {
        return (c == '\'');
    }

    /**
     * Whether cL is a token, where L is a sequence of characters until the end of the line
     * 宏定义
     */
    public boolean isLineAStart(char c) {
        return (c == '#');
    }

    /**
     * Same concept as isLineAStart(char), but Language and its subclasses can
     * specify a second type of symbol to use here
     */
    public boolean isLineBStart(char c) {
        return false;
    }

    /**
     * Whether c0c1L is a token, where L is a sequence of characters until the end of the line
     * 单行注释
     */
    public boolean isLineStart(char c0, char c1) {
        return (c0 == '/' && c1 == '/');
    }

    /**
     * Whether c0c1 signifies the start of a multi-line token
     * 多行注释开始
     */
    public boolean isMultilineStartDelimiter(char c0, char c1) {
        return (c0 == '/' && c1 == '*');
    }

    /**
     * Whether c0c1 signifies the end of a multi-line token
     * 多行注释结束
     */
    public boolean isMultilineEndDelimiter(char c0, char c1) {
        return (c0 == '*' && c1 == '/');
    }
}
