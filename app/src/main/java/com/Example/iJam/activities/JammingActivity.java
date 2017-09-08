package com.Example.iJam.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.Example.iJam.R;
import com.Example.iJam.models.MyAudioManager;
import com.Example.iJam.models.MyAudioRecorder;
import com.Example.iJam.models.MyTrackPlayer;
import com.Example.iJam.models.Track;
import com.Example.iJam.network.NetworkManager;
import com.Example.iJam.network.ServerManager;
import com.android.volley.toolbox.NetworkImageView;

import java.io.IOException;

public class JammingActivity extends ActionBarActivity implements View.OnClickListener {

    private String outputFile = null;
    TextView timer, countdown;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    NetworkImageView imagetrack;
    private MyTrackPlayer player = new MyTrackPlayer();
    private MyAudioRecorder recorder = null;
    Track myTrack;
    private int secs;

    ImageView recordbut, stopbut;

    AsyncTask StartPlayer = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] params) {
            player.playStream(myTrack.getTrackUrl());
            return null;
        }
    };

    AsyncTask StartRecorder = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] params) {
            recorder.startRecording();
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jamming);

        imagetrack=(NetworkImageView)findViewById(R.id.jamming_img_videoimage);
        recordbut=(ImageView)findViewById(R.id.jamming_image_record);
        stopbut=(ImageView)findViewById(R.id.jamming_image_stop);
        countdown=(TextView)findViewById(R.id.countdown);
        timer=(TextView)findViewById(R.id.timer);
        Button next = (Button) findViewById(R.id.jamming_bt_next);

        myTrack = (Track) getIntent().getSerializableExtra("track");
        final String imgUrl = myTrack.getImgUrl();
        final String trackUrl= myTrack.getTrackUrl();

        recordbut.setOnClickListener(this);
        stopbut.setOnClickListener(this);
        imagetrack.setOnClickListener(this);
        next.setOnClickListener(this);

        imagetrack.setImageUrl(imgUrl, NetworkManager.getInstance(getApplicationContext()).getImageLoader());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_jamming, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        case R.id.jamming_image_record:
            new CountDownTimer(5000,1000){

                @Override
                public void onTick(long millisUntilFinished) {
                    countdown.setText(""+ millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    try {
                        countdown.setVisibility(View.INVISIBLE);

                        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/premix";
                        recorder = new MyAudioRecorder(outputFile);

                        startMyTask(StartPlayer);
                        startMyTask(StartRecorder);

                        startTime = SystemClock.uptimeMillis();
                        customHandler.postDelayed(updateTimerThread, 0);
                        Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
                        recordbut.setEnabled(false);
                        stopbut.setEnabled(true);
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }.start();
        break;

        case R.id.jamming_image_stop:
            timeSwapBuff += timeInMilliseconds;
            customHandler.removeCallbacks(updateTimerThread);

            recorder.stopRecording();
            stopbut.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_LONG).show();

            player.finish();
            break;

        /*case R.id.jamming_img_videoimage:
            if(!player.isPlaying())
                player.resume();
            else
                player.pause();
            break;*/

        case R.id.jamming_bt_next:
            if(recorder != null) {
                if(recorder.isRecording())
                    recorder.stopRecording();

                if(player.isPlaying()){
                    player.finish();
                }

                Intent intent = new Intent(this, UploadTrackActivity.class);
                intent.putExtra("filename", Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording");
                intent.putExtra("id", myTrack.getID());
                intent.putExtra("url", myTrack.getTrackUrl());
                int parentDur = myTrack.getDuration();
                if (parentDur > secs)
                    secs = parentDur;
                intent.putExtra("duration", secs);

                startActivity(intent);
                finish();
            }
            else
                Toast.makeText(getApplicationContext(), "Please start recording before proceeding to the upload page", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(recorder != null && recorder.isRecording()) {
            recorder.stopRecording();
        }
        if(player.isPlaying())
            player.finish();
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            int Rsecs = secs % 60;
            timer.setText("" + String.format("%02d", mins) + ":" + String.format("%02d", Rsecs));
            customHandler.postDelayed(this, 0);
        }
    };

    @TargetApi(Build.VERSION_CODES.HONEYCOMB) // API 11
    void startMyTask(AsyncTask asyncTask) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            asyncTask.execute();
    }
}
