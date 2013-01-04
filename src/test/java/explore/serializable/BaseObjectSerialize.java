package explore.serializable;


import explore.serializable.model.User;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Morgan
 * Date: 13-1-4
 * Time: 下午9:30
 */
public class BaseObjectSerialize {

	@Test
	public void testWriteObject(){
		User user = new User("Young","Morgan");
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("user.bin"));

			objectOutputStream.writeObject(user);
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testWriteObjects(){
		List<User> users = new ArrayList<User>();
		for(int i = 0; i<100;i++){
			User user = new User("Young"+i,"Morgan"+i);
			users.add(user);
		}

		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("userList.bin"));

			objectOutputStream.writeObject(users  );
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testReadObject(){
		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("user.bin"));

			User user = (User) objectInputStream.readObject();
			System.out.println(user.getLastName()+user.getFirstName());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testReadObjects(){
		try {
			ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("userList.bin"));


			List<User> users = (List<User>) objectInputStream.readObject();
			for(User user : users){
				System.out.println(user.getLastName()+user.getFirstName());
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
