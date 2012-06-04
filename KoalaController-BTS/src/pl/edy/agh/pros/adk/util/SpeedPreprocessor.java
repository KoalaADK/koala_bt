package pl.edy.agh.pros.adk.util;

public class SpeedPreprocessor {

	public static String process(byte[] original) {
		int speed_motor_left =  original[2] + original[1];
		int speed_motor_right = original[2] - original[1];
		if (speed_motor_left - speed_motor_right < 20 && speed_motor_left - speed_motor_right > -20) {
			speed_motor_left = speed_motor_right;
		}
		return KoalaCommands.prepareSpeedCommandInt(processEvenFurther(speed_motor_left),
				processEvenFurther(speed_motor_right));
	}

	private static int processEvenFurther(int speed) {
		if (speed < 30 && speed > -30) {
			return 0;
		} else {
			return speed;
		}
	}

}
