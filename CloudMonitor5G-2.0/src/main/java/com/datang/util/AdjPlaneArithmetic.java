package com.datang.util;

/**
 * 
 *系统间邻区规划算法计算
 *
 * 
 *		1  邻区距离
 *		2 NodeB的方位角
 *		3 判断是否同向
 *		4判断是否正向对打小区
 *		5 计算两向量的夹角（小区的水平方向和基站连线的夹角）
 *		6 计算等效距离
 */
public class AdjPlaneArithmetic {
	
	static final  float EARTH_RADIUS = 6378.137f;	//地球半径
	
	static final  double PI = 3.14159265;    //π 取值
	
	static final double RADIANPERDEGREE = 0.0174532925;
	
	static final Double INVALIDVALUE = -255D;
	/**
	 * 邻区距离 单位（米）
	 * @param rSLon 源小区经度
	 * @param rSLat	源小区纬度
	 * @param rNLon 目标小区经度
	 * @param rNLat	目标小区纬度
	 * @return
	 */
	public static Double getDistance(Double rSLon, Double rSLat, Double rNLon, Double rNLat)
	{
		Double s=0D;
		try {
			Double a = rSLat*PI/180 - rNLat*PI/180;
			Double b = rSLon*PI/180 - rNLon*PI/180;
			s = (Double) (2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + Math.cos(rSLat*PI/180)*Math.cos(rNLat*PI/180)*Math.pow(Math.sin(b/2),2))));
			s = s * EARTH_RADIUS;
			s = (s * 10000000) / 10000;
		} catch (Exception e) {
			return 0D;
		}
		return s;
	}

	/**
	 * 获取距中心点的最小经度，最大经度，最小纬度，最大纬度
	 * @param lat 中心纬度
	 * @param lon 中心经度
	 * @param r  半径（米）
	 * @return
	 */
	public static double[] getRange(Double lat,Double lon,double r){
		double[] re=new double[4];
		Double degree = (24901 * 1609) / 360.0; // 获取每度
		Double mpdLng = degree * Math.cos(lat * (3.141592653 / 180));
		Double dpmLng = 1 / mpdLng;
		Double radiusLng = dpmLng * r;
		//获取最小经度
		Double minLon = lon - radiusLng;
		// 获取最大经度
		Double maxLon = lon + radiusLng;

		Double dpmLat = 1 / degree;
		Double radiusLat = dpmLat * r;
		// 获取最小纬度
		Double minLat = lat - radiusLat;
		Double maxLat = lat + radiusLat;
		re[0]=minLon;
		re[1]=maxLon;
		re[2]=minLat;
		re[3]=maxLat;
		return re;
	}

	
	/**
	 * 判断是否是同方向
	 * @param sLon
	 * @param sLat
	 * @param nLon
	 * @param nLat
	 * @param cellAzimuth
	 * @param includeAngle
	 * @return
	 */
	public static Boolean equalDirection(Double sLon,Double sLat,Double nLon,Double nLat,Double cellAzimuth,Double includeAngle){
		Boolean equalDirection=false;
		try {
			//首先获得node方位角
			Double nodeBsAngle=calNodeBsAngle(sLon, sLat, nLon, nLat);
			equalDirection=twoAngleDis(nodeBsAngle,cellAzimuth)<includeAngle ? true :false;
		} catch (Exception e) {
			return equalDirection;
		}
		return equalDirection;
	}

	/**
	 * 判断是否是同方向
	 * @param nodeBsAngle
	 * @param cellAzimuth
	 * @param includeAngle
	 * @return
	 */
	public static Boolean equalDirectionEx(Double nodeBsAngle,Double cellAzimuth,Double includeAngle){
		Boolean equalDirection=false;
		try {
			//首先获得node方位角
			equalDirection=twoAngleDis(nodeBsAngle,cellAzimuth)<includeAngle ? true :false;
		} catch (Exception e) {
			return equalDirection;
		}
		return equalDirection;
	}	
	
	public static Boolean backDirection(Double nodeBsAngle,Double cellAzimuth,Double cellAzimuth_n, Double includeAngle){
		if(nodeBsAngle < 0 || cellAzimuth < 0 || cellAzimuth_n < 0 )	//有全向站，则夹角为0度
		{
			return true;
		}
		double dbAngle1 = twoAngleDisEx(nodeBsAngle, cellAzimuth);
		double dbAngle2 = twoAngleDisEx(nodeBsAngle, cellAzimuth_n);
		return Math.abs(dbAngle1-dbAngle2)<includeAngle ? true : false;
	}
	
	/**
	 * NodeB的方位角
	 * @param sLon
	 * @param sLat
	 * @param nLon
	 * @param nLat
	 * @return
	 */
	public static Double calNodeBsAngle(Double sLon,Double sLat,Double nLon,Double nLat)
	{
		Double angle=0D;
		try {
			//是否转成米的形式，根据准确性
			int dX = ( (nLon*PI/180- sLon*PI/180) > 0 )? 1: -1;	//表示方向性
			int dY = ( (nLat*PI/180- sLat*PI/180) > 0 )? 1: -1;
			Double dbXOff = getDistance(sLon, nLat, nLon, nLat)*dX;
			Double dbYOff = getDistance(sLon, sLat, sLon, nLat)*dY;
			
			angle = Math.atan2(dbXOff, dbYOff)* 180 / PI;
			angle = (angle > 360) ? (angle - 360) : angle;
			angle = (angle < 0) ? (angle + 360) : angle;
		} catch (Exception e) {
			return angle;
		}
		// 两小区连线与正北方向的夹角，顺时针0～360度,再转换成弧度
		return angle;
	}

	/**
	 * 判断是否同向
	 * @param fNodeBAngle NodeB的方位角
	 * @param cellAzimuth 小区1方位角
	 * @return
	 */
	private static Double  twoAngleDis(Double  fNodeBAngle, Double cellAzimuth)	//两个角度差
	{
		Double angle=0D;
		try {
			if(fNodeBAngle < 0 || cellAzimuth < 0)	//有全向站，则夹角为0度
			{
				return 0D;
			}
			angle = Math.abs(fNodeBAngle - cellAzimuth);
			angle=((angle > 180) ? (360 - angle) : angle);
		} catch (Exception e) {
			return angle;
		}
		return angle;
	}

	private static Double twoAngleDisEx(Double  fNodeBAngle, Double cellAzimuth){	//两个角度差 180~-180
		Double angle=0D;
		try {
			if(fNodeBAngle < 0 || cellAzimuth < 0)	//有全向站，则夹角为0度
			{
				return 0D;
			}
			angle = cellAzimuth - fNodeBAngle;
			if ( angle > 180 ) angle = angle - 360;
			else if ( angle < -180 ) angle = angle + 360;
		} catch (Exception e) {
			return angle;
		}
		return angle;		
	}
	
	/**
	 * 判断是否正向对打小区
	 * @param nSHoriz 小区1方位角
	 * @param nNHoriz 小区2方位角
	 * @param fNodeBAngle NodeB的方位角
	 *  @param relativerAngle 两小区天线相对方向角度在β之内
	 * @return
	 */
	public static Boolean  coCellAngle(Double nSHoriz, Double nNHoriz, Double fNodeBAngle,Double relativerAngle)	//是否基站对打
	{
		Boolean coCellAngle=false;
		try {
			Double dbAngle = fNodeBAngle;

			Double angle1 = Math.abs(nSHoriz - dbAngle);
			Double angle2 = Math.abs(nNHoriz + 180 - dbAngle);
			//如果有全向站，则夹角Angle为0度
			angle1 = nSHoriz < 0 ? 0 : angle1;
			angle2 = nNHoriz < 0 ? 0 : angle2;

			angle1 = (angle1 > 360) ? (angle1-360) : angle1;
			angle2 = (angle2 > 360) ? (angle2-360) : angle2;

			angle1 = (angle1 > 180) ? (360 - angle1) : angle1;
			angle2 = (angle2 > 180) ? (360 - angle2) : angle2;

			Double outAngle = angle1 + angle2;
			
			coCellAngle = outAngle<relativerAngle?true:false;
		} catch (Exception e) {
			return coCellAngle;
		}
		
		return coCellAngle;
	}

		//水平方向和基站连线的夹角
	    
		
		//double dbGeoXA = INVALIDVALUE;    
		//double dbGeoYA = INVALIDVALUE;
		//double dbGeoXB = INVALIDVALUE;  
		//double dbGeoYB = INVALIDVALUE;
		//CalGeoXY(cellA.dbLongitude, cellA.dbLatitude, dbGeoXA, dbGeoYA, cellA.dbLongitude < 0);
		//CalGeoXY(cellB.dbLongitude, cellB.dbLatitude, dbGeoXB, dbGeoYB, cellB.dbLongitude < 0);
	

	/**
	 * 将A、B小区的经纬度转换为笛卡尔坐标
	 * //将经纬度转换成笛卡尔坐标
	 * @param dblLong
	 * @param dblLat
	 * @param bWest
	 * @return
	 */
	public static Double []  calGeoXY(Double dblLong, Double dblLat, Boolean bWest)
	{
		Double dblX = INVALIDVALUE;  
		Double dblY = INVALIDVALUE;
		try {
			
			boolean negative = false;	// 该变量为TRUE表示纬度为负值，在南半球，FALSE表示在北半球

			// 如果在西半球
			if (dblLong < 0)
			{
				// 将经度转换为正值
				dblLong += 360;
			}
			// 如果在西半球而且经度为0
			if ((bWest == true) && (dblLong == 0.0))
			{
				// 将经度转换为360
				dblLong=360.0;
			}
			// 如果在南半球
			if(dblLat<0)
			{
				// 将纬度转换为正值
				dblLat = -dblLat;
				// 设置标示位
				negative = true;
			}

			int zone; 
			zone = (int) (dblLong / 6 + 1);
			if (zone > 60)
			{
				zone = 60;
			}

			Double bb, ll, xx, yy;
			Double ss, e2, n2, t, t2, N, l2;
			bb = dblLat;	// 保存纬度
			ll = dblLong;	// 保存经度
			// 将纬度转换成角度
			bb = bb * 3.1415927 / 180.0;
			ss = 6367558.497*bb - 16036.4803*Math.sin(2*bb) + 16.8281*Math.sin(4*bb) - 0.02198*Math.sin(6*bb) + 0.000031*Math.sin(8*bb);
			e2=(6378245+6356863.02)*(6378245-6356863.02)/(6356863.02*6356863.02);
			n2 = e2*Math.cos(bb)*Math.cos(bb);
			t = Math.tan(bb);
			t2 = t*t;
			N = 6378245/Math.sqrt(1-e2*Math.sin(bb)*Math.sin(bb));
			ll = (ll-6*zone+3)*3600/206264.806;
			l2 = ll*ll;
			xx = ss+l2*N*Math.sin(bb)*Math.cos(bb)/2.0+(l2/24.0)*l2*N*Math.sin(bb)*Math.cos(bb)*Math.cos(bb)*Math.cos(bb)*
				(5-t2+9*n2+4*n2*n2)+(l2/720.0)*l2*l2*N*Math.sin(bb)*Math.cos(bb)*Math.cos(bb)*Math.cos(bb)*Math.cos(bb)*
				Math.cos(bb)*(61-58*t2+t2*t2);
			yy = ll*N*Math.cos(bb)+(ll/6.0)*ll*ll*N*Math.cos(bb)*Math.cos(bb)*Math.cos(bb)*(1-t2+n2)+
				(ll/120.0)*ll*ll*ll*ll*N*Math.cos(bb)*Math.cos(bb)*Math.cos(bb)*Math.cos(bb)*Math.cos(bb)*
				(5-18*t2+t2*t2+14*n2-58*n2*t2);
			if(negative==false)
			{
				dblY=xx;
			}
			else
			{
				dblY=-xx;
			}
			dblX=yy+500000.0;
		} catch (Exception e) {
			return new Double[]{dblX,dblY};
		}	
		return new Double[]{dblX,dblY};
	}
	
	
	/**计算两向量的夹角
	 * 
	 * @param dbGeoXB
	 * @param dbGeoXA
	 * @param dbGeoYB
	 * @param dbGeoYA
	 * @param dbAzimuthA
	 * @return
	 */
	public static double calculateAngle(Double dbGeoXB, Double dbGeoXA,Double dbGeoYB ,Double dbGeoYA,Double dbAzimuthA){
		Double fAngle=0D;
		try {
			Complex vecAB = new Complex(dbGeoXB - dbGeoXA, dbGeoYB - dbGeoYA);
			
			//将方向角转换为坐标系角度
			 dbAzimuthA = dbAzimuthA > 90? (450 - dbAzimuthA):(90 - dbAzimuthA);
			dbAzimuthA *= RADIANPERDEGREE;
			
			Complex vecA = new Complex(Math.cos(dbAzimuthA), Math.sin(dbAzimuthA));
			
			fAngle = 0D;
			assert(Complex.abs(vecAB) > 0 && Complex.abs(vecA) > 0);
			Complex cResult = vecAB.divide(vecA);
			//复数的实数/复数的绝对值
			Double fCosinAngle = cResult.getX()/Complex.abs(cResult);
			fAngle = Math.acos(fCosinAngle);
		} catch (Exception e) {
			return fAngle;
		}
		return fAngle;
	}

	/**
	 * 功能描述：计算等效距离 单位（米）
	 * 
	 * @param fDisLever 为输入的加权因子
	 * @param nSHoriz	服务小区水平角
	 * @param nNHoriz	其它小区水平角
	 * @param shSType	服务小区扇区类型
	 * @param shNType	其它小区扇区类型（1：全向；2：定向）
	 * @param Distance	计算的实际距离
	 * @param fNodeBAngle	两小区连线与正北方向夹角，单位：度
	 * @return
	 */
	public static double  getEffDistance(Double fDisLever, Double nSHoriz, Double nNHoriz, Integer shSType, Integer shNType, Double Distance, Double fNodeBAngle ){
		Double dbDis = Distance;
		Double dbAngle = fNodeBAngle;	//单位：度
		Double getEffDistance=0D;
		try {
			if((shSType == 1)	&& ( shNType == 1 ) ){	//都是全向
				getEffDistance=(dbDis*(1 - fDisLever*2));
				return getEffDistance;
			}else if(shSType == 1 ){	//主小区全向站
				getEffDistance=(dbDis*(1 + fDisLever*Math.cos((nNHoriz-dbAngle)*PI/180.0) - fDisLever));
				//nSHoriz = (dbAngle * 180 / PI);	//保证cos(nSHoriz*PI/180.0-dbAngle)) = 1；
				return getEffDistance;
			}else if(shNType == 1){	//邻小区全向站
				getEffDistance=(dbDis*(1 - fDisLever - fDisLever*Math.cos((nSHoriz-dbAngle)*PI/180.0)));
				//nNHoriz = 180 + (dbAngle * 180 / PI);	//保证cos(nNHoriz*PI/180.0-dbAngle)) = -1；
				return getEffDistance;
			}else{
				getEffDistance=(dbDis*(1+fDisLever*Math.cos((nNHoriz-dbAngle)*PI/180.0) - fDisLever*Math.cos((nSHoriz-dbAngle)*PI/180.0)));
			}
		} catch (Exception e) {
			return getEffDistance;
		}
		
		return getEffDistance;
	}

	/** 
	 * 功能描述：计算等效距离  单位（米）
	 * 
	 * @param fDisLever	为输入的加权因子
	 * @param nSHoriz	服务小区水平角
	 * @param nNHoriz	目标小区水平角
	 * @param shSType	服务小区扇区类型	
	 * @param shNType	目标小区扇区类型（1：全向；2：定向）
	 * @param rSLon 	源小区经度
	 * @param rSLat		源小区纬度
	 * @param rNLon 	目标小区经度
	 * @param rNLat		目标小区纬度
	 * @return
	 */
	public static double  getEffDistance(Double fDisLever, Double nSHoriz, Double nNHoriz, Integer shSType, Integer shNType,Double rSLon, Double rSLat, Double rNLon, Double rNLat ){
		
		Double dbDis = getDistance(rSLon, rSLat, rNLon,rNLat);		//单位：米
		Double dbAngle = calNodeBsAngle(rSLon, rSLat, rNLon,rNLat);	//单位：度
		Double getEffDistance=0D;
		try {
			if((shSType == 1)	&& ( shNType == 1 ) ){	//都是全向
				getEffDistance=(dbDis*(1 - fDisLever*2));
				return getEffDistance;
			}else if(shSType == 1 ){	//主小区全向站
				getEffDistance=(dbDis*(1 + fDisLever*Math.cos((nNHoriz-dbAngle)*PI/180.0) - fDisLever));
				//nSHoriz = (dbAngle * 180 / PI);	//保证cos(nSHoriz*PI/180.0-dbAngle)) = 1；
				return getEffDistance;
			}else if(shNType == 1){	//邻小区全向站
				getEffDistance=(dbDis*(1 - fDisLever - fDisLever*Math.cos((nSHoriz-dbAngle)*PI/180.0)));
				//nNHoriz = 180 + (dbAngle * 180 / PI);	//保证cos(nNHoriz*PI/180.0-dbAngle)) = -1；
				return getEffDistance;
			}else{
				getEffDistance=(dbDis*(1+fDisLever*Math.cos((nNHoriz-dbAngle)*PI/180.0) - fDisLever*Math.cos((nSHoriz-dbAngle)*PI/180.0)));
			}
		} catch (Exception e) {
			return getEffDistance;
		}
		
		return getEffDistance;
	}
	
	/** 
	 * 功能描述：计算等效距离  单位（米）
	 * 
	 * @param fDisLever	为输入的加权因子
	 * @param nSHoriz	服务小区水平角
	 * @param nNHoriz	目标小区水平角
	 * @param shSType	服务小区扇区类型	
	 * @param shNType	目标小区扇区类型（1：全向；2：定向）
	 * @param Distance	邻区距离	单位（米）
	 * @param rSLon 	源小区经度
	 * @param rSLat		源小区纬度
	 * @param rNLon 	目标小区经度
	 * @param rNLat		目标小区纬度
	 * @return
	 */
	public static Double  getEffDistance(Double fDisLever, Double nSHoriz, Double nNHoriz, Integer shSType, Integer shNType, Double Distance,Double rSLon, Double rSLat, Double rNLon, Double rNLat ){
		
		Double dbDis = Distance;		//单位：米
		Double dbAngle = calNodeBsAngle(rSLon, rSLat, rNLon,rNLat);	//单位：度
		Double getEffDistance=0D;
		try {
			if((shSType == 1)	&& ( shNType == 1 ) ){	//都是全向
				getEffDistance=(dbDis*(1 - fDisLever*2));
				return getEffDistance;
			}else if(shSType == 1 ){	//主小区全向站
				getEffDistance=(dbDis*(1 + fDisLever*Math.cos((nNHoriz-dbAngle)*PI/180.0) - fDisLever));
				//nSHoriz = (dbAngle * 180 / PI);	//保证cos(nSHoriz*PI/180.0-dbAngle)) = 1；
				return getEffDistance;
			}else if(shNType == 1){	//邻小区全向站
				getEffDistance=(dbDis*(1 - fDisLever - fDisLever*Math.cos((nSHoriz-dbAngle)*PI/180.0)));
				//nNHoriz = 180 + (dbAngle * 180 / PI);	//保证cos(nNHoriz*PI/180.0-dbAngle)) = -1；
				return getEffDistance;
			}else{
				getEffDistance=(dbDis*(1+fDisLever*Math.cos((nNHoriz-dbAngle)*PI/180.0) - fDisLever*Math.cos((nSHoriz-dbAngle)*PI/180.0)));
			}
		} catch (Exception e) {
			return getEffDistance;
		}
		
		return getEffDistance;
	}
}
