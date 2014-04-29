package understand.jvm.part_three.class_load;

/**
 * User: morgan
 * Date: 13-9-18
 * Time: 上午11:25
 */
public class ConstClass {

	static{
		System.out.println("ConstClass init!");
	}

	public static final String HELLO_WORLD = "hello world";
}
