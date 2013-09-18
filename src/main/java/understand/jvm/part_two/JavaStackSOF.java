package understand.jvm.part_two;

/**
 * User: morgan
 * Date: 13-9-13
 * Time: 下午1:03
 * 虚拟机栈和本地方法栈溢出
 * vm args: -Xss:128K
 */
public class JavaStackSOF {

	private int stackLength = 1;

	public void stackLeak() {
		stackLength ++;
		stackLeak();
	}

	public static void main(String[] args) throws Throwable {
		JavaStackSOF javaStackSOF = new JavaStackSOF();
		try{
			javaStackSOF.stackLeak();

		} catch (Throwable e) {
			System.out.println("stack length:"+javaStackSOF.stackLength);
			throw e;
		}
	}


}
