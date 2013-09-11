package java_explore.serializable;

import java_explore.serializable.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: morgan
 * Date: 13-9-5
 * Time: 下午5:33
 */
public class BaseSerializable {

	public static void main(String[] args) throws Exception {

		simpleWrite();
		simpleRead();
//		arrayWrite();
//		arrayRead();

	}

	public static void arrayWrite() throws Exception {
		User user = null;

		List<User> userList = new ArrayList<User>();
		for(int i = 0 ;i < 100; i++) {
			user = new User();
			user.setName("morgan"+i);
			user.setAge(22+i);
			user.setBirthday(new Date());
			userList.add(user);

		}
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("D:/serializable/userList2.bin"));
		objectOutputStream.writeObject(userList);
		objectOutputStream.flush();
		objectOutputStream.close();

	}

	public static void arrayRead() throws Exception {
		ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("D:/serializable/userList2.bin"));

		List<User> userObjs = (List<User>) objectInputStream.readObject();
		for( User user : userObjs ) {

			System.out.println(user);
		}
	}

	public static void simpleRead() throws Exception {
		ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("D:/serializable/user_p.bin"));

		User userObj = (User) objectInputStream.readObject();

		System.out.println(userObj);

	}

	public static void simpleWrite() throws Exception{
		User user = new User();
		user.setName("morgan");
		user.setAge(22);
		user.setBirthday(new Date());
		user.setIdCardNum("1234567890");
		user.setPassword("123456");

		ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("D:/serializable/user_p.bin"));
		objectOutputStream.writeObject(user);
		objectOutputStream.flush();
		objectOutputStream.close();
	}

}
