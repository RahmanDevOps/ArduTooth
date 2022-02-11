package zmq.com.ystlibrary.messages;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import zmq.com.ystlibrary.interfaces.StoryAction;
import zmq.com.ystlibrary.sprite.ShahSprite;
import zmq.com.ystlibrary.utility.GlobalVariables;
import zmq.com.ystlibrary.utility.StringDraw;
import zmq.com.ystlibrary.utility.Utility;

public  class Message implements StoryAction {

    public ShahSprite mess_bgImage;
    public String messString;
    public int messPosX;
    public int messPosY;
    public int messPadding=(int)(20* GlobalVariables.xScale_factor);

    protected boolean visible=true;
    public int messLinePixelWidth;

    public boolean isLastMessage=false;

    public Message(int resId, String messString) {

        isLastMessage=false;
        this.mess_bgImage = new ShahSprite(Utility.decodeSampledBitmapFromResource(GlobalVariables.getResource, resId));

        messLinePixelWidth= mess_bgImage.getWidth()-(int)(GlobalVariables.xScale_factor*10);
        this.setMessString(messString);
        int xPos=GlobalVariables.width/2- mess_bgImage.getWidth()/2;
        int yPos=GlobalVariables.height/2- mess_bgImage.getHeight()/2;
        this.mess_bgImage.setPosition(xPos, yPos);
        this.setMessTextPosition(xPos + messPadding / 2, yPos + messPadding / 2);
    }

    public void setMessString(String messString) {
        messLinePixelWidth = mess_bgImage.getWidth()-messPadding-(int)(GlobalVariables.xScale_factor*10);
        messString = messString.replace("\n"," ");
        String[] words = StringDraw.extractWords(messString + " ", ' ');
        String[] line = StringDraw.breakTextIntoMultipleLine(words,messLinePixelWidth);

        this.messString = StringDraw.joinStringWithCharacter(line,"\n");
    }

    @Override
    public void recycle() {
        if (!mess_bgImage.sourceImage.isRecycled()) {
            mess_bgImage.sourceImage.recycle();

            isLastMessage=false;
        }
    }

    @Override
    public void update(Canvas g, Paint paint) {

        if(visible){

            if(isLastMessage)
            {
                Paint paint2 = new Paint();
                paint2.setColor(Color.WHITE);
                paint2.setStrokeWidth(1);
                paint2.setStyle(Paint.Style.STROKE);

                final RectF rect = new RectF();
                paint2.setStyle(Paint.Style.FILL);

                rect.set(messPosX-15, messPosY-40, GlobalVariables.width-messPosX-10, getRectHeight(messString, messPosX, messPosY, g, paint)+120);
                g.drawRoundRect(rect, 15, 15, paint2);

            }
            else{
                mess_bgImage.paint(g,null);
            }

            drawMyText(messString, messPosX, messPosY, g, paint);
        }
    }

    public int getRectHeight(String text, int x, int y, Canvas g, Paint paint){


        y=y+10;
        int height = y;
        for (String line: text.split("\n"))
        {
            y += -paint.ascent() + paint.descent();
        }

        return y-height;
    }

    public void drawMyText(String text, int x, int y, Canvas g, Paint paint){

        y=y+10;
           for (String line: text.split("\n"))
           {
               g.drawText(line, x, y, paint);
               y += -paint.ascent() + paint.descent();
           }


    }
    public void setMessTextPosition(int messPosX,int messPosY) {
        this.messPosX = messPosX;
        this.messPosY = messPosY;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

   /* public void setMessLinePixelWidth(int messLinePixelWidth) {
        this.messLinePixelWidth = messLinePixelWidth;
        this.setMessString(messString);
    }

    public void setPosition(int xPos, int yPos) {
        this.mess_bgImage.setPosition(xPos, yPos);
        this.setMessTextPosition(xPos+messPadding/2,yPos+messPadding/2);
        }*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

}
