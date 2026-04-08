package tests;

import com.github.javafaker.Faker;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import requestsBuilder.ApiRequestBuilder;




import static org.hamcrest.CoreMatchers.equalTo;

public class UserRegistrationTest {

    static String registeredEmail;


    @Test
    public void adminLoginTest(){

        ApiRequestBuilder.loginUserResponse("spare@admin.com", "@12345678")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test(dependsOnMethods = "adminLoginTest")
    public void userRegistration(){
        registeredEmail = Faker.instance().internet().emailAddress();
        ApiRequestBuilder.registerUserResponse("Register","MphoTest",registeredEmail,"@1236544", "1deae17a-c67a-4bb0-bdeb-df0fc9e2e526")
                .then()
                .log().all()
                .assertThat()
                .statusCode(201)
                .body("success", equalTo(true));
    }

    @Test(dependsOnMethods = "userRegistration")
    public void approveUserRegistration(){
        ApiRequestBuilder.approveUserRegistrationResponse()
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test(dependsOnMethods = "approveUserRegistration")
    public void updateUserRole(){
        ApiRequestBuilder.updateUserRoleResponse("admin")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }


    @Test(dependsOnMethods = "updateUserRole")
    public void userLoginTest(){

        ApiRequestBuilder.loginUserResponse(registeredEmail, "@1236544")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test(dependsOnMethods = "userLoginTest")
    public void getUserTest()
    {
        ApiRequestBuilder.getUserResponse().then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("data.Role", equalTo("admin"));  // Check role of the user



    }


    @Test(dependsOnMethods = "getUserTest")
    public void adminLoginTestB(){

        ApiRequestBuilder.loginUserResponse("spare@admin.com", "@12345678")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test(dependsOnMethods = "adminLoginTestB")
    public void deleteUserTest()
    {
        ApiRequestBuilder.deleteUserResponse().then()
                .log().all()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true));




    }

}
