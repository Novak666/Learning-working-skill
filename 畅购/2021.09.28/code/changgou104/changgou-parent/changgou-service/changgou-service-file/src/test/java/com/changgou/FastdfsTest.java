package com.changgou;

import org.csource.fastdfs.*;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @version V1.0
 * @author: Novak
 * @date: 2021/09/27
 **/
public class FastdfsTest {

    //上传图片
    @Test
    public void upload() throws Exception {
        //1.创建一个配置文件 用于配置tracker_server的ip和端口
        //2.加载配置文件生效
        ClientGlobal.init("G:\\IdeaProjects\\changgou104\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        //3.创建trackerclient对象
        TrackerClient trackerClient = new TrackerClient();
        //4.获取trackerServer对象
        TrackerServer trackerServer = trackerClient.getConnection();
        //5.创建storageClient对象
        StorageClient storageClient = new StorageClient();
        //6.执行一个方法（上传图片的方法）storageClient 提供了许多操作文件的方法
        //参数1 指定要上传文件的本地的路径
        //参数2 指定文件的扩展名 不要带 “.”
        //参数3 指定元数据（图片的作者，拍摄日期 像素 。。。。。。）
        String[] strings = storageClient.upload_file("E:\\照片手绘\\帕神\\1 (23).jpg", "jpg", null);
        for (String string : strings) {
            System.out.println(string);
        }
    }

    //下载图片
    @Test
    public void download() throws Exception {
        //1.创建一个配置文件 用于配置tracker_server的ip和端口
        //2.加载配置文件生效
        ClientGlobal.init("G:\\IdeaProjects\\changgou104\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        //3.创建trackerclient对象
        TrackerClient trackerClient = new TrackerClient();
        //4.获取trackerServer对象
        TrackerServer trackerServer = trackerClient.getConnection();
        //5.创建storageClient对象
        StorageClient storageClient = new StorageClient();

        byte[] bytes = storageClient.download_file("group1", "M00/00/00/wKjThGFR8viAX4AtAACsT2qOujY877.jpg");

        //写入磁盘
        File file = new File("G:/abc.png");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bytes);
        fileOutputStream.close();
    }

    //删除图片
    @Test
    public void delete() throws Exception {
        //1.创建一个配置文件 用于配置tracker_server的ip和端口
        //2.加载配置文件生效
        ClientGlobal.init("G:\\IdeaProjects\\changgou104\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fdfs_client.conf");
        //3.创建trackerclient对象
        TrackerClient trackerClient = new TrackerClient();
        //4.获取trackerServer对象
        TrackerServer trackerServer = trackerClient.getConnection();
        //5.创建storageClient对象
        StorageClient storageClient = new StorageClient();

        int res = storageClient.delete_file("group1", "M00/00/00/wKjThGFR8viAX4AtAACsT2qOujY877.jpg");
        if (res == 0)
            System.out.println("success");
        else
            System.out.println("failure");
    }

    //获取文件的信息数据
    @Test
    public void getFileInfo() throws Exception {
        //加载全局的配置文件
        ClientGlobal.init("G:\\IdeaProjects\\changgou104\\changgou-parent\\changgou-service\\changgou-service-file\\src\\main\\resources\\fdfs_client.conf");

        //创建TrackerClient客户端对象
        TrackerClient trackerClient = new TrackerClient();
        //通过TrackerClient对象获取TrackerServer信息
        TrackerServer trackerServer = trackerClient.getConnection();
        //获取StorageClient对象
        StorageClient storageClient = new StorageClient(trackerServer, null);
        //执行文件上传

        FileInfo group1 = storageClient.get_file_info("group1", "M00/00/00/wKjThGFR8viAX4AtAACsT2qOujY877.jpg");

        System.out.println(group1);

    }
}
