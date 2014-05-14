###从weblogic到tomcat

这篇文章的主要目的是为了和大家分享一下我们是如何将车险网销从weblogic环境上迁移到tomcat上的。

***

weblogic的版本是10.3.4

tomcat版本是7.0.50

谈到weblogic和tomcat的移植问题是一个大的话题，我们不可能说的那么全面，只是针对车险网销的环境谈谈我们是如何移植的。

在实际开发的过程中，开发人员首先是在自己本地用tomcat测试自己负责的模块的代码，通过后在部署到weblogic的测试环境上去，一般我们都是这样做的。我们所迁移的工程也是这样的开发过程。

所以这就为我们将项目从weblogic上迁移到tomcat上面创造了很好的前期条件，至少每个模块都可以单独部署在tomcat上面。

####tomcat和weblogic比较

网上有很多这样的文章，在此我们也没有必要重复。只说一下我们需要关注的几个重点。

#####1. 中间件的实现
不管是weblogic还是tomcat都是用Java现实的。

同样他们依赖的运行环境也是一样都是JVM（尽管JVM的实现不一样，但都遵循同样的规范）。

两个中间件的体量上会有些区别。

######weblogic

weblogic最早是由美国的BEA公司（现在是Oracle的了）出品的application server确切的说是一个基于J2EE架构的中间件，它全面的支持J2EE的规范，并且有自己对于J2EE规范的实现。

BEA WebLogic是用于开发、集成、部署和管理大型分布式Web应用、网络应用和数据库应用的Java应用服务器。将Java的动态功能和Java Enterprise标准的安全性引入大型网络应用的开发、集成、部署和管理之中。（官网的一段话）

weblogic其自身就支持集群，WebLogic Server既实现了网页群集，也实现了EJB组件 群集，而且不需要任何专门的硬件或操作系统支持。

当然weblogic不是免费的需要购买相应功能的License才行。

######tomcat
首先引用官网的一段话

> Apache Tomcat is an open source software implementation of the Java Servlet and JavaServer Pages technologies. The Java Servlet and JavaServer Pages specifications are developed under the Java Community Process.

> Apache Tomcat is developed in an open and participatory environment and released under the Apache License version 2. Apache Tomcat is intended to be a collaboration of the best-of-breed developers from around the world. We invite you to participate in this open development project. 

> Apache Tomcat powers numerous large-scale, mission-critical web applications across a diverse range of industries and organizations. Some of these users and their stories are listed on the PoweredBy wiki page.

我们关注的是这几个关键字，open source 开源的；JSP 技术；a collaboration of the best-of-breed developers 最优秀的开发人员合作开发的；large-scale， mission-critical大规模关键任务；

tomcat本身并没有完全实现J2EE规范，需要添加第三方的J2EE的jar包。

tomcat需要联合Apache HTTP才能够完成集群，再加上Apache的扩展和tomcat的扩展，才能够成为一个功能完善运行稳定的大规模集群。


#####2. JDK版本
说道JVM那就不得不提JDK版本。

weblogic10版本用的是jdk1.6的（不管是sun的jdk还是oracle的jrocket）

而tomcat7同样也可以使用jdk1.6 jdk1.7

我们的项目的开发就是在1.6版本的JDK上面做的。

#####3. ClassNotFound
迁移过程中经常会出现这种ClassNotFound的异常。

总结来看大部分请情况是这样的。

从tomcat迁移到weblogic上面报ClassNotFound可能是由于jar包冲突，前面提到过，weblogic对于J2EE规范有自己的实现，而tomcat是通过采用第三方的jar包来完成对于J2EE的实现的；这样就难以避免会产生冲突。

从weblogic迁移到tomcat上面报ClassNotFound可能就是真的没有这个类，那就需要想tomcat中添加第三方的jar包。

######对于tomca中缺少jar包的比较懒的解决方式

当我们把weblogic上的项目迁移到tomcat上面时可能需要引用weblogic内部的jar，一般的做法是直接引用weblogic.jar；但是这里要说另外的一个做法，直接将weblogic的内部jar打成一个jar包。

我们用到了weblogic给我们提供好的工具 wljarbuilder.jar

首先要引用一下Oracle官方的文档

> The following sections provide information on creating the wlfullclient.jar using the WebLogic JarBuilder tool:

	
	Use the following steps to create a wlfullclient.jar file for a JDK 1.6 client application:

 	Change directories to the server/lib directory.
	打开weblogic安装目录下的server/lib目录

 	cd WL_HOME/server/lib

 	Use the following command to create wlfullclient.jar in the server/lib directory:
	运行如下命令执行wljarbuilder.jar这个jar包

 	java -jar wljarbuilder.jar

 	You can now copy and bundle the wlfullclient.jar with client applications.

 	Add the wlfullclient.jar to the client application’s classpath.

	你可以将生成的wlfullclient.jar拷贝到你的application的classpath中

并不是所有的缺失jar包都可以用wlfullclient.jar来代替，有时候真得需要weblogic.jar，或者真的需要增加第三方的jar包才可以。

关于wlfullclient.jar和weblogic.jar的选取给大家一个链接参考[oracle官方](http://docs.oracle.com/cd/E12840_01/wls/docs103/client/basics.html#wp1069994)

我们将wlfullclient.jar放到tomcat中之后，同样可能会引发tomcat自带的servlet-api的jar包冲突；我们需要去掉项目中的或者tomcat中的jar，当然还要具体情况具体分析，选择去掉哪方的jar包。

####短时间迁移不了的功能
由于车险网销的的规则引擎采用的是Ilog，tomcat上面是无法部署的，只能放到weblogic上面；而且调用方式也会产生变化，原来是在一个容器内的调用，现在需要改成容器外部基于某种网络协议的调用方式。

我们用了一台单独的服务器安装了weblogic，并将ilog部署在weblogic上面同时我们增加了ilog的jrules-res-session-WAS7的jar包，以便能够以remote的形式调用ilog

#####ilog java客户端调用方式
######J2SE级别

j2se是不访问rule execution server中的项目，是需要将rule project放到java project中的res_data目录下。

######simple级别
包括simple和management，二者的共同点是java代码必须和运行业务规则的服务器位于同一个JVM中。

######remote级别
通过ejb方式访问，所以允许客户端java代码和运行业务规则的服务器不在同一个jvm中（不在同一台机器）。

我们需要采用remote的方式调用，因为我们把tomcat和weblogic放在不同的机器上面了。

这是原来java调用ilog的代码

######ILogRuleServiceImpl代码
	protected IlrRuleSessionProvider getProvider()
			throws IlrRuleSessionCreationException, Exception, Exception {

		PrintWriter writer = new PrintWriter(System.out);
		return new IlrRuleSessionProviderFactory.Builder(
				IlrRuleSessionProviderFactory.SIMPLE_RULESESSION_PROVIDER)
				.setLogger(writer).build();
	}

######修改后的IlogRuleServiceImpl代码

	protected IlrRuleSessionProvider getProvider()
			throws IlrRuleSessionCreationException, Exception, Exception {

		PrintWriter writer = new PrintWriter(System.out);
        Properties jndiProperties = new Properties();
        jndiProperties.setProperty(Context.INITIAL_CONTEXT_FACTORY, 
			"weblogic.jndi.WLInitialContextFactory");
        jndiProperties.setProperty(Context.PROVIDER_URL,  "t3://xxx.xxx.xxx.xxx:7001");
        return new IlrRuleSessionProviderFactory.Builder(jndiProperties)
				.setLogger(writer).build();
	}

那weblogic的jndi为什么前面要t3:

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

为了防止规则引擎调用（本质是利用RMI），找不到类，我们必须把项目中需要作为返回结果或参数的自定义的类打包放到weblogic的classpath中（放到对应domain的lib下即可）。


