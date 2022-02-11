package zmq.com.ystlibrary.canvas;

import static androidx.recyclerview.widget.RecyclerView.HORIZONTAL;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import zmq.com.ystlibrary.R;
import zmq.com.ystlibrary.databinding.MainBinding;
import zmq.com.ystlibrary.download.Download;
import zmq.com.ystlibrary.download.DownloadService;
import zmq.com.ystlibrary.download.LanguageAdapter;
import zmq.com.ystlibrary.download.PostAdapter;
import zmq.com.ystlibrary.model.StoryListJson;
import zmq.com.ystlibrary.model.StoryXML;
import zmq.com.ystlibrary.rest.ApiClient;
import zmq.com.ystlibrary.rest.ApiInterface;
import zmq.com.ystlibrary.rest.OnConnectionTimeoutListener;
import zmq.com.ystlibrary.utility.GlobalVariables;
import zmq.com.ystlibrary.utility.TestDialog;
import zmq.com.ystlibrary.utility.Utility;
import zmq.com.ystlibrary.utility.matloob_dilog.XMLParser;

public class DownloadResourceActivity extends Activity implements
        PostAdapter.PostsAdapterListener, SwipeRefreshLayout.OnRefreshListener
{
    public static final String MESSAGE_PROGRESS = "message_progress";
    public static final String EXCEPTION_UNZIP_PROGRESS = "exception_unzip_progress";
    public static final String EXCEPTION_ZIP_PROGRESS = "exception_zip_progress";
    private RecyclerView recyclerView;
    private static MainBinding mainBinding;
    private PostAdapter postsAdapter;
    private String storyListToken = "1";
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_download_resource, null, false);
        setContentView(mainBinding.getRoot());

        storyListToken = getIntent().getStringExtra("tokenId").toString();
        System.out.println( "TokenID============>>>>>"+storyListToken );
        storyListToken = "301";


        Utility.pregnant_id = getIntent().getIntExtra("pregnant_id",0);
        Utility.non_pregnant_id = getIntent().getIntExtra("non_pregnant_id",0);
        Utility.group_id = getIntent().getIntExtra("group_id",0);
        Utility.mira_id = getIntent().getIntExtra("mira_id",0);
        Utility.pregnant_name=getIntent().getExtras().getString( "pregnant_name" ,null);
        Utility.non_pregnant_name=getIntent().getExtras().getString( "non_pregnant_name" ,null);
        Utility.ExternalMiraID=getIntent().getIntExtra("ExternalMiraId",0);
        Utility.ExternalUserID=getIntent().getIntExtra("ExternalUserId",0);
        TestDialog testDialog = new TestDialog();
        testDialog.dialogVisibility = true;
        mainBinding.setVm(testDialog);

        swipeRefreshLayout = mainBinding.swipeLayout;
        chkPermissions();
        initRecyclerView();
        swipeRefreshLayout.setOnRefreshListener(this);

        registerReceiver();
        printSecreenInfo();

    }
    private void chkPermissions(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET
                };

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        System.out.println("onRequestPermissionsResult...");
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Permission READ_PHONE_STATE Granted...");
                } else {
                    System.out.println("Permission READ_PHONE_STATE deny...");
                }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onResume() {
        super.onResume();

        mainBinding.header.setVisibility(View.VISIBLE);
        mainBinding.relativeBody.setVisibility(View.VISIBLE);
        mainBinding.frameDownloadPlay.setVisibility(View.GONE);
        StoryView.lng_code = 0;

        if(BaseSurface.isFinish){
            finish();
            BaseSurface.isFinish=false;
        }
    }

    @Override
    public void onRefresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadStoriesList();
            }
        }).start();
    }

    private void initRecyclerView() {
        recyclerView = mainBinding.storiesRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, HORIZONTAL);
        recyclerView.addItemDecoration(itemDecor);
        getStories();
    }

    private List<StoryListJson.Data> getResAvailable(List<StoryListJson.Data> dataList) {
        File sdcard = Environment.getExternalStorageDirectory();
        for (StoryListJson.Data data : dataList) {
            File dir = new File(sdcard.getAbsolutePath() + "/" + Utility.FOLDER_NAME + "/" + data.story_id);
            if (dir.exists()) {
                data.status = true;
            } else {
                data.status = false;
            }
        }

       // Collections.reverse(dataList);

        return dataList;
    }

    private void getStoriesFromPreference() {
        Gson gson = new Gson();
        String Json = Utility.readPreference(DownloadResourceActivity.this, Utility.STORYLISTJSONKEY);
        if (Json == null) return;
        List<StoryListJson> storyListJson = gson.fromJson(Json, new TypeToken<List<StoryListJson>>() {
        }.getType());

        if (storyListJson.get(0).data.size() > 1) {
            postsAdapter = new PostAdapter(this, getResAvailable(storyListJson.get(0).data), DownloadResourceActivity.this);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setAdapter(postsAdapter);
                    hideDialog();
                }
            });

        } else {
            Utility.SubFolderName = getResAvailable(storyListJson.get(0).data).get(0).story_id;
            loadStoryXml();
        }
    }

    private void getStories() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Utility.readPreference(DownloadResourceActivity.this, Utility.STORYLISTJSONKEY) == null) {
                    loadStoriesList();

                }
                getStoriesFromPreference();


            }
        }).start();
    }

    OnConnectionTimeoutListener onConnectionTimeoutListener = new OnConnectionTimeoutListener() {
        public void onConnectionTimeout() {
            stopService(new Intent(DownloadResourceActivity.this, DownloadService.class));
            hideDialog();
            mainBinding.txtProgress.setText("Wait...");
            mainBinding.progress.setProgress(0);
        }
    };


    public void startDownload() {
        showDialog();
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra("zipurl",zipURL);
        startService(intent);

    }

    private void registerReceiver() {

        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        intentFilter.addAction(EXCEPTION_ZIP_PROGRESS);
        intentFilter.addAction(EXCEPTION_UNZIP_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);

    }

    private void unregisterReceiver() {
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        bManager.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        unregisterReceiver();
    }

    private void updatePrefAfterDownloading(){
        List<StoryListJson.Data>data=postsAdapter.getPostList();
        for(StoryListJson.Data data1:data){
            if(data1.story_id.equals(dummy_storyId)){
                data1.status=true;
                postsAdapter.setPostList(data);
                postsAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case MESSAGE_PROGRESS:

                    Download download = intent.getParcelableExtra("download");
                    mainBinding.progress.setProgress(download.getProgress());
                    if (download.getProgress() == 100) {

                        mainBinding.txtProgress.setText("File Download Complete");
                        stopService(new Intent(DownloadResourceActivity.this, DownloadService.class));
                        hideDialog();
                        mainBinding.txtProgress.setText("Wait...");
                        mainBinding.progress.setProgress(0);

                        mainBinding.btnDownload.setVisibility(View.GONE);
                        mainBinding.btnPlay.setVisibility(View.VISIBLE);
                        updatePrefAfterDownloading();

                    } else {
                        mainBinding.txtProgress.setText(String.format(" (%d/%d) MB", download.getCurrentFileSize(), download.getTotalFileSize()));

                    }
                    break;

                case EXCEPTION_ZIP_PROGRESS:
                    onConnectionTimeoutListener.onConnectionTimeout();

                    break;

                case EXCEPTION_UNZIP_PROGRESS:
                    onConnectionTimeoutListener.onConnectionTimeout();
                    File outputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Utility.FOLDER_NAME + "/Zip", Utility.SubFolderName + ".zip");
                    if (outputFile.exists()) {
                        outputFile.delete();
                    }

                    String destinationFolder = new File(Environment.getExternalStorageDirectory() + "/" + Utility.FOLDER_NAME + "/" + Utility.SubFolderName).getAbsolutePath();
                    File directory = new File(destinationFolder);
                    if (directory.exists()) {
                        directory.delete();
                    }

                    break;
            }
        }
    };

    private static String dummy_storyId,zipURL;
    public static class MyClickHandler {
        Context context;

        public MyClickHandler(Context context) {
            this.context = context;
        }

        public void onResourceClick() {
            Toast.makeText(context, "onResourceClick", Toast.LENGTH_SHORT).show();
        }

        public void onStoryViewClick() {
            Toast.makeText(context, "onStoryViewClick", Toast.LENGTH_SHORT).show();
        }

        public void onStoryItemClick(StoryListJson.Data data) {
            System.out.println("**** onStoryItemClick" + data);
            dummy_storyId=data.story_id;
            zipURL=data.zipurl;

            Utility.SubFolderName = data.story_id;
            mainBinding.header.setVisibility(View.GONE);
            mainBinding.relativeBody.setVisibility(View.GONE);
            mainBinding.frameDownloadPlay.setVisibility(View.VISIBLE);
            if (data.status) {
                mainBinding.btnPlay.setVisibility(View.VISIBLE);
                mainBinding.btnDownload.setVisibility(View.GONE);
            } else {
                mainBinding.btnPlay.setVisibility(View.GONE);
                mainBinding.btnDownload.setVisibility(View.VISIBLE);
            }
            setLanguageAdapter(context, data);
        }

    }

    @Override
    public void onPostClicked(StoryListJson.Data post) {

    }

    @Override
    public void onBackPressed() {
        if (mainBinding.frameDownloadPlay.getVisibility() == View.VISIBLE) {
            mainBinding.header.setVisibility(View.VISIBLE);
            mainBinding.relativeBody.setVisibility(View.VISIBLE);
            mainBinding.frameDownloadPlay.setVisibility(View.GONE);
            StoryView.lng_code = 0;
        } else {
            super.onBackPressed();
        }
    }


    private void loadStoriesList() {
        try {
            Gson gson = new Gson();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<List<StoryListJson>> call = apiService.getStoriesListJson(storyListToken, "2");
            List<StoryListJson> storyListJsons = call.execute().body();
            String json = gson.toJson(storyListJsons);
            Utility.writeInPreference(DownloadResourceActivity.this, Utility.STORYLISTJSONKEY, json);
            System.out.println( "My Jason>>>>>>>>>>>>>>>>"+json );

        } catch (Exception e) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideDialog();
                    new AlertDialog.Builder(DownloadResourceActivity.this)
                            .setTitle("Network Error")
                            .setMessage(" No internet connectivity. Please try after some time.")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
            });
            e.printStackTrace();
        }

        if (swipeRefreshLayout.isRefreshing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getStoriesFromPreference();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }


    private void loadStoryXml() {

        if (!Utility.isFileExist(dummy_storyId+".xml")) {
            startDownload();

        } else {
            loadStoryResources();
        }
    }

    private void hideDialog() {
        mainBinding.getVm().setDialogVisibility(false);
        mainBinding.setVm(mainBinding.getVm());

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        isTimer=false;
        timerVal=0;
    }

    private int timerVal=0;
    private boolean isTimer=false;
    private void showDialog() {
        isTimer=true;
        mainBinding.getVm().setDialogVisibility(true);
        mainBinding.setVm(mainBinding.getVm());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(isTimer){
                        try {
                            Thread.sleep(1000);
                            timerVal++;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                 // mainBinding.txtTimer.setText("Time : "+timerVal);
                                    mainBinding.txtTimer.setText("");
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

    }

    private void loadStoryResources() {
        boolean isRes=true;
        try {
            byte[] bytes =Utility.getResourcesFromSD(Utility.SubFolderName+".xml");
            InputStream targetStream = new ByteArrayInputStream(bytes);
            StoryXML storyXML = XMLParser.xlmParsing(targetStream);

            String fileExtension=".png";
            for (StoryXML.Character character : storyXML.characters) {
                if(character.name.contains(".png")){
                    fileExtension="";
                }
                else fileExtension=".png";
                if (  !Utility.isFileExist(character.name + fileExtension)) {
                    isRes=false;
                }
            }
            for (StoryXML.Background background : storyXML.backgrounds) {
                if(background.name.contains(".png")){
                    fileExtension="";
                }
                else fileExtension=".png";

                if ( !Utility.isFileExist(background.name + fileExtension)) {
                    isRes=false;
                }
            }
            for (StoryXML.Audio audio : storyXML.audios) {
                if ( !Utility.isFileExist(audio.name)) {
                    isRes=false;
                }
            }

        } catch (Exception e) {
            hideDialog();
            e.printStackTrace();
        }

        if(!isRes){
            String destinationFolder = new File(Environment.getExternalStorageDirectory() + "/" + Utility.FOLDER_NAME + "/" + Utility.SubFolderName).getAbsolutePath();
            File directory = new File(destinationFolder);
            if (directory.exists()) {
                directory.delete();
            }

            startDownload();
        }
        else{
            hideDialog();
            Intent intent = new Intent(DownloadResourceActivity.this, PreAndPostActivity.class);
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
            intent.putExtra("type","pre");
            intent.putExtra( "preTime", currentTime);
            intent.putExtra( "preDate",currentDate );
            startActivity(intent);

        }
    }

    private static LanguageAdapter languageAdapter;

    private static void setLanguageAdapter(Context context, StoryListJson.Data data) {
        languageAdapter = new LanguageAdapter(data.languages, new LanguageAdapter.PostsAdapterListener() {
            @Override
            public void onPostClicked(StoryListJson.Language language, int code) {
                StoryView.lng_code = code;
                /*Toast.makeText(DownloadResourceActivity.this,language.language_code+" "+code,Toast.LENGTH_SHORT).show();*/
            }
        });
        mainBinding.recyclerLang.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true));
        mainBinding.recyclerLang.setAdapter(languageAdapter);
        mainBinding.setData(data);

    }

    public void onPlay(View view) {
        if (!mainBinding.getVm().getDialogVisibility()) {
            loadStoryXml();
        }
    }

    public void onDownload(View view) {
        if (!mainBinding.getVm().getDialogVisibility()) {
            startDownload();
        }
    }

    void printSecreenInfo() {
        GlobalVariables.getResource = getResources();
        GlobalVariables.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        GlobalVariables.paint.setTextSize(15);
        GlobalVariables.paint.setColor(Color.WHITE);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        float a = metrics.densityDpi;
        GlobalVariables.xScale_factor = (float) (metrics.widthPixels / 480.0);
        GlobalVariables.yScale_factor = (float) (metrics.heightPixels / 800.0);
        GlobalVariables.width = metrics.widthPixels;
        GlobalVariables.height = metrics.heightPixels;
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.FROYO) {

        } else {

        }

    }
}
