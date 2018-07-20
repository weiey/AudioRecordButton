package com.yzw.audiorecordbutton;

/**
 * 作者：ye.zhiwei
 * 版本：
 * 创建日期：2018/7/20
 * 描述：
 * 修订历史：
 */

public interface AudioRecord {
    boolean isRelease();
    void startRecorder();
    void stopRecorder();
    void deleteFile();
    void setSavePath(String path);
    String getSavePath();
    int getVolumeLevel();
}
