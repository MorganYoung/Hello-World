package understand.jvm.part_three.class_load;

/**
 * User: morgan
 * Date: 13-9-17
 * Time: 下午10:05
 * 被动使用类字段演示一
 * 通过子类引用父类的静态字段，不会导致子类初始化。
 */
public class SuperClass {

	static {
		System.out.println("SuperClass init!");
	}

	public static int value = 123;
}

