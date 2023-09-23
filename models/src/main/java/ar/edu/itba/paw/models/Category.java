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

    @Override
    public String toString() {
        return displayName;
    }

    public String getFormattedName() {
        return toString();
    }
}