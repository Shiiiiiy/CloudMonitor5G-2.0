package com.datang.web.beans.report;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shiyan
 * @date 2021/9/6 13:28
 */
@Data
public class KnowFeelingRequest implements Serializable {

    private Date beginDate;
    private Date endDate;
    private String logIds;
    private String cityIds;



}
