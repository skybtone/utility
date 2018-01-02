package com.xlx.utility.mlang;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

import com.xlx.utility.pojo.*;

public class MlangOutputStream {
	private ByteArrayOutputStream  baos = new ByteArrayOutputStream();
	
	public MlangOutputStream(){
	}
	
	public static byte[] longToByte(long number) { 
        long temp = number; 
        byte[] b = new byte[8]; 
        for (int i = 0; i < b.length; i++) { 
            b[i] = new Long(temp & 0xff).byteValue();
            temp = temp >> 8; // 向右移8位 
        } 
        return b; 
    } 
	
	public static byte[] intToByte(int number) { 
        int temp = number; 
        byte[] b = new byte[4]; 
        for (int i = 0; i < b.length; i++) { 
            b[i] = new Integer(temp & 0xff).byteValue();
            temp = temp >> 8; // 向右移8位 
        } 
        return b; 
    } 
	public static byte[] shortToByte(short number) { 
        int temp = number; 
        byte[] b = new byte[2]; 
        for (int i = 0; i < b.length; i++) { 
            b[i] = new Integer(temp & 0xff).byteValue();
            temp = temp >> 8; // 向右移8位 
        } 
        return b; 
    } 
	
	public static byte[] charToByte(char number) { 
        int temp = number; 
        byte[] b = new byte[1]; 
        for (int i = 0; i < b.length; i++) { 
            b[i] = new Integer(temp & 0xff).byteValue();
            temp = temp >> 8; // 向右移8位 
        } 
        return b; 
    } 
	
	public byte[] getBytes() {
		return baos.toByteArray();
	}
	public void code(Object model) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		Field[] field = model.getClass().getDeclaredFields();
		for (int j = 0; j < field.length; j++) {
			String name = field[j].getName();
			name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
			Type genType = field[j].getGenericType();
			String type = genType.toString();    //获取属性的类型  
			
			Method m = model.getClass().getMethod("get" + name);
			Object obj = m.invoke(model);
		
			if (type.equals("char"))
			{
				Character s = (Character)obj;
				byte[] b = charToByte(s.charValue());
				baos.write(b, 0, b.length);
			}
			else if (type.equals("short"))
			{
				Short s = (Short)obj;
				byte[] b = shortToByte(s.shortValue());
				baos.write(b, 0, b.length);
			}
			else if (type.equals("int"))
			{
				Integer s = (Integer)obj;
				byte[] b = intToByte(s.intValue());
				baos.write(b, 0, b.length);
			}
			else if (type.equals("long"))
			{
				Long s = (Long)obj;
				byte[] b = longToByte(s.longValue());
				baos.write(b, 0, b.length);
			}
			else if (type.equals("class java.lang.String"))
			{
				String s = (String)obj;
				byte[] b = s.getBytes();
				baos.write(b, 0, b.length);
				
				b = charToByte((char)0);
				baos.write(b, 0, b.length);
			}
			else if(type.contains("java.util.ArrayList"))
			{
				ArrayList<?> l = (ArrayList)obj;
				byte[] b = intToByte(l.size());
				baos.write(b, 0, b.length);
				for(int i = 0;i < l.size(); ++i)
					code(l.get(i));
			}
			else
			{
				code(obj);
			}
		}
	}

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		// TODO 自动生成的方法存根
		REQ_SQL sql = new REQ_SQL();
		sql.setStr_select("123");
		
		FIELD_STRU fs = new FIELD_STRU();
		fs.setField_length((short)1);
		fs.setField_no((short)2);
		fs.setField_type((short)3);
		sql.getBase_info().getSeq_field().add(fs);
		
		MlangOutputStream os = new MlangOutputStream();
		os.code(sql);
		byte[] bytes = os.getBytes();
		System.out.print("");
	}

}
