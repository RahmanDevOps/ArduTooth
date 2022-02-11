package zmq.com.ystlibrary.canvas;
import java.util.Calendar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import zmq.com.ystlibrary.R;
import zmq.com.ystlibrary.model.PreAndPost;
import zmq.com.ystlibrary.model.PreAndPostResult;
import zmq.com.ystlibrary.utility.Utility;
import zmq.com.ystlibrary.utility.matloob_dilog.Dialog;

public class PreAndPostActivity extends Activity implements View.OnClickListener {
  int flag=0;
    private TextView tittle;
    TextView ques;

    private RadioGroup rg;
    private RadioButton a1, a2, a3, a4;
    private Button play, next;
    private PreAndPost preAndPost;
    private PreAndPost.PreTestDetails preTestDetails;
    private PreAndPost.PostTestDetails postTestDetails;
    private List<PreAndPost.Question> questionList;
    private PreAndPost.Question question;
    private PreAndPostResult.PreTestScore preTestScore;
    private PreAndPostResult preAndPostResult;
    private PreAndPostResult.PostTestScore postTestScore;
    private List<Integer> question_id;
    private List<String> answer;
    private List<String> option;
    private List<Integer> quesMark;
    public int number = 0;
    private double score = 0;
    private String chooseAnswer = "", correctAnswer;

    private AlertDialog dlg = null;
     int i=0;
    String preStartTime="";
    String preStartDate="";
    String preClosetTime="";
    int preCloseFlag=0;
    int j=0;
    String postStartTime="";
    String postStartDate="";
    String postClosetTime="";
    int postCloseFlag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    /*    Calendar calendar=Calendar.getInstance();
        SimpleDateFormat mdformat=new SimpleDateFormat( "HH:mm:ss" );
        String time="Current Time-->"+mdformat.format( calendar.getTime() );
        System.out.println( time );*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_and_post_text);
        //Date currentTime = Calendar.getInstance().getTime();
        preTestScore = new PreAndPostResult.PreTestScore();
        postTestScore = new PreAndPostResult.PostTestScore();
        preAndPostResult=new PreAndPostResult();
     /*   preTestScore.PreStartDate="";
        preTestScore.PreStartTime="";
        postTestScore.PostStartDate="";
        postTestScore.PostStartTime="";
        preTestScore.PreCloseTime="";
        preTestScore.PreClose=0;
        postTestScore.PostCloseTime="";
        postTestScore.PostClose=0;*/
        init();
        if (getIntent().getExtras().getString("type").equalsIgnoreCase("pre")) {

            if (preAndPost != null && preAndPost.PreTestDetails != null && preAndPost.PreTestDetails.PreTestUrl!=null) {
                preTestDetails = preAndPost.PreTestDetails;

                preStartTime=getIntent().getExtras().getString("preTime");
                preStartDate=getIntent().getExtras().getString("preDate");
                preTestScore.PreStartDate=preStartDate;
                preTestScore.PreStartTime=preStartTime;
                System.out.println( "pre start time >>>>>>>>>>>"+preStartTime );
                System.out.println( "pre start date >>>>>>>>>>>"+preStartDate );

                if(StoryView.lng_code==1)
                {
                    questionList = preAndPost.PreTestDetails.PreTestQuestionE;
                    tittle.setText(preTestDetails.PreTestTitleE);
                }
                else{
                    questionList = preAndPost.PreTestDetails.PreTestQuestionH;
                    tittle.setText(preTestDetails.PreTestTitleH);
                }
               // questionList = preAndPost.PreTestDetails.PreTestQuestion;
                //tittle.setText(preTestDetails.PreTestTitle);
                preTestScore.MiraID = Utility.mira_id;
                preTestScore.GroupID = Utility.group_id;
                preTestScore.PregWomenID = Utility.pregnant_id;
                preTestScore.NonPregWomenID = Utility.non_pregnant_id;
                preTestScore.PreTestID = preTestDetails.PreTestID;
                preTestScore.StoryID = Integer.valueOf(Utility.SubFolderName);
                preTestScore.PregWomenName=Utility.pregnant_name;
                preTestScore.NonPregWomenName=Utility.non_pregnant_name;
                preAndPostResult.ExternalMiraID=0;
                preAndPostResult.ExternalUserID=0;
                System.out.println( "pregnant women name>>>>>>>>>>>>>>>>>>"+ preTestScore.PregWomenName);
                System.out.println( "Nonpregnant women name>>>>>>>>>>>>>>>>>>"+   preTestScore.NonPregWomenName);

            }
        } else if (getIntent().getExtras().getString("type").equalsIgnoreCase("post")) {
            if (preAndPost != null && preAndPost.PostTestDetails != null && preAndPost.PostTestDetails.PostTestUrl != null) {
                postTestDetails = preAndPost.PostTestDetails;

                postStartTime=getIntent().getExtras().getString("postTime");
                postStartDate=getIntent().getExtras().getString("postDate");
                postTestScore.PostStartDate=postStartDate;
                postTestScore.PostStartTime=postStartTime;
                System.out.println( "post start time>>>>>>>>>>>>>>>>"+postTestScore.PostStartTime );
                System.out.println( "post start date>>>>>>>>>>>>>>>>"+ postTestScore.PostStartDate);
                if(StoryView.lng_code==1)
                {
                    questionList = preAndPost.PostTestDetails.PostTestQuestionE;
                    tittle.setText(postTestDetails.PostTestTitleE);
                }
                else{
                    questionList = preAndPost.PostTestDetails.PostTestQuestionH;
                    tittle.setText(postTestDetails.PostTestTitleH);
                }

                //questionList = preAndPost.PostTestDetails.PostTestQuestion;
                //tittle.setText(postTestDetails.PostTestTitle);


                postTestScore.MiraID = Utility.mira_id;
                postTestScore.GroupID = Utility.group_id;
                postTestScore.PregWomenID = Utility.pregnant_id;
                postTestScore.NonPregWomenID = Utility.non_pregnant_id;
                postTestScore.PostTestID = postTestDetails.PostTestID;
                postTestScore.StoryID = Integer.valueOf(Utility.SubFolderName);
                postTestScore.PregWomenName=Utility.pregnant_name;
                postTestScore.NonPregWomenName=Utility.non_pregnant_name;
                preAndPostResult.ExternalMiraID=0;
                preAndPostResult.ExternalUserID=0;
            }
        }
        if (questionList == null) {
            if(StoryView.lng_code==1)
            {
                if (getIntent().getExtras().getString("type").equalsIgnoreCase("pre")) {
                    if(preAndPost.PostTestDetails!=null && preAndPost.PostTestDetails.PostTestQuestionE != null){
                        preTestScore = new PreAndPostResult.PreTestScore();
                        insertPreTest();
                    }
                    startActivity(new Intent(PreAndPostActivity.this, StoryViewActivity.class));
                    finish();
                } else {
                    if(preAndPost.PreTestDetails!=null && preAndPost.PreTestDetails.PreTestQuestionE != null){
                        postTestScore = new PreAndPostResult.PostTestScore();
                        insertPostTest(PreAndPostActivity.this);
                    }
                    finish();
                }
            }
            else{
                if (getIntent().getExtras().getString("type").equalsIgnoreCase("pre")) {
                    if(preAndPost.PostTestDetails!=null && preAndPost.PostTestDetails.PostTestQuestionH != null){
                        preTestScore = new PreAndPostResult.PreTestScore();
                        insertPreTest();
                    }
                    startActivity(new Intent(PreAndPostActivity.this, StoryViewActivity.class));
                    finish();
                } else {
                    if(preAndPost.PreTestDetails!=null && preAndPost.PreTestDetails.PreTestQuestionH != null){
                        postTestScore = new PreAndPostResult.PostTestScore();
                        insertPostTest(PreAndPostActivity.this);
                    }
                    finish();
                }
            }
            /*if (getIntent().getExtras().getString("type").equalsIgnoreCase("pre")) {
                if(preAndPost.PostTestDetails!=null && preAndPost.PostTestDetails.PostTestQuestion != null){
                    preTestScore = new PreAndPostResult.PreTestScore();
                    insertPreTest();
                }
                startActivity(new Intent(PreAndPostActivity.this, StoryViewActivity.class));
                finish();
            } else {
                if(preAndPost.PreTestDetails!=null && preAndPost.PreTestDetails.PreTestQuestion != null){
                    postTestScore = new PreAndPostResult.PostTestScore();
                    insertPostTest(PreAndPostActivity.this);
                }
                finish();
            }*/
        } else {
            showQuestion();
        }
    }

    public void setValue(){
        postTestScore = new PreAndPostResult.PostTestScore();
        question_id = new ArrayList<>();
        answer = new ArrayList<>();
        option = new ArrayList<>();
        quesMark=new ArrayList<>();

    }

    private void init() {
        rg = findViewById(R.id.radio_group);
        tittle = findViewById(R.id.tittle);
        ques = findViewById(R.id.question);
        a1 = findViewById(R.id.a1);
        a1.setOnClickListener(this);
        a2 = findViewById(R.id.a2);
        a2.setOnClickListener(this);
        a3 = findViewById(R.id.a3);
        a3.setOnClickListener(this);
        a4 = findViewById(R.id.a4);
        a4.setOnClickListener(this);
        play = findViewById(R.id.play_question);
        play.setOnClickListener(this);
        next = findViewById(R.id.next);
        next.setOnClickListener(this);

        String destinationFolder = new File(Environment.getExternalStorageDirectory() + "/STORY/" + Utility.SubFolderName + "/PreAndPostTest.txt").getAbsolutePath();
        FileInputStream fileInp = null;
        try {
            fileInp = new FileInputStream(destinationFolder);
            ObjectInputStream objectOut = new ObjectInputStream(fileInp);
            preAndPost = (PreAndPost) objectOut.readObject();
            objectOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            preAndPost = new PreAndPost();
        }

        question_id = new ArrayList<>();
        answer = new ArrayList<>();
        option = new ArrayList<>();
        quesMark=new ArrayList<>();
    }

    private void showQuestion() {
        question = questionList.get(number);
        ques.setText("Q." + (number + 1) + question.QuestionText);
        a1.setText(question.OptA);
        a2.setText(question.OptB);
        a3.setText(question.OptC);
        //System.out.println( "Chand==========================>>>"+question.OptC );
       // System.out.println( "Chand==========================>>>"+question.OptD );
        if(question.OptC == null  ||question.OptC==""  )
            a3.setVisibility( View.INVISIBLE );
        a4.setText(question.OptD);
        if(question.OptD == null  ||question.OptD==""  )
        a4.setVisibility( View.INVISIBLE );
        correctAnswer = question.RightAnswer;
        //new code add

        if(flag==0)
        {
            flag++;
            chooseAnswer=a1.getText().toString();
            opt = "OptA";
            String destinationFolder;
            if(preTestDetails != null){
                destinationFolder = new File(Environment.getExternalStorageDirectory() + "/STORY/" + Utility.SubFolderName + "/PreTest/"+preTestDetails.PreTestID+"/"+question.QuestionID+".mp3").getAbsolutePath();

            }else {
                destinationFolder = new File(Environment.getExternalStorageDirectory() + "/STORY/" + Utility.SubFolderName + "/PostTest/"+postTestDetails.PostTestID+"/"+question.QuestionID+".mp3").getAbsolutePath();
            }
            stopSound();
            playSound(this,destinationFolder);
        }
        else if(flag==1)
        {
            flag++;
            chooseAnswer = a2.getText().toString();
            opt = "OptB";
            String destinationFolder;
            if(preTestDetails != null){
                destinationFolder = new File(Environment.getExternalStorageDirectory() + "/STORY/" + Utility.SubFolderName + "/PreTest/"+preTestDetails.PreTestID+"/"+question.QuestionID+".mp3").getAbsolutePath();

            }else {
                destinationFolder = new File(Environment.getExternalStorageDirectory() + "/STORY/" + Utility.SubFolderName + "/PostTest/"+postTestDetails.PostTestID+"/"+question.QuestionID+".mp3").getAbsolutePath();
            }
            stopSound();
            playSound(this,destinationFolder);
        }
        else if(flag==2)
        {
            flag=0;
            chooseAnswer = a3.getText().toString();
            opt = "OptC";
            String destinationFolder;
            if(preTestDetails != null){
                destinationFolder = new File(Environment.getExternalStorageDirectory() + "/STORY/" + Utility.SubFolderName + "/PreTest/"+preTestDetails.PreTestID+"/"+question.QuestionID+".mp3").getAbsolutePath();

            }else {
                destinationFolder = new File(Environment.getExternalStorageDirectory() + "/STORY/" + Utility.SubFolderName + "/PostTest/"+postTestDetails.PostTestID+"/"+question.QuestionID+".mp3").getAbsolutePath();
            }
            stopSound();
            playSound(this,destinationFolder);
        }

    }
    private String opt;
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.a1) {
            chooseAnswer = a1.getText().toString();
            opt = "OptA";
        } else if (v.getId() == R.id.a2) {
            chooseAnswer = a2.getText().toString();
            opt = "OptB";
        } else if (v.getId() == R.id.a3) {
            chooseAnswer = a3.getText().toString();
            opt = "OptC";
        } else if (v.getId() == R.id.a4) {
            chooseAnswer = a4.getText().toString();
            opt = "OptD";
        } else if (v.getId() == R.id.next) {
            if(a1.isChecked() || a2.isChecked() || a3.isChecked() || a4.isChecked()) {
                stopSound();
                question_id.add(question.QuestionID);
                answer.add(chooseAnswer);
                option.add(opt);
           /*     if (chooseAnswer.equalsIgnoreCase(correctAnswer)) {
                    if (preTestDetails != null) {
                        score += preTestDetails.EachQuestionMark;
                    } else {
                        score += postTestDetails.EachQuestionMark;
                    }
                }*/
                if (chooseAnswer.equalsIgnoreCase(correctAnswer)) {
                    quesMark.add(5);
                    if (preTestDetails != null) {
                        score += preTestDetails.EachQuestionMark;
                        System.out.println(">>>>>>>>>>right pre Score===="+ score );
                    } else {
//
                        score += postTestDetails.EachQuestionMark;
                        System.out.println(">>>>>>>>>>right post Score===="+ score );
                    }
                }
              else
                {
                    quesMark.add(0);
                    System.out.println(">>>>>>>>>> wrong Score===="+ score );
                }

                if (++number < questionList.size()) {
                    rg.clearCheck();
                    showQuestion();
                } else {


                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    View view = getLayoutInflater().inflate(R.layout.score_dialog, null, false);
                    NumberFormat formatter = new DecimalFormat("#0.00");
                    ((TextView) view.findViewById(R.id.score)).setText(formatter.format(score));
                    ((Button) view.findViewById(R.id.ok_button)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getIntent().getExtras().getString("type").equalsIgnoreCase("pre")) {
                                 preClosetTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                preCloseFlag=1;
                                preTestScore.PreCloseTime=preClosetTime;
                                preTestScore.PreClose=preCloseFlag;

                                System.out.println( "PreClose time>>>>>>>>"+preTestScore.PreCloseTime);
                                System.out.println( "PreClose flag>>>>>>>>"+preTestScore.PreClose);
                              /*  for(int mark=0;mark<3;mark++)
                                {
                                    System.out.println( "pre MArk Per Question>>>>>>>>>> "+markOfSelectAnsPre[mark] );
                                }*/
                                preTestScore.Score = score;
                                insertPreTest();
                                builder.create().dismiss();
                                startActivity(new Intent(PreAndPostActivity.this, StoryViewActivity.class));
                                finish();


                            } else {
                                 postClosetTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                 postCloseFlag=1;
                                 postTestScore.PostCloseTime=postClosetTime;
                                 postTestScore.PostClose=postCloseFlag;
                                System.out.println( "postclose time>>>>>>>>"+ postTestScore.PostCloseTime);
                                System.out.println( "postclose flag>>>>>>>>"+  postTestScore.PostClose);
                               /* for(int mark=0;mark<3;mark++)
                                {
                                    System.out.println( "post MArk Per Question>>>>>>>>>> "+markOfSelectAnsPost[mark] );
                                }*/
                                postTestScore.Score = score;
                                insertPostTest(PreAndPostActivity.this);
                                builder.create().dismiss();
                                finish();
                            }
                        }
                    });
                    builder.setCancelable(false);
                    builder.setView(view);
                    dlg = builder.show();


                }
            }else {
                Toast.makeText(getApplicationContext(),"Select anyone option.",Toast.LENGTH_SHORT).show();
            }
        }else if(v.getId() == R.id.play_question){
            String destinationFolder;
            if(preTestDetails != null){
                destinationFolder = new File(Environment.getExternalStorageDirectory() + "/STORY/" + Utility.SubFolderName + "/PreTest/"+preTestDetails.PreTestID+"/"+question.QuestionID+".mp3").getAbsolutePath();

            }else {
                destinationFolder = new File(Environment.getExternalStorageDirectory() + "/STORY/" + Utility.SubFolderName + "/PostTest/"+postTestDetails.PostTestID+"/"+question.QuestionID+".mp3").getAbsolutePath();
            }
            stopSound();
            playSound(this,destinationFolder);
        }
    }

    public void insertPostTest(Context context) {
        List<PreAndPostResult.PostTestScore> postListJson;
        Gson gson = new Gson();
        String Json = Utility.readPreference(context, "POSTTEST");
        if (Json == null){
            postListJson = new ArrayList<>();
        }else {
            postListJson = gson.fromJson(Json, new TypeToken<List<PreAndPostResult.PostTestScore>>() {

            }.getType());
        }
        postListJson.add(postTestScore);
        String json = gson.toJson(postListJson);
        Utility.writeInPreference(context, "POSTTEST", json);

        List<List<Integer>> qLists;
        Json = Utility.readPreference(context, "POSTQUES");
        if (Json == null){
            qLists = new ArrayList<>();
        }else {
            qLists = gson.fromJson(Json, new TypeToken<List<List<Integer>>>() {
            }.getType());
        }
        qLists.add(question_id);
        json = gson.toJson(qLists);
        Utility.writeInPreference(context, "POSTQUES", json);

        List<List<String>> ansLists;
        gson = new Gson();
        Json = Utility.readPreference(context, "POSTANS");
        if (Json == null){
            ansLists = new ArrayList<>();
        }else {
            ansLists = gson.fromJson(Json, new TypeToken<List<List<String>>>() {
            }.getType());
        }
        ansLists.add(answer);
        json = gson.toJson(ansLists);
        Utility.writeInPreference(context, "POSTANS", json);

        List<List<String>> optLists;
        Json = Utility.readPreference(context, "POSTOPT");
        if (Json == null){
            optLists = new ArrayList<>();
        }else {
            optLists = gson.fromJson(Json, new TypeToken<List<List<String>>>() {
            }.getType());
        }
        optLists.add(option);
        json = gson.toJson(optLists);
        Utility.writeInPreference(context, "POSTOPT", json);
        ///

        List<List<Integer>> quetionwiseMrk;
        gson = new Gson();
        Json = Utility.readPreference(context, "PostMarkQuestionwise");
        if (Json == null){
            quetionwiseMrk = new ArrayList<>();
        }else {
            quetionwiseMrk = gson.fromJson(Json, new TypeToken<List<List<Integer>>>() {
            }.getType());
        }
        quetionwiseMrk.add(quesMark);
        json = gson.toJson(quetionwiseMrk);
        Utility.writeInPreference(context, "PostMarkQuestionwise", json);


    }

    private void insertPreTest() {

        List<PreAndPostResult.PreTestScore> preListJson;
        Gson gson = new Gson();
        String Json = Utility.readPreference(PreAndPostActivity.this, "PRETEST");
        if (Json == null){
            preListJson = new ArrayList<>();
        }else {
            preListJson = gson.fromJson(Json, new TypeToken<List<PreAndPostResult.PreTestScore>>() {
            }.getType());
        }
        preListJson.add(preTestScore);
        String json = gson.toJson(preListJson);
        Utility.writeInPreference(PreAndPostActivity.this, "PRETEST", json);

        List<List<Integer>> qLists;
        Json = Utility.readPreference(PreAndPostActivity.this, "PREQUES");
        if (Json == null){
            qLists = new ArrayList<>();
        }else {
            qLists = gson.fromJson(Json, new TypeToken<List<List<Integer>>>() {
            }.getType());
        }
        qLists.add(question_id);
        json = gson.toJson(qLists);
        Utility.writeInPreference(PreAndPostActivity.this, "PREQUES", json);

        List<List<String>> ansLists;
        gson = new Gson();
        Json = Utility.readPreference(PreAndPostActivity.this, "PREANS");
        if (Json == null){
            ansLists = new ArrayList<>();
        }else {
            ansLists = gson.fromJson(Json, new TypeToken<List<List<String>>>() {
            }.getType());
        }
        ansLists.add(answer);
        json = gson.toJson(ansLists);
        Utility.writeInPreference(PreAndPostActivity.this, "PREANS", json);

        List<List<String>> optLists;
        Json = Utility.readPreference(PreAndPostActivity.this, "PREOPT");
        if (Json == null){
            optLists = new ArrayList<>();
        }else {
            optLists = gson.fromJson(Json, new TypeToken<List<List<String>>>() {
            }.getType());
        }
        optLists.add(option);
        json = gson.toJson(optLists);
        Utility.writeInPreference(PreAndPostActivity.this, "PREOPT", json);
        //
        List<List<Integer>> quetionwiseMrk;
        gson = new Gson();
        Json = Utility.readPreference(PreAndPostActivity.this, "PreMarkQuestionwise");
        if (Json == null){
            quetionwiseMrk = new ArrayList<>();
        }else {
            quetionwiseMrk = gson.fromJson(Json, new TypeToken<List<List<Integer>>>() {
            }.getType());
        }
        quetionwiseMrk.add(quesMark);
        json = gson.toJson(quetionwiseMrk);
        Utility.writeInPreference(PreAndPostActivity.this, "PreMarkQuestionwise", json);
    }


    public static MediaPlayer mediaPlayer = null;

    public static void playSound(Context context,final String path) {
        System.out.println("Path====>>"+path);
        File file = new File(path);
        if (file.exists()) {
            stopSound();
            Uri uri= Uri.parse(path);
            mediaPlayer=MediaPlayer.create(context,uri);
            mediaPlayer.start();
        }else {

            Toast.makeText(context,"File does not Exit.",Toast.LENGTH_SHORT).show();
        }
    }

    public static void stopSound() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        try {
            if(dlg!=null)
                dlg.dismiss();
        }catch (Exception e){

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopSound();


    }
}
