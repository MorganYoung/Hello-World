package java_agent;


import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * User: Morgan
 * Date: 13-6-4
 * Time: 下午3:23
 */
public class MyTransformer implements ClassFileTransformer {

	@Override
	public byte[] transform(ClassLoader loader, String className,
	                        Class<?> classBeingRedefined,
	                        ProtectionDomain protectionDomain,
	                        byte[] classfileBuffer) throws IllegalClassFormatException {

		System.out.println("Hello, \t "+ className);
		return null;
	}

	public static void premain(String agentArgs, Instrumentation inst) {
//		inst.addTransformer();
		if(agentArgs != null) {

		}
	}
}
