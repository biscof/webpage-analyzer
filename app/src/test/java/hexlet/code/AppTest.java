package hexlet.code;

import hexlet.code.models.Url;
import hexlet.code.models.query.QUrl;
import io.ebean.DB;
import io.ebean.Database;
import io.javalin.Javalin;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    private static Javalin app;
    private static String baseUrl;
    private static Database database;

    @BeforeAll
    public static void beforeAll() {
        app = App.getApp();
        app.start(0);
        int port = app.port();
        baseUrl = "http://localhost:" + port;
        database = DB.getDefault();
    }

    @AfterAll
    public static void afterAll() {
//        database.script().run("/truncate.sql");
        app.stop();
    }

    @BeforeEach
    void beforeEach() {
        database.script().run("/truncate.sql");
        database.script().run("/seed.sql");
    }

    @Test
    void testRoot() {
        HttpResponse<String> response = Unirest.get(baseUrl).asString();
        assertEquals(200, response.getStatus());
        assertTrue(response.getBody().contains("<h1>Анализатор страниц</h1>"));
    }

    @Test
    void testCreateUrlValidInput() {
        String initUrl = "http://testwebsite.com/community/users";
        String newUrlName = "http://testwebsite.com";

        HttpResponse responsePost = Unirest.post(baseUrl + "/urls")
                .field("url", initUrl)
                .asEmpty();
        assertEquals(302, responsePost.getStatus());
        assertEquals("/urls", responsePost.getHeaders().getFirst("Location"));

        HttpResponse<String> responseGet = Unirest.get(baseUrl + "/urls?page=2").asString();
        assertEquals(200, responseGet.getStatus());
        assertTrue(responseGet.getBody().contains("Страница успешно добавлена"));
        assertTrue(responseGet.getBody().contains(newUrlName));
        
        Url url = new QUrl()
                .name.equalTo(newUrlName)
                .findOne();

        assertNotNull(url);
        assertEquals(newUrlName, url.getName());
    }

    @Test
    void testCreateUrlInvalidInput() {
        String invalidUrl = "http//testwebsitecom/community/users";

        HttpResponse responsePost = Unirest.post(baseUrl + "/urls")
                .field("url", invalidUrl)
                .asEmpty();
        assertEquals(302, responsePost.getStatus());
        assertEquals("/", responsePost.getHeaders().getFirst("Location"));

        HttpResponse<String> responseGet = Unirest.get(baseUrl).asString();
        assertEquals(200, responseGet.getStatus());
        assertTrue(responseGet.getBody().contains("Некорректный URL"));
    }

    @Test
    void testListUrls() {
        HttpResponse<String> response = Unirest.get(baseUrl + "/urls").asString();
        assertEquals(200, response.getStatus());
        assertTrue(response.getBody().contains("https://domainmarket.com"));
        assertTrue(response.getBody().contains("https://princeton.edu"));
        assertFalse(response.getBody().contains("http://samsung.com"));

        HttpResponse<String> response2 = Unirest.get(baseUrl + "/urls?page=2").asString();
        assertEquals(200, response.getStatus());
        assertTrue(response2.getBody().contains("http://samsung.com"));
        assertFalse(response2.getBody().contains("https://princeton.edu"));
    }

    @Test
    void showUrl() {
        HttpResponse<String> response = Unirest.get(baseUrl + "/urls/{id}")
                .routeParam("id", "3")
                .asString();
        assertEquals(200, response.getStatus());
        assertTrue(response.getBody().contains("http://amazon.de"));
        assertTrue(response.getBody().contains("28/06/2023 02:30"));
    }
}