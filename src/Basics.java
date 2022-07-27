import io.restassured.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import resources.Utils;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.assertEquals;

import java.io.IOException;

import files.Payload;


public class Basics {

	public static void main(String[] args) throws IOException {
		RestAssured.baseURI = "https://petstore.swagger.io/v2";
		String userNameData = Utils.getGlobalProperty("userName");
		String firstNameData = Utils.getGlobalProperty("firstName");
		String lastNameData = Utils.getGlobalProperty("lastName");
		String emailData = Utils.getGlobalProperty("email");
		String pwdData = Utils.getGlobalProperty("password");
		String phoneData = Utils.getGlobalProperty("phone");
		
		//Create User
		Response response = given().log().all().header("Content-Type","application/json").body(Payload.createUser(userNameData, firstNameData, lastNameData, emailData, pwdData, phoneData))
		.when().post("/user")
		.then().log().all().assertThat().statusCode(200).extract().response();
		
		String header = response.getHeaders().getValue("Content-Type");
		assertEquals(header,"application/json");
		 
		String idResponse =given().log().all().when().get("/user/"+userNameData+"").then().log().all().extract().response().asString();
		JsonPath js = new JsonPath(idResponse);		
		String userName = js.getString("username");
		assertEquals(userName, userNameData);
		String firstName = js.getString("firstName");
		assertEquals(firstName, firstNameData);
		String lastName = js.getString("lastName");
		assertEquals(lastName, lastNameData);
		String email = js.getString("email");
		assertEquals(email, emailData);
		String password = js.getString("password");
		assertEquals(password, pwdData);
		String phone = js.getString("phone");
		assertEquals(phone, phoneData);
		
		//Get User from user name
		response = given().log().all().when().get("/user/"+userNameData+"").then().log().all().assertThat().statusCode(200).extract().response();
				
		String headerGet = response.getHeaders().getValue("Content-Type");
		assertEquals(headerGet,"application/json");
		
		given().log().all().when().get("/user/Acentura").then().log().all().assertThat().statusCode(404).extract().response();
		given().log().all().when().get("/user/1234").then().log().all().assertThat().statusCode(200).extract().response();
		
		//User LogIn
		response = given().log().all().queryParam("username", userNameData).queryParam("password", pwdData)
		.when().get("/user/login").then().log().all().assertThat().statusCode(200).extract().response();
		
		String headerGetUser = response.getHeaders().getValue("Content-Type");
		assertEquals(headerGetUser,"application/json");
		
		given().log().all().queryParam("username", "Acent").queryParam("password", "Acent123")
		.when().get("/user/login").then().log().all().assertThat().statusCode(400).extract().response();
		
		//Create User Array
		given().log().all().header("Content-Type","application/json").body(Payload.createUserArray(array1, array2, array3));
		
		//Create User List
	}

}
