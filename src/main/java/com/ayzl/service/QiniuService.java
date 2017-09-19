package com.ayzl.service;

import com.alibaba.fastjson.JSONObject;
import com.ayzl.util.ToutiaoUtil;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.qiniu.util.Auth;

import java.io.IOException;
import java.util.UUID;

@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);
    //设置好账号的ACCESS_KEY和SECRET_KEY
    String ACCESS_KEY = "CNbFtrvfKaJzmHgQAiO-UHLyZxY_jrI1mgHRDv_Z";
    String SECRET_KEY = "1CnYSP_XsPMKoWOsyiaQpf4-oJXeo0HcIKTTIIdc";
    //要上传的空间
    String bucketname = "ayzl";

    //密钥配置
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    Configuration cfg = new Configuration(Zone.zone1());
    //创建上传对象
    UploadManager uploadManager = new UploadManager(cfg);

    private static String QINIU_IMAGE_DOMAIN = "http://ow6ao0los.bkt.clouddn.com/";

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public String getUpToken() {
        return auth.uploadToken(bucketname);
    }

    public String saveImage(MultipartFile file) throws IOException{
        try{
            int npos = file.getOriginalFilename().lastIndexOf('.');
            if(npos < 0)
                return null;
            String fileExt = file.getOriginalFilename().substring(npos+1).toLowerCase();
            if(!ToutiaoUtil.isFileAllowed(fileExt))
                return null;

            String fileName = UUID.randomUUID().toString().replaceAll("-","") + "." + fileExt;
            Response res = uploadManager.put(file.getBytes(), fileName, getUpToken());
            if(res.isOK() && res.isJson()){
                return QINIU_IMAGE_DOMAIN + JSONObject.parseObject(res.bodyString()).get("key");
            }else{
                logger.error("七牛异常:" + res.bodyString());
                return null;
            }
        }catch (Exception e){
            logger.error("七牛异常:" + e.getMessage());
            return null;
        }
    }
}
