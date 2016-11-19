package com.express.model;

import java.io.UnsupportedEncodingException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class SendMsg_webchinese {

	public static void check(int pass,String tel) throws Exception {

		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://utf8.sms.webchinese.cn");
		post.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");// 在头文件中设置转码3
		NameValuePair[] data = { new NameValuePair("Uid", "yangnaihua"),
				new NameValuePair("Key", "96e48b5ca4dd6e80f168"),
				new NameValuePair("smsMob", tel),
				new NameValuePair("smsText", "您好，您的临时密码是"+pass+"【成都东软学院】") };
		post.setRequestBody(data);

		client.executeMethod(post);
		Header[] headers = post.getResponseHeaders();
		int statusCode = post.getStatusCode();
		System.out.println("statusCode:" + statusCode);
		for (Header h : headers) {
			System.out.println(h.toString());
		}
		String result = new String(post.getResponseBodyAsString().getBytes(
				"utf-8"));
		System.out.println(result);

		post.releaseConnection();

	}

}
