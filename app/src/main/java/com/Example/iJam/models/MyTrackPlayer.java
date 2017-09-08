package com.Example.iJam.models;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Mostafa on 7/21/2015.
 */
public class MyTrackPlayer {

    public static final int FREQUENCY = 44100;
    public static final int CHANNEL_CONFIGURATION = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    public static final int AUDIO_ENCODING =  AudioFormat.ENCODING_PCM_16BIT;
    final static int STREAM_TYPE = AudioManager.STREAM_MUSIC;
    public static final int PLAY_STATIC = AudioTrack.MODE_STATIC;
    public static final int PLAY_STREAM = AudioTrack.MODE_STREAM;

    private AudioTrack track;
    private byte data[];
    private boolean playing;
    boolean open;

    public MyTrackPlayer(){
        int buffSize = AudioTrack.getMinBufferSize(FREQUENCY, CHANNEL_CONFIGURATION, AUDIO_ENCODING);
        track = new AudioTrack(STREAM_TYPE, FREQUENCY, CHANNEL_CONFIGURATION, AUDIO_ENCODING,buffSize,PLAY_STREAM);
        data = new byte[buffSize / 2];
        playing = false;
        open = true;
    }

    public void playStream(String streamURL){
        try {
            URL source = new URL(streamURL);
            BufferedInputStream in = new BufferedInputStream(source.openStream());

            track.play();
            playing = true;
            int x = 0;

            while(open) {
                while ((x = in.read(data, 0, 1024)) >= 0 && playing) {
                    track.write(data, 0, x);
                }
            }
            in.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resume(){
        track.play();
        playing = true;
    }

    public void pause(){
        playing = false;
        track.pause();
    }

    public void finish(){
        open = false;
        playing  = false;
        track.stop();
        track.release();
    }

    public boolean isPlaying(){
        return playing;
    }
}
