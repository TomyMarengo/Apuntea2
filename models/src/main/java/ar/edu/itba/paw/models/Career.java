package ar.edu.itba.paw.models;

import java.util.UUID;

public class Career {
    private UUID careerId;
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

}
