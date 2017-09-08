package com.Example.iJam.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Example.iJam.R;
import com.Example.iJam.models.MyAudioRecorder;

public class RecordActivity extends ActionBarActivity implements View.OnClickListener {

    private int secs;
    private TextView timer;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    ImageView recordbut,stopbut;
    private String outputFile = null;
    private MyAudioRecorder recorder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Button next;

        timer=(TextView)findViewById(R.id.timer);
        recordbut=(ImageView)findViewById(R.id.record_image_record);
        stopbut=(ImageView)findViewById(R.id.record_image_stop);
        next=(Button)findViewById(R.id.record_btn_next);

        next.setOnClickListener(this);
        recordbut.setOnClickListener(this);
        stopbut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.record_btn_next:
                if(recorder != null) {
                    if(recorder.isRecording())
                        recorder.stopRecording();

                    Intent intent = new Intent(RecordActivity.this, UploadTrackActivity.class);
                    intent.putExtra("filename", outputFile);
                    intent.putExtra("duration", secs);
                    startActivity(intent);
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), "Please start recording before proceeding to the upload page", Toast.LENGTH_SHORT).show();
                break;

            case R.id.record_image_record:
                try {
                    outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording";
                    recorder = new MyAudioRecorder(outputFile);
                    new AsyncTask<Void, Void, Void>(){

                        @Override
                        protected Void doInBackground(Void... params) {
                            recorder.startRecording();
                            return null;
                        }
                    }.execute();

                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                    Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();

                    recordbut.setEnabled(false);
                    stopbut.setEnabled(true);
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;

            case R.id.record_image_stop:
                timeSwapBuff += timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);

                recorder.stopRecording();
                stopbut.setEnabled(false);
                Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_LONG).show();
                break;
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(recorder != null && recorder.isRecording()) {
            recorder.stopRecording();
        }
    }
}
