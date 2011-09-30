import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {
    
    @Before
    public void setup() {
        Fixtures.deleteAllModels();
        Fixtures.loadModels("data.yml");
    }
    
    @Test
    public void testData() {
        AuctionItem item = AuctionItem.find("title = ?", "iPad").first();
        assertEquals("iPad", item.title);
        assertEquals((Float)4.99F, item.deliveryCost);
        assertEquals((Float)1F, item.startBid);
        assertEquals(false, item.buyNowEnabled);
        assertEquals(User.find("byUsername", "bobmarlay").first(), item.createdBy);
    }
    
    @Test
    public void testCount() {
        assertEquals(4, AuctionItem.count());
    }
    
    @Test
    public void testSearch() {
        assertEquals(2, (long)AuctionItem.search("Apple", 1).count);
        assertEquals(1, (long)AuctionItem.search("iPad", 1).count);        
    }
}
