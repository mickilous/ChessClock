package com.crazyapps.chessclock.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;

import com.crazyapps.chessclock.ChessClockActivity;
import com.crazyapps.chessclock.R;
import com.crazyapps.chessclock.widget.CountDown;

public class ChessClockActivityTestCase extends ActivityInstrumentationTestCase2<ChessClockActivity> {

	private ChessClockActivity	activity;
	private CountDown			countDown1;
	private CountDown			countDown2;

	public ChessClockActivityTestCase() {
		super("com.crazyapps.chessclock", ChessClockActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = this.getActivity();
		countDown1 = (CountDown) activity.findViewById(R.id.countdown1);
		countDown2 = (CountDown) activity.findViewById(R.id.countdown2);
	}

	public void test_activity_init() {

		assertTrue(countDown1.isClickable());
		assertTrue(countDown2.isClickable());

	}

	public void test_gameover() throws InterruptedException {

		long countDown2Time = countDown2.getTime();
		initializeGame();

		startGame();

		assertEquals(0, countDown1.getTime());
		assertEquals(countDown2Time, countDown2.getTime());
		assertFalse(countDown1.isClickable());
		assertFalse(countDown2.isClickable());

	}

	public void test_pause() {

		clickMenu();

		assertTrue(countDown1.isClickable());
		assertTrue(countDown2.isClickable());

	}

	public void test_pause_after_gameover() {

		initializeGame();
		startGame();
		clickMenu();

		assertFalse(countDown1.isClickable());
		assertFalse(countDown2.isClickable());

	}

	public void test_reset() {

		long countDown1Time = countDown1.getTime();
		long countDown2Time = countDown2.getTime();
		long countDown1TimeIncrement = countDown1.getTimeIncrement();
		long countDown2TimeIncrement = countDown2.getTimeIncrement();
		initializeGame();

		startGame();
		clickMenu();
		boolean isReset = clickReset();

		assertTrue(isReset);
		assertEquals(countDown1Time, countDown1.getTime());
		assertEquals(countDown2Time, countDown2.getTime());
		assertEquals(countDown1TimeIncrement, countDown1.getTimeIncrement());
		assertEquals(countDown2TimeIncrement, countDown2.getTimeIncrement());

	}

	private void initializeGame() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				countDown1.setTime(1L);
				countDown1.setTimeIncrement(0L);
				countDown1.setAppendTimeIncrement(false);
				countDown2.requestFocus();
			}
		});
	}

	private void startGame() {
		sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
	}

	private void clickMenu() {
		sendKeys(KeyEvent.KEYCODE_MENU);
	}

	private boolean clickReset() {
		boolean isReset = getInstrumentation().invokeMenuActionSync(activity, R.id.reset, 0);
		return isReset;
	}

}
