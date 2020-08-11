package com.glodon.demo.controller;

import com.glodon.demo.service.ShelfService;
import com.glodon.demo.service.impl.ShelfServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins="*",maxAge=3600)
@RequestMapping(value = "/shelf")

public class ShelfController {
    @Resource
    ShelfService shelfService;

    @RequestMapping("upShelf")
    public boolean upShelf(@RequestBody ArrayList<Integer> ids){
        if (ids!=null){
            boolean status=shelfService.batchUpShelf(ids);
            if (status){
                return true;
            }
        }
        return false;
    }
    @RequestMapping("downShelf")
    public boolean downShelf(@RequestBody ArrayList<Integer> ids){
        if (ids!=null){
            boolean status=shelfService.batchDownShelf(ids);
            if (status){
                return true;
            }
        }
        return false;
    }



}
