package de.marckoch.skillmatrix.skills.entity;

import javax.persistence.AttributeConverter;
import java.time.YearMonth;

class YearMonthStringAttributeConverter implements AttributeConverter<YearMonth, String> {

    @Override
    public String convertToDatabaseColumn(YearMonth attribute) {
        if (attribute != null) {
            return attribute.toString();
        }
        return null;
    }

    @Override
    public YearMonth convertToEntityAttribute(String db) {
        if (db != null) {
            String[] parts = db.split("-");
            return YearMonth.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
        return null;
    }
}
