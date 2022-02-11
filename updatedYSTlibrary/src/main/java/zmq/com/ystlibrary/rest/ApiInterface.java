package zmq.com.ystlibrary.rest;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import zmq.com.ystlibrary.model.PreAndPost;
import zmq.com.ystlibrary.model.StoryListJson;
import zmq.com.ystlibrary.model.StoryXML;

public interface ApiInterface {
    @GET("storyList?")
    Call<List<StoryListJson>> getStoriesListJson(@Query("u_id") String u_id, @Query("status") String status);
    @GET()
    @Streaming
    Call<ResponseBody> downloadZipRange(@Url String zipUrl ,@Header("Range") String contentRange);

    @GET("AndroidService/getStoryID/{id}")
    Call<StoryXML> getStoryById(@Path("id") String storyId);

    @POST
    Call<PreAndPost> getPreAndPost(@Url String fileUrl , @Query("StoryID") int story_id);

    @GET()
    @Streaming
    Call<ResponseBody> downloadQuesAudioZip(@Url String zipUrl);
}
