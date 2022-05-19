package zaritalk.community.enums;

public enum EAccountType {
    LESSOR(0, "임대인"), REALTOR(1, "공인중개사"), LESSEE(2, "임차인"), NONE(3, "외부 사용자");

    private final int code;
    private final String name;

    EAccountType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int code() {
        return this.code;
    }

    public String toString() {
        return this.name;
    }
}
