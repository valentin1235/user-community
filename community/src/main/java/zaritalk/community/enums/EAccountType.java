package zaritalk.community.utils.enums;

public enum EAccountType {
    LESSOR(0), REALTOR(1), LESSEE(2), NONE(3);

    private int code;

    EAccountType(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}
