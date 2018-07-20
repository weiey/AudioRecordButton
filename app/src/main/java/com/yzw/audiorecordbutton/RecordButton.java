package com.yzw.audiorecordbutton;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class RecordButton extends Button {

    private static final int MIN_RECORD_TIME = 1; // 最短录音时间，单位秒
    private static final int RECORD_OFF = 0; // 不在录音
    private static final int RECORD_ON = 1; // 正在录音

    private Dialog mRecordDialog;
    private AudioRecord mAudioRecord;
    private Thread mRecordThread;
    private RecordListener callback;

    private int recordState = 0; // 录音状态
    private float recodeTime = 0.0f; // 录音时长，如果录音时间太短则录音失败
    private boolean isCanceled = false; // 是否取消录音
    private float downY;
    private int voiceLevel = 0; // 录音的音量值

    private TextView record_time;
    private TextView dialogTextView;
    private ImageView dialogImg;
    private Context mContext;

    public RecordButton(Context context) {
        super(context);
        init(context);
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public RecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        this.setText("按住 说话");
    }

    public void setAudioRecord(AudioRecord record) {
        this.mAudioRecord = record;
    }

    public void setRecordCallback(RecordListener callback) {
        this.callback = callback;
    }

    // 录音时显示Dialog
    private void showVoiceDialog(int flag) {
        if (mRecordDialog == null) {
            mRecordDialog = new Dialog(mContext, R.style.theme_alert);
            mRecordDialog.setContentView(R.layout.record_dig);
            dialogImg = (ImageView) mRecordDialog.findViewById(R.id.record_val);
            dialogTextView = (TextView) mRecordDialog.findViewById(R.id.record_text);
            record_time = (TextView) mRecordDialog.findViewById(R.id.record_time);
        }
        switch (flag) {
            case 1:
                dialogImg.setImageResource(R.drawable.icon_record_cancel);
                dialogTextView.setText(mContext.getResources().getString(R.string.release_to_cancel));
                dialogTextView.setTextColor(mContext.getResources().getColor(R.color.color_ff8c8c));
                //this.setText("松开手指 取消录音");
                break;

            default:
                dialogImg.setImageResource(R.drawable.record_1);
                dialogTextView.setText(mContext.getResources().getString(R.string.move_up_to_cancel));
                dialogTextView.setTextColor(mContext.getResources().getColor(R.color.white));
                //this.setText("松开手指 完成录音");
                break;
        }
//        dialogTextView.setTextSize(14);
        mRecordDialog.show();
    }

    // 录音时间太短时Toast显示
    private void showWarnToast(String toastText) {
        Toast.makeText(mContext, toastText, Toast.LENGTH_SHORT).show();
    }

    // 开启录音计时线程
    private void callRecordTimeThread() {
        mRecordThread = new Thread(recordThread);
        mRecordThread.start();
    }

    // 录音Dialog图片随录音音量大小切换
    private void setDialogImage() {


        switch (voiceLevel){
            case 0:
                dialogImg.setImageResource(R.drawable.record_1);
                break;
            case 1:
                dialogImg.setImageResource(R.drawable.record_2);
                break;
            case 2:
                dialogImg.setImageResource(R.drawable.record_3);
                break;
            case 3:
                dialogImg.setImageResource(R.drawable.record_4);
                break;
            case 4:
                dialogImg.setImageResource(R.drawable.record_5);
                break;
            case 5:
                dialogImg.setImageResource(R.drawable.record_6);
                break;
            case 6:
                dialogImg.setImageResource(R.drawable.record_7);
                break;
            case 7:
                dialogImg.setImageResource(R.drawable.record_8);
                break;
            case 8:
                dialogImg.setImageResource(R.drawable.record_8);
                break;
        }


        record_time.setText(formatLongToTimeStr((int)recodeTime));
        if (recodeTime == RECORDER_TIME_MAXTIME - 10) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.the_recording_time_too_long), Toast.LENGTH_SHORT).show();
        }
        if (recodeTime == RECORDER_TIME_MAXTIME) {
            deleteVoiceFile();
            Toast.makeText(mContext, "录音超时", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    // 录音线程
    private Runnable recordThread = new Runnable() {

        @Override
        public void run() {
            recodeTime = 0.0f;
            while (recordState == RECORD_ON) {
                {
                    try {
                        Thread.sleep(100);
                        recodeTime += 0.1;
                        // 获取音量，更新dialog
                        if (!isCanceled) {
//                            voiceValue = mAudioRecorder.getAmplitude();
                            if(mAudioRecord != null){
                                voiceLevel = mAudioRecord.getVolumeLevel();
                            }
                            recordHandler.sendEmptyMessage(1);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler recordHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setDialogImage();
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 按下按钮 
                if (recordState != RECORD_ON) {
                    showVoiceDialog(0);
                    downY = event.getY();
                    recordState = RECORD_ON;
                    startRecorder();
                }

                break;
            case MotionEvent.ACTION_MOVE: // 滑动手指
                float moveY = event.getY();
                if (downY - moveY > 50) {
                    isCanceled = true;
                    showVoiceDialog(1);
                }
                if (downY - moveY < 20) {
                    isCanceled = false;
                    showVoiceDialog(0);
                }
                break;
            case MotionEvent.ACTION_UP: // 松开手指
                if (recordState == RECORD_ON) {
                    recordState = RECORD_OFF;
                    if (mRecordDialog.isShowing()) {
                        mRecordDialog.dismiss();
                    }
                    stopRecorder();

                    voiceLevel = 0;
                    if (isCanceled) {
                        deleteVoiceFile();
                    } else {
                        if (recodeTime < MIN_RECORD_TIME) {
                            showWarnToast(mContext.getResources().getString(R.string.the_recording_time_is_too_short));
                            deleteVoiceFile();
                        } else {
                            if (callback != null) {
                                callback.onRecordFinish(mAudioRecord.getSavePath(), (int)recodeTime);
                            }
                        }
                    }
                    isCanceled = false;
                    this.setText(R.string.audio_record_text);
                }
                break;
        }
        return true;
    }

    private void startRecorder() {
        try {
            if(mAudioRecord != null){
                mAudioRecord.startRecorder();
            }
            callRecordTimeThread();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 停止录音操作
    private void stopRecorder() {
        try {
            mRecordThread.interrupt();
            if(mAudioRecord != null){
                mAudioRecord.stopRecorder();
            }
//            voicePath = savePath + fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 删除录音文件
    private void deleteVoiceFile() {
        release();
        try {

            if(mAudioRecord != null){
                mAudioRecord.deleteFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//
    }

    /***
     * 释放录音及播放器
     */
    private void release() {
        if (recordState == RECORD_ON) {
            stopRecorder();
        }
    }

    private int RECORDER_TIME_MAXTIME = 60 * 3;

    public static String formatLongToTimeStr(int second) {
        int minute = 0;
        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        String strMinute = "";
        if (minute > 0) {
            strMinute = (minute > 9 ? minute + "′" : "0" + minute) + "′";
        }
        String strtime = strMinute + (second > 9 ? second + "″" : "0" + second + "″");
        return strtime;

    }

    public interface RecordListener {
        /**
         * 录音完成
         *
         * @param path
         * @param len
         */
        void onRecordFinish(String path, int len);
    }
}
