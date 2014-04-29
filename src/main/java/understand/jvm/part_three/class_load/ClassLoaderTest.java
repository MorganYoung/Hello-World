package understand.jvm.part_three.class_load;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: morgan
 * Date: 13-9-18
 * Time: 下午4:22
 */
public class ClassLoaderTest {

	public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {

		ClassLoader myClassLoader = new ClassLoader() {
			@Override
			public Class<?> loadClass(String name) throws ClassNotFoundException {

				String fileName = name.substring(name.lastIndexOf('.') + 1) + ".class";
				InputStream in = getClass().getResourceAsStream(fileName);
				if(in == null) {
					return super.loadClass(name);
				}
				try {
					byte[] b = new byte[in.available()];
					in.read(b);
					return defineClass(name,b,0,b.length);
				} catch (IOException e) {
					throw new ClassNotFoundException(name);
				}

			}
		};
		Object obj = myClassLoader.loadClass("understand.jvm.part_three.class_load.ClassLoaderTest").newInstance();
		Object obj2 = new ClassLoaderTest();
		System.out.println(obj.getClass());
		System.out.println(obj instanceof ClassLoaderTest);
		System.out.println(obj2 instanceof ClassLoaderTest);
	}

}
