package zmq.com.ystlibrary.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


public class ApiClient {

   public static final String BASE_URL_PrePost = "http://182.77.61.97/MiraPhilips/";

  // public static final String BASE_URL_PrePost = "http://www.islamsmart.com/";


    public static final String BASE_URL = "http://yourstoryteller.org/story/";

    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
           /* HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);*/

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(100, TimeUnit.SECONDS)
                     .addInterceptor(new CustomResponseInterceptor())
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    /*.addConverterFactory(SimpleXmlConverterFactory.createNonStrict(
                            new Persister(new AnnotationStrategy())))*/
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
