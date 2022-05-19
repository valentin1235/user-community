package zaritalk.community.app.controller.domain;

import zaritalk.community.enums.EAccountType;
import javax.persistence.AttributeConverter;

public class AccountTypeConvertor implements AttributeConverter<EAccountType, Integer> {

    // custom
    public static EAccountType convertStringToAttribute(final String accountTypeString) {
        switch (accountTypeString) {
            case "Lessor":
                return EAccountType.LESSOR;
            case "Lessee":
                return EAccountType.LESSEE;
            case "Realtor":
                return EAccountType.REALTOR;
            default:
                return EAccountType.NONE;
        }
    }

    @Override
    public Integer convertToDatabaseColumn(final EAccountType type) {
        if (type == null) {
            return null;
        }

        return type.code();
    }

    @Override
    public EAccountType convertToEntityAttribute(final Integer code) {
        switch (code) {
            case 0:
                return EAccountType.LESSOR;
            case 1:
                return EAccountType.REALTOR;
            case 2:
                return EAccountType.LESSEE;
            case 3:
                return EAccountType.NONE;
        }

        return EAccountType.NONE;
    }
}