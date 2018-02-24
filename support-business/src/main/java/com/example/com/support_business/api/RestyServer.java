package com.example.com.support_business.api;

import android.content.Context;

import com.example.com.support_business.module.Entity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import okhttp3.CacheControl;
import okhttp3.Headers;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rhm on 2018/2/22.
 */

public abstract class RestyServer {
    private final HashMap<String, CompositeDisposable> compositeDisposableHashMap = new HashMap<>();

    public RestyServer(Context context) {
        super();
    }

    protected final CacheControl cacheControl(boolean refresh) {
        return refresh ? CacheControl.FORCE_NETWORK : null;
    }

    protected final Date parseReceivedDate(Response<?> response) {
        return new Date(response.raw().receivedResponseAtMillis());
    }

    protected final Date parseServedDate(Response<?> response) {
        Headers headers = response.headers();
        return headers != null ? headers.getDate("Date") : null;
    }

    protected final <E extends Entity> void callOnResponseMethod(Callback<E> callback, Response<E> entityResponse) {
        if (callback != null) {
            callback.onResponse(parseReceivedDate(entityResponse), parseServedDate(entityResponse), entityResponse.body());
        }
    }

    protected final <E extends Entity> void callOnFailureMethod(Callback<E> callback, Throwable throwable) {
        if (callback != null) {
            callback.onFailure(throwable);
        }
    }

    protected final <E extends Entity> void callOnUnauthorizedMethod(SSOCallback<E> callback) {
        if (callback != null) {
            callback.onUnauthorized();
        }
    }

    protected final void throwNullOrFailureResponse() throws IllegalAccessException {
        throw new IllegalAccessException("Response为空，或Response为失败！");
    }

    protected final synchronized void add(String compositeTag, Disposable disposable) {
        CompositeDisposable compositeDisposable = compositeDisposableHashMap.get(compositeTag);
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
            compositeDisposableHashMap.put(compositeTag, compositeDisposable);
        }
        compositeDisposable.add(disposable);
    }

    protected final synchronized void clear(String compositeTag) {
        CompositeDisposable compositeDisposable = compositeDisposableHashMap.remove(compositeTag);
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public final synchronized void clearAll() {
        for (Map.Entry<String, CompositeDisposable> entry : compositeDisposableHashMap.entrySet()) {
            CompositeDisposable compositeDisposable = entry.getValue();
            if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
                compositeDisposable.dispose();
            }
        }
        compositeDisposableHashMap.clear();
    }

    public interface Callback<E extends Entity> {
        public void onResponse(Date receivedDate, Date servedDate, E entity);

        public void onFailure(Throwable throwable);
    }

    public interface SSOCallback<E extends Entity> extends Callback<E> {
        public void onUnauthorized();
    }

    private static final Gson GSON = new GsonBuilder().create();

    public static <T> T fromJson(Object src, Class<T> classOfT) {
        try {
            return GSON.fromJson(GSON.toJson(src), classOfT);
        } catch (JsonSyntaxException ignored) {
        }
        return null;
    }

    public static <T> T fromJson(Object src, Type typeOfT) {
        try {
            return GSON.fromJson(GSON.toJson(src), typeOfT);
        } catch (JsonSyntaxException ignored) {

        }
        return null;
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return GSON.fromJson(json, classOfT);
        } catch (JsonSyntaxException ignored) {

        }
        return null;
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        try {
            return GSON.fromJson(json, typeOfT);
        } catch (JsonSyntaxException ignored) {

        }
        return null;
    }

    public static String toJson(Object src) {
        try {
            return GSON.toJson(src);
        } catch (Exception ignored) {

        }
        return null;
    }
}
