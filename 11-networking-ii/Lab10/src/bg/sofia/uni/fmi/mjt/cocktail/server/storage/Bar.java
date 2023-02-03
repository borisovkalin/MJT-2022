package bg.sofia.uni.fmi.mjt.cocktail.server.storage;

import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.Ingredient;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Bar {
    private static final String CREATE_KEY = "create";
    private static final String GET_KEY_ALL = "get all";
    private static final String GET_KEY_NAME = "get by-name";
    private static final String GET_KEY_INGREDIENT = "get by-ingredient";

    private static final String UNKNOWN = "Unknown command";

    private static final String GET_PREFIX = "{\"status\":\"OK\",\"cocktails\":[";

    private static final String GET_POSTFIX = "]}" + System.lineSeparator();

    private final CocktailStorage storage;

    public Bar() {
        storage = new DefaultCocktailStorage();
    }

    public String clientCommand(String comm) {
        if (comm.startsWith(CREATE_KEY)) {
            String[] arr = comm.substring(CREATE_KEY.length() + 1).split(" ");
            String name = arr[0];
            Set<Ingredient> ingredients = new HashSet<>();
            Arrays.stream(arr)
                    .skip(1)
                    .forEach(str -> {
                        String[] temp = str.split("=");
                        Ingredient here = new Ingredient(temp[0].trim(), temp[1].trim());
                        ingredients.add(here);
                    });
            try {
                storage.createCocktail(new Cocktail(name, ingredients));
            } catch (CocktailAlreadyExistsException e) {
                return "{\"status\":\"ERROR\",\"errorMessage\":\"cocktail " + name + " already exists\"}"
                        + System.lineSeparator();
            }

            return "{\"status\":\"CREATED\"}" + System.lineSeparator();

        } else if (comm.startsWith(GET_KEY_ALL)) {
            return getServerMessage(storage.getCocktails());
        } else if (comm.startsWith(GET_KEY_NAME)) {

            String name = comm.substring(GET_KEY_NAME.length()).trim();

            try {
                return storage.getCocktail(name).toString();
            } catch (CocktailNotFoundException e) {
                return "{\"status\":\"ERROR\",\"errorMessage\":\"cocktail " + name
                        + " not found\"}" + System.lineSeparator();
            }

        } else if (comm.startsWith(GET_KEY_INGREDIENT)) {

            return getServerMessage(storage.getCocktailsWithIngredient(
                    comm.substring(GET_KEY_INGREDIENT.length()).trim()));
        }

        return UNKNOWN + System.lineSeparator();
    }


    private String getServerMessage(Collection<Cocktail> cocktails) {
        String answer = "";
        if (!cocktails.isEmpty()) {
            answer = String.join(",", cocktails.toString());
        }
        return GET_PREFIX + answer + GET_POSTFIX;
    }

}