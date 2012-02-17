package com.crazyapps.chessclock.manager;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;

import com.crazyapps.chessclock.R;

public class Notifier {

	private final long[]	gameOverPattern	= { 0L, 1000L, 100L, 1000L, 100L, 1000L };

	private boolean			isSoundOnClick;
	private boolean			isVibrateOnClick;
	private boolean			isSoundOnGameOver;
	private boolean			isVibrateOnGameOver;
	private Vibrator		vibrator;
	private MediaPlayer		clickMediaPlayer;
	private MediaPlayer		gameOverMediaPlayer;

	private Context			context;

	public Notifier(Context context) {
		this.context = context;
	}

	public void aquire() {
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		clickMediaPlayer = MediaPlayer.create(context, R.raw.click);
		gameOverMediaPlayer = MediaPlayer.create(context, R.raw.game_over);
		// clickMediaPlayer.prepareAsync();
		// gameOverMediaPlayer.prepareAsync();
	}

	public void release() {
		clickMediaPlayer.release();
		gameOverMediaPlayer.release();
	}

	public void click() {
		if (isSoundOnClick)
			clickMediaPlayer.start();
		if (isVibrateOnClick)
			vibrator.vibrate(100);
	}

	public void gameOver() {
		if (isSoundOnGameOver)
			gameOverMediaPlayer.start();
		if (isVibrateOnGameOver) {
			vibrator.vibrate(gameOverPattern, -1);
		}
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
