package ga.codehub.tools;

import ga.codehub.tools.exceptions.Different;
import ga.codehub.tools.exceptions.NotFound;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;

import java.io.*;
import java.util.List;

public class BasicOperations {


    final int TIME_OUT = 10000;
    public CookieMonster cookieMonster = new CookieMonster();
    HttpClient http = null;
    org.apache.http.client.config.RequestConfig RequestConfig;
    Header[] basicHeaders;

    String authentication;

    public BasicOperations() {

        //refreshHeader();
        buildHttpClient();

    }


    public String basicGET(String url) throws Different, NotFound, IOException {


        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(this.RequestConfig);
        httpGet.setHeaders(basicHeaders);

        System.out.println(url);
        HttpResponse httpResponse = http.execute(httpGet);

        String return_string = InputToString(httpResponse.getEntity().getContent());

        //cookieMonster.displayCookies();
        httpResponse.getEntity().getContent().close();


        if (httpResponse.getStatusLine().getStatusCode() == 404) {
            httpGet.completed();
            httpGet.releaseConnection();
            System.out.println(url);
            throw new NotFound();
        } else if (httpResponse.getStatusLine().getStatusCode() != 200) {
            httpGet.completed();
            httpGet.releaseConnection();
            System.out.println(httpResponse.getStatusLine().getStatusCode());
            System.out.println(url);
            System.out.println(return_string);
            throw new Different();
        } else {
            httpGet.completed();
            httpGet.releaseConnection();
            return return_string;
        }

    }

    public void buildHttpClient() {

        HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
            @Override
            public boolean retryRequest(IOException e, int i, HttpContext httpContext) {
                if (i < 3) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        HttpClientBuilder builder = HttpClientBuilder.create();

        builder.setDefaultCookieStore(cookieMonster.httpCookieStore);
        builder.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");
        builder.setRedirectStrategy(new LaxRedirectStrategy());
        builder.setRetryHandler(retryHandler);


        org.apache.http.client.config.RequestConfig.Builder requestBuilder = RequestConfig.custom();

        requestBuilder.setConnectTimeout(TIME_OUT);
        requestBuilder.setConnectionRequestTimeout(TIME_OUT);
        requestBuilder.setSocketTimeout(60000);


        //requestBuilder.setProxy(ProxyLoader.loadProxy());
        RequestConfig = requestBuilder.build();

        http = null;
        http = builder.build();


    }


    public String basicPost(String json_payload, String url) throws IOException, NotFound, Different {


        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(RequestConfig);
        httpPost.setHeaders(basicHeaders);

        StringEntity entity = new StringEntity(json_payload, ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        HttpResponse httpResponse = http.execute(httpPost);

        String return_string = InputToString(httpResponse.getEntity().getContent());

        httpResponse.getEntity().getContent().close();


        if (httpResponse.getStatusLine().getStatusCode() == 404) {
            httpPost.completed();
            httpPost.releaseConnection();
            System.out.println(url);
            throw new NotFound();
        } else if (httpResponse.getStatusLine().getStatusCode() != 200) {
            httpPost.completed();
            httpPost.releaseConnection();

            System.out.println(httpResponse.getStatusLine().getStatusCode());
            System.out.println(url);
            throw new Different();

        } else {
            httpPost.completed();
            httpPost.releaseConnection();

        }

        return return_string;
    }

    public HttpResponse basicPostWithHttpResponse(String json_payload, String url) throws IOException, NotFound, Different {


        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(RequestConfig);
        httpPost.setHeaders(basicHeaders);

        StringEntity entity = new StringEntity(
                json_payload,
                ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        HttpResponse httpResponse = http.execute(httpPost);

        String return_string = InputToString(httpResponse.getEntity().getContent());


        this.basicHeaders = new Header[]{new BasicHeader("Authorization", httpResponse.getFirstHeader("Authorization").getValue())};

        httpResponse.getEntity().getContent().close();


        if (httpResponse.getStatusLine().getStatusCode() == 404) {
            httpPost.completed();
            httpPost.releaseConnection();
            System.out.println(url);
            throw new NotFound();
        } else if (httpResponse.getStatusLine().getStatusCode() != 200) {
            httpPost.completed();
            httpPost.releaseConnection();

            System.out.println(httpResponse.getStatusLine().getStatusCode());
            System.out.println(url);
            throw new Different();

        } else {
            httpPost.completed();
            httpPost.releaseConnection();

        }

        return httpResponse;
    }


    public String InputToString(InputStream in) throws IOException {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            Reader reader = new InputStreamReader(in);
            int data = reader.read();


            while (data != -1) {
                stringBuilder.append((char) data);
                data = reader.read();
            }
            return stringBuilder.toString();
        } catch (IOException ex) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            throw ex;
        }
    }
}
