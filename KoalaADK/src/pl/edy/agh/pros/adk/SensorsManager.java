package pl.edy.agh.pros.adk;

import pl.edy.agh.pros.adk.util.KoalaCommands;
import android.app.Activity;
import android.widget.ProgressBar;

public class SensorsManager {

	private final Activity activity;
	private ProgressBar[] progressBars;

	public SensorsManager(Activity activity) {
		this.activity = activity;
	}

	public void init() {
		ProgressBar[] progressBars = { (ProgressBar) activity.findViewById(R.id.progressBarL0),
				(ProgressBar) activity.findViewById(R.id.progressBarL1),
				(ProgressBar) activity.findViewById(R.id.progressBarL2),
				(ProgressBar) activity.findViewById(R.id.progressBarL3),
				(ProgressBar) activity.findViewById(R.id.progressBarL4),
				(ProgressBar) activity.findViewById(R.id.progressBarL5),
				(ProgressBar) activity.findViewById(R.id.progressBarL6),
				(ProgressBar) activity.findViewById(R.id.progressBarL7),
				(ProgressBar) activity.findViewById(R.id.progressBarR0),
				(ProgressBar) activity.findViewById(R.id.progressBarR1),
				(ProgressBar) activity.findViewById(R.id.progressBarR2),
				(ProgressBar) activity.findViewById(R.id.progressBarR3),
				(ProgressBar) activity.findViewById(R.id.progressBarR4),
				(ProgressBar) activity.findViewById(R.id.progressBarR5),
				(ProgressBar) activity.findViewById(R.id.progressBarR6),
				(ProgressBar) activity.findViewById(R.id.progressBarR7) };
		this.progressBars = progressBars;
	}

	public void updateSensors(String msg) {
		if (msg == null) {
			return;
		}
		String values[] = msg.split(",");
		int len = Math.min(progressBars.length, values.length);
		try {
			for (int i = 0; i < len; i++) {
				progressBars[i].setProgress(computeProgress(Integer.valueOf(values[i])));
			}
		} catch (Exception e) {
			// ignore, it happens
		}

	}

	public int computeProgress(int value) {
		return 100 * value / KoalaCommands.MAX_SENSOR;
	}
}
