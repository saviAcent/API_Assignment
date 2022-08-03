import io.restassured.*;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import resources.Utils;

import static io.restassured.RestAssured.*;
import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.log4testng.Logger;

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
		String newEmailData = Utils.getGlobalProperty("newEmail");
		String[] array1 = {"Kamal", "Kamal", "De Silva", "kamalDS@gmail.com", "kamal123", "+6804584243"};
		String[] array2 = {"Thamal", "Thamal", "Peris", "thamalP@gmail.com", "thamal123", "+5749999235"};
		String[] array3 = {"Sumal", "Sumal", "Dissanayaka", "sumalDiss@gmail.com", "sumal123", "+6799900923"};
		int idData = Integer.parseInt(Utils.getGlobalProperty("id"));
		int categoryIDData = Integer.parseInt(Utils.getGlobalProperty("categoryId"));
		String categoryNameData = Utils.getGlobalProperty("categoryName");
		String nameData = Utils.getGlobalProperty("name");
		int tagIDData = Integer.parseInt(Utils.getGlobalProperty("tagId"));
		String tagNameData = Utils.getGlobalProperty("tagName");
		String statusData = Utils.getGlobalProperty("status");
		String newNameData = Utils.getGlobalProperty("newName");
		
		//Create User
		String addedUserName= createUser(userNameData, firstNameData, lastNameData, emailData, pwdData, phoneData);
		
		
		//Get User from user name
		getUserDetails(addedUserName);
		
		//User LogIn
		userLogIn(addedUserName, pwdData);
		
		//Create User Array
		given().log().all().header("Content-Type","application/json").body(Payload.createUserArray(array1, array2, array3))
		.when().post("/user/createWithArray").then().log().all().assertThat().statusCode(200);
		
		//Create User List
		given().log().all().header("Content-Type","application/json")
		.body(Payload.createUserList(userNameData, firstNameData, lastNameData, emailData, pwdData, phoneData))
		.when().post("/user/createWithList").then().log().all().assertThat().statusCode(200);
	
		//Update User
		updateUser(addedUserName, firstNameData, lastNameData, newEmailData, pwdData, phoneData);
		
		//Get user logOut
		userLogOut();
		
		//Delete User
		deleteUser(addedUserName);
		
		//Error 415 - unsupported media type
		//Add Pet
		int idData1 = addPet(categoryNameData, nameData, tagNameData, statusData);
		
		//Add pet with image
		addPetImage(idData1);
		
		//Update existing pet
		updatePet(idData1, categoryNameData, newNameData, tagNameData, statusData);
		
		//Get pet by petid
		getPetDetails(idData1, nameData);
		
	}
	
	public static String createUser(String userNameData, String firstNameData, String lastNameData, String emailData, String pwdData, String phoneData) {
		Response response = given().log().all().header("Content-Type","application/json").body(Payload.createUser(userNameData, firstNameData, lastNameData, emailData, pwdData, phoneData))
		.when().post("/user")
		.then().log().all().assertThat().statusCode(200).extract().response();		
		String header = response.getHeaders().getValue("Content-Type");
		assertEquals(header,"application/json");
		String idResponse =given().when().get("/user/"+userNameData+"").then().log().all().extract().response().asString();
		assertEquals(Utils.getJsonPath(idResponse, "username"), userNameData);
		assertEquals(Utils.getJsonPath(idResponse, "firstName"), firstNameData);
		assertEquals(Utils.getJsonPath(idResponse, "lastName"), lastNameData);
		assertEquals(Utils.getJsonPath(idResponse, "email"), emailData);
		assertEquals(Utils.getJsonPath(idResponse, "password"), pwdData);
		assertEquals(Utils.getJsonPath(idResponse, "phone"), phoneData);
		return Utils.getJsonPath(idResponse, "username");	
	}
	
	public static void getUserDetails(String userNameData) {
		try {
			Response response = given().log().all().when().get("/user/"+userNameData+"").then().log().all().assertThat().statusCode(200).extract().response();	
			String headerGet = response.getHeaders().getValue("Content-Type");
			assertEquals(headerGet,"application/json");		
		}catch(Exception e) {
			e.printStackTrace();
		}		
		
		try {
			given().log().all().when().get("/user/"+userNameData+"").then().log().all().assertThat().statusCode(404).extract().response();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			//When pass the invalid username but code id 404
			given().log().all().when().get("/user/"+userNameData+"").then().log().all().assertThat().statusCode(400).extract().response();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void userLogIn(String userNameData, String pwdData) {
		try {
			Response response = given().log().all().queryParam("username", userNameData).queryParam("password", pwdData)
			.when().get("/user/login").then().log().all().assertThat().statusCode(200).extract().response();
			String headerGetUser = response.getHeaders().getValue("Content-Type");
			assertEquals(headerGetUser,"application/json");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			//When pass the invalid username and password but code id 200
			given().log().all().queryParam("username", 2321).queryParam("password", "dnkfj")
			.when().get("/user/login").then().log().all().assertThat().statusCode(200).extract().response();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void updateUser(String userNameData, String firstNameData, String lastNameData, String newEmailData, String pwdData, String phoneData) {
		Response response = given().log().all().header("Content-Type","application/json").body(Payload.updateUser(userNameData, firstNameData, lastNameData, newEmailData, pwdData, phoneData))
		.when().put("/user/"+userNameData+"").then().assertThat().statusCode(200).log().all().extract().response();
		String headerUpdateUser = response.getHeaders().getValue("Content-Type");
		assertEquals(headerUpdateUser,"application/json");
		
		String updatedResponse =given().log().all().when().get("/user/"+userNameData+"").then().log().all().extract().response().asString();	
		assertEquals(Utils.getJsonPath(updatedResponse, "email"),newEmailData);
		
		//When pass the invalid username and password but code id 200
		given().log().all().header("Content-Type","application/json").body(Payload.updateUser(userNameData, firstNameData, lastNameData, newEmailData, pwdData, phoneData))
		.when().put("/user/Samal@123").then().assertThat().statusCode(404).log().all();
		
		//When pass the invalid username and password but code id 200
		given().log().all().header("Content-Type","application/json").body(Payload.updateUser(userNameData, firstNameData, lastNameData, newEmailData, pwdData, phoneData))
		.when().put("/user/123").then().assertThat().statusCode(404).log().all();
	}
	
	public static void userLogOut() {
		Response response= given().log().all().when().get("/user/logout").then().log().all().assertThat().statusCode(200).extract().response();
		String headerLogoutUser = response.getHeaders().getValue("Content-Type");
		assertEquals(headerLogoutUser,"application/json");
	}
	
	public static void deleteUser(String userNameData) {
		Response response= given().log().all().when().delete("/user/"+userNameData+"").then().log().all().assertThat().statusCode(200).extract().response();
		String headerDeleteUser = response.getHeaders().getValue("Content-Type");
		assertEquals(headerDeleteUser,"application/json");
		
		given().log().all().when().delete("/user/Samal").then().log().all().assertThat().statusCode(404);
		
		//When pass the invalid username but code id 404
		given().log().all().when().delete("/user/1234").then().log().all().assertThat().statusCode(400);
	}
	
	public static int addPet(String categoryNameData, String nameData, String tagNameData, String statusData) {
		Response response= given().log().all().body(Payload.addPet(categoryNameData, nameData, tagNameData, statusData)).when().post("/pet");
		String headerAddPet = response.getHeaders().getValue("Content-Type");
		assertEquals(headerAddPet,"application/xml");
		response.then().log().all().extract().asString();
		assertEquals(response.getStatusCode(), 200);
		String idData = Utils.getJsonPath(response.asString(), "id");
		
		String petResponse =given().log().all().when().get("/pet/"+idData+"").then().log().all().extract().response().asString();
		assertEquals(Utils.getJsonPath(petResponse, "name"), nameData);
		
		response= given().log().all().body(Payload.addPet(categoryNameData, "sdshd", tagNameData, statusData)).when().post("/pet");
		assertEquals(response.statusCode(), 405);
		
		return Integer.parseInt(idData);
	}
	
	public static void addPetImage(int idData) {
		File file = new File("download.jpg");
		Response response = given().log().all().multiPart(file).when().post("/pet/"+idData+"/uploadImage")
				.then().log().all().assertThat().statusCode(200).extract().response();
		String petHeader = response.getHeaders().getValue("Content-Type");
		assertEquals(petHeader,"application/json");
	}
	
	public static void updatePet(int id, String categoryNameData, String nameData, String tagNameData, String statusData) {
		Response response= given().log().all().body(Payload.updatePet(id, categoryNameData, nameData, tagNameData, statusData)).when().put("/pet");
		String headerAddPet = response.getHeaders().getValue("Content-Type");
		assertEquals(headerAddPet,"application/json");
		response.then().log().all().extract().asString();
		assertEquals(response.statusCode(), 200);
		assertEquals(Utils.getJsonPath(response.asString(), "name"), nameData);
	}
	
	public static void getPetDetails(int idData, String nameData) {
		String petResponse =given().log().all().when().get("/pet/"+idData+"").then().log().all().extract().response().asString();		
		assertEquals(Utils.getJsonPath(petResponse, "name"), nameData);
	}

}
