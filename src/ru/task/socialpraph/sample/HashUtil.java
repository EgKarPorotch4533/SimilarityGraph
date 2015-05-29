package ru.task.socialpraph.sample;

public class HashUtil {

	private static final long[] byteTable;
	static {
		byteTable = new long[256];
		long h = 0x544B2FBACAAF1684L;
		for (int i = 0; i < 256; i++) {
			for (int j = 0; j < 31; j++) {
				h = (h >>> 7) ^ h;
				h = (h << 11) ^ h;
				h = (h >>> 10) ^ h;
			}
			byteTable[i] = h;
		}
	}
	private static final long HSTART = 0xBB40E64DA205B064L;
	private static final long HMULT = 7664345821815920749L;

	public static long getHstart() {
		return HSTART;
	}

	/**
	 * Calculate Strong hashcode (long type).
	 *
	 * @param text the text
	 * @return the Strong hashcode (long type)
	 */
	public static long hashcode64bits(String text) {
		long rez = HSTART;
		for (int len = text.length(), i = 0; i < len; i++) {
			char c = text.charAt(i);
			rez = (rez * HMULT) ^ byteTable[c & 0xff];
			rez = (rez * HMULT) ^ byteTable[(c >>> 8) & 0xff];
		}
		return rez;
	}

	/**
	 * Calculate Strong hashcode (long type) for the part of the string.
	 * The part of the string is started at from index and extends to to-1;
	 *
	 * @param text the text
	 * @return the Strong hashcode (long type)
	 */
	public static long hashcode64bits(String text, int from, int to) {
		long rez = HSTART;
		for (int i = from; i < to; i++) {
			char c = text.charAt(i);
			rez = (rez * HMULT) ^ byteTable[c & 0xff];
			rez = (rez * HMULT) ^ byteTable[(c >>> 8) & 0xff];
		}
		return rez;
	}
}
