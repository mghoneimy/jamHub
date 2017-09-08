package com.Example.iJam.models;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Mostafa on 7/16/2015.
 */
public class MyAudioRecorder {

    String outputFile;
    private boolean isRecording;

    public MyAudioRecorder(String outputFile){
        this.outputFile = outputFile;
        isRecording = false;
    }

    public void recordSound(){
        File file = new File(outputFile);

        // Delete any previous recording.
        if (file.exists())
            file.delete();

        try {
            file.createNewFile();

            // Create a DataOuputStream to write the audio data into the saved file.
            OutputStream os = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            DataOutputStream dos = new DataOutputStream(bos);


            // Create a new AudioRecord object to record the audio.
            int bufferSize = AudioRecord.getMinBufferSize(MyTrackPlayer.FREQUENCY, MyTrackPlayer.CHANNEL_CONFIGURATION,
                    MyTrackPlayer.AUDIO_ENCODING);
            AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, MyTrackPlayer.FREQUENCY,
                    MyTrackPlayer.CHANNEL_CONFIGURATION, MyTrackPlayer.AUDIO_ENCODING, bufferSize);

            short[] buffer = new short[bufferSize];
            audioRecord.startRecording();

            int Fsize = 0;
            while (isRecording) {
                int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
                Fsize += bufferReadResult;
                for (int i = 0; i < bufferReadResult; i++)
                    dos.writeShort(buffer[i]);
            }

            audioRecord.stop();
            audioRecord.release();
            dos.close();

        }  catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            Log.e("shit", e.toString());
            //e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void startRecording(){
        isRecording = true;
        recordSound();
    }

    public void stopRecording(){
        isRecording = false;
    }

    public boolean isRecording(){
        return isRecording;
    }
}
