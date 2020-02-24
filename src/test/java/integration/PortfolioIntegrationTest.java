package integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zemoga.portfolio.model.Portfolio;
import io.restassured.RestAssured;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@Category(IntegrationCategoryMarker.class)
public class PortfolioIntegrationTest extends BaseIntegrationTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getPortfolioByIdWithTwitterIntegration() {
        RestAssured.get(this.url + "/user_info/1").then().assertThat()
                .statusCode(200)
                .body("idPortfolio", equalTo("1"))
                .body("twitterUserName", equalTo("ronaldo"))
                .body("imageUrl", equalTo("image-url"))
                .body("description", equalTo("description"))
                .body("title", equalTo("title"))
                .body("tweets.size()", is(5));
    }

    @Test
    public void getPortfolioByIdWithInvalidUsernameNoTwitterIntegration() {
        RestAssured.get(this.url + "/user_info/2").then().assertThat()
                .statusCode(200)
                .body("idPortfolio", equalTo("2"))
                .body("twitterUserName", equalTo("invalid user"))
                .body("imageUrl", equalTo("image2"))
                .body("description", equalTo("description2"))
                .body("title", equalTo("title2"))
                .body("tweets.size()", is(0));
    }

    @Test
    public void getPortfolioByIdNotFound() {
        RestAssured.get(this.url + "/user_info/100").then().assertThat()
                .statusCode(404)
                .body("Error", equalTo("Not Found"))
                .body("status", equalTo(404))
                .body("message", equalTo("There was not portfolio found with id: 100"));
    }

    @Test
    public void getPortfolioByIdInvalidId() {
        RestAssured.get(this.url + "/user_info/not_valid_id").then().assertThat()
                .statusCode(400)
                .body("Error", equalTo("Bad Request"))
                .body("status", equalTo(400))
                .body("message", equalTo("The portfolio Id can't be null and must be a valid Number greater than 0."));
    }

    @Test
    public void modifyPortfolioById() throws JsonProcessingException {
        var portfolio = RestAssured.get(this.url + "/user_info/1").then().extract().response().getBody().as(Portfolio.class);
        portfolio.setTwitterUserName("new_username");
        portfolio.setImageUrl("new_image");
        portfolio.setDescription("new_description");
        portfolio.setTitle("new_title");
        String jsonPortfolio = this.objectMapper.writeValueAsString(portfolio);

        RestAssured.given().body(jsonPortfolio).contentType("application/json").post(this.url + "/modify_user_info/1").then().assertThat()
                .statusCode(200)
                .body("idPortfolio", equalTo("1"))
                .body("twitterUserName", equalTo("new_username"))
                .body("imageUrl", equalTo("new_image"))
                .body("description", equalTo("new_description"))
                .body("title", equalTo("new_title"))
                .body("tweets.size()", is(0));
    }

    @Test
    public void modifyPortfolioByIdWrongUrlWithoutIdInPath() {
        RestAssured.given().body("").contentType("application/json").post(this.url + "/modify_user_info/").then().assertThat()
                .statusCode(301)
                .body("location", equalTo("/modify_user_info/{idPortfolio}"))
                .body("message", equalTo("This endpoint is not valid, should use '/modify_user_info/{idPortfolio}' endpoint instead"));
    }
}
