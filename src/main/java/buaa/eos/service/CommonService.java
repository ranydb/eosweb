package buaa.eos.service;

import buaa.eos.model.Block;
import net.sf.json.JSONObject;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommonService {
    public static Object autoSetAttr(String json,Object object)throws NoSuchMethodException {
        // 将json字符串转换成jsonObject
        JSONObject jsonObject = JSONObject.fromObject(json);
        Iterator ite = jsonObject.keys();
        // 遍历jsonObject数据
        while (ite.hasNext()) {
            String key = ite.next().toString();
            Object value = jsonObject.get(key);
            char[] cs = key.toCharArray();
            cs[0] -= 32;
            key = "set"+String.valueOf(cs);

            String c = value.getClass().toString();
            if(!c.startsWith("class java")){
                value = value.toString();
            }else if(value instanceof String){
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                try{
                    Date date = df.parse((String)value);
                    value = date;
                }catch (ParseException p){}
            }
            try {
                Method method = object.getClass().getDeclaredMethod(key,value.getClass());
                method.invoke(object, value);
            }catch (Exception e){
                System.out.println(e.fillInStackTrace());
            }
        }
        return object;
    }

}