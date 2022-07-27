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
	
	public static String createUserArray(String username, String firstname, String lastname, String email, String pwd, String phone) {
		return "[\r\n"
				+ "  {\r\n"
				+ "    \"id\": 0,\r\n"
				+ "    \"username\": \"string\",\r\n"
				+ "    \"firstName\": \"string\",\r\n"
				+ "    \"lastName\": \"string\",\r\n"
				+ "    \"email\": \"string\",\r\n"
				+ "    \"password\": \"string\",\r\n"
				+ "    \"phone\": \"string\",\r\n"
				+ "    \"userStatus\": 0\r\n"
				+ "  }\r\n"
				+ "]";
	}
}
