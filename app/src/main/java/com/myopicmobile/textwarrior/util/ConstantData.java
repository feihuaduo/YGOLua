package com.myopicmobile.textwarrior.util;

import android.content.Context;
import android.util.Log;


import com.myopicmobile.textwarrior.bean.LuaConstant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TODO
 *
 * @author 千年纹 1799426163@qq.com
 * @version 1.0.0
 */
public class ConstantData {
    public static List<LuaConstant> luaConstantList =new ArrayList<>();
    public static void init(Context context){
        List<String> lines = getFromAssets("constant.lua", context);
        String doc="";
        String value="";
        String name="";
        for(String line:lines){
            if(!line.startsWith("--")&&!isBlank(line)){
                Log.i("AAA",line);
                String[] split = line.split("=");
                name=split[0].trim();
                Log.i("AAA", Arrays.toString(split));
                String[] split1 = split[1].split("--");
                value=split1[0].trim();
                if(split1.length>1)
                    doc=split1[1];
                else
                    doc="";
                luaConstantList.add(new LuaConstant(value,name,doc));
            }
        }
        Log.i("AAA", luaConstantList.toString());
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
