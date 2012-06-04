package pl.edy.agh.pros.adk;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import pl.edy.agh.pros.adk.util.ADKSendUtility;
import pl.edy.agh.pros.adk.util.ValueMsg;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.TabHost;

import com.android.future.usb.UsbAccessory;
import com.android.future.usb.UsbManager;

public class KoalaActivity extends TabActivity implements Runnable {

	private static final String TAG = "KoalaActivity";
	private static final String ACTION_USB_PERMISSION = "pl.edy.agh.pros.adk.KoalaActivity.action.USB_PERMISSION";

	// ADK config
	private UsbManager mUsbManager;
	private PendingIntent mPermissionIntent;
	private boolean mPermissionRequestPending;
	private UsbAccessory mAccessory;
	private ParcelFileDescriptor mFileDescriptor;
	private FileInputStream mInputStream;
	private FileOutputStream mOutputStream;

	// utils
	private ADKSendUtility sendUtility = new ADKSendUtility();
	private SensorsManager sensorsManager;
	private MotorsManager motorsManager;
	private CustomCommandManager customCommandManager;

	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbAccessory accessory = UsbManager.getAccessory(intent);
					if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						openAccessory(accessory);
					} else {
						Log.d(TAG, "permission denied for accessory " + accessory);
					}
					mPermissionRequestPending = false;
				}
			} else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
				UsbAccessory accessory = UsbManager.getAccessory(intent);
				if (accessory != null && accessory.equals(mAccessory)) {
					closeAccessory();
				}
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUsbManager = UsbManager.getInstance(this);
		mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
		registerReceiver(mUsbReceiver, filter);

		if (getLastNonConfigurationInstance() != null) {
			mAccessory = (UsbAccessory) getLastNonConfigurationInstance();
			openAccessory(mAccessory);
		}

		setContentView(R.layout.main);
		prepareUI();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		if (mAccessory != null) {
			return mAccessory;
		} else {
			return super.onRetainNonConfigurationInstance();
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		UsbAccessory[] accessories = mUsbManager.getAccessoryList();
		UsbAccessory accessory = (accessories == null ? null : accessories[0]);
		if (accessory != null) {
			if (mUsbManager.hasPermission(accessory)) {
				openAccessory(accessory);
			} else {
				synchronized (mUsbReceiver) {
					if (!mPermissionRequestPending) {
						mUsbManager.requestPermission(accessory, mPermissionIntent);
						mPermissionRequestPending = true;
					}
				}
			}
		} else {
			Log.d(TAG, "mAccessory is null");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		closeAccessory();
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mUsbReceiver);
		super.onDestroy();
	}

	private void openAccessory(UsbAccessory accessory) {
		mFileDescriptor = mUsbManager.openAccessory(accessory);
		if (mFileDescriptor != null) {
			mAccessory = accessory;
			FileDescriptor fd = mFileDescriptor.getFileDescriptor();
			mInputStream = new FileInputStream(fd);
			mOutputStream = new FileOutputStream(fd);
			Thread thread = new Thread(null, this, "KoalaController");
			thread.start();
			sendUtility.setOutputStream(mOutputStream);
			Log.d(TAG, "accessory opened");
		} else {
			Log.d(TAG, "accessory open fail");
		}
	}

	private void closeAccessory() {
		try {
			if (mFileDescriptor != null) {
				mFileDescriptor.close();
			}
		} catch (IOException e) {
		} finally {
			mFileDescriptor = null;
			mAccessory = null;
		}
	}

	private void prepareUI() {

		TabHost mTabHost = getTabHost();

		mTabHost.addTab(mTabHost.newTabSpec("motors").setIndicator("Motors").setContent(R.id.motorsLayout));
		mTabHost.addTab(mTabHost.newTabSpec("sensors").setIndicator("Sensors").setContent(R.id.sensorsLayout));
		mTabHost.addTab(mTabHost.newTabSpec("custom").setIndicator("Custom").setContent(R.id.customLayout));

		mTabHost.setCurrentTab(0);

		sensorsManager = new SensorsManager(this);
		sensorsManager.init();

		motorsManager = new MotorsManager(this, sendUtility);
		motorsManager.init();

		customCommandManager = new CustomCommandManager(this, sendUtility);
		customCommandManager.init();

	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			ValueMsg t = (ValueMsg) msg.obj;
			// update UI
			if (sensorsManager != null) {
				sensorsManager.updateSensors(t.getValue());
			}
		}
	};

	@Override
	public void run() {
		int responseLength = 0;
		byte[] buffer = new byte[1024];

		while (true) { // read data
			try {
				responseLength = mInputStream.read(buffer);
			} catch (IOException e) {
				openAccessory(mAccessory);
				break;
			}

			if (responseLength > 1) {
				Message m = Message.obtain(mHandler);
				byte flag = buffer[0];
				if (flag == 'n') {
					String value = String.valueOf(new String(buffer).toCharArray(), 2, responseLength - 3);
					m.obj = new ValueMsg(flag, value);
					mHandler.sendMessage(m);
				}
			}
		}
	}
}