package bg.sofia.uni.fmi.mjt.cocktail.server.storage;

import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.Ingredient;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultCocktailStorage implements CocktailStorage {

    private final Set<Cocktail> cocktails;
    private final Map<String, Cocktail> cocktailsByName;
    private final Map<String, Set<Cocktail>> cocktailsByIngredients;

    public DefaultCocktailStorage() {
        cocktails = new HashSet<>();
        cocktailsByName = new HashMap<>();
        cocktailsByIngredients = new HashMap<>();
    }

    @Override
    public void createCocktail(Cocktail cocktail) throws CocktailAlreadyExistsException {
        if (cocktails.contains(cocktail)) {
            throw new CocktailAlreadyExistsException(cocktail.name());
        }
        cocktails.add(cocktail);
        cocktailsByName.put(cocktail.name(), cocktail);
        for (Ingredient i : cocktail.ingredients()) {
            cocktailsByIngredients.putIfAbsent(i.name(), new HashSet<>());
            cocktailsByIngredients.get(i.name()).add(cocktail);
        }
    }

    @Override
    public Collection<Cocktail> getCocktails() {
        return cocktails;
    }

    @Override
    public Collection<Cocktail> getCocktailsWithIngredient(String ingredientName) {
        return cocktailsByIngredients.entrySet()
                .stream()
                .filter(c -> c.getKey().equals(ingredientName))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public Cocktail getCocktail(String name) throws CocktailNotFoundException {
        if (!cocktails.contains(new Cocktail(name, null))) {
            throw new CocktailNotFoundException(name);
        }
        return cocktailsByName.get(name);
    }
}
