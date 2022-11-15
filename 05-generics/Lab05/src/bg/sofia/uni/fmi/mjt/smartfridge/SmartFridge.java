package bg.sofia.uni.fmi.mjt.smartfridge;

import bg.sofia.uni.fmi.mjt.smartfridge.comparator.StorableByExpiration;
import bg.sofia.uni.fmi.mjt.smartfridge.exception.FridgeCapacityExceededException;
import bg.sofia.uni.fmi.mjt.smartfridge.exception.InsufficientQuantityException;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.DefaultIngredient;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.Ingredient;
import bg.sofia.uni.fmi.mjt.smartfridge.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class SmartFridge implements SmartFridgeAPI {

    private final int totalCapacity;
    private int currentCapacity;
    private Map<String, Queue<Storable>> fridgeStorage;


    public SmartFridge(int totalCapacity) {
        this.totalCapacity = totalCapacity;
        fridgeStorage = new HashMap<>();
    }

    @Override
    public <E extends Storable> void store(E item, int quantity) throws FridgeCapacityExceededException {
        validateNull(item);
        validateQuantity(quantity);
        validateStoreQuantity(quantity);

        String itemName = item.getName();
        fridgeStorage.putIfAbsent(itemName, new PriorityQueue<>(new StorableByExpiration()));

        for (int i = 0; i < quantity; ++i) {
            fridgeStorage.get(itemName).add(item);
        }

        currentCapacity += quantity;
    }

    @Override
    public List<? extends Storable> retrieve(String itemName) {
        validateNull(itemName);
        validateString(itemName);

        if (!fridgeStorage.containsKey(itemName)) {
            return new ArrayList<>();
        }

        List<Storable> result = new ArrayList<>(fridgeStorage.get(itemName));
        fridgeStorage.remove(itemName);

        result.sort(new StorableByExpiration());
        return result;
    }

    @Override
    public List<? extends Storable> retrieve(String itemName, int quantity) throws InsufficientQuantityException {
        validateNull(itemName);
        validateString(itemName);
        validateQuantity(quantity);
        validateRetrieveItem(itemName);
        validateRetrieveQuantity(quantity, fridgeStorage.get(itemName).size());

        List<Storable> result = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            result.add(fridgeStorage.get(itemName).poll());
        }

        if (fridgeStorage.get(itemName) == null) {
            fridgeStorage.remove(itemName);
        }
        result.sort(new StorableByExpiration());
        return result;
    }

    @Override
    public int getQuantityOfItem(String itemName) {
        validateNull(itemName);
        validateString(itemName);

        if (!fridgeStorage.containsKey(itemName)) {
            return 0;
        }

        return fridgeStorage.get(itemName).size();
    }

    @Override
    public Iterator<Ingredient<? extends Storable>> getMissingIngredientsFromRecipe(Recipe recipe) {
        validateNull(recipe);
        Map<String, Queue<Storable>> cleanFridge = new HashMap<>(fridgeStorage);
        removeExpiredForRecipe(cleanFridge);

        Set<Ingredient<? extends Storable>> set = recipe.getIngredients();
        Set<Ingredient<? extends Storable>> resSet = new HashSet<>();

        for (var ingredient : set) {
            String itemName = ingredient.item().getName();

            if (!cleanFridge.containsKey(itemName)) {
                resSet.add(ingredient);
                continue;
            }
            if (cleanFridge.get(itemName).size() < ingredient.quantity()) {
                int difference = ingredient.quantity() - cleanFridge.get(itemName).size();
                Ingredient<? extends Storable> missingIng = new DefaultIngredient<>(ingredient.item(), difference);
                resSet.add(missingIng);
            }

        }

        return resSet.iterator();
    }


    @Override
    public List<? extends Storable> removeExpired() {
        List<Storable> result = new ArrayList<>();

        for (var entries : fridgeStorage.entrySet()) {
            Queue<Storable> q = entries.getValue();
            Storable item = q.peek();
            while (!q.isEmpty() && item.isExpired()) {
                result.add(item);
                q.remove();
                item = q.peek();
            }
        }

        currentCapacity -= result.size();
        return result;
    }

    private void removeExpiredForRecipe(Map<String, Queue<Storable>> map) {
        for (var entries : map.entrySet()) {
            Queue<Storable> q = entries.getValue();
            Storable item = q.peek();
            while (!q.isEmpty() && item.isExpired()) {
                q.remove();
                item = q.peek();
            }
        }
    }

    private <V> void validateNull(V obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Obj can't be null");
        }
    }

    private void validateString(String str) {
        if (str.isBlank()) {
            throw new IllegalArgumentException("String name can't be blank/empty");
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity can't be <= 0");
        }
    }

    private void validateRetrieveQuantity(int quantityNeeded, int quantityStored)
            throws InsufficientQuantityException {
        if (quantityStored - quantityNeeded < 0) {
            throw new InsufficientQuantityException("Not enough space to store this many items");
        }
    }

    private void validateStoreQuantity(int quantity) throws FridgeCapacityExceededException {
        if (currentCapacity + quantity > totalCapacity) {
            throw new FridgeCapacityExceededException("Not enough space to store this many items");
        }
    }

    private void validateRetrieveItem(String item) throws InsufficientQuantityException {
        if (!fridgeStorage.containsKey(item)) {
            throw new InsufficientQuantityException("Item is not in the fridge");
        }
    }
}
