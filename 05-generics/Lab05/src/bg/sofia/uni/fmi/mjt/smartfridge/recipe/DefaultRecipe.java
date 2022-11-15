package bg.sofia.uni.fmi.mjt.smartfridge.recipe;

import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.DefaultIngredient;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.Ingredient;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DefaultRecipe<E extends Storable> implements Recipe {

    private Map<String, Ingredient<? extends Storable>> ingredients;


    @Override
    public Set<Ingredient<? extends Storable>> getIngredients() {
        Set<Ingredient<? extends Storable>> ingredientsSet2 = new HashSet<>();
        for (var ingredient : ingredients.entrySet()) {
            ingredientsSet2.add(ingredient.getValue());
        }
        return ingredientsSet2;
    }

    @Override
    public void addIngredient(Ingredient<? extends Storable> ingredient) {
        String ingredientName = ingredient.item().getName();

        if (this.ingredients.containsKey(ingredientName)) {
            int newQuantity = this.ingredients.get(ingredientName).quantity() + ingredient.quantity();

            this.ingredients.put(ingredientName, new DefaultIngredient<>(ingredient.item(), newQuantity));
        }
        this.ingredients.put(ingredientName, ingredient);
    }

    @Override
    public void removeIngredient(String itemName) {
        ingredients.remove(itemName);
    }
}
