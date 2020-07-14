package com.atguigu.writer;

import com.atguigu.bean.Stu;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

public class EsWriterByBulk {
    public static void main(String[] args) {
        //1.创建工厂
        JestClientFactory jestClientFactory = new JestClientFactory();
        //2.创建配置信息
        HttpClientConfig httpClientConfig = new HttpClientConfig.Builder("http://hadoop102:9092").build();

        //3.设置配置信息
        jestClientFactory.setHttpClientConfig(httpClientConfig);

        //4.获取客户端对象
        JestClient jestClient = jestClientFactory.getObject();
        //5.创建多个Index对象
        Stu stu1 = new Stu();
        Stu stu2 = new Stu();
        Stu stu3 = new Stu();
        //6.创建Bulk对象

        //7.执行批量插入数据操作

        //8.关闭资源








    }
}
