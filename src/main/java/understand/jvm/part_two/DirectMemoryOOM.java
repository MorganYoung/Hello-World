package understand.jvm.part_two;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * User: morgan
 * Date: 13-9-13
 * Time: 下午3:40
 * 本机直接内存溢出测试
 *
 * DirectMemory容量可以通过 -XX:MaxDirectMemorySize指定
 * 如果不指定默认与 -Xmx一样
 *
 * vm args: -Xmx20M -XX:MaxDirectMemorySize=10M
 *
 */
public class DirectMemoryOOM {

	private static final int _1MB = 1024 * 1024;

	public static void main(String args[]) throws IllegalAccessException {
		Field field = Unsafe.class.getDeclaredFields()[0];
		field.setAccessible(true);
		Unsafe unsafe = (Unsafe) field.get(null);
		int i = 1;
		while(true) {
			System.out.println("allocateMemory : "+ i++);
			unsafe.allocateMemory(_1MB);
		}
//		while(true){
//			;
//		}
	}

}
