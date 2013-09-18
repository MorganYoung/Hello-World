package understand.jvm.part_two;

/**
 * User: morgan
 * Date: 13-9-13
 * Time: 下午2:20
 *
 * 创建线程导致堆栈内存溢出异常
 * vm args -Xmx20M -Xss2M
 * 在HotSpot虚拟机中，试验证明
 * 在相同的-Xmx情况下，-Xss大小和能创建的线程数量成反比
 * 在-Xss不变情况下，-Xmx越大，能创建的线程数量就越少
 */
public class JavaStackOOM {

	int count = 1;

	private void dontStop(){
		while(true) {
		}
	}

	synchronized private void addCount() {
		count ++;
		System.out.println("the number of threads is:" + count);
	}

	public void stackLeakByThread() {
		while(true){
			count ++;
			System.out.println("the number of threads is:" + count);
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
//					addCount();
					dontStop();
				}
			});
			thread.start();
		}
	}

	public static void main(String args[]) {
		JavaStackOOM javaStackOOM = new JavaStackOOM();
		javaStackOOM.stackLeakByThread();
	}

}
