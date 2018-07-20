package com.yzw.audiorecordbutton;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecordButton mRecordButton = (RecordButton)findViewById(R.id.mRecordButton);
        mRecordButton.setAudioRecord(new AudioRecorder());
        mRecordButton.setRecordCallback(new RecordButton.RecordListener() {
            @Override
            public void onRecordFinish(String path, int len) {

            }
        });
    }
}
