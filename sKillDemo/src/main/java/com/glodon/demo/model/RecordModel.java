package com.glodon.demo.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author huangs-e
 * @Date 2020/8/10 8:56
 * @Version 1.0
 */
@Data
public class RecordModel {
    Date date;
    String CommodityName;
    BigDecimal price;
    Integer status;
}
