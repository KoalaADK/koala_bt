package pl.edy.agh.pros.adk;

import pl.edy.agh.pros.adk.util.ADKSendUtility;
import pl.edy.agh.pros.adk.util.KoalaCommands;
import android.app.Activity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;

public class MotorsManager {

	private final Activity activity;
	private final ADKSendUtility sendUtility;

	// UI
	private CheckBox checkBoxLMS;
	private SeekBar leftMotorSpeedBar;
	private CheckBox checkBoxRMS;
	private SeekBar rightMotorSpeedBar;
	private ToggleButton toggleRobotButton;

	public MotorsManager(Activity activity, ADKSendUtility sendUtility) {
		this.activity = activity;
		this.sendUtility = sendUtility;
	}

	public void init() {
		checkBoxLMS = (CheckBox) activity.findViewById(R.id.checkBoxLMS);
		checkBoxLMS.setOnCheckedChangeListener(new SpeedCheckBoxOnCheckedChangeListener());
		leftMotorSpeedBar = (SeekBar) activity.findViewById(R.id.leftMotorSpeedBar);
		leftMotorSpeedBar.setOnSeekBarChangeListener(new SpeedSeekBarChangeListener());
		checkBoxRMS = (CheckBox) activity.findViewById(R.id.checkBoxRMS);
		checkBoxRMS.setOnCheckedChangeListener(new SpeedCheckBoxOnCheckedChangeListener());
		rightMotorSpeedBar = (SeekBar) activity.findViewById(R.id.rightMotorSpeedBar);
		rightMotorSpeedBar.setOnSeekBarChangeListener(new SpeedSeekBarChangeListener());

		toggleRobotButton = (ToggleButton) activity.findViewById(R.id.toggleRobotButton);
		toggleRobotButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					sendSpeedCommand();
				} else {
					stopRobot();
				}
			}
		});
	}

	private void sendSpeedCommand() {
		sendUtility.sendMessage(KoalaCommands.TYPE_SPEED, KoalaCommands.prepareSpeedCommandSB(
				leftMotorSpeedBar.getProgress(), rightMotorSpeedBar.getProgress(), checkBoxLMS.isChecked(),
				checkBoxRMS.isChecked()));
	}

	private void stopRobot() {
		sendUtility.sendMessage(KoalaCommands.TYPE_SPEED, KoalaCommands.prepareSpeedCommand((byte) 0, (byte) 0));
		sendUtility.sendMessage(KoalaCommands.TYPE_POSITION, KoalaCommands.preparePositionCommand(0, 0));
	}

	class SpeedSeekBarChangeListener implements OnSeekBarChangeListener {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			if (toggleRobotButton.isChecked()) {
				sendSpeedCommand();
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// do nothing
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			// do nothing
		}

	}

	class SpeedCheckBoxOnCheckedChangeListener implements OnCheckedChangeListener {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (toggleRobotButton.isChecked()) {
				sendSpeedCommand();
			}
		}
	}
}
