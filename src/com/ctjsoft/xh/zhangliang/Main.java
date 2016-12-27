package com.ctjsoft.xh.zhangliang;

import com.ctjsoft.xh.zhangliang.com.ctjsoft.xh.zhangliang.strategy.Config;
import com.ctjsoft.xh.zhangliang.com.ctjsoft.xh.zhangliang.strategy.Upload;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println("hello world!");

        Upload upload = new Upload();
        try {
            upload.run();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
