package com.zhy.http.okhttp;

import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

public class ServerAPI
{
//	public static void login(String phone,String password,String device_id,StringCallback callback){
//		String url = UrlBuilder.URL_BASE_API+UrlBuilder.URL_LOGIN;
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("phone", phone);
//		params.put("password", password);
//		params.put("device_id", device_id);
//		OkHttpUtils.get().url(url).params(params).build().execute(callback);
//	}
//	public static void getInfo(String userKey,StringCallback callback){
//		String url = UrlBuilder.URL_BASE_API+UrlBuilder.URL_GET_MEMBERINFO;
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("userkey",userKey);
//		OkHttpUtils.get().url(url).params(params).build().execute(callback);
//	}
//
//	public static void updateInfo(String userKey,String headUri,StringCallback callback){
//		String url = UrlBuilder.URL_BASE_API+UrlBuilder.URL_MEMBER_INFO_UPDATE;
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("userkey", userKey);
//		params.put("nickname", "wanjian");
//		params.put("QQ", "");
//		params.put("wechat", "");
//		params.put("phone", "");
//		params.put("school", "");
//		params.put("grade", "");
//		params.put("subject", "");
//		params.put("profile", "");
//		File file = new File( headUri);
//		OkHttpUtils.post().url(url).params(params).addFile("headUri", file.getName(),file).build().execute(callback);
//	}
//	public static void downloadFile(String url,FileCallBack fileCallBack){
//		OkHttpUtils.get().url(url).build().execute(fileCallBack);
//	}
}
