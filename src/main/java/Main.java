import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;

public class Main {
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();
        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=4uHiBFNg9m3HDh0amVL3DGvdTOqNMo4fsubWORCO");
        CloseableHttpResponse response = httpClient.execute(request);
        Nasa nasa = mapper.readValue(response.getEntity().getContent(), Nasa.class);
        System.out.println(nasa);
        HttpGet requestSecond = new HttpGet(nasa.getUrl());
        CloseableHttpResponse responSecond = httpClient.execute(requestSecond);
        String[] url = nasa.getUrl().split("/");
        String file = url[6];
        HttpEntity entity = responSecond.getEntity();
        BufferedOutputStream obj = new BufferedOutputStream(new FileOutputStream(file));
        entity.writeTo(obj);
        obj.flush();
        obj.close();
    }
}
