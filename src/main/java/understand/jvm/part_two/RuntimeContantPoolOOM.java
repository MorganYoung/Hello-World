package understand.jvm.part_two;

import java.util.ArrayList;
import java.util.List;

/**
 * User: morgan
 * Date: 13-9-13
 * Time: 下午3:02
 *
 * 在JDK1.6以及以前的版本中，由于常量池分配在永久代内，
 * 我们可以通过-XX:PermSize和-XX:MaxPermSize限制
 * 方法区大小，从而间接限制其中常量池的容量。
 *
 * vm args: -XX:PermSize=10M -XX:MaxPermSize=10M
 *
 * 在jdk 1.6中 java.lang.OutOfMemoryError: PermGen space
 * 在jdk 1.7中 没有内存溢出，一直运行，可见1.7中已经开始去永久代
 */
public class RuntimeContantPoolOOM {

	/*public static void main(String[] args) {
		//使用List保持常量池引用，便面Full GC回收
		List<String> list = new ArrayList<String>();
		//10M的PermSize在integer范围内足够OOM了
		int i = 0;
		while(true) {
			list.add(String.valueOf(i++).intern());
		}
	}*/



	/**
	 *
	 * @param args
	 * String.intern()返回引用的测试
	 * 1.7中 true、false
	 * 1.6中 false、false
	 */
	public static void main(String[] args) {
		String str1 = new StringBuilder("计算机").append("软件").toString();
		System.out.println(str1.intern() == str1);

		String str2 = new StringBuilder("ja").append("va").toString();
		System.out.println(str2.intern() == str2);
	}

}
