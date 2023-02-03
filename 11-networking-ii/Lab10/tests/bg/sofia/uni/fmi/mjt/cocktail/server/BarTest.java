package bg.sofia.uni.fmi.mjt.cocktail.server;

import bg.sofia.uni.fmi.mjt.cocktail.server.storage.Bar;
import org.junit.jupiter.api.Test;

public class BarTest {

    @Test
    void testCreate(){
        Bar na = new Bar();
        na.clientCommand("create manhattan whysky=100ml cola=100ml\n".trim());
        System.out.println(na.clientCommand("get all"));
    }
}
