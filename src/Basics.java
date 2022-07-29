import io.restassured.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import resources.Utils;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import files.Payload;


public class Basics {

	public static void main(String[] args) throws IOException {
		Response response;
		RestAssured.baseURI = "https://petstore.swagger.io/v2";
		String userNameData = Utils.getGlobalProperty("userName");
		String firstNameData = Utils.getGlobalProperty("firstName");
		String lastNameData = Utils.getGlobalProperty("lastName");
		String emailData = Utils.getGlobalProperty("email");
		String pwdData = Utils.getGlobalProperty("password");
		String phoneData = Utils.getGlobalProperty("phone");
		String newEmailData = Utils.getGlobalProperty("newEmail");
		String[] array1 = {"Kamal", "Kamal", "De Silva", "kamalDS@gmail.com", "kamal123", "+6804584243"};
		String[] array2 = {"Thamal", "Thamal", "Peris", "thamalP@gmail.com", "thamal123", "+5749999235"};
		String[] array3 = {"Sumal", "Sumal", "Dissanayaka", "sumalDiss@gmail.com", "sumal123", "+6799900923"};
		List<String> user = new ArrayList<String>();
		
		//Create User
		response = given().log().all().header("Content-Type","application/json").body(Payload.createUser(userNameData, firstNameData, lastNameData, emailData, pwdData, phoneData))
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
		//When pass the invalid username but code id 404
		given().log().all().when().get("/user/NimaT").then().log().all().assertThat().statusCode(404).extract().response();
		
		//User LogIn
		response = given().log().all().queryParam("username", userNameData).queryParam("password", pwdData)
		.when().get("/user/login").then().log().all().assertThat().statusCode(200).extract().response();
		String headerGetUser = response.getHeaders().getValue("Content-Type");
		assertEquals(headerGetUser,"application/json");
		//When pass the invalid username and password but code id 200
		given().log().all().queryParam("username", 2321).queryParam("password", "dnkfj")
		.when().get("/user/login").then().log().all().assertThat().statusCode(200).extract().response();
		
		//Create User Array
		given().log().all().header("Content-Type","application/json").body(Payload.createUserArray(array1, array2, array3)).when().post("/user/createWithArray").then().log().all().assertThat().statusCode(200);
		
		//Create User List
		given().log().all().header("Content-Type","application/json").body(Payload.createUserArray(array1, array2, array3)).when().post("/user/createWithList").then().log().all().assertThat().statusCode(200);
	
		//Update User
		response = given().log().all().header("Content-Type","application/json").body(Payload.updateUser(userNameData, firstNameData, lastNameData, newEmailData, pwdData, phoneData))
		.when().put("/user/"+userNameData+"").then().assertThat().statusCode(200).log().all().extract().response();
		String headerUpdateUser = response.getHeaders().getValue("Content-Type");
		assertEquals(headerUpdateUser,"application/json");
		String updatedResponse =given().log().all().when().get("/user/"+userNameData+"").then().log().all().extract().response().asString();
		JsonPath js1 = new JsonPath(updatedResponse);	
		assertEquals(js1.get("email"),newEmailData);
		//When pass the invalid username and password but code id 200
		given().log().all().header("Content-Type","application/json").body(Payload.updateUser(userNameData, firstNameData, lastNameData, newEmailData, pwdData, phoneData))
		.when().put("/user/Samal@123").then().assertThat().statusCode(404).log().all();
		//When pass the invalid username and password but code id 200
		given().log().all().header("Content-Type","application/json").body(Payload.updateUser(userNameData, firstNameData, lastNameData, newEmailData, pwdData, phoneData))
		.when().put("/user/123").then().assertThat().statusCode(404).log().all();
		
		//Get user logOut
		response= given().log().all().when().get("/user/logout").then().log().all().assertThat().statusCode(200).extract().response();
		String headerLogoutUser = response.getHeaders().getValue("Content-Type");
		assertEquals(headerLogoutUser,"application/json");
		
		//Delete User
		response= given().log().all().when().delete("/user/"+userNameData+"").then().log().all().assertThat().statusCode(200).extract().response();
		String headerDeleteUser = response.getHeaders().getValue("Content-Type");
		assertEquals(headerDeleteUser,"application/json");
		given().log().all().when().delete("/user/Samal").then().log().all().assertThat().statusCode(404);
		//When pass the invalid username but code id 404
		given().log().all().when().delete("/user/1234").then().log().all().assertThat().statusCode(404);
		
		//Add Pet
		response= given().log().all().when().delete("/user/"+userNameData+"");
		
	}

}
