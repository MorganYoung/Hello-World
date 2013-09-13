package understand.jvm.part_two;

import java.util.ArrayList;
import java.util.List;

/**
 * User: morgan
 * Date: 13-9-13
 * Time: 上午11:58
 *
 * Java堆栈溢出，的测试
 * 需要设置虚拟机参数
 *  -Xms20M -Xmx20M - Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:+HeapDumpOnOutOfMemoryError
 */
public class JavaHeapOOM {

	public static void main(String[] args) {
		List<OOMObject> list = new ArrayList<OOMObject>();
		while(true) {
			list.add(new OOMObject());
		}
	}


	static class OOMObject {

	}
}
