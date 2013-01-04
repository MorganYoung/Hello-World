package explore.serializable.model;

import java.io.*;

/**
 * User: Morgan
 * Date: 13-1-4
 * Time: 下午9:27
 */
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final ObjectStreamField[] serialPersistentFields = {
			new ObjectStreamField("firstName",String.class)
	};

	private String firstName;
	private String lastName;

	public User(String firstName, String lastName){
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	private void writeObject(ObjectOutputStream output) throws IOException {
		output.defaultWriteObject();
		output.writeUTF("Hello "+getLastName());
	}

	private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
		input.defaultReadObject();
		System.out.println(input.readUTF());
	}
}
