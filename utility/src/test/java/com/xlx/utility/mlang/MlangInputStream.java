package com.xlx.utility.mlang;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;

import com.xlx.utility.pojo.*;
import com.xlx.utility.mlang.MlangOutputStream;

public class MlangInputStream {
	private byte[] buffer;
	private int offset;
	
	public MlangInputStream(byte[] b)
	{
		buffer = b;
	}
	
	private void copy(byte[] b) {
		for(int i = 0; i < b.length; ++i)
			b[i] = buffer[offset + i];
		
		offset += b.length;
	}
	
	public void decode(Object model) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException
	{
		Field[] field = model.getClass().getDeclaredFields();
		for (int j = 0; j < field.length; j++) {
			String name = field[j].getName();
			name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
			Type genType = field[j].getGenericType();
			Class<?> fType = field[j].getType();
			String type = genType.toString();    //获取属性的类型  
			
			Method get = model.getClass().getMethod("get" + name);
			
			if (type.equals("char"))
			{
				Method set = model.getClass().getMethod("set" + name,  field[j].getType());
				byte[] b = new byte[1];
				copy(b);
				set.invoke(model, byteToChar(b));
			}
			else if (type.equals("short"))
			{
				Method set = model.getClass().getMethod("set" + name,  field[j].getType());
				byte[] b = new byte[2];
				copy(b);
				set.invoke(model, byteToShort(b));
			}
			else if (type.equals("int"))
			{
				Method set = model.getClass().getMethod("set" + name,  field[j].getType());
				byte[] b = new byte[4];
				copy(b);
				set.invoke(model, byteToInt(b));
			}
			else if (type.equals("long"))
			{
				Method set = model.getClass().getMethod("set" + name,  field[j].getType());
				byte[] b = new byte[8];
				copy(b);
				set.invoke(model, byteToLong(b));
			}
			else if (type.equals("class java.lang.String"))
			{
				Method set = model.getClass().getMethod("set" + name,  field[j].getType());
				int length = 0;
				while(buffer[offset + length] != 0)
					++length;
				
				byte[] b = new byte[length];
				copy(b);
				set.invoke(model, b.toString());
			}
			else if(type.contains("java.util.ArrayList"))
			{				
				Object obj = get.invoke(model);
				ArrayList<Object> l = (ArrayList<Object>)obj;
				
				byte[] b = new byte[4];
				copy(b);
				int count = byteToInt(b);
				
				ParameterizedType pt= (ParameterizedType)get.getGenericReturnType();
				Class type1 = (Class)pt.getActualTypeArguments()[0]; 
				for(int i = 0; i < count; ++i)
				{
					Object newObject = type1.newInstance();
					decode(newObject);
					l.add(newObject);
				}
			}
			else
			{
				Object obj = get.invoke(model);
				decode(obj);
			}
		}
	}
	public static long byteToLong(byte[] b) { 
        long s = 0; 
        long s0 = b[0] & 0xff;// 最低位 
        long s1 = b[1] & 0xff; 
        long s2 = b[2] & 0xff; 
        long s3 = b[3] & 0xff; 
        long s4 = b[4] & 0xff;// 最低位 
        long s5 = b[5] & 0xff; 
        long s6 = b[6] & 0xff; 
        long s7 = b[7] & 0xff; 
 
        // s0不变 
        s1 <<= 8; 
        s2 <<= 16; 
        s3 <<= 24; 
        s4 <<= 8 * 4; 
        s5 <<= 8 * 5; 
        s6 <<= 8 * 6; 
        s7 <<= 8 * 7; 
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7; 
        return s; 
    } 
	
	public static int byteToInt(byte[] b) { 
        int s = 0; 
        int s0 = b[0] & 0xff;// 最低位 
        int s1 = b[1] & 0xff; 
        int s2 = b[2] & 0xff; 
        int s3 = b[3] & 0xff; 
        s3 <<= 24; 
        s2 <<= 16; 
        s1 <<= 8; 
        s = s0 | s1 | s2 | s3; 
        return s; 
    } 
	
	public static short byteToShort(byte[] b) { 
        short s = 0; 
        short s0 = (short) (b[0] & 0xff);// 最低位 
        short s1 = (short) (b[1] & 0xff); 
        s1 <<= 8; 
        s = (short) (s0 | s1); 
        return s; 
    }
	
	public static char byteToChar(byte[] b) { 
        char s = (char) (b[0] & 0xff);
        return s; 
    }
	
	
	public static void main(String[] args) {
		
		try {
			// TODO 自动生成的方法存根
			MlangOutputStream mos = new MlangOutputStream();
			REQ_SQL sql = new REQ_SQL();
			sql.setStr_select("123");
			
			FIELD_STRU fs = new FIELD_STRU();
			fs.setField_length((short)1);
			fs.setField_no((short)2);
			fs.setField_type((short)3);
			sql.getBase_info().getSeq_field().add(fs);
			mos.code(sql);
			byte[] bytes = mos.getBytes();
			
			REQ_SQL sql2 = new REQ_SQL();
			MlangInputStream is = new MlangInputStream(bytes);
			is.decode(sql2);
			System.out.println(sql2.toString());
		} catch (NoSuchMethodException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}catch(InstantiationException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}

}
