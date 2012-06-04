package pl.edy.agh.pros.adk.util;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.util.ByteArrayBuffer;

import android.util.Log;

public class ADKSendUtility {

	private static final String TAG = "SendUtility";

	private OutputStream mOutputStream;

	public ADKSendUtility() {

	}

	public ADKSendUtility(OutputStream mOutputStream) {
		this.mOutputStream = mOutputStream;
	}

	public void sendMessage(byte type, byte[] value) {
		if (mOutputStream != null) {
			ByteArrayBuffer bAB = new ByteArrayBuffer(1 + value.length);
			bAB.append(type);
			bAB.append((byte) value.length);
			bAB.append(value, 0, value.length);
			if (mOutputStream != null) {
				try {
					mOutputStream.write(bAB.toByteArray());
				} catch (IOException e) {
					Log.e(TAG, "write failed", e);
				}
			}
		} else {
			Log.d(TAG, "Accessory not connected");
		}
	}

	public void sendMessage(byte type, String value) {
		sendMessage(type, value.getBytes());
	}

	public void setOutputStream(OutputStream mOutputStream) {
		this.mOutputStream = mOutputStream;
	}

}
