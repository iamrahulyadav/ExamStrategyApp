package com.talentsprint.android.esa.utils;

import com.talentsprint.android.esa.TalentSprintApp;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by Anudeep Reddy on 7/6/2017.
 */

public class InterceptorReceivedCookies implements Interceptor {
    boolean isCache;

    public InterceptorReceivedCookies(boolean isCache) {
        this.isCache = isCache;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (!new ServiceManager(TalentSprintApp.appContext).isNetworkAvailable()) {
            if (!isCache)
                throw (new IOException("No network available"));
            if (originalResponse.networkResponse() == null && originalResponse.cacheResponse() == null) {
                throw (new IOException("No network available"));
            }
        }
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();
            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }
            PreferenceManager.saveStringSet(TalentSprintApp.appContext, AppConstants.COOKIES, cookies);
        }
        return originalResponse;
    }
}
