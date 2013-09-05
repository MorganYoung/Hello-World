package java_explore.cpmplier;

import javax.tools.JavaCompiler;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * User: morgan
 * Date: 13-8-31
 * Time: 下午10:40
 */
public class ComplierTest  {

	public static void main(String[] args) throws Exception{

		String source = "public class Main{public static void mian(String[]args){System.out.println(\"Hello World!\")}}";

		JavaCompiler complier = ToolProvider.getSystemJavaCompiler();

		StandardJavaFileManager fileManager = complier.getStandardFileManager(null, null, null);

		StringSourceJavaObject sourceObject = new StringSourceJavaObject("Main", source);

		Iterable<? extends SimpleJavaFileObject > fileObjects = Arrays.asList(sourceObject);

		JavaCompiler.CompilationTask complierTask = complier.getTask(null, fileManager, null, null, null, fileObjects);

		boolean result = complierTask.call();

		if(result) {
			System.out.println("编译成功！！！");
		}

	}

	static class StringSourceJavaObject extends SimpleJavaFileObject {

		private String content = null;

		/**
		 * Construct a SimpleJavaFileObject of the given kind and with the
		 * given URI.
		 *
		 * @param uri  the URI for this file object
		 * @param kind the kind of this file object
		 */
		protected StringSourceJavaObject(URI uri, Kind kind) {
			super(uri, kind);

		}

		public StringSourceJavaObject(String name, String content) throws URISyntaxException{
			super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
			this.content = content;

		}

		public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
			return content;
		}
	}

}
