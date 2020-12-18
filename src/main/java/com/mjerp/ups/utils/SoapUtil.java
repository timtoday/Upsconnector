package com.mjerp.ups.utils;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author tim
 */
public class SoapUtil {


    public static String postXml(String xml, String url) {
        String result = "";
        try {

            CloseableHttpClient httpclient = HttpClients.createDefault();
            //System.out.println(xml);

            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "text/html;charset=UTF-8");

            //解决中文乱码问题
            StringEntity stringEntity = new StringEntity(xml, "UTF-8");
            stringEntity.setContentEncoding("UTF-8");

            httpPost.setEntity(stringEntity);

            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(final HttpResponse response)
                        throws ClientProtocolException, IOException {//
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException(
                                "Unexpected response status: " + status);
                    }
                }
            };
            String responseBody = httpclient.execute(httpPost, responseHandler);
            //System.out.println("----------------------------------------");
            //System.out.println(XmlToJson.xmlToJson(null,responseBody));
            //result = XmlToJson.xmlToJson(null,responseBody);
            result = responseBody;

        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

}
