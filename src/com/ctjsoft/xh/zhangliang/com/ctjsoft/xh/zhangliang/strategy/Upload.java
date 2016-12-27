package com.ctjsoft.xh.zhangliang.com.ctjsoft.xh.zhangliang.strategy;

import com.ctjsoft.xh.zhangliang.com.ctjsoft.xh.zhangliang.strategy.progress.ProgressOutHttpEntity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.magiclen.magicurlnetwork.MagicURLNetwork;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by Tim on 2016/12/22.
 */
public class Upload {

    public void doUpload(String projectName,String projectVersion,File file) throws Exception {

        MultipartEntityBuilder builder= MultipartEntityBuilder.create();

        builder.addBinaryBody("file",file);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpEntity request = builder.build();
            long totalSize = request.getContentLength();
            int fen = (int)totalSize/30;

            ProgressOutHttpEntity progressHttpEntity = new ProgressOutHttpEntity(request, new ProgressOutHttpEntity.ProgressListener() {
                int i = 1;
                @Override
                public void transferred(long transferedBytes) {

                    if((int)transferedBytes/(fen*i)>=1&&i<=31){
                        System.out.print(">");
                        i++;
                    }
                }
            });

            HttpPost post = new HttpPost(Config.getPublishServer() + "/projects/" + projectName + "/" + projectVersion + "/file");
            post.setEntity(progressHttpEntity);
            CloseableHttpResponse response = httpclient.execute(post);
            int ireturn = response.getStatusLine().getStatusCode();
            System.out.println(ireturn==201?"\r\n状态：上传成功！":"状态：上传失败！");
        } finally {
            httpclient.close();
        }
    }

    public void doUploadMagic(String projectName,String projectVersion,File file) throws Exception {
        MagicURLNetwork net = MagicURLNetwork.POST(Config.getPublishServer() + "/projects/" +projectName+"/"+projectVersion+"/file");
        net.setParameter("file",file);
        net.open();
    }

    public void run() throws Exception {
        //获取产品及版本号
        if(Config.getPublicshProduct().length>0){
            for(int i=0,n=Config.getPublicshProduct().length;i<n;i++){
                String sp = Config.getPublicshProduct()[i];

                File fp = new File(Config.getPublishPath());
                if(!fp.exists() && !fp.isDirectory()){
                    System.out.println("发布路径配置有误，没有找到对应路径:"+Config.getPublishPath());
                }

                File[] fVersion = fp.listFiles();
                for(int j=0;j<fVersion.length;j++) {
                    //
                    if (fVersion[j].isDirectory()) {
                        String sVersion = fVersion[j].getName();
                        if (sVersion.toUpperCase().indexOf(sp.toUpperCase()) > -1) {
                            Config.mapVersion.put(sp.toUpperCase(), sVersion.toUpperCase());
                        }
                    }
                }
            }
        }
        else{
            System.out.println("没有配置需要发布的产品，在配置文件中配置如下：public.products=pm;budget;gp");
        }

        if(Config.mapVersion.size()<1){
            System.out.println("在配置的目录下，没找到对应的版本目录！");
        }
        else{
            Iterator iter = Config.mapVersion.keySet().iterator();
            while (iter.hasNext()){
                String sp = (String)iter.next();
                String sv = (String)Config.mapVersion.get(sp);

                //创建版本
                this.createVersion(sp,sv);

                File file = new File(Config.getPublishPath()+"/"+sv);
                File[] flist = file.listFiles();
                for (int i = 0; i <flist.length ; i++) {
                    File f = flist[i];
                    if(!f.isDirectory()){
                        this.doUpload(sp,sv,f);
//                        this.doUploadMagic(sp,sv,f);
                        System.out.println(sv + "的" +f.getName() + "--文件发布完成。");
                    }
                }
            }
        }
    }

    private void createVersion(String sp, String sv) {
        final MagicURLNetwork network = MagicURLNetwork.POST(Config.getPublishServer() + "/projects/" +sp+"/"+sv);
        System.out.println("创建"+sv+"版本.....");
        network.open();
        System.out.println(network.getResultAsString());
//        if(network.getResultAsString().indexOf("201")>-1){
//            System.out.print("成功！");
//        }
//        else{
//            System.out.print("失败！");
//        }
    }

}
