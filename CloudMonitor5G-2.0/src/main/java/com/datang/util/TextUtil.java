package com.datang.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class TextUtil {
	/**
	 * 得到全拼
	 * @author lucheng
	 * @date 2020年8月18日 上午11:40:01
	 * @param inputString 需要转化的中文字符串 
	 * @return
	 */
	public static String getPingYin(String inputString) {
	    HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
	    
	    format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
	    
	    format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	    
	    format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
	
	    
	    char[] input = inputString.trim().toCharArray();
	    
	    StringBuffer output = new StringBuffer();
	    
	    try {
	      for (int i = 0; i < input.length; i++) {
	        
	        if (Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
	          String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
	          output.append(temp[0]);
	        } else {
	          
	          output.append(Character.toString(input[i]));
	        } 
	      } 
	    } catch (BadHanyuPinyinOutputFormatCombination e) {
	      e.printStackTrace();
	    } 
	    return output.toString();
	}
	
	/** 
	 * 得到中文首字母小写 
	 * @param str 需要转化的中文字符串 
	 * @return 
	 */  
	public static String getPinYinHeadChar(String str)  
	{  
	    String convert = "";  
	    for (int j = 0; j < str.length(); j++)  
	    {  
	        char word = str.charAt(j);  
	        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);  
	        if (pinyinArray != null)  
	        {  
	            convert += pinyinArray[0].charAt(0);  
	        } else  
	        {  
	            convert += word;  
	        }  
	    }  
	    return convert;  
	}
	
	/** 
	 * 得到中文首字母大写
	 * @param str 需要转化的中文字符串 
	 * @return 
	 */  
	public static String getPinYinHeadCharToUpper(String str)  
	{  
		String spells = getPinYinHeadChar(str);
		String newstr2 = spells.toUpperCase();//使用toUpperCase()方法实现大写转换
	    return newstr2;  
	}
    
    public static void main(String[] args) {
    	String spells = "01";
    	String valueOf = String.valueOf(Long.valueOf(spells)+1);
    	System.out.println(valueOf);
    }
}
