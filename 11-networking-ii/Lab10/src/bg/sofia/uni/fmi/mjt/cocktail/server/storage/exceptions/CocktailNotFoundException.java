package bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions;

public class CocktailNotFoundException extends Exception {
    public CocktailNotFoundException(String msg) {
        super("{\"status\":\"ERROR\",\"errorMessage\":\"cocktail " + msg + " not found\"}");
    }
}
