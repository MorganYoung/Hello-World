package understand.jvm.part_two.gc;

/**
 * User: morgan
 * Date: 13-9-13
 * Time: 下午4:21
 */
public class ReferenceCountingGC {

	public Object instence = null;

	private static final int _1MB = 1024 * 1024;

	private byte[] bigSize = new byte[2 * _1MB];

	public static void testGC() {
		ReferenceCountingGC objA = new ReferenceCountingGC();
		ReferenceCountingGC objB = new ReferenceCountingGC();

		objA.instence = objB;
		objB.instence = objA;

		objA = null;
		objB = null;

		System.gc();
	}

	public static void main(String[] args) {
		testGC();
	}

}
