/**
 * 
 */
package com.datang.server.geocode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datang.common.util.BeanLoader;
import com.datang.common.util.StringUtils;
import com.datang.domain.VoLTEDissertation.qualityBadRoad.VolteQualityBadRoad;
import com.datang.service.VoLTEDissertation.qualityBadRoad.IVoiceQualityBadRoadService;
import com.datang.service.VoLTEDissertation.qualityBadRoad.impl.VoiceQualityBadRoadServiceImpl;

/**
 * 质差路段地理编码服务
 * 
 * @author yinzhipeng
 * @date:2016年2月22日 上午9:29:13
 * @version
 */
@SuppressWarnings("all")
public class QBRGeocodeTask {

	private static final int Max_Num = 10;
	private static final int TimeOut = 10 * 60 * 1000;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(QBRGeocodeTask.class);
	private static final ExecutorService QBRGeocodeTaskExecutor = Executors
			.newSingleThreadExecutor(new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r, "QBRGeocodeTask");
				}
			});
	private List<Future<?>> futures = new ArrayList<Future<?>>(Max_Num);

	/**
	 * 开启Geocode服务
	 */
	public void start() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(180000);// 等待3分钟WEB服务启动
				} catch (InterruptedException e) {
					LOGGER.error("[QBRGeocodeTask]" + e.getMessage(), e);
				}
				for (;;) {
					try {
						Thread.sleep(300000);// 执行周期
					} catch (InterruptedException e) {
						LOGGER.error("[QBRGeocodeTask]" + e.getMessage(), e);
					}
					Iterator<Future<?>> sListIterator = futures.iterator();
					while (sListIterator.hasNext()) {
						Future<?> future = sListIterator.next();
						try {
							future.get(TimeOut, TimeUnit.MILLISECONDS);
							if (future.isDone()) {
								sListIterator.remove();
								continue;
							}
						} catch (Exception e) {
							if (future.isDone() || future.cancel(true)) {
								sListIterator.remove();
							}
							LOGGER.warn(
									"[QBRGeocodeTask] Geocode Timeout "
											+ e.getMessage(), e);
							continue;
						}
					}
					if (!futures.add(submit())) {
						LOGGER.info("[QBRGeocodeTask] List is Small!");
					}
				}
			}
		}, "QBRGeocodeTaskHeart");
		thread.setDaemon(true);
		thread.start();

	}

	/**
	 * 质差路段地理编码执行主体
	 * 
	 * @return
	 */
	private Future<Integer> submit() {
		return QBRGeocodeTaskExecutor.submit(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				try {
					return geocode();
				} catch (Throwable th) {
					LOGGER.error("[QBRGeocodeTask]" + th.getMessage(), th);
				}
				return 0;
			}
		});
	}

	/**
	 * 质差路段地理编码服务方法主体
	 * 
	 * @return
	 */
	private Integer geocode() {
		int i = 0;

		/**
		 * 获取所有路段名称为null的质差路段
		 */
		IVoiceQualityBadRoadService voiceQualityBadRoadService = (VoiceQualityBadRoadServiceImpl) BeanLoader
				.getBean("voiceQualityBadRoadServiceImpl");
		List<VolteQualityBadRoad> nullRoadNameQBR = voiceQualityBadRoadService
				.getNullRoadNameQBR();

		if (null != nullRoadNameQBR && 0 != nullRoadNameQBR.size()) {
			i = nullRoadNameQBR.size();
			QBRGeocodeThread geocodeThread = new QBRGeocodeThread(
					nullRoadNameQBR);
			new Thread(geocodeThread).start();

		}
		return i;
	}

	/**
	 * 批量地理编码线程
	 * 
	 * @author yinzhipeng
	 * @date:2016年2月22日 上午11:15:37
	 * @version
	 */
	private class QBRGeocodeThread implements Runnable {

		private List<VolteQualityBadRoad> nullRoadNameQBR;

		/**
		 * @param nullRoadNameQBR
		 */
		public QBRGeocodeThread(List<VolteQualityBadRoad> nullRoadNameQBR) {
			super();
			this.nullRoadNameQBR = nullRoadNameQBR;
		}

		/**
		 * 解析百度返回的地址信息
		 * 
		 * @param responseBodyAsString
		 * @return 路段名称
		 */
		private String getRoadName(String responseBodyAsString) {
			if (!StringUtils.hasText(responseBodyAsString)) {
				return null;
			}

			JSONObject fromObject = JSONObject.fromObject(responseBodyAsString);
			Object status = fromObject.get("status");
			if (null != status) {
				int statusValue = (Integer) status;
				if (0 == statusValue) {
					JSONObject result = fromObject.getJSONObject("result");
					if (null != result) {
						JSONObject addressComponent = result
								.getJSONObject("addressComponent");
						if (null != addressComponent) {
							String city = addressComponent.getString("city");
							String district = addressComponent
									.getString("district");
							String street = addressComponent
									.getString("street");
							return city + district + street;
						}
					}
				}
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			if (null != nullRoadNameQBR && 0 != nullRoadNameQBR.size()) {
				IVoiceQualityBadRoadService voiceQualityBadRoadService = (VoiceQualityBadRoadServiceImpl) BeanLoader
						.getBean("voiceQualityBadRoadServiceImpl");
				for (VolteQualityBadRoad qbr : nullRoadNameQBR) {
					// 三组经纬度信息
					Float beginLatitude = qbr.getBeginLatitude();
					Float beginLongitude = qbr.getBeginLongitude();
					Float courseLatitude = qbr.getCourseLatitude();
					Float courseLongitude = qbr.getCourseLongitude();
					Float endLatitude = qbr.getEndLatitude();
					Float endLongitude = qbr.getEndLongitude();

					// 三组路段名称
					String beginRoadName = null;
					String courseRoadName = null;
					String endRoadName = null;
					String roadName = null;

					// 创建http客户端
					HttpClient httpClient = new HttpClient();
					httpClient.getHttpConnectionManager().getParams()
							.setConnectionTimeout(10000);
					httpClient.getHttpConnectionManager().getParams()
							.setSoTimeout(10000);

					/**
					 * 地理编码路段开始经纬度
					 */
					if (null != beginLatitude && null != beginLongitude
							&& 0 != beginLatitude && 0 != beginLongitude) {
						// 27.066043,110.582647
						PostMethod postMethod = new PostMethod(
								"http://api.map.baidu.com/geocoder/v2/?ak=liKpDfLP41rNnZmM1D33WljN&location="
										+ beginLatitude + "," + beginLongitude
										+ "&coordtype=wgs84ll&output=json");
						try {
							httpClient.executeMethod(postMethod);
							String responseBodyAsString = postMethod
									.getResponseBodyAsString();
							beginRoadName = getRoadName(responseBodyAsString);
						} catch (IOException e) {
							LOGGER.error(
									"[QBRGeocodeTask] Geocode BeginLatLon Error "
											+ e.getMessage(), e);
						}
					}

					/**
					 * 地理编码路段过程经纬度
					 */
					if (null != courseLatitude && null != courseLongitude
							&& 0 != courseLatitude && 0 != courseLongitude) {
						// 27.066043,110.582647
						PostMethod postMethod = new PostMethod(
								"http://api.map.baidu.com/geocoder/v2/?ak=liKpDfLP41rNnZmM1D33WljN&location="
										+ courseLatitude + ","
										+ courseLongitude
										+ "&coordtype=wgs84ll&output=json");
						try {
							httpClient.executeMethod(postMethod);
							String responseBodyAsString = postMethod
									.getResponseBodyAsString();
							courseRoadName = getRoadName(responseBodyAsString);
						} catch (IOException e) {
							LOGGER.error(
									"[QBRGeocodeTask] Geocode CourseLatLon Error "
											+ e.getMessage(), e);
						}
					}
					/**
					 * 地理编码路段结束经纬度
					 */
					if (null != endLatitude && null != endLongitude
							&& 0 != endLatitude && 0 != endLongitude) {
						// 27.066043,110.582647
						PostMethod postMethod = new PostMethod(
								"http://api.map.baidu.com/geocoder/v2/?ak=liKpDfLP41rNnZmM1D33WljN&location="
										+ endLatitude + "," + endLongitude
										+ "&coordtype=wgs84ll&output=json");
						try {
							httpClient.executeMethod(postMethod);
							String responseBodyAsString = postMethod
									.getResponseBodyAsString();
							endRoadName = getRoadName(responseBodyAsString);
						} catch (IOException e) {
							LOGGER.error(
									"[QBRGeocodeTask] Geocode EndLatLon Error "
											+ e.getMessage(), e);
						}
					}

					if (StringUtils.hasText(beginRoadName)) {
						roadName = beginRoadName;
					} else {
						if (StringUtils.hasText(courseRoadName)) {
							roadName = courseRoadName;
						} else {
							if (StringUtils.hasText(endRoadName)) {
								roadName = endRoadName;
							} else {
								roadName = "--";
							}

						}
					}
					voiceQualityBadRoadService.addQBRRoadName(roadName,
							qbr.getId());
				}
			}
		}

		/**
		 * @return the nullRoadNameQBRnullRoadNameQBR
		 */
		public List<VolteQualityBadRoad> getNullRoadNameQBR() {
			return nullRoadNameQBR;
		}

		/**
		 * @param nullRoadNameQBR
		 *            the nullRoadNameQBR to set
		 */
		public void setNullRoadNameQBR(List<VolteQualityBadRoad> nullRoadNameQBR) {
			this.nullRoadNameQBR = nullRoadNameQBR;
		}

	}

	public void stop() {
		QBRGeocodeTaskExecutor.shutdownNow();
	}

	public static QBRGeocodeTask getMosGradeTask() {
		return QBRGeocodeTaskHolder.Holder;
	}

	static class QBRGeocodeTaskHolder {
		public static QBRGeocodeTask Holder = new QBRGeocodeTask();
	}

	private QBRGeocodeTask() {
	}

}
