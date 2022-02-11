package zmq.com.ystlibrary.download;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.ResponseBody;
import retrofit2.Call;
import zmq.com.ystlibrary.R;
import zmq.com.ystlibrary.canvas.DownloadResourceActivity;
import zmq.com.ystlibrary.model.PreAndPost;
import zmq.com.ystlibrary.rest.ApiClient;
import zmq.com.ystlibrary.rest.ApiInterface;
import zmq.com.ystlibrary.utility.Utility;

public class DownloadService extends IntentService {

    private final String FOLDER_NAME = "STORIES";
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private int totalFileSize;
    private String zipURL = "";

    public DownloadService() {
        super("Download Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("Download")
                .setContentText("Downloading File")
                .setSmallIcon(R.drawable.no)
                .setAutoCancel(true);
        notificationManager.notify(0, notificationBuilder.build());
        zipURL = intent.getStringExtra("zipurl");
        downloadPreAndPost();
        initDownload();
    }

    private void downloadPreAndPost() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<PreAndPost> call = apiService.getPreAndPost(ApiClient.BASE_URL_PrePost+"api/MiraApi/PreAndPostQuestions", Integer.valueOf(Utility.SubFolderName));
        PreAndPost preAndPost = null;
        try {
            preAndPost = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (preAndPost != null) {
            String destinationFolder = new File(Environment.getExternalStorageDirectory() + "/STORY/" + Utility.SubFolderName).getAbsolutePath();
            File directory = new File(destinationFolder);
            if (!directory.exists())
                directory.mkdirs();
            FileOutputStream fileOut = null;
            try {
                directory = new File(destinationFolder, "PreAndPostTest.txt");
                directory.createNewFile();
                fileOut = new FileOutputStream(directory);
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
                objectOut.writeObject(preAndPost);
                objectOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (preAndPost.PreTestDetails != null && preAndPost.PreTestDetails.PreTestUrl != null) {
                try {
                    Call<ResponseBody> preTestCall = apiService.downloadQuesAudioZip(preAndPost.PreTestDetails.PreTestUrl);
                    ResponseBody responseBody = preTestCall.execute().body();
                    writeResponseZip(responseBody, "PreTest.zip","PreTest");

                } catch (Exception e) {
                    sendIntent(new Download(), DownloadResourceActivity.EXCEPTION_ZIP_PROGRESS);
                    e.printStackTrace();
                }
            }

            if (preAndPost.PostTestDetails != null && preAndPost.PostTestDetails.PostTestUrl != null) {
                try {
                    Call<ResponseBody> preTestCall = apiService.downloadQuesAudioZip(preAndPost.PostTestDetails.PostTestUrl);
                    ResponseBody responseBody = preTestCall.execute().body();
                    writeResponseZip(responseBody, "PostTest.zip","PostTest");

                } catch (Exception e) {
                    sendIntent(new Download(), DownloadResourceActivity.EXCEPTION_ZIP_PROGRESS);
                    e.printStackTrace();
                }
            }
        }
    }

    private void writeResponseZip(ResponseBody responseBody, String fileName , String folder) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            int count;
            byte data[] = new byte[1024 * 4];
            inputStream = responseBody.byteStream();
            String destinationFolder = new File(Environment.getExternalStorageDirectory() + "/STORY/"+ Utility.SubFolderName+"/Zip").getAbsolutePath();
            File directory = new File(destinationFolder);
            if (directory.exists()) {
                directory.delete();
            }
            directory.mkdirs();

            File outputFile = new File(directory, fileName);
            outputStream = new FileOutputStream(outputFile);

            while ((count = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, count);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            unzip(outputFile,new File(Environment.getExternalStorageDirectory() + "/STORY/"+ Utility.SubFolderName+"/"+folder).getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void unzip(File zipFile, String location) throws IOException {
        try {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
            try {
                ZipEntry ze;
                while ((ze = zin.getNextEntry()) != null) {
                    String path = location + File.separator + ze.getName();

                    if (ze.isDirectory()) {
                        File unzipFile = new File(path);
                        if (!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    } else {
                        FileOutputStream fout = new FileOutputStream(path, false);

                        try {
                            for (int c = zin.read(); c != -1; c = zin.read()) {
                                fout.write(c);
                            }
                            zin.closeEntry();
                        } finally {
                            fout.close();
                        }
                    }
                }
                zipFile.delete();
            } finally {
                zin.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDownload() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        try {
            File zipFile = new File(Environment.getExternalStorageDirectory() + "/" + Utility.FOLDER_NAME, "Zip" + "/" + Utility.SubFolderName + ".zip");

            String range = "0";
            if (zipFile.exists()) {
                range = "bytes=" + zipFile.length() + "-";
            }

            zipURL = zipURL.substring(1, zipURL.length());
            Call<ResponseBody> call = apiService.downloadZipRange(zipURL, range);
            ResponseBody responseBody = null;
            responseBody = call.execute().body();
            totalFileSize = 0;
            writeResponseBodyToDisk(responseBody, Utility.SubFolderName + ".zip", zipFile);

        } catch (Exception e) {
            sendIntent(new Download(), DownloadResourceActivity.EXCEPTION_ZIP_PROGRESS);
            e.printStackTrace();
        }
    }

    private void writeResponseBodyToDisk(ResponseBody body, String fileName, File downloadedFile) {
        InputStream bis = null;
        OutputStream output = null;
        try {
            int count;
            byte data[] = new byte[1024 * 4];
            long fileSize = body.contentLength();
            bis = body.byteStream();//new BufferedInputStream(body.byteStream(), 1024 * 8);
            String destinationFolder = new File(Environment.getExternalStorageDirectory() + "/" + FOLDER_NAME + "/Zip").getAbsolutePath();
            File directory = new File(destinationFolder);
            if (!directory.exists()) {
                directory.mkdirs();
            } else {
                directory.delete();
                directory.mkdirs();
            }

            File outputFile = new File(directory, fileName);
            output = new FileOutputStream(outputFile, true);
            long total = 0;
            Download download = new Download();

            if (downloadedFile.exists()) {
                total += downloadedFile.length();
                fileSize += downloadedFile.length();
            }


            while ((count = bis.read(data)) != -1) {

                total += count;
                totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
                double current = Math.round(total / (Math.pow(1024, 2)));

                int progress = (int) ((total * 100) / fileSize);
                download.setTotalFileSize(totalFileSize);

                download.setCurrentFileSize((int) current);
                download.setProgress(progress);
                sendNotification(download, DownloadResourceActivity.MESSAGE_PROGRESS);
                output.write(data, 0, count);

//                System.out.println("totalFileSize "+(fileSize / (Math.pow(1024, 2)))+" Current "+Math.round(total / (Math.pow(1024, 2))));
            }
            onDownloadComplete();
            output.flush();
            output.close();
            bis.close();

        } catch (Exception e) {
            sendIntent(new Download(), DownloadResourceActivity.EXCEPTION_ZIP_PROGRESS);
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendNotification(Download download, String action) {

        sendIntent(download, action);
        notificationBuilder.setProgress(100, download.getProgress(), false);
        notificationBuilder.setContentText("Downloading file " + download.getCurrentFileSize() + "/" + totalFileSize + " MB");
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendIntent(Download download, String action) {

        Intent intent = new Intent(action);
        intent.putExtra("download", download);
        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete() {
        unZipIt();

        Download download = new Download();
        download.setProgress(100);
        sendIntent(download, DownloadResourceActivity.MESSAGE_PROGRESS);

        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText("File Downloaded");
        notificationManager.notify(0, notificationBuilder.build());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }

    public void unZipIt() {
        String zipFile = new File(Environment.getExternalStorageDirectory() + "/" + Utility.FOLDER_NAME, "Zip" + "/" + Utility.SubFolderName + ".zip").getAbsolutePath();

        String destinationFolder = new File(Environment.getExternalStorageDirectory() + "/" + Utility.FOLDER_NAME + "/" + Utility.SubFolderName).getAbsolutePath();
        File directory = new File(destinationFolder);

        if (!directory.exists())
            directory.mkdirs();

        byte[] buffer = new byte[2048];

        try {
            SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, sks);


            FileInputStream fInput = new FileInputStream(zipFile);
            ZipInputStream zipInput = new ZipInputStream(fInput);

            ZipEntry entry = zipInput.getNextEntry();

            while (entry != null) {
                String entryName = entry.getName();
                File file = new File(destinationFolder + File.separator + entryName);

                System.out.println("Unzip file " + entryName + " to " + file.getAbsolutePath());

                // create the directories of the zip directory
                if (entry.isDirectory()) {
                    File newDir = new File(file.getAbsolutePath());
                    if (!newDir.exists()) {
                        boolean success = newDir.mkdirs();
                        if (success == false) {
                            System.out.println("Problem creating Folder");
                        }
                    }
                } else {
                    FileOutputStream fOutput = new FileOutputStream(file);

                    if (!entry.getName().contains(".xml")) {
                        CipherOutputStream cos = new CipherOutputStream(fOutput, cipher);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        int count = 0;
                        while ((count = zipInput.read(buffer)) > 0) {
                                                       if (entry.getName().contains(".png")) {
                                System.out.println("Entry..." + entry.getName());
                                byteArrayOutputStream.write(buffer, 0, count);
                            } else {
                                fOutput.write(buffer, 0, count);
                            }
                        }
                        if (entry.getName().contains(".png")) {
                            fOutput.flush();
                            fOutput.write(cipher.doFinal(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.toByteArray().length));
                        }

                        fOutput.close();
                      cos.close();
                    } else {
                        unpackZipXML( file, zipInput );
                        fOutput.close();
                    }
                }
                zipInput.closeEntry();
                entry = zipInput.getNextEntry();
            }

            zipInput.closeEntry();
            zipInput.close();
            fInput.close();

            File index = new File(zipFile);
            if (index.exists()) {
                index.delete();
            }

            index = new File(Environment.getExternalStorageDirectory() + "/" + Utility.FOLDER_NAME, "Zip");
            if (index.exists() && index.listFiles().length == 0) {
                index.delete();
            }

        } catch (IOException e) {
            sendIntent(new Download(), DownloadResourceActivity.EXCEPTION_UNZIP_PROGRESS);
            e.printStackTrace();
        } catch (Exception e) {
            sendIntent(new Download(), DownloadResourceActivity.EXCEPTION_UNZIP_PROGRESS);
            e.printStackTrace();
        }
    }

    private void writeXML(File file, ZipInputStream zipInput) throws IOException {
        StringBuilder s = new StringBuilder();
        byte[] buffer = new byte[1024];
        int read = 0;
        while ((read = zipInput.read(buffer, 0, 1024)) >= 0) {
            s.append(new String(buffer, 0, read));
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(s.toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean unpackZipXML(File file, ZipInputStream zis)
    {
        try
        {
            byte[] buffer = new byte[1024];
            int count;
            FileOutputStream fout = new FileOutputStream(file);

            while ((count = zis.read(buffer)) != -1)
            {
                fout.write(buffer, 0, count);
            }
            fout.close();
            // zis.closeEntry();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }




}