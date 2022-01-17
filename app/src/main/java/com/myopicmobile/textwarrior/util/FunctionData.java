package com.myopicmobile.textwarrior.util;

import android.content.Context;

import com.myopicmobile.textwarrior.bean.LuaFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author 千年纹 1799426163@qq.com
 * @version 1.0.0
 */
public class FunctionData {
    public static List<LuaFunction> luaFunctionList =new ArrayList<>();
    public static void init(Context context){
        List<String> lines = getFromAssets("_functions.txt", context);
        String ascription="";
        String doc="";
        String returnValue="";
        String name="";
        String args="";
        for(String line:lines){
            if(line.startsWith("=")){
                ascription=line.replaceAll("[= ]","");
                continue;
            }
            if(line.startsWith("●")){
                line=line.replaceAll("(●|"+ascription+"\\.)","");
                int index=line.indexOf("(");
                String[] start=line.substring(0,index).split(" ");
                String end=line.substring(index+1,line.length()-1);
                returnValue=start[0];
                name=start[1];
                args=end;
                continue;
            }
            if(isBlank(line)){
                luaFunctionList.add(new LuaFunction(returnValue,name,args,doc,ascription));
                doc="";
            }else {
                doc+=line.trim()+"\n";
            }
        }
    }
    private static List<String> getFromAssets(String fileName,Context context){
        List<String> lines=new ArrayList<>();
        try(InputStreamReader inputStreamReader = new InputStreamReader(context.getResources().getAssets().open(fileName))) {
            BufferedReader bufReader = new BufferedReader(inputStreamReader);
            String line="";
            while((line = bufReader.readLine()) != null){
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
    public static boolean isBlank(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

}
