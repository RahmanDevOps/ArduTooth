package zmq.com.ystlibrary.canvas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.MotionEvent;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

import zmq.com.ystlibrary.R;
import zmq.com.ystlibrary.interfaces.Recycler;
import zmq.com.ystlibrary.messages.Message;
import zmq.com.ystlibrary.model.ElementSortByZIndex;
import zmq.com.ystlibrary.model.StoryXML;
import zmq.com.ystlibrary.sprite.ShahSprite;
import zmq.com.ystlibrary.utility.GlobalVariables;
import zmq.com.ystlibrary.utility.Utility;
import zmq.com.ystlibrary.utility.matloob_dilog.Dialog;
import zmq.com.ystlibrary.utility.matloob_dilog.MatloobDialogModel;
import zmq.com.ystlibrary.utility.matloob_dilog.XMLParser;

public class StoryView extends BaseSurface implements Recycler, MediaPlayer.OnCompletionListener {
    private ShahSprite bg;
    private ShahSprite slider_CloseBg, slider_OpenBg, slider_left, slider_right, slider_play, slider_pause, slider_audio_play, slider_audio_stop, slider_home_screen;
    private ShahSprite faded_bg, yes_btn, no_btn,back_btn,exit_btn;
    private ArrayList<ShahSprite> characters;
    private Message message;
    //    private Message dialog;
    private Message backgroundMessage;

    private int sceneIndex = 0, elementIndex = 0;
    private boolean isLoading = true, isStop = false;
    private ArrayList<StoryXML.Elements> elementsArrayList;
    private StoryXML.Elements element;
    private StoryXML storyXML;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private boolean onTymFlag = true, isLast = false, isPause = false, isNextElement = false, isNextScene = false;
    private int scene = 0;
    public static int lng_code=0;

    public StoryView(final Context context) {
        super(context);
        setWillNotDraw(false);

    }

    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public void drawText(Canvas canvas, String Text, int xMidPosition, int yMidPosition, int textSize, Paint.Align align) {
        textPaint.setTextSize(textSize);
        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);
        textPaint.setTypeface(tf);
        textPaint.setTextAlign(align);
        canvas.drawText(Text, xMidPosition, yMidPosition, textPaint);

    }

    private void waitScreen(Canvas g) {
        textPaint.setColor(Color.BLUE);
        g.drawRect(0, 0, GlobalVariables.width, GlobalVariables.height, textPaint);
        textPaint.setColor(Color.WHITE);
        drawText(g, "Loading", GlobalVariables.width / 2, GlobalVariables.height / 2, (int) (GlobalVariables.xScale_factor * 30), Paint.Align.CENTER);

    }

    private int status=0;
    @Override
    protected void onDraw(Canvas g) {
        switch (scene) {
            case 0:
//                waitScreen(g);
                if(status==0) {
                    status=1;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            loadResources(gson);
                        }
                    }).start();
                }
                break;

            case 1:
                setWillNotDraw(true);
                break;
        }
        invalidate();
    }

    @Override
    protected void drawSomething(Canvas g) {
        g.drawColor(Color.TRANSPARENT);

        if (!isLoading) {
            bg.paint(g, paint);
            for (ShahSprite shahSprite : characters) {
                shahSprite.paint(g, null);
            }

            if (onTymFlag) {
                switch (element.type) {
                    case "message":
                        onTymFlag = false;
                        showMessage();
                        break;
                    case "dilog":
                        onTymFlag = false;
                        showDialog();
                        break;
                    case "sequence":
                        if (sceneIndex == 0) {
                            onTymFlag = false;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(3000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    isNextScene = true;
                                    hideSlider();
                                }
                            }).start();
                        } else {
                            onTymFlag = false;
                            isNextScene = true;
                        }

                        break;
                    case "draw":
                        onTymFlag = false;
                        isNextElement = true;
                        break;
                    case "stop":
                        onTymFlag = false;
                        stop();
                        break;
                }

            }
            paint.setColor(Color.BLACK);

            if (dialogMatloob != null && dialogMatloob.isVisible) {
                dialogMatloob.draw(g, matloobDialogModel.getMessage(), matloobDialogModel.getImg_width(), matloobDialogModel.getX_cord(), matloobDialogModel.getY_cord());
            }
//            dialog.update(g, paint);
            paint.setColor(Color.WHITE);
            message.update(g, paint);
            displaySlider(g);

            if (isStop) {
                faded_bg.paint(g, null);
                paint.setColor(Color.BLACK);
                backgroundMessage.update(g, paint);
                yes_btn.paint(g, null);
                no_btn.paint(g, null);
                back_btn.paint(g,null);
                exit_btn.paint(g,null);
            }
            if(isBookMarkStory){
                back_btn.setVisible(true);
                exit_btn.setVisible(true);
                isBookMarkStory=false;
            }
            if (isFinish || isBack) {
                isDelay = false;
                gameThread.running = false;
                gameThread = null;
                this.surfaceHolder.removeCallback(this);
                ((Activity) context).finish();
                this.recycle();

            }
            if (isPause) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();

                    if (gameThread != null) {
                        gameThread.running = false;
                        gameThread.interrupt();
                        gameThread = null;
                    }
                }
            }
            if (isNextElement) {
                nextElement();
                isNextElement = false;
            }
            if (isNextScene) {
                nextScene();
                isNextScene = false;
            }
        }

       /* drawText(g, "Scene: " + sceneIndex, GlobalVariables.width - (int) (GlobalVariables.xScale_factor * 150), (int) (GlobalVariables.yScale_factor * 200));
        drawText(g, "Element: " + elementIndex, GlobalVariables.width - (int) (GlobalVariables.xScale_factor * 150), (int) (GlobalVariables.yScale_factor * 250));*/
    }

    /*private void addElement() {
        switch (element.type) {
            case "draw":
                StoryXML.Property properties = element.properties;
                ArrayList<StoryXML.Character> characters = storyXML.characters;
                String characterId = properties.character;
                for (StoryXML.Character character : characters) {
                    if (character.id.equalsIgnoreCase(characterId)) {
                        ShahSprite shahSprite = Utility.loadImgResource(character.name);// + ".png");
                        if (properties.x.contains(".")) {
                            String x1 = properties.x.substring(0, properties.x.indexOf('.'));
                            properties.x = x1;
                        }
                        if (properties.y.contains(".")) {
                            String y1 = properties.y.substring(0, properties.y.indexOf('.'));
                            properties.y = y1;
                        }
                        int x = (int) (GlobalVariables.xScale_factor * Integer.parseInt(properties.x));
                        int y = (int) (GlobalVariables.yScale_factor * Integer.parseInt(properties.y));
                        shahSprite.setPosition(x, y);

                        shahSprite.setZ(Integer.parseInt(properties.z));
                        shahSprite.setSequence(Integer.parseInt(element.sequence));
                        shahSprite.setCharacterId(characterId);

                        this.characters.add(shahSprite);
                        //prevElement = elements;
                    }
                }
                break;
        }
        Collections.sort(characters, new ElementSortByZIndex());
    }*/


    private void stop() {
        faded_bg = new ShahSprite(Utility.decodeSampledBitmapFromResource(GlobalVariables.getResource, R.drawable.fade));
        yes_btn = new ShahSprite(Utility.decodeSampledBitmapFromResource(GlobalVariables.getResource, R.drawable.yes));
        no_btn = new ShahSprite(Utility.decodeSampledBitmapFromResource(GlobalVariables.getResource, R.drawable.no));
        back_btn = new ShahSprite(Utility.decodeSampledBitmapFromResource(GlobalVariables.getResource, R.drawable.back_arrow));
        exit_btn = new ShahSprite(Utility.decodeSampledBitmapFromResource(GlobalVariables.getResource, R.drawable.quit1));
        backgroundMessage = new Message(R.drawable.box_without_arrow, element.properties.question.split("#")[lng_code]);

        int xPos = GlobalVariables.width / 2 - backgroundMessage.mess_bgImage.getWidth() / 2;
        int yPos = (int) (GlobalVariables.yScale_factor * 40);
        backgroundMessage.mess_bgImage.setPosition(xPos, yPos);
        int messPadding = (int) (20 * GlobalVariables.xScale_factor);
        backgroundMessage.setMessTextPosition(xPos + messPadding / 2, yPos + messPadding);

        no_btn.setPosition(backgroundMessage.mess_bgImage.getX() + no_btn.getWidth(), GlobalVariables.height - (2 * no_btn.getHeight()));
        yes_btn.setPosition(no_btn.getX() + (2 * yes_btn.getWidth()), GlobalVariables.height - (2 * no_btn.getHeight()));
        back_btn.setPosition(no_btn.getX(), no_btn.getY());
        exit_btn.setPosition(yes_btn.getX(),yes_btn.getY());

        back_btn.setVisible(false);
        exit_btn.setVisible(false);

        StoryXML.Audio audio = getAudioName(element.properties.q_audio_id);
        playMp3(audio.name);
        isStop = true;

        backgroundMessage.isLastMessage = true;
    }

    private void showMessage() {

        message.setMessString(element.properties.message.split("#")[lng_code]);
        message.setVisible(true);

        StoryXML.Audio audio = getAudioName(element.properties.audio_id);
        playMp3(audio.name);
    }

    Dialog dialogMatloob = new Dialog(context);
    MatloobDialogModel matloobDialogModel;

    private void showDialog() {
        matloobDialogModel = new MatloobDialogModel();

        String characterId = element.properties.character;
        for (StoryXML.Elements elements : elementsArrayList) {
            if (elements.type.equalsIgnoreCase("draw")) {
                if (elements.properties.character.equalsIgnoreCase(characterId)) {
                    int x = (int) (GlobalVariables.xScale_factor * Integer.parseInt(elements.properties.x));
                    int y = (int) (GlobalVariables.yScale_factor * Integer.parseInt(elements.properties.y));

//                    dialog.mess_bgImage.setPosition(0, 0);

                    matloobDialogModel.setX_cord(x);
                    matloobDialogModel.setY_cord(y);

                    for (ShahSprite shahSprite : characters) {
                        if (shahSprite.getCharacterId().equalsIgnoreCase(characterId)) {
                            matloobDialogModel.setImg_width(shahSprite.getWidth());
                            shahSprite.setVisible(true);
                        }
                    }

                    break;
                }
            }
        }
        matloobDialogModel.setMessage(element.properties.dilog.split("#")[lng_code]);
        dialogMatloob.isVisible = true;
        /*dialog.setMessString(element.properties.dilog.split("#")[0]);
        dialog.setVisible(true);*/


        StoryXML.Audio audio = getAudioName(element.properties.audio_id);
        playMp3(audio.name);
    }

    ArrayList<Integer> indexes;
    private void showCharacters() {
        characters = new ArrayList<>();
        int index=0;
        indexes = new ArrayList<>();

        StoryXML.Elements prevElement = null;
        for (StoryXML.Elements elements : elementsArrayList) {

            switch (elements.type) {
                case "draw":
                    StoryXML.Property properties = elements.properties;
                    ArrayList<StoryXML.Character> characters = storyXML.characters;
                    String characterId = properties.character;
                    for (StoryXML.Character character : characters) {
                        if (character.id.equalsIgnoreCase(characterId)) {
                            ShahSprite shahSprite = Utility.loadImgResource(character.name);// + ".png");
                            if (properties.x.contains(".")) {
                                String x1 = properties.x.substring(0, properties.x.indexOf('.'));
                                properties.x = x1;
                            }
                            if (properties.y.contains(".")) {
                                String y1 = properties.y.substring(0, properties.y.indexOf('.'));
                                properties.y = y1;
                            }
                            int x = (int) (GlobalVariables.xScale_factor * Integer.parseInt(properties.x));
                            int y = (int) (GlobalVariables.yScale_factor * Integer.parseInt(properties.y));
                            shahSprite.setPosition(x, y);

                            shahSprite.setZ(Integer.parseInt(properties.z));
                            shahSprite.setSequence(Integer.parseInt(elements.sequence));
                            shahSprite.setCharacterId(characterId);

                            if (prevElement != null) {
                                if (prevElement.type.equalsIgnoreCase("dilog")) {
                                    shahSprite.setVisible(false);
                                }
                            }
                            this.characters.add(shahSprite);
                            index++;
                            //prevElement = elements;
                        }
                    }
                    break;

                case "dilog":
                    indexes.add(index);
                    prevElement = elements;
                    break;

                case "message":
                    indexes.add(index);
                    break;

                case "sequence":
                    break;

            }
        }

        Collections.sort(characters, new ElementSortByZIndex());
    }

    private StoryXML.Background getBackgroundImageName(String sceneBackgroundId) {
        for (StoryXML.Background background : storyXML.backgrounds) {
            if (background.id.equals(sceneBackgroundId)) {
                return background;
            }
        }
        return null;
    }

    private StoryXML.Audio getAudioName(String audioId) {
        for (StoryXML.Audio audio : storyXML.audios) {
            if (audio.id.equals(audioId)) {
                return audio;
            }
        }
        return null;
    }

    private void nextElement() {
        onTymFlag = true;
        if (elementIndex < elementsArrayList.size() - 1) elementIndex++;
        element = elementsArrayList.get(elementIndex);
        message.setVisible(false);
//        dialog.setVisible(false);

        dialogMatloob.isVisible = false;

    }

    private void nextScene() {
        isLoading = true;
        if (sceneIndex < storyXML.scenes.size() - 1) sceneIndex++;

        bg.recycle();
        System.gc();
        StoryXML.Background background = getBackgroundImageName(storyXML.scenes.get(sceneIndex).background);
        bg = Utility.loadImgResource(background.name);// + ".png");

        elementsArrayList = storyXML.scenes.get(sceneIndex).elements;
        elementIndex = 0;
        element = elementsArrayList.get(elementIndex);

        if (characters.size() > 0) {
            for (ShahSprite shahSprite : characters) {
                shahSprite.recycle();
            }
            System.gc();
        }

        showCharacters();
        dialogMatloob.isVisible=false;
        message.setVisible(false);
        isLoading = false;
        onTymFlag = true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (!isStop) {
            if (element.type.equalsIgnoreCase("sequence")) {
                isNextScene = true;
            } else {
                isNextElement = true;

                try {
                    for(int a = indexes.get(0);a<indexes.get(1);a++){
                        characters.get(a).setVisible(true);
                    }
                    indexes.remove(0);
                }catch (Exception e){

                }

            }
        } else {
            if (isLast) {
                ((StoryViewActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((StoryViewActivity) context).showRatingDialog();
                    }
                });
            }
        }
    }

    private boolean b = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        RectF touchrectF = new RectF(event.getX(), event.getY(), event.getX() + 1, event.getY() + 1);

        if (!isStop) {
            if (!isPause && slider_right.isVisible() && slider_right.getDstRect().intersect(touchrectF)) {
                /*if (elementIndex < elementsArrayList.size() - 1) {
                    isNextElement = true;
                    return super.onTouchEvent(event);
                }*/
                if (sceneIndex < storyXML.scenes.size() - 1) {
                    isNextScene = true;
                    return super.onTouchEvent(event);
                }
                return super.onTouchEvent(event);
            }

            if (!isPause && slider_left.isVisible() && slider_left.getDstRect().intersect(touchrectF)) {
                if (sceneIndex - 1 > 0) {
                    sceneIndex = sceneIndex - 2;
                    if (sceneIndex < 0) sceneIndex = -1;
                    isNextScene = true;
                }
                return super.onTouchEvent(event);
            }

            if (!isPause && slider_home_screen.isVisible() && slider_home_screen.getDstRect().intersect(touchrectF)) {
                isFinish = false;
                isBack=true;
                return super.onTouchEvent(event);
            }

            if (!isPause && slider_pause.isVisible() && slider_pause.getDstRect().intersect(touchrectF)) {
                isPause = true;
                slider_pause.setVisible(false);
                slider_play.setVisible(true);
                return super.onTouchEvent(event);
            }

            if (isPause && slider_play.isVisible() && slider_play.getDstRect().intersect(touchrectF)) {
                isPause = false;
                slider_pause.setVisible(true);
                slider_play.setVisible(false);

                if ((!mediaPlayer.isPlaying())) {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition());
                    mediaPlayer.start();

                    if (gameThread == null) {
                        gameThread = new GameThread(surfaceHolder, this);
                        gameThread.start();
                    }
                }
                return super.onTouchEvent(event);
            }
            if (!isPause && slider_audio_play.isVisible() && slider_audio_play.getDstRect().intersect(touchrectF)) {
                slider_audio_play.setVisible(false);
                slider_audio_stop.setVisible(true);
                b = true;
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.setVolume(0, 0);
                }
                return super.onTouchEvent(event);
            }
            if (!isPause && slider_audio_stop.isVisible() && slider_audio_stop.getDstRect().intersect(touchrectF)) {
                slider_audio_play.setVisible(true);
                slider_audio_stop.setVisible(false);
                b = false;
                if ((mediaPlayer.isPlaying())) {
                    mediaPlayer.setVolume(1, 1);

                }
                return super.onTouchEvent(event);
            }

            if (!isPause && slider_OpenBg.isVisible() &&
                    touchrectF.intersect(rect_show_slider.left, rect_show_slider.top, rect_show_slider.right, rect_show_slider.bottom)) {
                hideSlider();
                slider_CloseBg.setVisible(true);

                int y11 = (int) (GlobalVariables.yScale_factor * 30);
                message.mess_bgImage.setPosition(0, slider_CloseBg.getY() - (slider_CloseBg.getHeight() + y11));
                message.setMessTextPosition(message.mess_bgImage.getX() + (int) (GlobalVariables.xScale_factor * 10), message.mess_bgImage.getY() + message.mess_bgImage.getHeight() / 4);
                return super.onTouchEvent(event);
            }

            if (!isPause && slider_CloseBg.isVisible() &&
                    touchrectF.intersect(rect_hide_slider.left, rect_hide_slider.top, rect_hide_slider.right, rect_hide_slider.bottom)) {

                showSlider();
                slider_audio_play.setVisible(!b);
                slider_audio_stop.setVisible(b);
                slider_OpenBg.setVisible(true);

                int y11 = (int) (GlobalVariables.yScale_factor * 30);
                message.mess_bgImage.setPosition(0, slider_OpenBg.getY() - (slider_OpenBg.getHeight() + y11));
                message.setMessTextPosition(message.mess_bgImage.getX() + (int) (GlobalVariables.xScale_factor * 10), message.mess_bgImage.getY() + message.mess_bgImage.getHeight() / 4);
                return super.onTouchEvent(event);
            }
        }

        if (isStop) {

            if (yes_btn.isVisible() && yes_btn.getDstRect().intersect(touchrectF)) {

                backgroundMessage.isLastMessage = true;

                backgroundMessage.setMessString(element.properties.correct.split("#")[lng_code]);

                System.out.println("Messs==========>>"+element.properties.correct.split("#")[lng_code]);

                yes_btn.setVisible(false);
                no_btn.setVisible(false);
                StoryXML.Audio audio = getAudioName(element.properties.a_audio_id);
                playMp3(audio.name);
                isLast = true;
                return super.onTouchEvent(event);
            }
            if (no_btn.isVisible() && no_btn.getDstRect().intersect(touchrectF)) {
                backgroundMessage.setMessString(element.properties.incorrect.split("#")[lng_code]);
                yes_btn.setVisible(false);
                no_btn.setVisible(false);
                StoryXML.Audio audio = getAudioName(element.properties.w_audio_id);
                playMp3(audio.name);
                isLast = true;
                return super.onTouchEvent(event);
            }

            if(back_btn.isVisible() && back_btn.getDstRect().intersect(touchrectF)){
                isFinish=false;
                isBack=true;

                Intent intent = new Intent(context, PreAndPostActivity.class);
                intent.putExtra("type","post");
                context.startActivity(intent);
                return super.onTouchEvent(event);
            }
            if(exit_btn.isVisible() && exit_btn.getDstRect().intersect(touchrectF)){
                ((StoryViewActivity)context).showExitDialog();
                return super.onTouchEvent(event);
            }
        }
        return super.onTouchEvent(event);
    }

    private Rect rect_show_slider, rect_hide_slider;

    private void loadResources(Gson gson) {

//        String Json = new String(Utility.getResourcesFromSD(Utility.SubFolderName+".xml"));
//        storyXML = gson.fromJson(s, StoryXML.class);

        byte[] bytes =Utility.getResourcesFromSD(Utility.SubFolderName+".xml");
        InputStream targetStream = new ByteArrayInputStream(bytes);
        storyXML = XMLParser.xlmParsing(targetStream);
        elementsArrayList = storyXML.scenes.get(sceneIndex).elements;
        element = elementsArrayList.get(elementIndex);

        StoryXML.Background background = getBackgroundImageName(storyXML.scenes.get(sceneIndex).background);
        bg = Utility.loadImgResource(background.name);// + ".png");

        message = new Message(R.drawable.popupbg, "Click on the blinker to rescue trapped children");

        /*dialog = new Message(R.drawable.speach_l1, "Click on the blinker to rescue trapped children");
        dialog.mess_bgImage.setPosition(0, 0);
        dialog.setMessTextPosition(dialog.mess_bgImage.getX() + (int) (GlobalVariables.xScale_factor * 10), dialog.mess_bgImage.getY() + dialog.mess_bgImage.getHeight() / 4);
        dialog.setVisible(false);*/

        slider_CloseBg = new ShahSprite(Utility.decodeSampledBitmapFromResource(GlobalVariables.getResource, R.drawable.slider_new_extra_mini));
        slider_OpenBg = new ShahSprite(Utility.decodeSampledBitmapFromResource(GlobalVariables.getResource, R.drawable.main_slider));
        slider_left = new ShahSprite(Utility.decodeSampledBitmapFromResource(GlobalVariables.getResource, R.drawable.left));
        slider_right = new ShahSprite(Utility.decodeSampledBitmapFromResource(GlobalVariables.getResource, R.drawable.right));
        slider_play = new ShahSprite(Utility.decodeSampledBitmapFromResource(GlobalVariables.getResource, R.drawable.palyfinalimage));
        slider_pause = new ShahSprite(Utility.decodeSampledBitmapFromResource(GlobalVariables.getResource, R.drawable.pause));
        slider_audio_play = new ShahSprite(Utility.decodeSampledBitmapFromResource(GlobalVariables.getResource, R.drawable.speaker_on));
        slider_audio_stop = new ShahSprite(Utility.decodeSampledBitmapFromResource(GlobalVariables.getResource, R.drawable.speaker_mute));
        slider_home_screen = new ShahSprite(Utility.decodeSampledBitmapFromResource(GlobalVariables.getResource, R.drawable.homefinalimage));

        slider_CloseBg.setPosition(0, GlobalVariables.height - slider_CloseBg.getHeight());
        slider_OpenBg.setPosition(0, GlobalVariables.height - slider_OpenBg.getHeight());

        slider_play.setPosition(GlobalVariables.width / 2 - slider_play.getWidth() / 2, GlobalVariables.height - slider_play.getHeight());
        slider_pause.setPosition(GlobalVariables.width / 2 - slider_pause.getWidth() / 2, GlobalVariables.height - slider_pause.getHeight());

        slider_right.setPosition(slider_pause.getX() + (2 * slider_pause.getWidth()), GlobalVariables.height - slider_right.getHeight());
        slider_left.setPosition(slider_pause.getX() - (2 * slider_pause.getWidth()), GlobalVariables.height - slider_left.getHeight());

        slider_audio_play.setPosition(0, GlobalVariables.height - slider_audio_play.getHeight());
        slider_audio_stop.setPosition(0, GlobalVariables.height - slider_audio_stop.getHeight());
        slider_home_screen.setPosition(GlobalVariables.width - slider_home_screen.getWidth(), GlobalVariables.height - slider_audio_stop.getHeight());

        int x = (int) (GlobalVariables.xScale_factor * 200),
                y = (int) (GlobalVariables.yScale_factor * 697),
                deltaX = (int) (GlobalVariables.xScale_factor * 80),
                deltaY = (int) (GlobalVariables.yScale_factor * 40);
        rect_show_slider = new Rect(x, y, x + deltaX, y + deltaY);

        x = (int) (GlobalVariables.xScale_factor * 200);
        y = (int) (GlobalVariables.yScale_factor * 771);
        deltaX = (int) (GlobalVariables.xScale_factor * 80);
        deltaY = (int) (GlobalVariables.yScale_factor * 40);
        rect_hide_slider = new Rect(x, y, x + deltaX, y + deltaY);

        showCharacters();
        hideSlider();
        slider_CloseBg.setVisible(false);

        int y11 = (int) (GlobalVariables.yScale_factor * 30);
        if (slider_OpenBg.isVisible()) {
            message.mess_bgImage.setPosition(0, slider_OpenBg.getY() - (slider_OpenBg.getHeight() + y11));
            message.setMessTextPosition(message.mess_bgImage.getX() + (int) (GlobalVariables.xScale_factor * 10), message.mess_bgImage.getY() + message.mess_bgImage.getHeight() / 4);

        } else if (!slider_CloseBg.isVisible()) {
            message.mess_bgImage.setPosition(0, slider_CloseBg.getY() - (slider_CloseBg.getHeight() + y11));
            message.setMessTextPosition(message.mess_bgImage.getX() + (int) (GlobalVariables.xScale_factor * 10), message.mess_bgImage.getY() + message.mess_bgImage.getHeight() / 4);
        }
        message.setVisible(false);
        isLoading = false;
        scene = 1;
    }

    private void displaySlider(Canvas g) {
        slider_CloseBg.paint(g, null);
        slider_OpenBg.paint(g, null);
        slider_left.paint(g, null);
        slider_right.paint(g, null);
        slider_play.paint(g, null);
        slider_pause.paint(g, null);
        slider_audio_play.paint(g, null);
        slider_audio_stop.paint(g, null);
        slider_home_screen.paint(g, null);
    }

    private void showSlider() {
        slider_CloseBg.setVisible(false);
        slider_OpenBg.setVisible(true);
        slider_left.setVisible(true);
        slider_right.setVisible(true);
        slider_play.setVisible(false);
        slider_pause.setVisible(true);
        slider_audio_play.setVisible(true);
        slider_audio_stop.setVisible(false);
        slider_home_screen.setVisible(true);
    }

    private void hideSlider() {
        slider_CloseBg.setVisible(true);
        slider_OpenBg.setVisible(false);
        slider_left.setVisible(false);
        slider_right.setVisible(false);
        slider_play.setVisible(false);
        slider_pause.setVisible(false);
        slider_audio_play.setVisible(false);
        slider_audio_stop.setVisible(false);
        slider_home_screen.setVisible(false);
    }

    private void playMp3(String resName) {
        byte[] mp3SoundByteArray = Utility.getResourcesFromSD(resName);
        try {
            // create temp file that will hold byte array
            File tempMp3 = File.createTempFile(resName, "mp3", context.getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3SoundByteArray);
            fos.close();

            mediaPlayer.reset();
            FileInputStream fis = new FileInputStream(tempMp3);
            mediaPlayer.setDataSource(fis.getFD());

            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);

        } catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }

        if (((StoryViewActivity) context).getMediaPlayerLocal() == null) {
            ((StoryViewActivity) context).setMediaPlayer(mediaPlayer);
        }
    }

    @Override
    public void recycle() {
        bg.recycle();
        message.recycle();
//        dialog.recycle();
        backgroundMessage.recycle();
        faded_bg.recycle();
        yes_btn.recycle();
        no_btn.recycle();
        for (ShahSprite shahSprite : characters) {
            shahSprite.recycle();
        }

        slider_CloseBg.recycle();
        slider_OpenBg.recycle();
        slider_left.recycle();
        slider_right.recycle();
        slider_play.recycle();
        slider_pause.recycle();
        slider_audio_play.recycle();
        slider_audio_stop.recycle();
        slider_home_screen.recycle();
        back_btn.recycle();
        exit_btn.recycle();

        System.gc();
    }

}