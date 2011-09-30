
import org.junit.*;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

public class ApplicationTest extends FunctionalTest {

    private Long id;

    @org.junit.Before
    public void setup() {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("data.yml");
    }

    @Test
    public void testThatIndexPageWorks() {
        Response response = GET("/");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset(play.Play.defaultWebEncoding, response);
    }

    @Test
    public void testValidation() {
        Response response = GET("/search?search=");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset("utf-8", response);
        assertTrue(getContent(response).contains("Errors Encountered"));
    }

    @Test
    public void testSearchPlural() {
        Response response = GET("/search?search=Apple");
        assertIsOk(response);
        assertTrue(getContent(response).contains("Found 2 items"));
    }

    @Test
    public void testSearchSingle() {
        Response response = GET("/search?search=HTC");
        assertIsOk(response);
        assertTrue(getContent(response).contains("Found 1 item"));
        assertFalse(getContent(response).contains("Found 1 items"));
    }

    @Test
    public void testShow() {
        AuctionItem item = AuctionItem.find("title = ?", "iPad").first();
        id = item.id;
        Response response = GET("/listing/" + id);
        assertIsOk(response);
        assertTrue(getContent(response).contains("A 32Gb Apple iPad with Wifi"));
    }
}