package com.datang.web.beans.testLogItem;

import com.datang.service.influx.bean.AbEventConfig;
import lombok.Data;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.*;

@Data
/**
 * 问题索引帮助类
 * */
public class QuestionEventTitleBean {
    private String id;
    private String text;
    private Map<String,String> attributes;
    private Set<QuestionEventTitleBean> children;

    private static QuestionEventTitleBean create(String text) {
        return create(text,"Province");
    }

    private static QuestionEventTitleBean create(String text,String type){
        QuestionEventTitleBean bean = new QuestionEventTitleBean();
        bean.setId(text);
        bean.setText(text);
        bean.setAttributes(new HashMap<>());
        bean.getAttributes().put("refId",text);
        bean.getAttributes().put("type",type);
        bean.setChildren(new HashSet<>());
        return bean;
    }


    private static List<QuestionEventTitleBean> fromMap(List<AbEventConfig> list){
        Map<String,QuestionEventTitleBean> beanMap = new HashMap<>();
        for(AbEventConfig abEventConfig:list){
            QuestionEventTitleBean bean = null;
            if(beanMap.containsKey(abEventConfig.getType())){
                bean = beanMap.get(abEventConfig.getType());
            }else{
                bean = create(abEventConfig.getType());
                beanMap.put(abEventConfig.getType(),bean);
            }
            QuestionEventTitleBean.addChild(bean,abEventConfig.getEvtCnName());
        }

        List<QuestionEventTitleBean> questionEventTitleBeans = new ArrayList<>();
        for(Map.Entry<String,QuestionEventTitleBean> entry:beanMap.entrySet()){
            questionEventTitleBeans.add(entry.getValue());
        }

        return questionEventTitleBeans;
    }


    public static String jsonFromMap(List<AbEventConfig> list){
        List<QuestionEventTitleBean> questionEventTitleBeans = fromMap(list);
        String s = JSONArray.fromObject(questionEventTitleBeans).toString();
       return "[{\"id\":\"-1\",\"text\":\"区域\",\"children\":" + s + "}]";
    }


    private static QuestionEventTitleBean addChild(QuestionEventTitleBean bean,String text){
        QuestionEventTitleBean child = create(text,"City");
        bean.getChildren().add(child);
        return bean;
    }


}
