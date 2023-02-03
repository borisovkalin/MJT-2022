package bg.sofia.uni.fmi.mjt.feed;

import bg.sofia.uni.fmi.mjt.feed.util.URICreator;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class URICreatorTest {

    @Test
    void testNextPage() {
        URICreator creator = new URICreator();
        URICreator creator2 = new URICreator();
        creator.create("","","news",1);
        URI newer = creator2.create("","","news",2);
        URI old = creator.nextPage();
        System.out.println(newer.toString());
        System.out.println(old.toString());
        assertEquals(newer.toString(),old.toString(),"Expected equal paths");

    }
}
