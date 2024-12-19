package library.domain.enums;

public enum MemberType {
    STUDENT("Student"),
    TEACHER("Teacher"),
    ADMIN("Admin");

    private final String displayName;

    MemberType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
