package com.glodon.demo.domain;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zhangzy-t
 */
public class CommodityFile {

    private Integer id;

    private String code;

    private String name;

    private BigDecimal marketPrice;

    private BigDecimal secPrice;

    private String startTime;

    private String endTime;

    private Integer stock;

    private String description;

    private Integer status;

    private MultipartFile image;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getSecPrice() {
        return secPrice;
    }

    public void setSecPrice(BigDecimal secPrice) {
        this.secPrice = secPrice;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public CommodityFile(Integer id, String code, String name, BigDecimal marketPrice, BigDecimal secPrice, String startTime, String endTime, Integer stock, String description, Integer status, MultipartFile image) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.marketPrice = marketPrice;
        this.secPrice = secPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.stock = stock;
        this.description = description;
        this.status = status;
        this.image = image;
    }
}
