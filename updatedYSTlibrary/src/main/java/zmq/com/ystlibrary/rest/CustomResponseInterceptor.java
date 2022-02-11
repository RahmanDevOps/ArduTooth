package zmq.com.ystlibrary.rest;

import android.util.Log;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CustomResponseInterceptor implements Interceptor {
    private final String TAG = getClass().getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        System.out.println("URL "+request.url());
        Response response = chain.proceed(request);
        if (response.code() != 200) {

        }
        Log.d(TAG, "INTERCEPTED:$ " +response.toString());
        return response;
    }

}