package bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions;

public class CocktailAlreadyExistsException extends Exception {
    public CocktailAlreadyExistsException(String msg) {
        super("{\"status\":\"ERROR\",\"errorMessage\":\"cocktail " + msg + " already exists\"}");
    }
}
