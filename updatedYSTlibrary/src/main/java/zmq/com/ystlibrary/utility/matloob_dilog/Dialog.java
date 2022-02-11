package zmq.com.ystlibrary.utility.matloob_dilog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;

import java.util.ArrayList;

import zmq.com.ystlibrary.R;
import zmq.com.ystlibrary.utility.GlobalVariables;

public class Dialog {
    Context ctx;
    int  x, x_pos, i, characterWidth, scrollTime = 30, numOfLines, reminderSpace, linesHeight, linesHeightDup, extraHeight;
    double spaceWidth;

    String matter1 = "", fontSize[];
    Paint paint2;
    int x_width_change=15, x_width_change_left=15;
    String StringLine = "";
    Rect bound;
    ArrayList<StringCordinate> stringCordinates;
    ArrayList<String> stringLineArray, stringChunkArray;
    ArrayList<ArrayList<String>> stringWholeChunk;
    int  decrease = 0, stringLineNumber = 0;
    float width;
    String searchWord = "djkfdshkhksfd";
    public static final String TAG = "CUSTOM DIALOG";

    public Dialog(Context _ctx) {
        ctx = _ctx;
        paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint2.setColor(Color.BLACK);
        paint2.setTextSize(20 * GlobalVariables.xScale_factor);


        stringLineArray = new ArrayList<>();
        stringWholeChunk = new ArrayList<>();
        stringCordinates = new ArrayList<>();
        DialogConstant.DIALOGUE_WIDTH_SMALL = 0;
        DialogConstant.DIALOGUE_HEIGHT_SMALL = 0;
        DialogConstant.STRING_RECT_WIDTH = 0;
        DialogConstant.STRING_RECT_HEIGHT = 0;
        DialogConstant.STRING_RECT_SPACE = 0;
        DialogConstant.STRING_LINE_NUMBER = 0;

        DialogConstant.initializeConstant(ctx);
    }

    private void initializeVaribales(){
        stringLineArray = new ArrayList<>();
        stringWholeChunk = new ArrayList<>();
        stringCordinates = new ArrayList<>();
        decrease = 0;stringLineNumber = 0;StringLine = "";
        x=0; x_pos=0; i=0; characterWidth=0; scrollTime = 30; numOfLines=0;
        reminderSpace=0; linesHeight=0; linesHeightDup=0;extraHeight=0;
        spaceWidth=0;
    }

    public boolean isVisible,oneTym;
    public void draw(Canvas canvas, String message, int charWidth, int x_pos, int y_pos) {
        if(isVisible) {

            if (!(matter1.equals(message))) {
                oneTym=true;
                if(oneTym) {
                    matter1 = message;
                    initializeVaribales();
                    DialogConstant.DIALOGUE_WIDTH_SMALL = 0;
                    DialogConstant.DIALOGUE_HEIGHT_SMALL = 0;
                    DialogConstant.STRING_RECT_WIDTH = 0;
                    DialogConstant.STRING_RECT_HEIGHT = 0;
                    DialogConstant.STRING_RECT_SPACE = 0;
                    DialogConstant.STRING_LINE_NUMBER = 0;
                    breakString(matter1, searchWord);
                    oneTym = false;
                }
            }
            characterWidth = charWidth;
            this.x_pos = x_pos;
            drawSomething(canvas);
        }
    }


    protected void breakString(String matter, String searchWord) {
        DialogConstant.sharedPreferences = ctx.getSharedPreferences(DialogConstant.PREFERENCES, ctx.MODE_PRIVATE);
        DialogConstant.LANGUAGE = DialogConstant.sharedPreferences.getInt("language", 0);
        DialogConstant.FONT = DialogConstant.sharedPreferences.getInt("font", 0);
        paint2.setTypeface(Typeface.DEFAULT);
        bound = new Rect();
        DialogConstant.STRING_RECT_WIDTH = DialogConstant.WIDTH - (30 * DialogConstant.SCALE_FACTOR_X);
        width = DialogConstant.STRING_RECT_WIDTH;

        if (DialogConstant.LANGUAGE == 0) {
            fontSize = ctx.getResources().getStringArray(R.array.font_size_hin);
            spaceWidth = Math.ceil(Float.valueOf(fontSize[DialogConstant.FONT]) / 5);
        } else if (DialogConstant.LANGUAGE == 1) {
            fontSize = ctx.getResources().getStringArray(R.array.font_size_eng);
            spaceWidth = Math.ceil(Float.valueOf(fontSize[DialogConstant.FONT]) / 3);
        } else {
            fontSize = ctx.getResources().getStringArray(R.array.font_size_urdu);
        }
        if (DialogConstant.FONT == 0) {
            paint2.setTextSize(Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_X);
            DialogConstant.STRING_LINE_SPACE = 5 * DialogConstant.SCALE_FACTOR_Y;
        } else if (DialogConstant.FONT == 1) {
            paint2.setTextSize(Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y);
            DialogConstant.STRING_LINE_SPACE = 5 * DialogConstant.SCALE_FACTOR_Y;
        } else if (DialogConstant.FONT == 2) {
            paint2.setTextSize(Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y);
            DialogConstant.STRING_LINE_SPACE = 5 * DialogConstant.SCALE_FACTOR_Y;
        }


        char ch[] = matter.toCharArray();
        String add = "";
        for (i = 0; i < matter.length(); i++) {

            if (ch[i] != ' ' && ch[i] != '.' && i != matter.length() - 1) {
                add = add + ch[i];
            } else {
                if (i == matter.length() - 1) {
                    add = add + ch[i];
                }
                paint2.getTextBounds(add, 0, add.length(), bound);
                width = width - bound.width();

                if (width >= 0) {
                    StringLine = StringLine + add;
                    add = "";
                    if (ch[i] == ' ') {
                        StringLine = StringLine + " ";
                        width = (float) (width - spaceWidth * DialogConstant.SCALE_FACTOR_X);
                    }
                    if (ch[i] == '.' && i != matter.length() - 1) {
                        StringLine = StringLine + ".";
                        paint2.getTextBounds(".", 0, ".".length(), bound);
                        width = width - bound.width();
                    }
                } else {
                    stringLineArray.add(StringLine);
                    StringLine = "";
                    x++;
                    width = DialogConstant.STRING_RECT_WIDTH;
                    paint2.getTextBounds(add, 0, add.length(), bound);
                    width = width - bound.width();
//                    if (width > 0) {
                    StringLine = StringLine + add;
                    add = "";
                    if (ch[i] == ' ') {
                        StringLine = StringLine + " ";
                        width = (float) (width - spaceWidth * DialogConstant.SCALE_FACTOR_X);
                    }
                    if (ch[i] == '.' && i != matter.length() - 1) {
                        StringLine = StringLine + ".";
                        paint2.getTextBounds(".", 0, ".".length(), bound);
                        width = width - bound.width();
                    }
//                    }
                }
            }
        }
        if (i == matter.length()) {
            stringLineArray.add(StringLine);
            if (stringLineArray.size() == 1) {
                DialogConstant.DIALOGUE_WIDTH_SMALL = DialogConstant.STRING_RECT_WIDTH - width;
            } else {
                DialogConstant.DIALOGUE_WIDTH_SMALL = DialogConstant.DIALOGUE_WIDTH;
            }
        }


        if (DialogConstant.LANGUAGE == 0) {
            extraHeight = 1;
            linesHeightDup = (int) (Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y +
                    (Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * 0.30) + DialogConstant.STRING_LINE_SPACE);
            linesHeight = (int) (Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y +
                    (Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * 0.30) + DialogConstant.STRING_LINE_SPACE);
            numOfLines = (int) DialogConstant.HEIGHT / linesHeight;
            if (stringLineArray.size() > numOfLines) {
                reminderSpace = (int) (DialogConstant.HEIGHT % linesHeight);
                linesHeight = linesHeight + (reminderSpace / numOfLines);
            }
        } else if (DialogConstant.LANGUAGE == 1) {
            extraHeight = 0;
            linesHeightDup = (int) (Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y +
                    DialogConstant.STRING_LINE_SPACE);
            linesHeight = (int) (Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y +
                    DialogConstant.STRING_LINE_SPACE);
            numOfLines = (int) DialogConstant.HEIGHT / linesHeight;
            if (stringLineArray.size() > numOfLines) {
                reminderSpace = (int) (DialogConstant.HEIGHT % linesHeight);
                linesHeight = linesHeight + (reminderSpace / numOfLines);
            }

        } else if (DialogConstant.LANGUAGE == 2) {

        }


        for (int z = 0; z < stringLineArray.size(); z++) {
            StringCordinate cordinate = new StringCordinate();
            cordinate.setX((DialogConstant.X * DialogConstant.SCALE_FACTOR_X) + 15 * DialogConstant.SCALE_FACTOR_X);
            cordinate.setY((DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y) + ((1 + z) * linesHeight));
            stringCordinates.add(cordinate);
        }

        for (int z = 0; z < stringLineArray.size(); z++) {
            StringCordinate cordinate = stringCordinates.get(z);
        }

        DialogConstant.DIALOGUE_HEIGHT_SMALL = DialogConstant.DIALOGUE_HEIGHT_SMALL + DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y;
        for (int p = 0; p < stringLineArray.size(); p++) {
            DialogConstant.DIALOGUE_HEIGHT_SMALL = DialogConstant.DIALOGUE_HEIGHT_SMALL + linesHeight;
        }


        for (int x = 0; x < stringLineArray.size(); x++) {
            String line = stringLineArray.get(x);
            stringChunkArray = new ArrayList<>();
            int start = 0, i;

            for (i = -1; (i = line.toLowerCase().indexOf(searchWord.toLowerCase(), i + 1)) != -1; i++) {
                System.out.println("index position = " + i);
                stringChunkArray.add(line.substring(start, i));
                stringChunkArray.add(line.substring(i, i + searchWord.length()));
                start = i + searchWord.length();
            }
            System.out.println("index position = " + i);
            if (i == -1) {
                stringChunkArray.add(line.substring(start, line.length()));
            }
            stringWholeChunk.add(stringChunkArray);
        }

        for (int q = 0; q < stringWholeChunk.size(); q++) {
            ArrayList<String> ss = stringWholeChunk.get(q);
            for (int r = 0; r < ss.size(); r++) {
                System.out.print(ss.get(r) + "    ");
            }
            System.out.println();
        }
    }


    protected void drawSomething(Canvas canvas) {
        if (stringLineArray.size() == 1) {
            if ((x_pos - 15 * DialogConstant.SCALE_FACTOR_X) > DialogConstant.X * DialogConstant.SCALE_FACTOR_X) {
                if ((int) (x_pos + DialogConstant.DIALOGUE_WIDTH_SMALL + 15 * DialogConstant.SCALE_FACTOR_X) <= DialogConstant.DIALOGUE_WIDTH) {
                    paint2.setStyle(Paint.Style.STROKE);
                    paint2.setColor(Color.BLACK);
                    paint2.setStrokeWidth(5);
                    RectF rectF1 = new RectF(x_pos - 15 * DialogConstant.SCALE_FACTOR_X-x_width_change_left* DialogConstant.SCALE_FACTOR_X, DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y, x_pos + DialogConstant.DIALOGUE_WIDTH_SMALL + x_width_change * DialogConstant.SCALE_FACTOR_X, DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight);
                    canvas.drawRoundRect(rectF1, 20, 20, paint2);
                    paint2.setStyle(Paint.Style.FILL);
                    paint2.setColor(Color.WHITE);
                    RectF rectF = new RectF(x_pos - 15 * DialogConstant.SCALE_FACTOR_X-x_width_change_left* DialogConstant.SCALE_FACTOR_X, DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y, x_pos + DialogConstant.DIALOGUE_WIDTH_SMALL + x_width_change * DialogConstant.SCALE_FACTOR_X, DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight);
                    canvas.drawRoundRect(rectF, 20, 20, paint2);

                    paint2.setTextSize(Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_X);
                    Log.d(TAG, "drawSomething size = " + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y);

                    for (int q = 0; q < stringWholeChunk.size(); q++) {
                        StringCordinate cordinate = stringCordinates.get(q);
                        float startPoint = x_pos;
                        float endPoint = DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 15 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight;
                        ArrayList<String> ss = stringWholeChunk.get(q);
                        for (int r = 0; r < ss.size(); r++) {
                            if (searchWord.equalsIgnoreCase(ss.get(r))) {
                                paint2.setColor(Color.RED);
                                paint2.setStyle(Paint.Style.FILL);
                                canvas.drawText(ss.get(r), startPoint, endPoint, paint2);

                            } else {
                                paint2.setColor(Color.BLACK);
                                paint2.setStyle(Paint.Style.FILL);
                                canvas.drawText(ss.get(r), startPoint-x_width_change_left* DialogConstant.SCALE_FACTOR_X, endPoint, paint2);
                            }
                            float blackTextWidth = paint2.measureText(ss.get(r));
                            startPoint = startPoint + blackTextWidth;
                        }
                    }


                    Point a, b, c;
                    if ((x_pos + (characterWidth / 2)  + 40 * DialogConstant.SCALE_FACTOR_X) > (x_pos + DialogConstant.DIALOGUE_WIDTH_SMALL + 15 * DialogConstant.SCALE_FACTOR_X)) {
                        a = new Point(x_pos,
                                (int) (DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight));
                        b = new Point((int) (x_pos + 40 * DialogConstant.SCALE_FACTOR_X),
                                (int) (DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight));
                        c = new Point((int) (x_pos + (characterWidth / 2) ),
                                (int) (DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight + 50 * DialogConstant.SCALE_FACTOR_Y));
                    } else {
                        a = new Point((int) (x_pos + (characterWidth / 2) ),
                                (int) (DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight));
                        b = new Point((int) (x_pos + (characterWidth / 2)  + 40 * DialogConstant.SCALE_FACTOR_X),
                                (int) (DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight));
                        c = new Point((int) (x_pos + (characterWidth / 2) ),
                                (int) (DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight + 50 * DialogConstant.SCALE_FACTOR_Y));
                    }


                    paint2.setStrokeWidth(4);
                    paint2.setColor(Color.BLACK);
                    paint2.setStyle(Paint.Style.STROKE);
                    Path path = new Path();
                    path.moveTo(a.x, a.y);
                    path.lineTo(b.x, b.y);
                    path.lineTo(c.x, c.y);
                    path.lineTo(a.x, a.y);
                    path.close();
                    canvas.drawPath(path, paint2);

                    paint2.setStyle(Paint.Style.FILL);
                    paint2.setColor(Color.WHITE);
                    path.moveTo(a.x, a.y);
                    path.lineTo(b.x, b.y);
                    path.lineTo(c.x, c.y);
                    path.lineTo(a.x, a.y);
                    path.close();
                    canvas.drawPath(path, paint2);

                    canvas.drawLine(a.x, a.y, b.x, b.y, paint2);

                } else {
                    float outWidth = (x_pos + DialogConstant.DIALOGUE_WIDTH_SMALL + 15 * DialogConstant.SCALE_FACTOR_X - DialogConstant.DIALOGUE_WIDTH);
                    Log.d(TAG, "difference outWidth = " + outWidth);
                    paint2.setStyle(Paint.Style.STROKE);
                    paint2.setColor(Color.BLACK);
                    paint2.setStrokeWidth(5);
                    RectF rectF1 = new RectF(x_pos - 15 * DialogConstant.SCALE_FACTOR_X - outWidth-x_width_change_left* DialogConstant.SCALE_FACTOR_X, DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y, x_pos - outWidth + DialogConstant.DIALOGUE_WIDTH_SMALL + x_width_change * DialogConstant.SCALE_FACTOR_X, DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight);
                    canvas.drawRoundRect(rectF1, 20, 20, paint2);
                    paint2.setStyle(Paint.Style.FILL);
                    paint2.setColor(Color.WHITE);
                    RectF rectF = new RectF(x_pos - 15 * DialogConstant.SCALE_FACTOR_X - outWidth-x_width_change_left* DialogConstant.SCALE_FACTOR_X, DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y, x_pos - outWidth + DialogConstant.DIALOGUE_WIDTH_SMALL + x_width_change * DialogConstant.SCALE_FACTOR_X, DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight);
                    canvas.drawRoundRect(rectF, 20, 20, paint2);

                    paint2.setTextSize(Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_X);
                    Log.d(TAG, "drawSomething size = " + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y);


                    for (int q = 0; q < stringWholeChunk.size(); q++) {
                        StringCordinate cordinate = stringCordinates.get(q);
                        float startPoint = x_pos - outWidth;
                        float endPoint = DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 15 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight;
                        ArrayList<String> ss = stringWholeChunk.get(q);
                        for (int r = 0; r < ss.size(); r++) {
                            if (searchWord.equalsIgnoreCase(ss.get(r))) {
                                paint2.setColor(Color.RED);
                                paint2.setStyle(Paint.Style.FILL);
                                canvas.drawText(ss.get(r), startPoint, endPoint, paint2);

                            } else {
                                paint2.setColor(Color.BLACK);
                                paint2.setStyle(Paint.Style.FILL);
                                canvas.drawText(ss.get(r), startPoint-x_width_change_left* DialogConstant.SCALE_FACTOR_X, endPoint, paint2);
                            }
                            float blackTextWidth = paint2.measureText(ss.get(r));
                            startPoint = startPoint + blackTextWidth;
                        }
                    }


                    Point a, b, c;
                    if ((x_pos + (characterWidth / 2)  + 40 * DialogConstant.SCALE_FACTOR_X) > (x_pos + DialogConstant.DIALOGUE_WIDTH_SMALL + 15 * DialogConstant.SCALE_FACTOR_X)) {
                        a = new Point(x_pos,
                                (int) (DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight));
                        b = new Point((int) (x_pos + 40 * DialogConstant.SCALE_FACTOR_X),
                                (int) (DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight));
                        c = new Point((int) (x_pos + (characterWidth / 2) ),
                                (int) (DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight + 50 * DialogConstant.SCALE_FACTOR_Y));
                    } else {
                        a = new Point((int) (x_pos + (characterWidth / 2) ),
                                (int) (DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight));
                        b = new Point((int) (x_pos + (characterWidth / 2) + 40 * DialogConstant.SCALE_FACTOR_X),
                                (int) (DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight));
                        c = new Point((int) (x_pos + (characterWidth / 2) ),
                                (int) (DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight + 50 * DialogConstant.SCALE_FACTOR_Y));
                    }


                    paint2.setStrokeWidth(4);
                    paint2.setColor(Color.BLACK);
                    paint2.setStyle(Paint.Style.STROKE);
                    Path path = new Path();
                    path.moveTo(a.x, a.y);
                    path.lineTo(b.x, b.y);
                    path.lineTo(c.x, c.y);
                    path.lineTo(a.x, a.y);
                    path.close();
                    canvas.drawPath(path, paint2);

                    paint2.setStyle(Paint.Style.FILL);
                    paint2.setColor(Color.WHITE);
                    path.moveTo(a.x, a.y);
                    path.lineTo(b.x, b.y);
                    path.lineTo(c.x, c.y);
                    path.lineTo(a.x, a.y);
                    path.close();
                    canvas.drawPath(path, paint2);

                    canvas.drawLine(a.x, a.y, b.x, b.y, paint2);

                }


            } else {
                paint2.setStyle(Paint.Style.STROKE);
                paint2.setColor(Color.BLACK);
                paint2.setStrokeWidth(5);
                RectF rectF1 = new RectF(DialogConstant.X * DialogConstant.SCALE_FACTOR_X-x_width_change_left* DialogConstant.SCALE_FACTOR_X, DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y, DialogConstant.X * DialogConstant.SCALE_FACTOR_X +x_width_change*DialogConstant.SCALE_FACTOR_X+ DialogConstant.DIALOGUE_WIDTH_SMALL + 40 * DialogConstant.SCALE_FACTOR_X, DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight);
                canvas.drawRoundRect(rectF1, 20, 20, paint2);
                paint2.setStyle(Paint.Style.FILL);
                paint2.setColor(Color.WHITE);
                RectF rectF = new RectF(DialogConstant.X * DialogConstant.SCALE_FACTOR_X-x_width_change_left* DialogConstant.SCALE_FACTOR_X, DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y, DialogConstant.X * DialogConstant.SCALE_FACTOR_X +x_width_change*DialogConstant.SCALE_FACTOR_X+ DialogConstant.DIALOGUE_WIDTH_SMALL + 40 * DialogConstant.SCALE_FACTOR_X, DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight);
                canvas.drawRoundRect(rectF, 20, 20, paint2);

                paint2.setTextSize(Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y);
                Log.d(TAG, "drawSomething size = " + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y);


                for (int q = 0; q < stringWholeChunk.size(); q++) {
                    StringCordinate cordinate = stringCordinates.get(q);
                    float startPoint = DialogConstant.X * DialogConstant.SCALE_FACTOR_X + 15 * DialogConstant.SCALE_FACTOR_X;
                    float endPoint = DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 15 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight;
                    ArrayList<String> ss = stringWholeChunk.get(q);
                    for (int r = 0; r < ss.size(); r++) {
                        if (searchWord.equalsIgnoreCase(ss.get(r))) {
                            paint2.setColor(Color.RED);
                            paint2.setStyle(Paint.Style.FILL);
                            canvas.drawText(ss.get(r), startPoint, endPoint, paint2);

                        } else {
                            paint2.setColor(Color.BLACK);
                            paint2.setStyle(Paint.Style.FILL);
                            canvas.drawText(ss.get(r), startPoint-x_width_change_left* DialogConstant.SCALE_FACTOR_X, endPoint, paint2);
                        }
                        float blackTextWidth = paint2.measureText(ss.get(r));
                        startPoint = startPoint + blackTextWidth;
                    }
                }


                Point a, b, c;
                if ((x_pos + (characterWidth / 2)  + 40 * DialogConstant.SCALE_FACTOR_X) > (x_pos + DialogConstant.DIALOGUE_WIDTH_SMALL + 15 * DialogConstant.SCALE_FACTOR_X)) {
                    a = new Point((int) (DialogConstant.X * DialogConstant.SCALE_FACTOR_X + 15 * DialogConstant.SCALE_FACTOR_X),
                            (int) (DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight));
                    b = new Point((int) (DialogConstant.X * DialogConstant.SCALE_FACTOR_X + 15 * DialogConstant.SCALE_FACTOR_X + 40 * DialogConstant.SCALE_FACTOR_X),
                            (int) (DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight));
                    c = new Point((int) (x_pos + (characterWidth / 2) ),
                            (int) (DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight + 50 * DialogConstant.SCALE_FACTOR_Y));
                } else {
                    a = new Point((int) (x_pos + (characterWidth / 2) ),
                            (int) (DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight));
                    b = new Point((int) (x_pos + (characterWidth / 2)  + 40 * DialogConstant.SCALE_FACTOR_X),
                            (int) (DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight));
                    c = new Point((int) (x_pos + (characterWidth / 2) ),
                            (int) (DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight + 50 * DialogConstant.SCALE_FACTOR_Y));
                }


                paint2.setStrokeWidth(4);
                paint2.setColor(Color.BLACK);
                paint2.setStyle(Paint.Style.STROKE);
                Path path = new Path();
                path.moveTo(a.x, a.y);
                path.lineTo(b.x, b.y);
                path.lineTo(c.x, c.y);
                path.lineTo(a.x, a.y);
                path.close();
                canvas.drawPath(path, paint2);

                paint2.setStyle(Paint.Style.FILL);
                paint2.setColor(Color.WHITE);
                path.moveTo(a.x, a.y);
                path.lineTo(b.x, b.y);
                path.lineTo(c.x, c.y);
                path.lineTo(a.x, a.y);
                path.close();
                canvas.drawPath(path, paint2);

                canvas.drawLine(a.x, a.y, b.x, b.y, paint2);
            }
        } else if (stringLineArray.size() <= numOfLines) {
            paint2.setStyle(Paint.Style.STROKE);
            paint2.setColor(Color.BLACK);
            paint2.setStrokeWidth(5);
            RectF rectF1 = new RectF(DialogConstant.X * DialogConstant.SCALE_FACTOR_X-x_width_change_left* DialogConstant.SCALE_FACTOR_X, DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y, DialogConstant.DIALOGUE_WIDTH_SMALL+x_width_change*DialogConstant.SCALE_FACTOR_X, DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + stringLineArray.size() * (Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight));
            canvas.drawRoundRect(rectF1, 20, 20, paint2);
            paint2.setStyle(Paint.Style.FILL);
            paint2.setColor(Color.WHITE);
            RectF rectF = new RectF(DialogConstant.X * DialogConstant.SCALE_FACTOR_X-x_width_change_left* DialogConstant.SCALE_FACTOR_X, DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y, DialogConstant.DIALOGUE_WIDTH_SMALL+x_width_change*DialogConstant.SCALE_FACTOR_X, DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + stringLineArray.size() * (Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight));
            canvas.drawRoundRect(rectF, 20, 20, paint2);

            paint2.setTextSize(Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y);
            Log.d(TAG, "drawSomething size = " + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y);


            for (int q = 0; q < stringWholeChunk.size(); q++) {
                StringCordinate cordinate = stringCordinates.get(q);
                float startPoint = DialogConstant.X * DialogConstant.SCALE_FACTOR_X + 15 * DialogConstant.SCALE_FACTOR_X;
                float endPoint = DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 15 * DialogConstant.SCALE_FACTOR_X + (q + 1) * (Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight);
                ArrayList<String> ss = stringWholeChunk.get(q);
                for (int r = 0; r < ss.size(); r++) {
                    if (searchWord.equalsIgnoreCase(ss.get(r))) {
                        paint2.setColor(Color.RED);
                        paint2.setStyle(Paint.Style.FILL);
                        canvas.drawText(ss.get(r), startPoint, endPoint, paint2);

                    } else {
                        paint2.setColor(Color.BLACK);
                        paint2.setStyle(Paint.Style.FILL);
                        canvas.drawText(ss.get(r), startPoint-x_width_change_left* DialogConstant.SCALE_FACTOR_X, endPoint, paint2);
                    }
                    float blackTextWidth = paint2.measureText(ss.get(r));
                    startPoint = startPoint + blackTextWidth;
                }
            }


            Point a, b, c;
            a = new Point((int) (x_pos + (characterWidth / 2) ),
                    (int) (DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + stringLineArray.size() * (Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight)));
            b = new Point((int) (x_pos + (characterWidth / 2)  + 40 * DialogConstant.SCALE_FACTOR_X),
                    (int) (DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + stringLineArray.size() * (Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight)));
            c = new Point((int) (x_pos + (characterWidth / 2) ),
                    (int) ((DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + 30 * DialogConstant.SCALE_FACTOR_X + stringLineArray.size() * (Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y * .30f * extraHeight)) + 50 * DialogConstant.SCALE_FACTOR_Y));


            paint2.setStrokeWidth(4);
            paint2.setColor(Color.BLACK);
            paint2.setStyle(Paint.Style.STROKE);
            Path path = new Path();
            path.moveTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            path.lineTo(c.x, c.y);
            path.lineTo(a.x, a.y);
            path.close();
            canvas.drawPath(path, paint2);

            paint2.setStyle(Paint.Style.FILL);
            paint2.setColor(Color.WHITE);
            path.moveTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            path.lineTo(c.x, c.y);
            path.lineTo(a.x, a.y);
            path.close();
            canvas.drawPath(path, paint2);

            canvas.drawLine(a.x, a.y, b.x, b.y, paint2);


        }
        else {
            paint2.setStyle(Paint.Style.STROKE);
            paint2.setColor(Color.BLACK);
            paint2.setStrokeWidth(5);
            RectF rectF1 = new RectF(DialogConstant.X * DialogConstant.SCALE_FACTOR_X-x_width_change_left* DialogConstant.SCALE_FACTOR_X, DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y, DialogConstant.DIALOGUE_WIDTH_SMALL+x_width_change*DialogConstant.SCALE_FACTOR_X, DialogConstant.DIALOGUE_HEIGHT);
            canvas.drawRoundRect(rectF1, 20, 20, paint2);
            paint2.setStyle(Paint.Style.FILL);
            paint2.setColor(Color.WHITE);
            RectF rectF = new RectF(DialogConstant.X * DialogConstant.SCALE_FACTOR_X-x_width_change_left* DialogConstant.SCALE_FACTOR_X, DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y, DialogConstant.DIALOGUE_WIDTH_SMALL+x_width_change*DialogConstant.SCALE_FACTOR_X, DialogConstant.DIALOGUE_HEIGHT);
            canvas.drawRoundRect(rectF, 20, 20, paint2);

            paint2.setTextSize(Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y);
            Log.d(TAG, "drawSomething size = " + Integer.valueOf(fontSize[DialogConstant.FONT]) * DialogConstant.SCALE_FACTOR_Y);
            paint2.setColor(Color.BLACK);
            paint2.setStyle(Paint.Style.FILL);
            for (int i = 0; i < numOfLines - 1; i++) {
                StringCordinate cordinate = stringCordinates.get(i);
                canvas.drawText(stringLineArray.get(i + stringLineNumber), cordinate.getX(), cordinate.getY() + linesHeightDup - decrease, paint2);
            }

            if (scrollTime <= 0) {
                if (decrease == linesHeight || decrease == (linesHeight + 1)) {
                    if (!(stringLineNumber == stringLineArray.size() - numOfLines + 1)) {
                        stringLineNumber = stringLineNumber + 1;
                        decrease = 0;
                        scrollTime = 30;
                    }
                } else {
                    if (!(stringLineNumber == stringLineArray.size() - numOfLines + 2))
                        decrease = decrease + (int) (1 * DialogConstant.SCALE_FACTOR_X);
                    else {
                    }
                }
            }
            scrollTime--;

            paint2.setStyle(Paint.Style.FILL);
            paint2.setColor(Color.WHITE);
            RectF rectFUpper = new RectF(DialogConstant.X * DialogConstant.SCALE_FACTOR_X, DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y, DialogConstant.DIALOGUE_WIDTH_SMALL, DialogConstant.Y * DialogConstant.SCALE_FACTOR_Y + linesHeight);
            canvas.drawRoundRect(rectFUpper, 20, 20, paint2);
            RectF rectFLower = new RectF(DialogConstant.X * DialogConstant.SCALE_FACTOR_X, DialogConstant.DIALOGUE_HEIGHT - linesHeight, DialogConstant.DIALOGUE_WIDTH_SMALL, DialogConstant.DIALOGUE_HEIGHT);
            canvas.drawRoundRect(rectFLower, 20, 20, paint2);


            Point a, b, c;
            a = new Point((int) (x_pos + (characterWidth / 2)),
                    (int) (DialogConstant.DIALOGUE_HEIGHT));
            b = new Point((int) (x_pos + (characterWidth / 2)  + 40 * DialogConstant.SCALE_FACTOR_X),
                    (int) (DialogConstant.DIALOGUE_HEIGHT));
            c = new Point((int) (x_pos + (characterWidth / 2)),
                    (int) (DialogConstant.DIALOGUE_HEIGHT + 50 * DialogConstant.SCALE_FACTOR_Y));


            paint2.setStrokeWidth(4);
            paint2.setColor(Color.BLACK);
            paint2.setStyle(Paint.Style.STROKE);
            Path path = new Path();
            path.moveTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            path.lineTo(c.x, c.y);
            path.lineTo(a.x, a.y);
            path.close();
            canvas.drawPath(path, paint2);

            paint2.setStyle(Paint.Style.FILL);
            paint2.setColor(Color.WHITE);
            path.moveTo(a.x, a.y);
            path.lineTo(b.x, b.y);
            path.lineTo(c.x, c.y);
            path.lineTo(a.x, a.y);
            path.close();
            canvas.drawPath(path, paint2);

            canvas.drawLine(a.x, a.y, b.x, b.y, paint2);
        }
    }
}
