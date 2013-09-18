package understand.jvm.part_two.gc;

/**
 * User: morgan
 * Date: 13-9-13
 * Time: 下午4:47
 *
 * 一次对象自我拯救的演示
 * 此代码演示了两点
 * 1.对象可以在GC时自我拯救。
 * 2.这种自救的机会只有一次，因为一个对象的finalize方法最多只会被系统调用一次。
 */
public class FinalizeEscapeGC {

	public static FinalizeEscapeGC SAVE_HOOK = null;

	public void isAlive(){
		System.out.println("yes , I am still alive :)");
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println("finalize execute!!!");
		FinalizeEscapeGC.SAVE_HOOK = this;
	}

	public static void main(String[]args) throws InterruptedException {
		SAVE_HOOK = new FinalizeEscapeGC();

		//对象第一次成功拯救自己
		SAVE_HOOK = null;
		System.gc();

		//因为finalize方法优先级很低，所以暂停0.5秒以等待它
		Thread.sleep(500);
		if(SAVE_HOOK != null){
			SAVE_HOOK.isAlive();
		} else {
			System.out.println("no I am dead");
		}

		SAVE_HOOK = null;
		System.gc();

		//因为finalize方法优先级很低，所以暂停0.5秒以等待它
		Thread.sleep(500);
		if(SAVE_HOOK != null){
			SAVE_HOOK.isAlive();
		} else {
			System.out.println("no I am dead");
		}

	}


}
