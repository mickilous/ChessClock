package com.crazyapps.chessclock.manager;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;

import com.crazyapps.chessclock.R;

public class Notifier {

	private boolean		isSoundOnClick;
	private boolean		isVibrateOnClick;
	private boolean		isSoundOnGameOver;
	private boolean		isVibrateOnGameOver;
	private Vibrator	vibrator;
	private MediaPlayer	clickMediaPlayer;
	private MediaPlayer	gameOverMediaPlayer;

	private Context		context;

	public Notifier(Context context) {
		this.context = context;
	}

	public void aquire() {
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		clickMediaPlayer = MediaPlayer.create(context, R.raw.click);
		gameOverMediaPlayer = MediaPlayer.create(context, R.raw.game_over);
	}

	public void release() {
		clickMediaPlayer.release();
		gameOverMediaPlayer.release();
	}

	public void click() {
		if (isVibrateOnClick)
			vibrator.vibrate(100);
		if (isSoundOnClick)
			clickMediaPlayer.start();
	}

	public void gameOver() {
		if (isVibrateOnGameOver)
			vibrator.vibrate(300);
		if (isSoundOnGameOver)
			gameOverMediaPlayer.start();
	}

	public void setSoundOnClick(boolean isSoundOnClick) {
		this.isSoundOnClick = isSoundOnClick;
	}

	public void setVibrateOnClick(boolean isVibrateOnClick) {
		this.isVibrateOnClick = isVibrateOnClick;
	}

	public void setSoundOnGameOver(boolean isSoundOnGameOver) {
		this.isSoundOnGameOver = isSoundOnGameOver;
	}

	public void setVibrateOnGameOver(boolean isVibrateOnGameOver) {
		this.isVibrateOnGameOver = isVibrateOnGameOver;
	}

}
