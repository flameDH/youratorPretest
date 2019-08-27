package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpHelper {

	public static String postFile(String strGet, ArrayList<String> postParam, ArrayList<String> fileName,
			ArrayList<byte[]> fileBytes, Map<String, String> headers) throws IOException {

		MultipartEntityBuilder builder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		for (int i = 0; i < postParam.size(); i++) {
			builder.addBinaryBody(postParam.get(i), fileBytes.get(i), ContentType.MULTIPART_FORM_DATA, fileName.get(i));
		}
		HttpEntity entity = builder.build();

		String postUrl = strGet;
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(2 * 1000).setSocketTimeout(2 * 1000)
				.build();
		HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
		HttpPost post = new HttpPost(postUrl);

		for (Entry<String, String> entry : headers.entrySet()) {
			String headerKey = entry.getKey();
			String headerValue = entry.getValue();
			post.addHeader(headerKey, headerValue);
		}

		post.setEntity(entity);
		HttpResponse response = httpClient.execute(post);
		response.getStatusLine().getStatusCode();
		byte[] bytes = EntityUtils.toByteArray(response.getEntity());
		return new String(bytes);
	}

	public static String postJSON(String sURI, String strPost) throws IOException {

		String postUrl = sURI;
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(2 * 1000).setSocketTimeout(2 * 1000)
				.build();
		HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
		HttpPost post = new HttpPost(postUrl);
		StringEntity postingString = new StringEntity(strPost);
		post.setEntity(postingString);
		post.setHeader("Content-type", "application/json");
		HttpResponse response = httpClient.execute(post);
		response.getStatusLine().getStatusCode();
		byte[] bytes = EntityUtils.toByteArray(response.getEntity());
		return new String(bytes);
	}

	public static String postForm(String sURI, JSONObject json) throws IOException, JSONException {

		String postUrl = sURI;
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(2 * 1000).setSocketTimeout(2 * 1000)
				.build();
		HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();

		Iterator<?> keys = json.keys();

		while (keys.hasNext()) {
			String key = (String) keys.next();
			urlParameters.add(new BasicNameValuePair(key, json.getString(key)));
		}

		HttpPost post = new HttpPost(postUrl);
		post.setEntity(new UrlEncodedFormEntity(urlParameters));

		HttpResponse response = httpClient.execute(post);
		response.getStatusLine().getStatusCode();
		byte[] bytes = EntityUtils.toByteArray(response.getEntity());
		return new String(bytes);

	}

	public static String postJSON(String sURI, String strPost, Map<String, String> headers) throws IOException {
		String postUrl = sURI;

		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(2 * 1000).setSocketTimeout(2 * 1000)
				.build();
		HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
		HttpPost post = new HttpPost(postUrl);
		StringEntity postingString = new StringEntity(strPost);
		post.setEntity(postingString);
		for (Entry<String, String> entry : headers.entrySet()) {
			String headerKey = entry.getKey();
			String headerValue = entry.getValue();
			post.addHeader(headerKey, headerValue);
		}

		post.addHeader("Content-type", "application/json");
		HttpResponse response = httpClient.execute(post);
		response.getStatusLine().getStatusCode();
		byte[] bytes = EntityUtils.toByteArray(response.getEntity());
		return new String(bytes);
	}

	public static String getHtml(String sUri) {
		try {
			return Request.Get(sUri).version(HttpVersion.HTTP_1_1).connectTimeout(2 * 1000).socketTimeout(2 * 1000)
					.execute().returnContent().asString();
		} catch (Exception e) {
			return null;
		}
	}

	public static byte[] getHtmlAsByte(String sUri) {
		try {
			return Request.Get(sUri).version(HttpVersion.HTTP_1_1).connectTimeout(2 * 1000).socketTimeout(2 * 1000)
					.execute().returnContent().asBytes();
		} catch (Exception e) {
			return null;
		}
	}

	public static String getHtml(String sUri, Map<String, String> headers) {
		try {
			Request getRequest = Request.Get(sUri).version(HttpVersion.HTTP_1_1).connectTimeout(2 * 1000)
					.socketTimeout(2 * 1000);
			for (Entry<String, String> entry : headers.entrySet()) {
				String headerKey = entry.getKey();
				String headerValue = entry.getValue();
				getRequest.addHeader(headerKey, headerValue);
			}
			return getRequest.execute().returnContent().asString();
		} catch (Exception e) {
			return null;
		}
	}
}
