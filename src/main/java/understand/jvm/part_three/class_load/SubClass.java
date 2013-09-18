package understand.jvm.part_three.class_load;

/**
 * User: morgan
 * Date: 13-9-17
 * Time: 下午10:14
 */

public class SubClass extends SuperClass{

	static {
		System.out.println("SubClass init!");
	}
}

