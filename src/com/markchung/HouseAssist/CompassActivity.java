package com.markchung.HouseAssist;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class CompassActivity extends Activity implements SensorEventListener {
	private SensorManager sm;
	ImageView imageView;
	Matrix matrix;
	Bitmap m_bmp;

	private void CreateBitmap() {
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels - 80;
		Bitmap bmp = BitmapFactory.decodeResource(getResources(),
				R.drawable.compass);
		Matrix matrix = new Matrix();
		float scale = Math.min(((float) width) / bmp.getWidth(),
				((float) height) / bmp.getHeight());
		matrix.setScale(scale, scale);
		m_bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(),
				matrix, true);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compass);
		CreateBitmap();
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		imageView = (ImageView) findViewById(R.id.image_compass);
		matrix = new Matrix();
		matrix.setScale(1, 1);
		imageView.setScaleType(ScaleType.MATRIX);
		imageView.setImageMatrix(matrix);
		imageView.setImageBitmap(m_bmp);
	}

	@Override
	protected void onPause() {
		sm.unregisterListener(this);
		super.onPause();
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() != Sensor.TYPE_ORIENTATION)
			return;
		float value[] = event.values;
		matrix.setRotate(360 - value[SensorManager.DATA_X],
				m_bmp.getWidth() / 2, m_bmp.getHeight() / 2);
		imageView.setImageMatrix(matrix);

	}

}
