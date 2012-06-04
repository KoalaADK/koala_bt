package pl.edy.agh.pros.adk.util;

public class ValueMsg {

	private byte flag;

	private String value;

	public ValueMsg(byte flag, String value) {
		super();
		this.flag = flag;
		this.value = value;
	}

	public byte getFlag() {
		return flag;
	}

	public String getValue() {
		return value;
	}

}