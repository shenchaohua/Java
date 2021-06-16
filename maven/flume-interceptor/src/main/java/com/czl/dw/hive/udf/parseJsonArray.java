package com.czl.dw.hive.udf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.ArrayList;

public class parseJsonArray extends UDF {
    public ArrayList<String> evaluate(String jsonStr, String arrKey){
        if (Strings.isNullOrEmpty(jsonStr)) {
            return null;
        }
        try{
            JSONObject object = JSON.parseObject(jsonStr);
            JSONArray jsonArray = object.getJSONArray(arrKey);
            ArrayList<String> result = new ArrayList<>();
            for (Object o: jsonArray){
                result.add(o.toString());
            }
            return result;
        } catch (JSONException e){
            return null;
        }
    }
}
