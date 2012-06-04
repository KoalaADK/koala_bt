package pl.edu.agh.pros.bt.client.gravity;

import java.util.Arrays;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(bytes);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GravityVO other = (GravityVO) obj;
		if (!Arrays.equals(bytes, other.bytes))
			return false;
		return true;
	}

}
