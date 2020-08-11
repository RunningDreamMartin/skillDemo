package com.glodon.demo.controller;

import com.glodon.demo.service.SKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
@CrossOrigin(origins="*",maxAge=3600)
@RestController
@RequestMapping("/skill")
public class SKillController {
    @Resource
    private SKillService sKillService;

    @RequestMapping("/skillCommodity")
    public String secKill(@RequestParam("userId") Integer userId,@RequestParam("commodityId") Integer commodityId){
        if (userId != null&&commodityId!=null) {
            boolean status= sKillService.secKill(userId,commodityId);
            if (status) {
                return "秒杀成功";
            }
        }
        return "秒杀失败";
    }
}
