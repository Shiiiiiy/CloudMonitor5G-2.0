package com.datang.bean.railway;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class TrainInfoNew implements Serializable{
    
    String startStationName;
    
    String endStationName;
    
    String trainCode;
    
    String trainLength;
    
    ArrayList<Node> items;

    public ArrayList<Node> getItems() {
        return items;
    }

    public void setItems(ArrayList<Node> items) {
        this.items = items;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public void setStartStationName(String startStationName) {
        this.startStationName = startStationName;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public void setEndStationName(String endStationName) {
        this.endStationName = endStationName;
    }

    public String getTrainCode() {
        return trainCode;
    }

    public void setTrainCode(String trainCode) {
        this.trainCode = trainCode;
    }

    public String getTrainLength() {
        return trainLength;
    }

    public void setTrainLength(String trainLength) {
        this.trainLength = trainLength;
    }

}
