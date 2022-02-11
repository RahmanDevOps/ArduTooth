package zmq.com.ystlibrary.utility.matloob_dilog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * updated by ZMQ501 on Feb/4/2022.
 */

public class DialogConstant {
    public static SharedPreferences sharedPreferences;
    public static final String PREFERENCES = "dialogue" ;
    public static int FIRSTLOGIN = 0;
    public static int LANGUAGE = 0;
    public static int LINE = 0;
    public static int ALIGNMENT = 0;
    public static int FONT = 0;
    public static int PRINT = 0;
    public static int X = 20;
    public static int Y = 20;
    public static float WIDTH = 0;
    public static float HEIGHT = 0;
    public static int WIDTH_CANVAS = 0;
    public static int HEIGHT_CANVAS = 0;
    public static float SCALE_FACTOR_X = 0;
    public static float SCALE_FACTOR_Y = 0;
    public static float DIALOGUE_WIDTH_TEMP = 0;
    public static float DIALOGUE_HEIGHT_TEMP = 0;
    public static float DIALOGUE_WIDTH = 0;
    public static float DIALOGUE_HEIGHT = 0;
    public static float DIALOGUE_WIDTH_SMALL = 0;
    public static float DIALOGUE_HEIGHT_SMALL = 0;
    public static float STRING_RECT_WIDTH = 0;
    public static float STRING_RECT_HEIGHT = 0;
    public static float STRING_RECT_SPACE = 0;
    public static float STRING_LINE_SPACE = 0;
    public static float STRING_LINE_NUMBER = 0;


    /*For Bubble Rectangle*/

    public static int X_bubble = 40;
    public static int Y_bubble = 40;
    public static float WIDTH_bubble = 0;
    public static float HEIGHT_bubble = 0;
    public static float DIALOGUE_WIDTH_TEMP_bubble = 0;
    public static float DIALOGUE_HEIGHT_TEMP_bubble = 0;
    public static float DIALOGUE_WIDTH_bubble = 0;
    public static float DIALOGUE_HEIGHT_bubble = 0;
    public static float DIALOGUE_WIDTH_SMALL_bubble = 0;
    public static float DIALOGUE_HEIGHT_SMALL_bubble = 0;
    public static float STRING_RECT_WIDTH_bubble = 0;
    public static float STRING_RECT_HEIGHT_bubble = 0;
    public static float STRING_RECT_SPACE_bubble = 0;
    public static float STRING_LINE_SPACE_bubble = 0;
    public static float STRING_LINE_NUMBER_bubble = 0;
    public static float NumberOfCircle_X = 15;
    public static float NumberOfCircle_Y = 5;
    public static int MAX_RADIUS = 0;
    public static int MIN_RADIUS = 0;
    public static int MAX_ARCH_WIDTH = 0;
    public static int MIN_ARCH_WIDTH = 0;

    public static void initializeConstant(Context context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)(context)).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DialogConstant.HEIGHT_CANVAS = displayMetrics.heightPixels;
        DialogConstant.WIDTH_CANVAS = displayMetrics.widthPixels;
        DialogConstant.SCALE_FACTOR_X = DialogConstant.WIDTH_CANVAS/480;
        DialogConstant.SCALE_FACTOR_Y = DialogConstant.HEIGHT_CANVAS/800;
        DialogConstant.DIALOGUE_HEIGHT_TEMP = (float)(displayMetrics.heightPixels * 0.20);
        DialogConstant.DIALOGUE_WIDTH_TEMP = displayMetrics.widthPixels;
        DialogConstant.WIDTH = DialogConstant.DIALOGUE_WIDTH_TEMP - (2*DialogConstant.X*DialogConstant.SCALE_FACTOR_X);
        DialogConstant.DIALOGUE_WIDTH = DialogConstant.DIALOGUE_WIDTH_TEMP - (DialogConstant.X*DialogConstant.SCALE_FACTOR_X);
        DialogConstant.HEIGHT = DialogConstant.DIALOGUE_HEIGHT_TEMP - (2*DialogConstant.Y*DialogConstant.SCALE_FACTOR_Y);
        DialogConstant.DIALOGUE_HEIGHT = DialogConstant.DIALOGUE_HEIGHT_TEMP - (DialogConstant.Y*DialogConstant.SCALE_FACTOR_Y);
        Log.d("Splash Activity","Width of dialogue "+DialogConstant.DIALOGUE_WIDTH_TEMP+"  "+DialogConstant.DIALOGUE_WIDTH+"  "+DialogConstant.WIDTH);
        Log.d("Splash Activity","Height of dialogue "+DialogConstant.DIALOGUE_HEIGHT_TEMP+"  "+DialogConstant.DIALOGUE_HEIGHT+"  "+DialogConstant.HEIGHT);





        DialogConstant.DIALOGUE_HEIGHT_TEMP_bubble = (float)(displayMetrics.heightPixels * 0.20);
        DialogConstant.DIALOGUE_WIDTH_TEMP_bubble = displayMetrics.widthPixels;
        DialogConstant.WIDTH_bubble = DialogConstant.DIALOGUE_WIDTH_TEMP_bubble - (2*DialogConstant.X_bubble*DialogConstant.SCALE_FACTOR_X);
        DialogConstant.DIALOGUE_WIDTH_bubble = DialogConstant.DIALOGUE_WIDTH_TEMP_bubble - (DialogConstant.X_bubble*DialogConstant.SCALE_FACTOR_X);
        DialogConstant.HEIGHT_bubble = DialogConstant.DIALOGUE_HEIGHT_TEMP_bubble - (2*DialogConstant.Y_bubble*DialogConstant.SCALE_FACTOR_Y);
        DialogConstant.DIALOGUE_HEIGHT_bubble = DialogConstant.DIALOGUE_HEIGHT_TEMP_bubble - (DialogConstant.Y_bubble*DialogConstant.SCALE_FACTOR_Y);
        DialogConstant.MAX_RADIUS = (int)((DialogConstant.Y_bubble-5)*DialogConstant.SCALE_FACTOR_Y);
        DialogConstant.MIN_RADIUS = (int)((DialogConstant.Y_bubble-DialogConstant.Y_bubble/2)*DialogConstant.SCALE_FACTOR_Y);
        DialogConstant.MAX_ARCH_WIDTH = 125*(int)DialogConstant.SCALE_FACTOR_X;
        DialogConstant.MIN_ARCH_WIDTH = 50*(int)DialogConstant.SCALE_FACTOR_X;
    }

}
