package java_explore.serializable.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: morgan
 * Date: 13-9-5
 * Time: 下午5:34
 */
public class User implements Serializable {

	private static final long serialVersionUID = 123456L;

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String name;
	private Integer age;
	private Date birthday;

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

	@Override
	public String toString() {
		return "name:"+this.name+"\tage:"+this.age+"\tbirthday:"+format.format(birthday);
	}
}
