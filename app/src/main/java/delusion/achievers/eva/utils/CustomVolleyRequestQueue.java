package delusion.achievers.eva.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

import delusion.achievers.eva.httprequest.TLSSocketFactory;

/**
 * Custom implementation of Volley Request Queue
 */
public class CustomVolleyRequestQueue {

//    private static CustomVolleyRequestQueue mInstance;
    private Activity mCtx;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;


    public CustomVolleyRequestQueue(Activity context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

//    public static synchronized CustomVolleyRequestQueue getInstance(Activity context) {
//        if (mInstance == null) {
//            mInstance = new CustomVolleyRequestQueue(context);
//        }
//        return mInstance;
//    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(mCtx.getCacheDir(), 10 * 1024 * 1024);
            HurlStack stack;
            try {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
// Use a socket factory that removes sslv3 and add TLS1.2
                    stack = new HurlStack(null, new TLSSocketFactory());
                } else {
                    stack = new HurlStack();
                }
            } catch (Exception e) {
                stack = new HurlStack();
                EvaLog.i("NetworkClient", "can no create custom socket factory");
            }
            Network network = new BasicNetwork(stack);
            mRequestQueue = new RequestQueue(cache, network);
            // Don't forget to start the volley request queue
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

}