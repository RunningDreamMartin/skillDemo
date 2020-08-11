package com.glodon.demo.service;

import com.glodon.demo.model.RecordModel;

import java.util.List;

/**
 * @Author huangs-e
 * @Date 2020/8/8 14:33
 * @Version 1.0
 */
public interface RecordService {
    List<RecordModel> getRecord(Integer id);
}
