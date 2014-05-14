
下面的内容摘自[iteye](http://scoffor.iteye.com/blog/714143)
#####JNDI配置原理详解


	和多数java服务一样，SUN对JNDI也只提供接口，使用JNDI只需要用到JNDI接口而不必关心具体实现：

	private static Object jndiLookup() throws Exception {
	  InitialContext ctx = new InitialContext();
	  return ctx.lookup("java:comp/env/systemStartTime");
	}

	上述代码在J2EE服务器环境下工作得很好，但是在main()中就会报一个NoInitialContextException，许多文章会说你创建InitialContext的时候还要传一个Hashtable或者Properties，像这样：
	
	Hashtable env = new Hashtable();
	env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
	env.put(Context.PROVIDER_URL,"t3://localhost:7001");
	InitialContext ctx = new InitialContext(env);
	
	这个在WebLogic环境下是对的，但是换到JBoss呢？再用JBoss的例子？
	
	其实之所以有NoInitialContextException是因为无法从System.properties中获得必要的JNDI参数，在服务器环境下，服务器启动时就把这些参数放到System.properties中了，于是直接new InitialContext()就搞定了，不要搞env那么麻烦，搞了env你的代码还无法移植，弄不好管理员设置服务器用的不是标准端口还照样抛异常。
	
	但是在单机环境下，可没有JNDI服务在运行，那就手动启动一个JNDI服务。我在JDK 5的rt.jar中一共找到了4种SUN自带的JNDI实现：
	
	LDAP，CORBA，RMI，DNS。
	
	这4种JNDI要正常运行还需要底层的相应服务。一般我们没有LDAP或CORBA服务器，也就无法启动这两种JNDI服务，DNS用于查域名的，以后再研究，唯一可以在main()中启动的就是基于RMI的JNDI服务。
	
	现在我们就在main()中启动基于RMI的JNDI服务并且绑一个Date对象到JNDI上：
	
	LocateRegistry.createRegistry(1099);
	System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
	System.setProperty(Context.PROVIDER_URL, "rmi://localhost:1099");
	InitialContext ctx = new InitialContext();
	class RemoteDate extends Date implements Remote {};
	ctx.bind("java:comp/env/systemStartTime", new RemoteDate());
	ctx.close();
	
	注意，我直接把JNDI的相关参数放入了System.properties中，这样，后面的代码如果要查JNDI，直接new InitialContext()就可以了，否则，你又得写Hashtable env = ...
	
	在RMI中绑JNDI的限制是，绑定的对象必须是Remote类型，所以就自己扩展一个。
	
	其实JNDI还有两个Context.SECURITY_PRINCIPAL和Context.SECURITY_CREDENTIAL，如果访问JNDI需要用户名和口令，这两个也要提供，不过一般用不上。
	
	在后面的代码中查询就简单了：
	
	InitialContext ctx = new InitialContext();
	Date startTime = (Date) ctx.lookup("java:comp/env/systemStartTime");

#####JNDI调用时,各种应用服务器InitialContext的写法

	调用ejb时,如果客户端和ejb不在同一个jvm,就要设置InitialContext,不同的应用服务器InitialContext写法也不同.
	
	Context.INITIAL_CONTEXT_FACTORY:指定到目录服务的连接工厂
	Context.PROVIDER_URL:目录服务提供者URL.
	
	//jboss:
	Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory"
	Context.URL_PKG_PREFIXES, "org.jboss.naming"
	Context.PROVIDER_URL, "localhost:1099"
	
	//weblogic:
	
	Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory"
	Context.PROVIDER_URL, "t3://localhost:7001"
	
	//apusic(金蝶):
	
	Context.INITIAL_CONTEXT_FACTORY, "com.apusic.jndi.InitialContextFactory"
	Context.PROVIDER_URL, "rmi://localhost:6888"
	
	//WebSphere:
	Context.INITIAL_CONTEXT_FACTORY, "com.ibm.websphere.naming.WsnInitialContextFactory"
	Context.PROVIDER_URL, "iiop://localhost:900"
	
	//J2EE  SDK(J2EE  RI):
	Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.cosnaming.CNCtxFactory"
	Context.PROVIDER_URL, "iiop://127.0.0.1:1050"
	
	//SilverStream:
	Context.INITIAL_CONTEXT_FACTORY, "com.sssw.rt.jndi.AgInitCtxFactory"
	Context.PROVIDER_URL, "sssw://localhost:80"
	
	//OC4J:
	Context.INITIAL_CONTEXT_FACTORY, "com.evermind.server.rmi.RMIInitialContextFactory"
	Context.PROVIDER_URL, "ormi://127.0.0.1/"
	 
	//WAS5:
	Context.INITIAL_CONTEXT_FACTORY, "com.ibm.websphere.naming.WsnInitialContextFactory"
	Context.PROVIDER_URL, "iiop://localhost:2809"
	
	常用JNDI服务提供者连接工厂:
	Filesystem:  Com.sun.jndi.fscontext.FSContextFactory
	
	或者com.sun.jndi.fscontext.RefFSContextFactory
	LDAPv3:    Com.sun.jndi.ldap.LdapCtxFactory
	NDS:       com.novell.naming.service.nds.NdsInitialContextFactory
	NIS:       com.sun.jndi.nis.NISCtxFactory
	RMI registry: com.sun.jndi.rmi.registry.RegistryContextFactory
	IBM LDAP服务提供者:   com.ibm.jndi.LDAPCtxFactory
	BEA 名字服务提供者:    weblogic.jndi.WLInitialContextFactory
	JBOSS名字服务提供者:  org.jnp.interfaces.NamingContextFactory
