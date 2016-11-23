package com.twkj.lovebook.utils;


import java.util.UUID;

/**
 * UUID 生成器
 * 每次通过getUUID生成的uuid实例,
 * 都能产生一个唯一的字符串变量
 * 表的主键字段统一规定为VARCHAR2(36)(以oracle为例)；
 * 生成方式统一使用java的UUID方法生成的值，此值字符统一为大写，不含有“-”中划线。
 * @see java.util.UUID
 */


public class UUIDFactory {
     /**
      * 获得UUID字符串
      * 直接获得uuid生成的唯一字符串
      */
     public static String getUUIDStr(){
         UUID uuid = UUID.randomUUID();
         String str = uuid.toString();
         str = str.replaceAll("[-]", "");
         return str.toUpperCase();
     }

//	public static void main(String[] args) {
//		//测试UUID使用
//
//		System.out.println("测试UUID使用");
//		for(int i=0;i<10;i++){
//			String str = getUUIDStr();
//			System.out.println("UUID.str=["+str+"]\t:UUID.str.length="+str.length());
//		}
//
//	}
}
