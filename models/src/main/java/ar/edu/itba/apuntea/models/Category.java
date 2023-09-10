package ar.edu.itba.apuntea.models;

public enum Category {
    EXAM("Exam"),
    PRACTICE("Practice"),
    THEORY("Theory"),
    OTHER("Other");

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