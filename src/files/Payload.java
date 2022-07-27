package files;

public class Payload {

	public static String createUser(String username, String firstname, String lastname, String email, String pwd, String phone) {
		return "{\r\n"
				+ "  \"id\": 0,\r\n"
				+ "  \"username\": \""+username+"\",\r\n"
				+ "  \"firstName\": \""+firstname+"\",\r\n"
				+ "  \"lastName\": \""+lastname+"\",\r\n"
				+ "  \"email\": \""+email+"\",\r\n"
				+ "  \"password\": \""+pwd+"\",\r\n"
				+ "  \"phone\": \""+phone+"\",\r\n"
				+ "  \"userStatus\": 0\r\n"
				+ "}";
	}
}
