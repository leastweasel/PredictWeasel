package org.leastweasel.predict.domain;

import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.joda.time.DateTime;

@Converter
public class DateTimeToDateConverter implements AttributeConverter<DateTime, Date> {
	/**
	 * 
	 */
    @Override
    public Date convertToDatabaseColumn(DateTime attribute) {
        if (attribute == null) {
            return null;
        }
        
        return attribute.toDate();
    }

    /**
     * 
     * @param dbData
     * @return
     */
    @Override
    public DateTime convertToEntityAttribute(Date dbData) {
        if (dbData == null) {
            return null;
        }

        return new DateTime(dbData.getTime());
    }
}