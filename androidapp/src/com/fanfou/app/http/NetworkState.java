package com.fanfou.app.http;

import com.fanfou.app.App;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

/**
 * 网络连接状态判断，是否连接，接入点，WIFI等，
 * 
 * @author mcxiaoke
 * @version 1.0 2011.01.29
 * @version 1.1 2011.05.02
 * @version 1.2 2011.05.02
 * 
 */
public class NetworkState {
	/**
	 * 字符串标志，是否为WAP接入点
	 */
	private static final String tag = NetworkState.class.getSimpleName();
	private ConnectivityManager cm;

	public boolean connected = true;

	public String apnTypeName = "cmnet";
	public Type apnType = Type.NET;

	/**
	 * 记录调试信息
	 * 
	 * @param message
	 *            调试信息内容
	 */
	private void log(String message) {
		Log.e(tag, message);
	}

	/**
	 * New网络连接状态对象
	 * 
	 * @param c
	 */
	public NetworkState(Context context) {
		this.cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		initState();
	}

	private void initState() {
		try {
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) {
				connected = true;
				if (App.DEBUG) {
					log(info.toString());
				}
				if (info.getType() == ConnectivityManager.TYPE_WIFI) {
					apnType = Type.WIFI;
					apnTypeName = "wifi";
				} else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
					apnTypeName = info.getExtraInfo();
					if (!TextUtils.isEmpty(apnTypeName)) {
						if (apnTypeName.equals("3gnet")) {
							apnType = Type.HSDPA;
						} else if (apnTypeName.equals("ctwap")) {
							apnType = Type.CTWAP;
						} else if (apnTypeName.contains("wap")) {
							apnType = Type.WAP;
						}
					}
				}
			} else {
				connected = false;
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 判断网络连接状态
	 * 
	 * @return 返回网络是否可用
	 */
	public boolean isAvailable() {
		return connected;
	}

	/**
	 * 判断接入点类型
	 * 
	 * @return 返回接入点类型
	 */
	public Type getApnType() {
		return apnType;
	}

	public String getApnName() {
		return apnTypeName;
	}

	/**
	 * 判断是否WIFI接入
	 * 
	 * @return 是否WIFI
	 */
	public boolean isWIFI() {
		boolean result = false;
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI
				&& info.isConnected()) {
			result = true;
		}
		return result;
	}

	public boolean is3G() {
		boolean result = false;
		try {
			NetworkInfo info = cm.getActiveNetworkInfo();
			if (info != null && info.isAvailable()) {
				connected = true;
				log(info.toString());
				if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
					apnTypeName = info.getExtraInfo();
					if (!TextUtils.isEmpty(apnTypeName)) {
						if (apnTypeName.equals("3gnet")) {
							result = true;
						}
					}
				}
			} else {
				connected = false;
			}
		} catch (Exception e) {
			if (App.DEBUG) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 网络连接类型
	 */
	public static enum Type {
		WIFI, HSDPA, NET, WAP, CTWAP, ;
	}

}
