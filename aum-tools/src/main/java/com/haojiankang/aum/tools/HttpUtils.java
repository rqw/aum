package com.haojiankang.aum.tools;

import javafx.util.Callback;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings("deprecation")
public class HttpUtils {
    /**
     *
     * formBody:构建form body 内容.
     *
     * @author ren7wei
     * @param bodys: k1,v1,k2,v2
     * @return
     * @since JDK 1.7
     */
    public static List<NameValuePair> formBody(String... bodys) {
        List<NameValuePair> params = new ArrayList<>();
        for (int i = 0; i < bodys.length / 2; i++) {
            params.add(new BasicNameValuePair(bodys[2 * i], bodys[2 * i + 1]));
        }
        return params;
    }

    /**
     * 获取远程json数据(也可以用于获取服务器返回的其他类型数据)
     *
     * @param url
     *            远程url地址
     * @param bodys
     *            post提交的参数类容,格式为： p1=1&p2=2
     * @param headers
     *            请求头参数
     * @return 服务端响应数据
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String post(String url, String bodys, Map<String, String> headers)
            throws ClientProtocolException, IOException {
        Map<String, Object> options = new HashMap<>();
        options.put("method", "POST");
        options.put("charset.response", "UTF-8");
        options.put("charset.request", "UTF-8");
        return request(url, bodys, headers, options);
    }

    /**
     * 获取远程json数据(也可以用于获取服务器返回的其他类型数据)
     *
     * @param url
     *            远程url地址
     * @param bodys
     *            post提交的参数类容
     * @param headers
     *            请求头参数
     * @return 服务端响应数据
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String post(String url, List<NameValuePair> bodys, Map<String, String> headers)
            throws ClientProtocolException, IOException {
        Map<String, Object> options = new HashMap<>();
        options.put("method", "POST");
        options.put("charset.response", "UTF-8");
        options.put("charset.request", "UTF-8");
        UrlEncodedFormEntity entity = null;
        if (bodys != null) {
            entity = new UrlEncodedFormEntity(bodys, Charset.forName(options.get("charset.request").toString()));
        }
        return request(url, entity, headers, options);
    }

    /**
     *
     * @param url
     *            远程url地址（参数直接拼接在url上）
     * @param headers
     *            请求头参数
     * @return 服务端响应数据
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String get(String url, Map<String, String> headers)
            throws ClientProtocolException, IOException {
        Map<String, Object> options = new HashMap<>();
        options.put("method", "GET");
        options.put("charset.response", "UTF-8");
        options.put("charset.request", "UTF-8");
        StringEntity entry = null;
        return request(url, entry, headers, options);
    }

    public static String request(String url, String bodys, Map<String, String> headers, Map<String, Object> options)
            throws ClientProtocolException, IOException {
        StringEntity entry = null;
        if (bodys != null) {
            entry = new StringEntity(bodys, options.get("charset.request").toString());
        }
        return request(url, entry, headers, options);
    }

    /**
     *
     * @param url
     *            远程请求地址
     * @param body
     *            post请求参数
     * @param headers
     *            请求头参数
     * @param options
     *            请求设置
     *            <ul>
     *            <li>method:http请求方法，默认为GET，目前支持GET/POST/DELETE/PUT</li>
     *            <li>charset.request:请求参数编码格式，默认为UTF-8</li>
     *            <li>charset.response:响应内容编码格式，默认为UTF-8</li>
     *            </ul>
     * @return 服务端响应数据
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String request(String url, StringEntity body, Map<String, String> headers,
                                     Map<String, Object> options) throws ClientProtocolException, IOException {
        HttpUriRequest request = null;
        if (options == null) {
            options = new HashMap<>();
        }
        // 检查options是否合法并设定默认值
        checkOptionsAndSettingDefaultValue(options);
        // 构建请求实体
        request = builderRequest(url, body, headers, options, request);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            CloseableHttpResponse response = client.execute(request);
            options.put("response.headers", response.getAllHeaders());
            return EntityUtils.toString(response.getEntity(),
                    Charset.forName(options.get("charset.response").toString()));
        } finally {
        }

    }

    public static void requestResponse(String url, StringEntity body, Map<String, String> headers, Map<String, Object> options, Consumer<HttpResponse> call) throws ClientProtocolException, IOException {
        HttpUriRequest request = null;
        if (options == null) {
            options = new HashMap<>();
        }
        // 检查options是否合法并设定默认值
        checkOptionsAndSettingDefaultValue(options);
        // 构建请求实体
        request = builderRequest(url, body, headers, options, request);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            CloseableHttpResponse response = client.execute(request);
            options.put("response.headers", response.getAllHeaders());
            call.accept(response);
        } finally {
        }

    }

    private static HttpUriRequest builderRequest(String url, StringEntity entry, Map<String, String> headers,
                                                 Map<String, Object> options, HttpUriRequest request) {
        switch (options.get("method").toString()) {
            case "GET":
                request = new HttpGet(url);
                break;
            case "POST":
                request = new HttpPost(url);
                if (entry != null) {
                    ((HttpPost) request).setEntity(entry);
                }
                break;
            case "PUT":
                request = new HttpPut(url);
                if (entry != null) {
                    ((HttpPut) request).setEntity(entry);
                }
                break;
            case "DELETE":
                request = new HttpDelete(url);
                break;
            default:
                throw new RuntimeException("not support http method:" + options.get("method").toString());
        }
        if (headers != null)
            for (Map.Entry<String, String> e : headers.entrySet()) {
                request.addHeader(e.getKey(), e.getValue());
            }
        return request;
    }
    private static void checkOptionsAndSettingDefaultValue(Map<String, Object> options) {
        // 校验method
        if (options.get("method") == null) {
            options.put("method", "GET");
        }
        // 校验charset.response
        if (options.get("charset.response") == null) {
            options.put("charset.response", "UTF-8");
        }
        // 校验charset.request
        if (options.get("charset.request") == null) {
            options.put("charset.request", "UTF-8");
        }
    }
    private static String buildUrl(String host, String path, Map<String, String> querys)
            throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(host);
        if (!StringUtils.isBlank(path)) {
            sbUrl.append(path);
        }
        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append("&");
                }
                if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!StringUtils.isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isBlank(query.getValue())) {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
                    }
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append("?").append(sbQuery);
            }
        }
        return sbUrl.toString();
    }
    private static HttpClient wrapClient(String host) {
        HttpClient httpClient = new DefaultHttpClient();
        if (host.startsWith("https://")) {
            sslClient(httpClient);
        }
        return httpClient;
    }
    private static void sslClient(HttpClient httpClient) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] xcs, String str) {
                }
                public void checkServerTrusted(X509Certificate[] xcs, String str) {
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry registry = ccm.getSchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
}