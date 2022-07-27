import io.restassured.*;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.assertEquals;

import files.Payload;


public class Basics {

	public static void main(String[] args) {
		RestAssured.baseURI = "https://petstore.swagger.io/v2";
		
		//Create User
		String response = given().log().all().header("Content-Type","application/json").body(Payload.createUser("username", "firstname", "lastname", "email", "pwd", "phone"))
		.when().post("/user")
		.then().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js = new JsonPath(response);
		String header = js.getString("Content-Type");
		assertEquals(header,"application/json");
		
		String userId = js.getString("id");
		String idResponse =when().get("/user/username").then().extract().response().asString();
		JsonPath js1 = new JsonPath(idResponse);
		assertEquals(userId, js1.get("id"));
		
		String userName = js.getString("username");
		assertEquals(userName, "username");
		String firstName = js.getString("firstname");
		assertEquals(firstName, "firstname");
		String lastName = js.getString("lastname");
		assertEquals(lastName, "lastname");
		String email = js.getString("email");
		assertEquals(email, "email");
		String password = js.getString("pwd");
		assertEquals(password, "pwd");
		String phone = js.getString("phone");
		assertEquals(phone, "phone");
		
		//Get User from user name
		
	}

}
