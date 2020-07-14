package com.atguigu.writer;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Index;

import java.io.IOException;

public class EsWriter  {
    public static void main(String[] args) throws IOException {
        //1.创建ES客户端连接池(工厂类)
        JestClientFactory jestClientFactory = new JestClientFactory();


        //2.创建ES客户端连接地址（配置信息）
        HttpClientConfig httpClientConfig = new HttpClientConfig.Builder("http://hadoop102:9092").build();

        //3.设置ES连接地址   (将es客户端连接地址放在 es客户端连接池里)
       jestClientFactory.setHttpClientConfig(httpClientConfig);


        //4.获取ES客户端连接(jestClient)
        JestClient jestClient = jestClientFactory.getObject();
        //5.构建ES插入数据对象
        Index index = new Index.Builder("{\n" +
                "  \"name\":\"zhangsan\",\n" +
                "  \"age\":17\n" +
                "}").index("test5").type("_doc").id("2").build();

        //6.执行插入数据操作
        jestClient.execute(index);
        //7.关闭连接
       jestClient.close();

    }
}
