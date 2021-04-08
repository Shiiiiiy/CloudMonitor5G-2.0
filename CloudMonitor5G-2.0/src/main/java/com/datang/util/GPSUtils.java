/**
 * 
 */
package com.datang.util;

import java.util.Date;

/**
 * 三种坐标系互转工具<br>
 * WGS-84:国际标准,GPS坐标(Google Earth使用,或者GPS模块)<br>
 * GCJ-02:中国坐标偏移标准,Google Map,高德,腾讯使用<br>
 * BD-09:百度坐标偏移标准,Baidu Map使用<br>
 * <br>
 * WGS-84 to GCJ-02:GPSUtils.wgs2gcj(lat,lng);<br>
 * <br>
 * GCJ-02 to WGS-84 粗略:GPSUtils.gcj2wgs(lat,lng);<br>
 * <br>
 * GCJ-02 to WGS-84 精确(二分极限法)<br>
 * double threshold = 0.000000001;
 * 目前设置的是精确到小数点后9位，这个值越小，越精确，但是java中，浮点运算本身就不太精确，九位在GPS里也偏差不大了<br>
 * GPSUtils.gcj2wgs_exact(lat,lng);<br>
 * <br>
 * GCJ-02 to BD-09:GPSUtils.gcj2bd09(lat,lng);<br>
 * <br>
 * BD-09 to GCJ-02:GPSUtils.bd092gcj(lat,lng);<br>
 * <br>
 * 求距离:GPSUtils.distance(latA,lngA,latB,lngB);<br>
 * <br>
 * 
 * @see 示例：见mian();
 * 
 * @author yinzhipeng
 * @date:2015年12月11日 下午3:23:39
 * @version
 */
public class GPSUtils {

	private static double PI = 3.14159265358979324;
	private static double X_PI = 3.14159265358979324 * 3000.0 / 180.0;

	private static double[] delta(double lat, double lon) {
		double a = 6378245.0; // a: 卫星椭球坐标投影到平面地图坐标系的投影因子。
		double ee = 0.00669342162296594323; // ee: 椭球的偏心率。
		double dLat = transformLat(lon - 105.0, lat - 35.0);
		double dLon = transformLon(lon - 105.0, lat - 35.0);
		double radLat = lat / 180.0 * PI;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * PI);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * PI);
		return new double[] { dLat, dLon };
	}

	/**
	 * WGS-84 to GCJ-02
	 * 
	 * @param wgsLat
	 * @param wgsLon
	 * @return double[] {gcjLat,gcjLon}
	 */
	public static double[] wgs2gcj(double wgsLat, double wgsLon) {
		if (outOfChina(wgsLat, wgsLon)) {
			return new double[] { wgsLat, wgsLon };
		}
		double[] d = delta(wgsLat, wgsLon);
		return new double[] { wgsLat + d[0], wgsLon + d[1] };
	}

	/**
	 * GCJ-02 to WGS-84
	 * 
	 * @param gcjLat
	 * @param gcjLon
	 * @return double[] {wgsLat,wgsLon}
	 */
	public static double[] gcj2wgs(double gcjLat, double gcjLon) {
		if (outOfChina(gcjLat, gcjLon)) {
			return new double[] { gcjLat, gcjLon };
		}
		double[] d = delta(gcjLat, gcjLon);
		return new double[] { gcjLat - d[0], gcjLon - d[1] };
	}

	/**
	 * GCJ-02 to WGS-84 exactly
	 * 
	 * @param gcjLat
	 * @param gcjLon
	 * @return double[] {wgsLat,wgsLon}
	 */
	public static double[] gcj2wgs_exact(double gcjLat, double gcjLon) {
		double initDelta = 0.01;
		double threshold = 0.000000001;
		double dLat = initDelta, dLon = initDelta;
		double mLat = gcjLat - dLat, mLon = gcjLon - dLon;
		double pLat = gcjLat + dLat, pLon = gcjLon + dLon;
		double wgsLat, wgsLon, i = 0;
		while (true) {
			wgsLat = (mLat + pLat) / 2;
			wgsLon = (mLon + pLon) / 2;
			double[] tmp = wgs2gcj(wgsLat, wgsLon);
			dLat = tmp[0] - gcjLat;
			dLon = tmp[1] - gcjLon;
			if ((Math.abs(dLat) < threshold) && (Math.abs(dLon) < threshold))
				break;
			if (dLat > 0)
				pLat = wgsLat;
			else
				mLat = wgsLat;
			if (dLon > 0)
				pLon = wgsLon;
			else
				mLon = wgsLon;
			if (++i > 10000)
				break;
		}
		return new double[] { wgsLat, wgsLon };
	}

	/**
	 * GCJ-02 to BD-09
	 * 
	 * @param gcjLat
	 * @param gcjLon
	 * @return double[] {bdLat,bdLon}
	 */
	public static double[] gcj2bd09(double gcjLat, double gcjLon) {
		double x = gcjLon, y = gcjLat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * X_PI);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * X_PI);
		double bdLon = z * Math.cos(theta) + 0.0065;
		double bdLat = z * Math.sin(theta) + 0.006;
		return new double[] { bdLat, bdLon };
	}

	/**
	 * BD-09 to GCJ-02
	 * 
	 * @param bdLat
	 * @param bdLon
	 * @return double[] {gcjLat,gcjLon}
	 */
	public static double[] bd092gcj(double bdLat, double bdLon) {
		double x = bdLon - 0.0065, y = bdLat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * X_PI);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * X_PI);
		double gcjLon = z * Math.cos(theta);
		double gcjLat = z * Math.sin(theta);
		return new double[] { gcjLat, gcjLon };
	}

	/**
	 * WGS-84 to Web mercator<br>
	 * mercatorLat -> y mercatorLon -> x
	 * 
	 * @param wgsLat
	 * @param wgsLon
	 * @return double[] { y, x }
	 */
	public static double[] wgs2mercator(double wgsLat, double wgsLon) {
		double x = wgsLon * 20037508.34 / 180.;
		double y = Math.log(Math.tan((90. + wgsLat) * PI / 360.)) / (PI / 180.);
		y = y * 20037508.34 / 180.;
		return new double[] { y, x };

	}

	/**
	 * Web mercator to WGS-84<br>
	 * mercatorLat -> y mercatorLon -> x
	 * 
	 * @param mercatorLat
	 * @param mercatorLon
	 * @return double[] { y, x }
	 */
	public static double[] mercator2wgs(double mercatorLat, double mercatorLon) {
		double x = mercatorLon / 20037508.34 * 180.;
		double y = mercatorLat / 20037508.34 * 180.;
		y = 180 / PI * (2 * Math.atan(Math.exp(y * PI / 180.)) - PI / 2);
		return new double[] { y, x };

	}

	/**
	 * two point's distance
	 * 
	 * @param latA
	 * @param lonA
	 * @param latB
	 * @param lonB
	 * @return double distance
	 */
	public static double distance(double latA, double lonA, double latB,
			double lonB) {
		double earthR = 6371000.;
		double x = Math.cos(latA * PI / 180.) * Math.cos(latB * PI / 180.)
				* Math.cos((lonA - lonB) * PI / 180);
		double y = Math.sin(latA * PI / 180.) * Math.sin(latB * PI / 180.);
		double s = x + y;
		if (s > 1)
			s = 1;
		if (s < -1)
			s = -1;
		double alpha = Math.acos(s);
		double distance = alpha * earthR;
		return distance;
	}

	private static boolean outOfChina(double lat, double lon) {
		if (lon < 72.004 || lon > 137.8347)
			return true;
		if (lat < 0.8293 || lat > 55.8271)
			return true;
		return false;
	}

	private static double transformLat(double x, double y) {
		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
				+ 0.2 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
		ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
		return ret;
	}

	private static double transformLon(double x, double y) {
		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
				* Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
		ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
		ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0
				* PI)) * 2.0 / 3.0;
		return ret;
	}

	public static void main(String[] args) {
		// 121.47432, 31.2339
		// 121.46771, 31.25407
		System.out.println("GPS: 31.175633,121.393348678679");
		System.out.println(new Date().getTime());
		double[] arr2 = wgs2gcj(31.00, 121.24);
		System.out.println(new Date().getTime());
		System.out.println("Google:" + arr2[0] + "," + arr2[1]);
		System.out.println("实际Google:31.173769,121.397993");
		System.out.println(new Date().getTime());
		double[] arr3 = gcj2bd09(arr2[0], arr2[1]);
		System.out.println(new Date().getTime());
		System.out.println("Baidu:" + arr3[0] + "," + arr3[1]);
		System.out.println("实际Baidu:31.179554645498,121.40453538124");
		System.out.println("Google相差距离:"
				+ distance(arr2[0], arr2[1], 31.173769, 121.397993) + "米");
		System.out.println("Baidu相差距离:"
				+ distance(arr3[0], arr3[1], 31.179554645498, 121.40453538124)
				+ "米");
		double[] arr4 = gcj2wgs_exact(arr2[0], arr2[1]);
		System.out.println("逆算:" + arr4[0] + "," + arr4[1]
				+ "需要和第一行相似（目前是小数点后9位相等）");
		System.out.println("距离:"
				+ distance(31.2339, 121.47432, 31.25407, 121.46771) + "米");

		System.out.println("距离:"
				+ distance(31.494024, 104.72702, 31.49402, 104.72693) + "米");
		// "x":13521978.546658712,"y":3662655.183304521
		// "xmin" : 75.23577374943316,
		// "ymin" : 15.643296458511712,
		// "xmax" : 131.48577374943324,
		// "ymax" : 55.76536677101177
		// 经度：121.47
		// 纬度：31.23

		double[] arr21 = wgs2gcj(12.590178885765, 43.88955327932);
		double[] arr31 = gcj2bd09(arr21[0], arr21[1]);

		double[] mercator2wgs = wgs2mercator(arr31[0], arr31[1]);
		double[] mercator2wgs2 = wgs2mercator(12.590178885765, 43.88955327932);

		System.out.println(mercator2wgs[0]);
		System.out.println(mercator2wgs[1]);
		System.out.println(mercator2wgs2[0]);
		System.out.println(mercator2wgs2[1]);
	}
}
