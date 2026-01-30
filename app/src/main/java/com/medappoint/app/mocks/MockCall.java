package com.medappoint.app.mocks;

import java.io.IOException;
import okhttp3.Request;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MockCall<T> implements Call<T> {

    private final T responseBody;
    private boolean isExecuted = false;
    private boolean isCanceled = false;

    public MockCall(T responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public Response<T> execute() throws IOException {
        isExecuted = true;
        return Response.success(responseBody);
    }

    @Override
    public void enqueue(Callback<T> callback) {
        isExecuted = true;
        // Simulate network delay for realism
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            if (!isCanceled) {
                if (responseBody == null && !(responseBody instanceof Void)) {
                     // Simulate 404 or empty if body is explicitly null
                     // But for our mock, we usually pass objects.
                }
                callback.onResponse(this, Response.success(responseBody));
            }
        }, 800);
    }

    @Override
    public boolean isExecuted() {
        return isExecuted;
    }

    @Override
    public void cancel() {
        isCanceled = true;
    }

    @Override
    public boolean isCanceled() {
        return isCanceled;
    }

    @Override
    public Call<T> clone() {
        return new MockCall<>(responseBody);
    }

    @Override
    public Request request() {
        return new Request.Builder().url("http://mock").build();
    }

    @Override
    public Timeout timeout() {
        return Timeout.NONE;
    }
}
