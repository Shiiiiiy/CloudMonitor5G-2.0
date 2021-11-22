package com.datang.bean.railway;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * @author shiyan
 * @date 2021/9/10 16:34
 */
@Data
public class TrainInfo {

    private String startStationName;
    private String trainCode;
    private Integer trainLength;
    private String endStationName;

    private List<StationItem>  items;

}

