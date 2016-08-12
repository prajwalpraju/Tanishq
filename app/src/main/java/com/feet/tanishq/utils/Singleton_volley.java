package com.feet.tanishq.utils;

import android.app.Application;
import android.text.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.feet.tanishq.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class Singleton_volley extends Application {
	 
	    public static final String TAG = Singleton_volley.class
	            .getSimpleName();
	 
	    private RequestQueue mRequestQueue;
	    private ImageLoader mImageLoader;
	 
	    private static Singleton_volley mInstance;
	 
	    @Override
	    public void onCreate() {
	        super.onCreate();
	        mInstance = this;
	       
	    }

	private Tracker mTracker;

	/**
	 * Gets the default {@link Tracker} for this {@link Application}.
	 * @return tracker
	 */
	synchronized public Tracker getDefaultTracker() {
		if (mTracker == null) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			// To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
//			mTracker = analytics.newTracker(R.xml.global_tracker);
			mTracker = analytics.newTracker("UA-81487116-1");
		}
		return mTracker;
	}
	 
	    public static synchronized Singleton_volley getInstance() {
	        return mInstance;
	    }
	 
	    public RequestQueue getRequestQueue() {
	        if (mRequestQueue == null) {
	            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
	        }
	 
	        return mRequestQueue;
	    }
	 
	    public ImageLoader getImageLoader() {
	        getRequestQueue();
	        if (mImageLoader == null) {
	            mImageLoader = new ImageLoader(this.mRequestQueue,new LruBitmapCache());
	        }
	        return this.mImageLoader;
	    }
	 
	    public <T> void addToRequestQueue(Request<T> req, String tag) {
	        // set the default tag if tag is empty
	        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
	        getRequestQueue().add(req);
	    }
	 
	    public <T> void addToRequestQueue(Request<T> req) {
	        req.setTag(TAG);
	        getRequestQueue().add(req);
	    }
	 
	    public void cancelPendingRequests(Object tag) {
	        if (mRequestQueue != null) {
	            mRequestQueue.cancelAll(tag);
	        }
	    }
	}


