package com.yzw.audiorecordbutton;

import android.os.Environment;

import com.czt.mp3recorder.MP3Recorder;

import java.io.File;

/**
 * 作者：ye.zhiwei
 * 版本：
 * 创建日期：2018/7/20
 * 描述：
 * 修订历史：
 */

public class AudioRecorder implements AudioRecord{
    private double mEMA = 0.0;
    private MP3Recorder mRecorder = null;
    String savePath = "";
    String fileName = "";
    @Override
    public boolean isRelease() {
        return  MP3Recorder.isRelease();
    }

    @Override
    public void startRecorder() {
        try {
            mRecorder = new MP3Recorder(createNewFile());
            mRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder = null;
        }
    }

    @Override
    public void deleteFile() {
        File newPath = new File(savePath);
        if (newPath.exists()) {
            newPath.delete();
        }
    }

    @Override
    public void setSavePath(String path) {//../dir/xxxxfile.mp3
        savePath = path;
    }

    @Override
    public String getSavePath() {
        return savePath;
    }
    private File createNewFile(){
        try {
            // 判断是否挂载了SD卡
            String storageState = Environment.getExternalStorageState();
            if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                String savedir = Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/audio/";// 存放照片的文件夹

                File dir = new File(savedir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String fileName = System.currentTimeMillis() + ".mp3";// 语音命名
                File myFilePath = new File(dir,fileName);
                if (!myFilePath.exists()) {
                    myFilePath.createNewFile();
                }
                savePath = myFilePath.getAbsolutePath();
                return myFilePath;
            }
        } catch (Exception e) {
        }
        return null;
    }



    @Override
    public int getVolumeLevel() {
        double voiceValue = mRecorder.getVolume();
        if (voiceValue < 600.0) {
            return 0;
        } else if (voiceValue > 600.0 && voiceValue < 1000.0) {
            return 1;
        } else if (voiceValue > 1000.0 && voiceValue < 1200.0) {
            return 2;
        } else if (voiceValue > 1200.0 && voiceValue < 1400.0) {
            return 3;
        } else if (voiceValue > 1400.0 && voiceValue < 1600.0) {
            return 4;
        } else if (voiceValue > 1600.0 && voiceValue < 1800.0) {
            return 5;
        } else if (voiceValue > 1800.0 && voiceValue < 2000.0) {
            return 6;
        } else if (voiceValue > 2000.0 && voiceValue < 3000.0) {
            return 7;
        } else if (voiceValue > 3000.0) {
            return 8;
        }
        return 0;
    }


}
