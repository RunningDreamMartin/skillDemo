package com.glodon.demo.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * record
 * @author 
 */
@Data
public class Record implements Serializable {
    private Integer id;

    private Integer userId;

    private Integer commodityId;

    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    private static final long serialVersionUID = 1L;

    public Record(Integer userId, Integer commodityId, Integer status, Date time) {
        this.userId = userId;
        this.commodityId = commodityId;
        this.status = status;
        this.time = time;
    }

    public Record(Integer id, Integer userId, Integer commodityId, Integer status, Date time) {
        this.id = id;
        this.userId = userId;
        this.commodityId = commodityId;
        this.status = status;
        this.time = time;
    }

    public Record() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Integer commodityId) {
        this.commodityId = commodityId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Record(Integer id, Integer userId) {
        this.id = id;
        this.userId = userId;
    }
}