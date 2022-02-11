package zmq.com.ystlibrary.canvas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import zmq.com.ystlibrary.utility.GlobalVariables;


public abstract class BaseSurface extends SurfaceView implements SurfaceHolder.Callback {

    public GameThread gameThread;
    public SurfaceHolder surfaceHolder;
    public Context context;
    public int delayCounter;
    public boolean isDelay = true;
    private Long lastMiliSeconds = System.currentTimeMillis();
    public Paint paint;
    public int width = GlobalVariables.width;
    public int height = GlobalVariables.height;
    public static boolean isFinish = false,isBack=false;
    public boolean isBookMarkStory=false;

    @SuppressLint("ResourceType")
    public BaseSurface(Context context) {
        super(context);
        this.context = context;
        paint = GlobalVariables.paint;
        paint.setTextSize((int) (GlobalVariables.xScale_factor * 20));
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        this.setId(1234);
        isFinish = false;
        isBack=false;
    }

    protected void delayCount() {
        long currentMilisecond = System.currentTimeMillis();

        long temp = (currentMilisecond - lastMiliSeconds) / 1000;
        if (temp >= 1) {
            lastMiliSeconds = currentMilisecond;
            delayCounter = delayCounter + 1;
        }
    }

    abstract protected void drawSomething(Canvas g);

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        System.out.println(" BaseSurface surfaceCreated Called...");

        boolean isCreate = ((StoryViewActivity) context).isRecreate;
        System.out.println(" BaseSurface isCreate Called..." + isCreate);
        if (gameThread == null && isCreate) {
            System.out.println(" BaseSurface gameThread null...");
            gameThread = new GameThread(surfaceHolder, this);
            gameThread.start();
            ((StoryViewActivity) context).isRecreate = false;
        } else {
            System.out.println(" BaseSurface gameThread not null...");
        }
        gameThread = new GameThread(surfaceHolder, this);
        gameThread.start();
    }

    public void startGameThread() {
        System.out.println(" BaseSurface startGameThreadl...");

        if (gameThread == null) {
            gameThread = new GameThread(surfaceHolder, this);
            gameThread.start();
        }
        gameThread.setName("Worker Thread");
    }

    public void setHolder() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (gameThread != null) {
            gameThread.running = false;
            gameThread.interrupt();
            gameThread = null;
        }
        surfaceHolder.removeCallback(this);
        System.out.println(" BaseSurface surfaceDestroyed Called...");
    }

    public class GameThread extends Thread {
        SurfaceHolder _suHolder;
        BaseSurface _myMycanvas;
        public boolean running = true;

        public GameThread(SurfaceHolder surfaceHolder, BaseSurface mycanvas) {
            // TODO Auto-generated constructor stub
            _suHolder = surfaceHolder;
            _myMycanvas = mycanvas;

        }

        @Override
        public void run() {
            Canvas canvas;
            while (running) {
                canvas = null;
                try {
                    canvas = _suHolder.lockCanvas(null);
                    synchronized (_suHolder) {
                        if (running) {
                            _myMycanvas.drawSomething(canvas);
                        }
                    }
                    try {
                        Thread.sleep(GlobalVariables.sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (canvas != null)
                            _suHolder.unlockCanvasAndPost(canvas);
                         System.out.println("123");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }

    }

}

