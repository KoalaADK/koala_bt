package pl.edu.agh.pros.bt.client.gravity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.os.Message;

public class GravityListener implements SensorEventListener {

	private final Handler accelerometerHandler;

	public GravityListener(Handler accelerometerHandler) {
		this.accelerometerHandler = accelerometerHandler;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// ignore
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		// Normalize the gravity vector and rescale it so that every component
		// fits one byte.
		float size = (float) Math.sqrt(Math.pow(event.values[0], 2) + Math.pow(event.values[1], 2)
				+ Math.pow(event.values[2], 2));

		byte result[] = new byte[3];
		result[0] = (byte) (128 * event.values[0] / size);
		result[1] = (byte) (128 * event.values[1] / size);
		result[2] = (byte) (128 * event.values[2] / size);

		// Propagate sensorEvent to UI
		Message m = Message.obtain(accelerometerHandler);
		m.obj = new GravityVO(result);
		accelerometerHandler.sendMessage(m);
	}
}
