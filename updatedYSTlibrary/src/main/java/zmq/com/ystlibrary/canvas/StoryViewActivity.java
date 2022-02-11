package zmq.com.ystlibrary.canvas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RatingBar;

import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import zmq.com.ystlibrary.R;
import zmq.com.ystlibrary.model.PreAndPost;
import zmq.com.ystlibrary.utility.Utility;

public class StoryViewActivity extends Activity {
    private BaseSurface baseSurface;
    private StoryView storyView;
    public boolean isRecreate=false;
    private MediaPlayer mediaPlayerLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new StoryView(this));

        if(mediaPlayerLocal!=null) {
            if ((!mediaPlayerLocal.isPlaying())){
                mediaPlayerLocal.stop();
                mediaPlayerLocal.release();
                mediaPlayerLocal=null;
            }
        }
    }
    public void setMediaPlayer(MediaPlayer mediaPlayer){
        mediaPlayerLocal =mediaPlayer;
    }
    public MediaPlayer getMediaPlayerLocal() {
        return mediaPlayerLocal;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onResume() {
        super.onResume();
        baseSurface= (BaseSurface) findViewById(1234);

        if(baseSurface!=null) {
            if (isRecreate && baseSurface.gameThread == null) {
                baseSurface.setHolder();
            }
        }
        else{
            System.out.println("baseSurface null..");
        }

        if(mediaPlayerLocal!=null) {
            if ((!mediaPlayerLocal.isPlaying())){
                mediaPlayerLocal.seekTo(mediaPlayerLocal.getCurrentPosition());
                mediaPlayerLocal.start();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaPlayerLocal !=null){
            if(mediaPlayerLocal.isPlaying()){
                mediaPlayerLocal.pause();
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        isRecreate=true;
        Log.d("MyTag", "" + getClass().getSimpleName() + " onStop.........");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MyTag", "" + getClass().getSimpleName() + " onDestroy.........");
        if(mediaPlayerLocal !=null){
            if(mediaPlayerLocal.isPlaying()){
                mediaPlayerLocal.stop();
                mediaPlayerLocal.release();
                mediaPlayerLocal =null;
            }
        }
    }
    public void showRatingDialog(){
        LayoutInflater factory = LayoutInflater.from(this);
        final View deleteDialogView = factory.inflate(
                R.layout.rating_dialog, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(deleteDialogView);
        final RatingBar ratingBar=deleteDialogView.findViewById(R.id.dialog_ratingbar);
        deleteDialogView.findViewById(R.id.rank_dialog_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
                deleteDialog.cancel();
                System.out.println("Rating..."+ratingBar.getRating());
                baseSurface.isBookMarkStory=false;

                baseSurface.isFinish=true;
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                Intent intent = new Intent(StoryViewActivity.this, PreAndPostActivity.class);
                intent.putExtra("type","post");
                intent.putExtra( "postDate",currentDate );
                intent.putExtra( "postTime",currentTime );
                startActivity(intent);
            }
        });
        deleteDialog.setCancelable(false);
        deleteDialog.show();
    }

    public void showExitDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Do you want to exit");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        baseSurface.isFinish=true;

                        Intent intent = new Intent(StoryViewActivity.this, PreAndPostActivity.class);
                        intent.putExtra("type","post");
                        startActivity(intent);
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        baseSurface.isFinish=false;
                        BaseSurface.isBack=true;

                        Intent intent = new Intent(StoryViewActivity.this, PreAndPostActivity.class);
                        intent.putExtra("type","post");
                        startActivity(intent);
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onBackPressed() {
        PreAndPost preAndPost = null;
        String destinationFolder = new File(Environment.getExternalStorageDirectory() + "/STORY/" + Utility.SubFolderName + "/PreAndPostTest.txt").getAbsolutePath();
        FileInputStream fileInp = null;
        try {
            fileInp = new FileInputStream(destinationFolder);
            ObjectInputStream objectOut = new ObjectInputStream(fileInp);
            preAndPost = (PreAndPost) objectOut.readObject();
            objectOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(StoryView.lng_code==0)
        {
            if(preAndPost!=null && preAndPost.PreTestDetails.PreTestQuestionE!=null){
                PreAndPostActivity preAndPostActivity = new PreAndPostActivity();
                preAndPostActivity.setValue();
                preAndPostActivity.insertPostTest(this);
            }
        }
        else{
            if(preAndPost!=null && preAndPost.PreTestDetails.PreTestQuestionH!=null){
                PreAndPostActivity preAndPostActivity = new PreAndPostActivity();
                preAndPostActivity.setValue();
                preAndPostActivity.insertPostTest(this);
            }
        }

        super.onBackPressed();
    }
}
