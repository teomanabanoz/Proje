package library.domain.enums;

public enum BookStatus {
    AVAILABLE("Available"),
    BORROWED("Borrowed"),
    LOST("Lost"),
    DAMAGED("Damaged"),
    UNDER_MAINTENANCE("Under Maintenance");

    private final String displayName;

    BookStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
