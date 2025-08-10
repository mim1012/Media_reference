package com.movies.player.utils;

import android.os.Handler;
import android.os.Looper;
import com.movies.player.utils.HttpUtils;
import java.io.IOException;
import java.util.Objects;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class HttpUtils {

    /* loaded from: classes.dex */
    public interface ResultCallback {
        void onFailure(okhttp3.Call call, IOException iOException);

        void onResponse(okhttp3.Call call, Response response);

        void onSuccess(String str);
    }

    public static void PostJson(String str, JSONObject jSONObject, ResultCallback resultCallback) {
        new OkHttpClient().newCall(new Request.Builder().url(str).post(RequestBody.create((MediaType) Objects.requireNonNull(MediaType.parse("application/json; charset=utf-8")), jSONObject.toString())).build()).enqueue(new AnonymousClass1(resultCallback));
    }

    /* renamed from: com.movies.player.utils.HttpUtils$1  reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass1 implements Callback {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        final /* synthetic */ ResultCallback val$result;

        AnonymousClass1(ResultCallback resultCallback) {
            this.val$result = resultCallback;
        }

        @Override // okhttp3.Callback
        public void onFailure(final okhttp3.Call call, final IOException iOException) {
            Handler handler = new Handler(Looper.getMainLooper());
            final ResultCallback resultCallback = this.val$result;
            handler.post(new Runnable() { // from class: com.movies.player.utils.HttpUtils$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    HttpUtils.ResultCallback.this.onFailure(call, iOException);
                }
            });
        }

        @Override // okhttp3.Callback
        public void onResponse(final okhttp3.Call call, final Response response) {
            Handler handler = new Handler(Looper.getMainLooper());
            final ResultCallback resultCallback = this.val$result;
            handler.post(new Runnable() { // from class: com.movies.player.utils.HttpUtils$1$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    HttpUtils.AnonymousClass1.lambda$onResponse$1(Response.this, resultCallback, call);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$onResponse$1(Response response, ResultCallback resultCallback, okhttp3.Call call) {
            if (response.isSuccessful()) {
                try {
                    resultCallback.onSuccess(response.body().string());
                    return;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            resultCallback.onResponse(call, response);
        }
    }

    public static void Post(String str, FormBody formBody, ResultCallback resultCallback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        if (formBody == null) {
            formBody = new FormBody.Builder().build();
        }
        okHttpClient.newCall(new Request.Builder().url(str).post(formBody).build()).enqueue(new AnonymousClass2(resultCallback));
    }

    /* renamed from: com.movies.player.utils.HttpUtils$2  reason: invalid class name */
    /* loaded from: classes.dex */
    class AnonymousClass2 implements Callback {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        final /* synthetic */ ResultCallback val$result;

        AnonymousClass2(ResultCallback resultCallback) {
            this.val$result = resultCallback;
        }

        @Override // okhttp3.Callback
        public void onFailure(final okhttp3.Call call, final IOException iOException) {
            Handler handler = new Handler(Looper.getMainLooper());
            final ResultCallback resultCallback = this.val$result;
            handler.post(new Runnable() { // from class: com.movies.player.utils.HttpUtils$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    HttpUtils.ResultCallback.this.onFailure(call, iOException);
                }
            });
        }

        @Override // okhttp3.Callback
        public void onResponse(final okhttp3.Call call, final Response response) {
            Handler handler = new Handler(Looper.getMainLooper());
            final ResultCallback resultCallback = this.val$result;
            handler.post(new Runnable() { // from class: com.movies.player.utils.HttpUtils$2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    HttpUtils.AnonymousClass2.lambda$onResponse$1(Response.this, resultCallback, call);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ void lambda$onResponse$1(Response response, ResultCallback resultCallback, okhttp3.Call call) {
            if (response.isSuccessful()) {
                try {
                    resultCallback.onSuccess(response.body().string());
                    return;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            resultCallback.onResponse(call, response);
        }
    }
}
