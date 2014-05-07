## 使用JMX透过防火墙远程监控tomcat服务 ##

JDK的bin目录下有jvisualvm或jconsole可以监控本地和远程jvm实例的运行动态（包括cpu，内存等），
对于性能分析或内存泄露分析等极其方便。下面介绍如何通过这两个工具远程监控有防火墙的linux上的tomcat服务。
废话少说，直接上正题：

我的测试环境是：centos 6.2（IP为192.168.1.118）上通过jsvc将tomcat 7作为服务启动。




- 下载http://mirror.bjtu.edu.cn/apache/tomcat/tomcat-7/v7.0.28/bin/extras/catalina-jmx-remote.jar并放在tomcat7的$CATALINA_BASE/lib目录。



- 修改tomcat7的$CATALINA_BASE/conf/server.xml,在<Server port="8005" shutdown="SHUTDOWN"> 下加入监听器：

		<Listener className="org.apache.catalina.mbeans.JmxRemoteLifecycleListener"
          rmiRegistryPortPlatform="10001" rmiServerPortPlatform="10002" />



- 建立文本文件$CATALINA_BASE/conf/jmxremote.password，其内容为：admin letmein


- 建立文本文件$CATALINA_BASE/conf/jmxremote.access，其内容为：admin readwrite


- 修改jsvc的服务启动配置文件，加入启动参数，如：

		CATALINA_OPTS="$CATALINA_OPTS -Xms128m -Xmx200m -XX:PermSize=64M -XX:MaxPermSize=256m -XX:+UseConcMarkSweepGC \
		-Djava.rmi.server.hostname=192.168.1.118 \
		-Dcom.sun.management.jmxremote.password.file=$CATALINA_BASE/conf/jmxremote.password \
		-Dcom.sun.management.jmxremote.access.file=$CATALINA_BASE/conf/jmxremote.access \
		-Dcom.sun.management.jmxremote.ssl=false"



- 开通linux防火墙的端口：10001和10002.（例如：在/etc/sysconfig/iptables中加入

		-A INPUT -m state --state NEW -m tcp -p tcp --dport 10001 -j ACCEPT
		-A INPUT -m state --state NEW -m tcp -p tcp --dport 10002 -j ACCEPT），
注意要重启防火墙使生效。



- 至此远程可以使用jvisualvm或jconsole通过地址：（192.168.1.118:10001或者service:jmx:rmi:///jndi/rmi://192.168.1.118:10001/jmxrmi）,使用用户admin密码letmein登录
动态监控tomcat服务了.
