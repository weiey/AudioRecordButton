package com.yzw.audiorecordbutton;

import android.os.Environment;

import java.io.File;

import com.czt.mp3recorder.MP3Recorder;

/**
 * 录音工具包
 *
 */
public class AudioRecordUtils {
	
	static final private double EMA_FILTER = 0.6;

//	private MediaRecorder mRecorder = null;
	private double mEMA = 0.0;
	
//	private RecMicToMp3 mRecorder = null;
	private  MP3Recorder mRecorder = null;//new MP3Recorder(new File(Environment.getExternalStorageDirectory(),"test.mp3"));
//	private static final int DEFAULT_SAMPLING_RATE = 44100;//模拟器仅支持从麦克风输入8kHz采样率
public boolean isRelease(){
	return  MP3Recorder.isRelease();
}
	/**
	 * 开启录音
	 * @param path 存储的路径
	 * @param name 文件的名字
	 */
	public void start(String path,String name) {
		try {
			mRecorder = new MP3Recorder(new File(path, name));
			mRecorder.start();
		} catch (Exception e) {
			e.printStackTrace();
		}


//		if (mRecorder == null) {
//			mRecorder = new MediaRecorder();
//			//指定音频来源（麦克风）
//			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//			//指定音频输出格式
//			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
//			//指定音频编码方式
//			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//			//指定录制音频输出信息的文件
//			mRecorder.setOutputFile(path+"/"+name);
//			try {
//				mRecorder.prepare();
//				mRecorder.start();
//				mEMA = 0.0;
//			} catch (IllegalStateException e) {
//				System.out.print(e.getMessage());
//			} catch (IOException e) {
//				System.out.print(e.getMessage());
//			}
//		}
		
	}

	public void stop() {
		if (mRecorder != null) {
			mRecorder.stop();
			mRecorder = null;
		}
//		if (mRecorder != null) {
//			mRecorder.stop();
//			mRecorder.release();
//			mRecorder = null;
//		}
	}

//	public void pause() {
////		if (mRecorder != null) {
////			mRecorder.stop();
////		}
//
//		if (mRecorder != null) {
//			mRecorder.stop();
//		}
//
//	}

	public void start() {
		if (mRecorder != null) {
			try {
				mRecorder.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public double getAmplitude() {
		//mRecorder.getVolume()
//		if (mRecorder != null)
//			// 获取在前一次调用此方法之后录音中出现的最大振幅
//			return (mRecorder.getMaxAmplitude() / 2700.0);
//		else
			return mRecorder.getVolume();
	}
	
	public double getAmplitudeEMA() {
//		double amp = getAmplitude();
//		mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
		return mEMA;
	}

	public int getVolume(){
		if(mRecorder == null){
			return 0;
		}
		return mRecorder.getVolume();
	}

	public static  String getRecorderPath() {
		String savePath = "";
		try {
			// 判断是否挂载了SD卡
			String storageState = Environment.getExternalStorageState();
			if (storageState.equals(Environment.MEDIA_MOUNTED)) {
				savePath = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/audio/";// 存放照片的文件夹

				File savedir = new File(savePath);
				if (!savedir.exists()) {
					savedir.mkdirs();
				}
			}
		} catch (Exception e) {
		}
		return savePath;
	}
}
