package com.datang.web.beans.report;

public class PngColorInfo {
	
	Double min;
	Double max;
	Integer red;
	Integer green;
	Integer blue;
	
	public PngColorInfo(String strRsrp, String strColor){
		String []arrRsrp = strRsrp.split("[|]",-1);
		String []arrColor = strColor.split("[|]",-1);
		
		if ( arrRsrp.length == 2 ){
			if ( arrRsrp[0].equals("MIN") ){
				min = -1000000000.0;
			}else{
				min = Double.valueOf(arrRsrp[0]);
			}
			
			if ( arrRsrp[1].equals("MAX") ){
				max = 1000000000.0;
			}else{
				max = Double.valueOf(arrRsrp[1]);
			}	
		}
		
		if ( arrColor.length == 2 ){
			red = Integer.valueOf(arrColor[0].substring(0, 2), 16);
			green = Integer.valueOf(arrColor[0].substring(2, 4), 16);
			blue = Integer.valueOf(arrColor[0].substring(4, 6), 16);	
		}
	}
	public Double getMin() {
		return min;
	}
	public void setMin(Double min) {
		this.min = min;
	}
	public Double getMax() {
		return max;
	}
	public void setMax(Double max) {
		this.max = max;
	}
	public Integer getRed() {
		return red;
	}
	public void setRed(Integer red) {
		this.red = red;
	}
	public Integer getGreen() {
		return green;
	}
	public void setGreen(Integer green) {
		this.green = green;
	}
	public Integer getBlue() {
		return blue;
	}
	public void setBlue(Integer blue) {
		this.blue = blue;
	}
	public boolean isWithin(Double dRsrp){
		return (dRsrp >= min && dRsrp < max);
	}
}
