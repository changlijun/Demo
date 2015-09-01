package cn.changlijun.onlinegamedemo.temp;

import android.content.Context;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

	private final static int SLEEP_TIME = 1000 / 60;

	private SurfaceHolder holder;

	private Thread thread;
	private boolean isRun;// 控制线程

	private int screenWidth;
	private int screenHeight;

	public MySurfaceView(Context context) {
		super(context);

		// 创建SurfaceHolder
		holder = this.getHolder();
		holder.addCallback(this);
	}

	private void threadRelease() {
		if (thread != null) {
			isRun = false;
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			thread = null;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		screenWidth = this.getWidth();
		screenHeight = this.getHeight();
		// 创建线程
		thread = new Thread(this);
		thread.start();

		// 设置视图相应触摸
		setFocusableInTouchMode(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		threadRelease();
	}

	@Override
	public void run() {
		isRun = true;
		SurfaceHolder mSurfaceHolder = holder;
		while (isRun) {
			Canvas canvas = mSurfaceHolder.lockCanvas();
			synchronized (mSurfaceHolder) {
				// TODO 绘制图形

			}
			mSurfaceHolder.unlockCanvasAndPost(canvas);
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN) {
			return super.onTouchEvent(event);
		}
		synchronized (holder) {
			float x = event.getX();
			float y = event.getY();

			System.out.println("X: " + x + " Y： " + y);
		}
		return true;

	}

}
