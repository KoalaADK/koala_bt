package pl.edu.agh.pros.bt.server.gravity;

public class GravityVO {

	private final byte[] bytes;

	public GravityVO(byte[] bytes) {
		if (bytes.length != 3) {
			throw new IllegalArgumentException();
		}
		this.bytes = bytes;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public byte getX() {
		return bytes[0];
	}

	public byte getY() {
		return bytes[1];
	}

	public byte getZ() {
		return bytes[2];
	}

}
