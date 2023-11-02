package ar.edu.itba.paw.models;

public enum Category {
    EXAM("exam"),
    PRACTICE("practice"),
    THEORY("theory"),
    OTHER("other"),

    DIRECTORY("directory"),
    NOTE("note"); // Abstraction for all categories except directory

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getFormattedName() {
        return displayName;
    }

    public String getType() {
        return this == DIRECTORY ? DIRECTORY.getFormattedName() : NOTE.getFormattedName();
    }
}