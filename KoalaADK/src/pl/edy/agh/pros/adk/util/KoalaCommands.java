package pl.edy.agh.pros.adk.util;

public class KoalaCommands {

	public static final byte TYPE_CUSTOM = 0x1;
	public static final byte TYPE_SPEED = 0x2;
	public static final byte TYPE_POSITION = 0x3;
	public static final byte TYPE_SENSORS = 0x4;

	public static final int MIN_SPEED = 0;
	public static final int MAX_SPEED = 127;

	public static final long MIN_POSITION = 0;
	public static final long MAX_POSITION = 100000;

	public static final int MIN_SENSOR = 0;
	public static final int MAX_SENSOR = 1023;

	public static String prepareCommand(String command) {
		return command + "\n";
	}

	// D, speed_motor_left, speed_motor_right
	public static String prepareSpeedCommand(byte speed_motor_left, byte speed_motor_right) {
		StringBuilder builder = new StringBuilder("D,");
		builder.append(speed_motor_left);
		builder.append(",");
		builder.append(speed_motor_right);
		builder.append("\n");
		return builder.toString();
	}

	public static String prepareSpeedCommandSB(int speed_motor_left, int speed_motor_right, boolean reverseL,
			boolean reverseR) {
		return prepareSpeedCommand((byte) prepareValueFromSeekBar(speed_motor_left, MIN_SPEED, MAX_SPEED, reverseL),
				(byte) prepareValueFromSeekBar(speed_motor_right, MIN_SPEED, MAX_SPEED, reverseR));
	}

	// G, position_motor_left, position_motor_right
	public static String preparePositionCommand(long position_motor_left, long position_motor_right) {
		StringBuilder builder = new StringBuilder("G,");
		builder.append(position_motor_left);
		builder.append(",");
		builder.append(position_motor_right);
		builder.append("\n");
		return builder.toString();
	}

	public static String preparePositionCommandSB(int position_motor_left, int position_motor_right, boolean reverseL,
			boolean reverseR) {
		return preparePositionCommand(
				prepareValueFromSeekBar(position_motor_left, MIN_POSITION, MAX_POSITION, reverseL),
				prepareValueFromSeekBar(position_motor_right, MIN_POSITION, MAX_POSITION, reverseR));
	}

	public static String prepareReadSensorsCommand() {
		return "N\n";
	}

	public static long prepareValueFromSeekBar(long value, long min, long max, boolean reverse) {
		long result = (long) (min + (max - min) * value / (double) 100);
		if (reverse) {
			result = -result;
		}
		return result;
	}
}
