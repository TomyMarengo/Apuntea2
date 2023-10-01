package ar.edu.itba.paw.models;

import java.util.UUID;

public class Career {
    private final UUID careerId;
    private String name;
    public Career(UUID careerId, String name) {
        this.careerId = careerId;
        this.name = name;
    }

    public UUID getCareerId() {
        return careerId;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return careerId.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Career)) return false;
        Career c = (Career) o;
        return c.careerId.equals(careerId);
    }

}
