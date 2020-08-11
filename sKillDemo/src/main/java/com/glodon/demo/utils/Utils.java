package com.glodon.demo.utils;

import com.glodon.demo.domain.Commodity;
import com.glodon.demo.domain.CommodityFile;


import io.undertow.server.handlers.form.FormData;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Encoder;


import java.io.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.util.HashMap;

/**
 * 工具类
 * @author zhangzy-t
 */
public class Utils {

    /**
     * 创建并上传图片到classpath目录下
     * @param
     * @return
     * @throws IOException
     */
    public static boolean createImage(CommodityFile commodityFile) throws IOException {
        MultipartFile photo = commodityFile.getImage();
        ModelAndView mv = new ModelAndView();
        //判断用户是否上传了文件
        if(!photo.isEmpty()){
            //文件上传的地址
            String path = "E:\\temp";
            //用于查看路径是否正确
            System.out.println(path);
            //获取文件的名称
            final String fileName = commodityFile.getCode() + ".jpg";
            //限制文件上传的类型
            String contentType = photo.getContentType();
            if("image/jpeg".equals(contentType) || "image/jpg".equals(contentType) || "image/png".equals(contentType)){
                File file = new File(path,fileName);
                //完成文件的上传
                photo.transferTo(file);
                System.out.println("图片上传成功!");
                return true;
            } else {
                System.out.println("上传失败！");
                return false;
            }
        } else {
            System.out.println("上传失败！");
            return false;
        }
    }

    /**
     * 将前端传过来的对象，转换为与数据库中字段符合的对象
     * @param commodityFile
     * @return
     */
    public static Commodity convert(CommodityFile commodityFile){
        String filePath = "E:\\temp\\" + commodityFile.getCode() + ".jpg";
        String start = commodityFile.getStartTime();
        String end = commodityFile.getEndTime();
        Date dateStart = null;
        Date dateEnd = null;
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(start!=null){
            try {
                //使用SimpleDateFormat的parse()方法生成Date
                dateStart = sf.parse(start);

            } catch (ParseException e) {

            }
        }
        if(end!=null){
            try {
                //使用SimpleDateFormat的parse()方法生成Date
                dateEnd = sf.parse(end);
            } catch (ParseException e) {

            }
        }
        Commodity commodity = new Commodity(
                commodityFile.getId(),
                commodityFile.getCode(),
                commodityFile.getName(),
                commodityFile.getMarketPrice(),
                commodityFile.getMarketPrice(),
                start!=null?dateStart:null,
                end!=null?dateEnd:null,
                commodityFile.getStock(),
                commodityFile.getDescription(),
                commodityFile.getStatus()!=null?commodityFile.getStatus():null,
                filePath);
        return commodity;
    }

    /**
     * 将commodity对象转换为与前端数据相对应的commodityFile对象，并返回给前端
     * @param
     * @return
     */
    public static Commodity reConvert(Commodity commodity) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fileName = commodity.getImage();

        FileInputStream fileInputStream = new FileInputStream(fileName);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len = -1;
        while((len = fileInputStream.read(b)) != -1) {
            bos.write(b, 0, len);
        }
        byte[] fileByte = bos.toByteArray();
        //以上为读取图片变成字节数组

        //进行base64位加密
        BASE64Encoder encoder = new BASE64Encoder();
        String data = encoder.encode(fileByte);

        return new Commodity(
                commodity.getId(),
                commodity.getCode(),
                commodity.getName(),
                commodity.getMarketPrice(),
                commodity.getSecPrice(),
                commodity.getStartTime(),
                commodity.getEndTime(),
                commodity.getStock(),
                commodity.getDescription(),
                commodity.getStatus(),
                data
        );
    }


}
