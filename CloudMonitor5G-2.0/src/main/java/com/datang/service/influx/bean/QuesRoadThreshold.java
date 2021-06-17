package com.datang.service.influx.bean;

import javax.persistence.*;

/**
 * 问题路段门限类
 * @author xp
 */
@Entity
@Table(name = "t_quesroad_threshold")
public class QuesRoadThreshold {
    private Integer id;
    private String type;
    private String cnname;
    private String name;
    private Double value;
    private String unit;
    @Id
    @Column(name = "id")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Column(name = "threshold_cnname")
    public String getCnname() {
        return cnname;
    }

    public void setCnname(String cnname) {
        this.cnname = cnname;
    }
    @Column(name = "threshold_name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "value")
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
    @Column(name = "unit")
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
