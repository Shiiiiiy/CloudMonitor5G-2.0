package com.datang.constant;

public class RailWayHttpConstants {

    /**
     * 通过分析12306的网站代码，拿到全国车次的URL
     * https://kyfw.12306.cn/otn/resources/js/query/train_list.js?scriptVersion=3.0
     * 废弃
     */
    public static final String TRAIN_12306_LIST_JS = "https://kyfw.12306.cn/otn/resources/js/query/train_list.js?scriptVersion=3.0";

    /**
     * 通过分析12306的网站代码，拿到全国车站信息的URL
     * https://kyfw.12306.cn/otn/resources/js/framework/station_name.js?station_version=1.9151
     */
    public static final String TRAIN_12306_STATION_JS = "https://kyfw.12306.cn/otn/resources/js/framework/station_name.js";

    /**
     * 根据车站获取所有在该站办客的车次
     * https://kyfw.12306.cn/otn/czxx/query?train_start_date=2020-08-17&train_station_code=HZH
     */
    public static final String TRAIN_12306_QUERY_LIST = "https://kyfw.12306.cn/otn/czxx/query";

    /**
     * 12306根据车次编码获取车次时刻表
     * https://kyfw.12306.cn/otn/czxx/queryByTrainNo?train_no=5l000C373800&from_station_telecode=AOH&to_station_telecode=NUH&depart_date=2021-09-04
     */
    public static final String TRAIN_12306_QUERY_TRAIN_FAHRPLAN_BY_TRAINNO = "https://kyfw.12306.cn/otn/czxx/queryByTrainNo";

    /**
     * 12306根据车次获取车次编码<2020版本后时刻表查询>
     * https://search.12306.cn/search/v1/train/search?keyword=C3739&date=20210904
     */
    public static final String TRAIN_12306_QUERY_TRAINNO = "https://search.12306.cn/search/v1/train/search";

    /**
     * 12306根据车次获取车次时刻表<2020版本后时刻表查询>
     * https://kyfw.12306.cn/otn/queryTrainInfo/query?leftTicketDTO.train_no=54000D541400&leftTicketDTO.train_date=2020-08-22&rand_code=
     */
    public static final String TRAIN_12306_QUERY_TRAIN_FAHRPLAN = "https://kyfw.12306.cn/otn/queryTrainInfo/query";

    /**
     * 12306实时查询当天剩余列车
     * https://kyfw.12306.cn/otn/leftTicket/query?leftTicketDTO.train_date=2021-09-13&leftTicketDTO.from_station=SNH&leftTicketDTO.to_station=CQW&purpose_codes=ADULT
     **/
    public static final String TRAIN_12306_QUERY_REALTIME = "https://kyfw.12306.cn/otn/leftTicket/";

    /**
     * 携程根据车次编码获取车次时刻表
     */
    public static final String TRAIN_CTRIP_QUERY_TSEATDETAIL = "https://trains.ctrip.com/TrainSchedule/";

    /**
     * 去哪儿根据车次编码获取车次时刻表
     */
    public static final String TRAIN_QUNAR_QUERY_TSEATDETAIL = "https://train.qunar.com/dict/open/seatDetail.do";

    public static final String TRAIN_STATION_JS_PREFIX_CHARACTER= "var station_names =";

    public static final String TRAIN_STATION_JS_REGEX_TRIM = "'";

    public static final String TRAIN_STATION_JS_REGEX_SPLIT = "@";

}
