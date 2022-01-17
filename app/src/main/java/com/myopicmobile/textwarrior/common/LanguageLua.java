/*
 * Copyright (c) 2013 Tah Wei Hoon.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License Version 2.0,
 * with full text available at http://www.apache.org/licenses/LICENSE-2.0.html
 *
 * This software is provided"as is". Use at your own risk.
 */
package com.myopicmobile.textwarrior.common;

import com.myopicmobile.textwarrior.bean.LuaFunction;
import com.myopicmobile.textwarrior.util.ConstantData;
import com.myopicmobile.textwarrior.util.FunctionData;
import com.ourygo.ygolua.LuaApplication;

import java.util.List;

/**
 * Singleton class containing the symbols and operators of the Java language
 */
public class LanguageLua extends Language {
	private static Language _theOne = null;

	private final static String[] keywords = {
		"and","break","do","else","elseif","end","false","for","function"
			,"if","in","local","nil","not","or","repeat","return"
			,"then","true","until","while","goto"
    };

    private final static String[] function = {"Card.GetCode(Card c)"};

    private final static char[] BASIC_C_OPERATORS = {
        '(', ')', '{', '}', '.', ',', ';', '=', '+', '-',
        '/', '*', '&', '!', '|', ':', '[', ']', '<', '>',
        '?', '~', '%', '^'
	};

	public static Language getInstance() {
		if (_theOne == null) {
			_theOne = new LanguageLua();
		}
		return _theOne;
	}

	private LanguageLua() {
        setOperators(BASIC_C_OPERATORS);
		setKeywords(keywords);
		FunctionData.init(LuaApplication.getContext());
		ConstantData.init(LuaApplication.getContext());
		List<LuaFunction> luaFunctionList=FunctionData.luaFunctionList;
//		String[] ss=new String[luaFunctionList.size()];
//		for (int i=0;i<luaFunctionList.size();i++) {
//			LuaFunction luaFunction=luaFunctionList.get(i);
//			String message =luaFunction.getName()+ " | "
//					+luaFunction.getReturnValue()+ " | "
//					+luaFunction.getArgs()+ " | "
//					+luaFunction.getDoc()+ " | "
//					+luaFunction.getAscription()+ " | ";
//			ss[i]=message;
//		}


        setNames(luaFunctionList);
        setConstants(ConstantData.luaConstantList);
		addUserWord("Card");
		addUserWord("Effect");
		addUserWord("Group");
		addUserWord("Duel");
		addUserWord("aux");
		addUserWord("Debug");
		updateUserWord();
	}

	/**
	 * Java has no preprocessors. Override base class implementation
	 */
	public boolean isLineAStart(char c) {
		return false;
	}
}
