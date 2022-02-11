package zmq.com.ystlibrary.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;
import zmq.com.ystlibrary.sprite.ShahSprite;
import static android.content.Context.MODE_PRIVATE;


public class Utility {
    public static final String STORYLISTJSONKEY = "json";
    public static final String FOLDER_NAME = "STORIES";
    public static String SubFolderName="Icons";
    public static int pregnant_id = 0;
    public static int non_pregnant_id = 0;
    public static int group_id = 0;
    public static int mira_id = 0;
    public static int ExternalMiraID=0;
    public static int ExternalUserID=0;
    public static String pregnant_name="";
    public static String non_pregnant_name="";
    public static String prestartTime="";
    public static String prestartDate="";
    public static String precloseTime="";
    public static Integer precloseFlag=0;
    public static String poststartTime="";
    public static String poststartDate="";
    public static String postcloseTime="";
    public static Integer postcloseFlag=0;
    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        int imageHeight = (int) (options.outHeight * GlobalVariables.yScale_factor);
        int imageWidth = (int) (options.outWidth * GlobalVariables.xScale_factor);
        options.inJustDecodeBounds = false;
        return Utility.getResizedBitmap(BitmapFactory.decodeResource(res, resId, options), imageWidth, imageHeight);
    }

    public static Bitmap decodeSampledBitmapFromByte(byte[] bytes) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        int imageHeight = (int) (options.outHeight * GlobalVariables.yScale_factor);
        int imageWidth = (int) (options.outWidth * GlobalVariables.xScale_factor);

        options.inJustDecodeBounds = false;
        return Utility.getResizedBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options), imageWidth, imageHeight);

    }


    public static boolean isFileExist(String fileName) {
        File sdcard = Environment.getExternalStorageDirectory();
        File dir = new File(sdcard.getAbsolutePath() + "/" + FOLDER_NAME +"/"+SubFolderName+  "/" + fileName);
        if (dir.exists()) return true;
        return false;
    }

    public static void writeInPreference(Context context,String key,String json) {
        SharedPreferences mPrefs = context.getSharedPreferences("MyPref",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString(key, json);
        prefsEditor.commit();

    }

    public static String readPreference(Context context,String key) {
        SharedPreferences mPrefs =  context.getSharedPreferences("MyPref",MODE_PRIVATE);
        String json = mPrefs.getString(key, "NA");
        if (json.equalsIgnoreCase("NA")) return null;
        return json;
    }

    public static void removePreference(Context context,String key){
        SharedPreferences mPrefs = ((Activity) context).getSharedPreferences("MyPref",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.remove(key);
        prefsEditor.commit();

    }

    public static byte[] getResourcesFromSD(String imageName) {
        try {
            SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, sks);

            File sdcard = Environment.getExternalStorageDirectory();
            File dir = new File(sdcard.getAbsolutePath() + "/" + FOLDER_NAME + "/" + SubFolderName + "/" + imageName);

            File file = dir;
            FileInputStream fin = null;
            CipherInputStream cis=null;

            try {
                fin = new FileInputStream(file);
                cis = new CipherInputStream(fin, cipher);
                byte fileContent[] = new byte[(int) file.length()];

                    if (imageName.contains(".png")) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        int b;
                        byte[] d = new byte[1024];
                        while ((b = fin.read(d)) != -1) {
                            baos.write(d, 0, b);
                        }
                        baos.flush();
                        return cipher.doFinal(baos.toByteArray(), 0, baos.toByteArray().length);
                    } else {
                        fin.read(fileContent);
                    }

                fin.read(fileContent);

                return fileContent;
            } catch (FileNotFoundException e) {
                System.out.println("File not found" + e);
            } catch (IOException ioe) {
                System.out.println("Exception while reading file " + ioe);
            } finally {
                try {
                    if (fin != null) {
                        fin.close();
                        cis.close();
                    }
                } catch (IOException ioe) {
                    System.out.println("Error while closing stream: " + ioe);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static ShahSprite loadImgResource(String resName) {
        byte[] fileContent = getResourcesFromSD(resName);
        ShahSprite shahSprite = new ShahSprite(Utility.decodeSampledBitmapFromByte(fileContent));
        return shahSprite;
    }

    public static boolean writeResponseBodyToDisk(InputStream stream, String fileName) {
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File dir = new File(sdcard.getAbsolutePath() + "/" + FOLDER_NAME + "/"+SubFolderName+ "/");
            if (!dir.exists())
                dir.mkdir();
            File futureStudioIconFile = new File(dir, fileName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

//                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

//                inputStream = body.byteStream();
                inputStream = stream;
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
//                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

}
