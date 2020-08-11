package com.glodon.demo.controller;

import com.glodon.demo.domain.Record;
import com.glodon.demo.model.RecordModel;
import com.glodon.demo.service.RecordService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author huangs-e
 * @Date 2020/8/8 14:46
 * @Version 1.0
 */
@CrossOrigin(origins="*",maxAge=3600)
@RestController
@RequestMapping(value = "/record")
public class RecordController {
    @Resource
    public RecordService recordService;
    @RequestMapping(value="getRecord")
    public List<RecordModel> getRecord(@RequestParam("id") Integer id){
        if (id!=null){
          List <RecordModel> records= recordService.getRecord(id);
          if (records!=null){
              return records;
          }
        }
        return null;
    }
}
