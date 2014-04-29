Centos6.4 tomcat安装配置apr

####获得APR源码

首先到apr的官网去下载源码

官网地址：[http://apr.apache.org/](http://apr.apache.org/)

我们使用的是apr-1.5.0版本。

####安装APR

	$ tar -zxvf apr-1.5.0.tar.gz 
	$ cd apr-1.5.0
	$ ./configure 
	$ make

	$ su root
	# make install
	
安装成功后会出现如下信息

> Libraries have been installed in:
   /usr/local/apr/lib

####编译tomcat本地代码

到tomcat官网下载好tomcat后

官网地址：[http://tomcat.apache.org/](http://tomcat.apache.org/)

在tomcat目录下(我们使用的是apache-tomcat-7.0.53版本)

	cd apache-tomcat-7.0.53/
	cd bin/
此时会发现有一个tomcat-native.tar.gz的压缩文件，解压
	
	tar -zxvf tomcat-native.tar.gz
进入到解压出来的文件夹下的jni/native/下

	cd tomcat-native-1.1.29-src/jni/native/
	$ ./configure --with-apr=/usr/local/apr/
此时如果没有安装或配置好JDK的JAVA_HOME的话会导致如下错误出现

> checking for JDK location (please wait)... checking Try to guess JDK location... configure: error: can't locate a valid JDK location

添加JAVA_HOME环境变量

	$ vi ~/.bash_profile
在最后面加上
	
	JAVA_HOME=/usr/java/default
	export JAVA_HOME
	PATH=$PATH:$JAVA_HOME/bin
	export PATH
	：wq //保存退出
	
	$ source ~/.bash_profile 
再次执行

	$ ./configure --with-apr=/usr/local/apr/
	$ make
切换到root，回到刚才的目录

	# make install

####在tomcat中配置APR

首先在tomcat根目录/conf/server.xml，找到<Connector 将 protocol改为

	protocol="org.apache.coyote.http11.Http11AprProtocol"

修改tomcat根目录/bin/catalina.sh,找到JAVA_OPTS,添加参数

	-Djava.library.path=/usr/local/apr/lib
启动tomcat如果出现如下信息，证明apr已经配置好了。

> INFO: Loaded APR based Apache Tomcat Native library 1.1.29 using APR version 1.5.0.
> 
> Apr 28, 2014 5:40:26 PM org.apache.catalina.core.AprLifecycleListener init
> 
> INFO: APR capabilities: IPv6 [true], sendfile [true], accept filters [false], random [true].
> 
> Apr 28, 2014 5:40:27 PM org.apache.coyote.AbstractProtocol init

> INFO: Initializing ProtocolHandler ["http-apr-8080"]

> Apr 28, 2014 5:40:27 PM org.apache.coyote.AbstractProtocol init

> INFO: Initializing ProtocolHandler ["ajp-apr-8009"]

> Apr 28, 2014 5:40:27 PM org.apache.catalina.startup.Catalina load

> INFO: Initialization processed in 1031 ms


	

	