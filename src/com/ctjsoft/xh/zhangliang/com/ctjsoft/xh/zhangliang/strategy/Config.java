package com.ctjsoft.xh.zhangliang.com.ctjsoft.xh.zhangliang.strategy;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Tim on 2016/12/22.
 */
public class Config {
    static String publishPath;

    public static String getPublishPath() {
        return publishPath;
    }

    public static String getPublishServer() {
        return publishServer;
    }

    public static String[] getPublicshProduct() {
        return publicshProduct;
    }

    static String publishServer;

    static String[] publicshProduct;

    static Map mapVersion;

    static  {
        Properties prop =  new  Properties();
        InputStream in = Object. class.getResourceAsStream( "/publish.properties" );
        try  {
            prop.load(in);
            publishPath = prop.getProperty( "publish.dir" ).trim();
            publishServer = prop.getProperty( "publish.server" ).trim();

            String s = prop.getProperty("public.products").trim();
            publicshProduct = s.split(";");
            mapVersion = new HashMap();

        }  catch  (IOException e) {
            e.printStackTrace();
        }
    }
}
