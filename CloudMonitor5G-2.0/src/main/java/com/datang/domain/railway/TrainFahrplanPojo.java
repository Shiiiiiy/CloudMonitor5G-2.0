package com.datang.domain.railway;

import com.alibaba.fastjson.JSONObject;
import com.datang.bean.railway.TrainInfo;
import lombok.Data;

import javax.persistence.*;

/**
 * @ClassNmae lc
 * @Description
 * @Author lucheng
 * @Date 2021/11/15 14:21
 * @Version 1.0
 **/
@Entity
@Data
@Table(name = "IADS_TRAIN_FAHRPLAN")
public class TrainFahrplanPojo {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "ID")
    private Long id; //编码

    @Column(name = "TRAIN_CODE")
    private String trainCode; //车次',

    @Column(name = "TRAIN_INFO_NEW",length=12000)
    private String trainInfoNew; //车次时刻信息(树状节点)',

    @Column(name = "TRAIN_INFO")
    private String trainInfos; //车次时刻信息(一级节点展示)',

    @Column(name = "TRAIN_NO")
    private String trainNo; //车次编码',

    @Column(name = "UPDATE_TIME")
    private String updateTime; //车次时刻更新时间',

    @Column(name = "CREATE_TIME")
    private String createTime; //创建时间',

//    private Integer isFrozen; //是否冻结',
//    private String frozenReason; //冻结原因',
//    private Integer lengthType; //车厢长度，1 - 16车厢,2 - 24车厢,3 - 8车厢',
//    private Integer isDirect; //是否直连，0 - 否，1 - 是',
//    private Integer splitType; //分割类型，0 - 无断隔，1 - 有断隔',
//    private Integer trainCompanyId; //归属公司ID',
//    private String trainSmall; //小号火车,由大号接管.',

    @Transient
    private TrainInfo trainInfoObject;

    public TrainInfo getTrainInfoObject(){
        if (trainInfoObject!=null){
            return trainInfoObject;
        }
        this.trainInfoObject = JSONObject.parseObject(trainInfoNew,TrainInfo.class);
        return trainInfoObject;
    }
}
