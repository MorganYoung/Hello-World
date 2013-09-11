package java_explore.serializable.model;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * User: morgan
 * Date: 13-9-5
 * Time: 下午5:34
 */
public class User implements Serializable {

	private static final long serialVersionUID = 123456L;

	private static final ObjectStreamField[] serialPersistentFields = {
			new ObjectStreamField("name",String.class),
			new ObjectStreamField("birthday",Date.class)
	};

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String name;
	private Integer age;
	private Date birthday;


	private transient String password;

	private transient String idCardNum;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIdCardNum() {
		return idCardNum;
	}

	public void setIdCardNum(String idCardNum) {
		this.idCardNum = idCardNum;
	}

	private void writeObject( ObjectOutputStream out ) throws IOException {
		out.defaultWriteObject();
		out.writeUTF("hello !!!");
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		String value = in.readUTF();
		System.out.println(value);
	}


	@Override
	public String toString() {
		return "name:"+this.name+"\tage:"+this.age+"\tbirthday:"+
				(this.birthday == null ? "null" : format.format(birthday)) +
				"\tpassword:"+this.password+"\tidCardNum:"+this.idCardNum;

	}
}
