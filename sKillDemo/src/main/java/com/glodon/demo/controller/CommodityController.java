package com.glodon.demo.controller;
import com.glodon.demo.domain.Commodity;
import com.glodon.demo.domain.CommodityFile;
import com.glodon.demo.page.PageRequest;
import com.glodon.demo.service.ICommodityService;
import com.glodon.demo.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangzy-t
 */
@CrossOrigin(origins="*",maxAge=3600)
@RestController
@RequestMapping(value = "/commodity")
public class CommodityController {

    @Autowired
    private ICommodityService commodityService;

    private List<Commodity> list = new ArrayList<Commodity>();

    private static final Integer ITEM_PER_PAGE=20;

    /**
     * 访问商品列表，返回全部商品记录
     * @return
     */
    @RequestMapping(value = "/getCommodity")
    public Object getCommodityList(@RequestParam("index") Integer index) throws IOException {
        PageRequest pr = new PageRequest(index,ITEM_PER_PAGE);
        return commodityService.findPage(pr);
    }

    /**
     * 新增一条商品记录，传入商品记录，返回新增后的全部商品记录
     * @param
     * @return
     */
    @RequestMapping(value = "/addCommodity")
    public Object addCommodity(CommodityFile commodityFile){
        try {
            Utils.createImage(commodityFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Commodity commodity = Utils.convert(commodityFile);
        String res = commodityService.addCommodity(commodity);
        //根据Service层的返回值，判断编码是否重复
        if("Success".equals(res)){
            list = commodityService.findAll();
            return list;
        }else {
            return "Error : Duplicate Commodity Code";
        }

    }

    /**
     * 访问某一商品的修改界面，传入该商品的id，返回该商品记录
     * @param id
     * @return
     */
    @RequestMapping(value = "/getUpdatePage")
    public Commodity getUpdatePage(@RequestParam("id") Integer id){
        return commodityService.findById(id);
    }

    /**
     * 访问某一商品的详情界面，传入该商品的id，返回该商品记录
     * @param
     * @return
     */
    @RequestMapping(value = "/getDetailPage")
    public Commodity getDetail(@RequestParam("id") Integer id){
        return commodityService.findById(id);
    }

    /**
     * 修改一条商品记录，传入修改后的商品信息，返回修改后的全部商品记录
     * @param
     * @return
     */
    @RequestMapping(value = "/updateCommodity")
    public Object updateCommodity(CommodityFile commodityFile){
        String res = commodityService.updateCommodity(commodityFile);
        if("Success".equals(res)){
            return commodityService.findById(commodityFile.getId());
        }else{
            Commodity commodity =null;
            return commodity;
        }
    }
    @RequestMapping(value = "/test")
    public Object test(){
            Commodity commodity =null;
            return commodity;

    }

    /**
     * 根据三个条件，组合查询符合要求的商品记录
     * @param code
     * @param name
     * @param status
     * @return
     */
    @RequestMapping(value = "/query")
    public List<Commodity> queryCommodity(@RequestParam(value = "code", required = false, defaultValue = "")String code,
                                          @RequestParam(value = "name", required = false, defaultValue = "") String name,
                                          @RequestParam(value = "status", required = false, defaultValue = "") Integer status){
        return commodityService.queryCommodity(code, name, status);
    }


}
