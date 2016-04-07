package com.feet.tanishq.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.HttpHeaderParser;


public class VolleyHttpRequest extends Request<String> {

	private AsyncTaskCompleteListener listener;
	private Map<String, String> params;
	private int serviceCode;
	private static String TAG = "VolleyHttpRequest";

	public VolleyHttpRequest(int method, Map<String, String> params,
			int serviceCode, AsyncTaskCompleteListener reponseListener,
			ErrorListener errorListener) {
		super(method, params.get(Const.URL), errorListener);
		if (AppLog.isDebug) {
			for (String key : params.keySet()) {
				AppLog.Log(TAG, key + "  < === >  " + params.get(key));
			}
		}
		params.remove(Const.URL);
		setRetryPolicy(new DefaultRetryPolicy(600000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		this.listener = reponseListener;
		this.params = params;
		this.serviceCode = serviceCode;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		HashMap<String,String> mapAuth=new HashMap<String,String>();
		String authCred=String.format("%s:%s", Const.BASEAUTH_USERNAME, Const.BASEAUTH_PASSWORD);
		String auth="Basic "+Base64.encodeToString(authCred.getBytes(),Base64.NO_WRAP);
		mapAuth.put("Authorization",auth);
		return mapAuth;
	}

	@Override
	protected Map<String, String> getParams()
			throws com.android.volley.AuthFailureError {
		return params;
	};

	@Override
	protected void deliverResponse(String response) {
		listener.onTaskCompleted(response, serviceCode);
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		try {
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(jsonString, getCacheEntry());
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		}
	}
}
