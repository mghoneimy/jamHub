package com.Example.iJam.models;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Environment;

import com.Example.iJam.activities.RecordActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mostafa on 7/16/2015.
 */
public abstract class MyAudioManager {

    public static void mixFiles(String url, String fileInput2, String fileOuput){

        String targetfile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp";
        readIntoFile(url, targetfile);

        List<Short> music1 = createMusicArray(Environment.getExternalStorageDirectory().getAbsolutePath()+ "/temp");
        List<Short> music2 = createMusicArray(fileInput2);

        completeStreams(music1, music2);
        short[] music1Array = buildShortArray(music1);
        short[] music2Array = buildShortArray(music2);

        short[] output = new short[music1Array.length];
        for(int i=0; i < output.length; i++){

            float samplef1 = music1Array[i] / 32768.0f;
            float samplef2 = music2Array[i] / 32768.0f;

            float mixed = samplef1 + samplef2;
            // reduce the volume a bit:
            mixed *= 0.8;
            // hard clipping
            if (mixed > 1.0f) mixed = 1.0f;
            if (mixed < -1.0f) mixed = -1.0f;
            short outputSample = (short)(mixed * 32768.0f);

            output[i] = outputSample;
        }
        saveToFile(output, fileOuput);

    }

    private static List<Short> createMusicArray(String fileName){
        List<Short> audioData = new ArrayList<>();
        try {
            File file = new File(fileName);
            InputStream is = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);
            int audioLength = (int) file.length();

            //INSERTING THE DATA IN THE FILE TO FILL THE UADIODATA ARRAY
            int i = 0;
            while (dis.available() > 0 && i < audioLength) {
                short data = dis.readShort();
                audioData.add(data);
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return audioData;
    }

    private static void completeStreams(List<Short> track1, List<Short> track2){
        int size1 = track1.size();
        int size2 = track2.size();
        if(size1 > size2)
            completeList(track2, size1-size2);
        else if(size2 > size1)
            completeList(track1, size2-size1);
    }

    private static void completeList(List<Short> track, int difference){
        for(int i=0; i<difference; i++){
            Short data = 0;
            track.add(data);
        }
    }

    private static short[] buildShortArray(List<Short> track){
        short[] shorts = new short[track.size()];
        for(int i=0; i<track.size(); i++)
            shorts[i] = track.get(i);
        return shorts;
    }

    private static void saveToFile(short[] mix, String targetFile){
        try {
            File output = new File(targetFile);
            OutputStream os = new FileOutputStream(output);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);

            for(int i=0; i<mix.length; i++)
                dos.writeShort(mix[i]);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readIntoFile(String url, String outFileName){
        try {
            URL source = new URL(url);
            BufferedInputStream in = new BufferedInputStream(source.openStream());
            FileOutputStream fos = new FileOutputStream(outFileName);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);

            byte[] data = new byte[1024];
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                bout.write(data, 0, x);
            }
            fos.flush();
            bout.flush();
            fos.close();
            bout.close();
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
