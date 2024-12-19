package library.domain.enums;

public enum BookCategory {
    PROGRAMMING("Programming"),
    MATHEMATICS("Mathematics"),
    SCIENCE("Science"),
    LITERATURE("Literature"),
    HISTORY("History"),;

    private final String displayName;

    BookCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}