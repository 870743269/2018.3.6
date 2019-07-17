package com.gy.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ByUtils {
	/**
	 * 完成对象的序列化
	 * **/
	public static byte[] objToByte(Object obj){
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			
			oos.writeObject(obj);
			byte[] buf = baos.toByteArray();
			
			oos.flush();
			oos.close();
			baos.close();
			return buf;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 完成对象的反序列化
	 * **/
	public static <T> T byteToObj(byte[] buf,Class<T> clz){
		try{
			//创建字节数组输入流对象
			ByteArrayInputStream bais=new ByteArrayInputStream(buf);
			//创建对象输入流
			ObjectInputStream ois = new ObjectInputStream(bais);
			
			T t = (T) ois.readObject();
			
			ois.close();
			bais.close();
			return t;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
}