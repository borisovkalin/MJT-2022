package bg.sofia.uni.fmi.mjt.cocktail.server;

import java.util.Objects;
import java.util.Set;

public record Cocktail(String name, Set<Ingredient> ingredients) {
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{\"name\":\"").append(name).append("\",\"ingredients\":[");
        stringBuilder.append(String.join(",", ingredients.toString()));

        stringBuilder.append("]}");
        return  stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cocktail cocktail = (Cocktail) o;
        return name.equals(cocktail.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
