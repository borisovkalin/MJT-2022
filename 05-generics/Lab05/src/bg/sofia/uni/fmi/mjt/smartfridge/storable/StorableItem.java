package bg.sofia.uni.fmi.mjt.smartfridge.storable;

import bg.sofia.uni.fmi.mjt.smartfridge.storable.type.StorableType;

import java.time.LocalDate;

public class StorableItem implements Storable {

    private final String name;
    private final StorableType type;
    private final LocalDate expiration;

    public StorableItem(String name, StorableType type, LocalDate expiration) {
        this.name = name;
        this.type = type;
        this.expiration = expiration;
    }

    @Override
    public StorableType getType() {
        return this.type;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public LocalDate getExpiration() {
        return this.expiration;
    }

    @Override
    public boolean isExpired() {
        return expiration.isBefore(LocalDate.now());
    }
}
