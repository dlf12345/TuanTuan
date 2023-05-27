package com.example.tuantuan.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.example.tuantuan.domain.AliOSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 阿里云 OSS 工具类,该类中定义了一个方法用于上传文件，并返回文件存储在阿里云空间中的URL地址
 */
@Component//将该类的创建对象统治权交给IOC容器
public class AliOSSUtils {
    @Autowired//资源注入
    private AliOSS aliOSS;
    /**
     * 定义一个静态方法实现上传图片到OSS
     */
    public  String upload(MultipartFile file) throws IOException {
        String endpoint = aliOSS.getEndpoint();
        String accessKeyId = aliOSS.getAccessKeyId();
        String accessKeySecret = aliOSS.getAccessKeySecret();
        String bucketName = aliOSS.getBucketName();
        // 获取上传的文件的输入流
        InputStream inputStream = file.getInputStream();

        // 避免文件覆盖,使用UUID创建一个文件名
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        //上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, fileName, inputStream);

        //文件访问路径
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;
        // 关闭ossClient
        ossClient.shutdown();
        return url;// 把上传到oss的路径返回
    }

}
