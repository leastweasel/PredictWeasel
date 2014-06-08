package org.leastweasel.predict.domain;

import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.joda.time.DateTime;

/**
 * An object that converts between Date and DateTime objects so that JPA can
 * persist Joda dates correctly. 
 */
@Converter
public class DateTimeToDateConverter implements AttributeConverter<DateTime, Date> {
	/**
	 * Convert a DateTime value into a Date so that it can be stored in a database.
	 * 
	 * @param attribute the DateTime to convert
	 * @return the converted Date
	 */
    @Override
    public Date convertToDatabaseColumn(DateTime attribute) {
        if (attribute == null) {
            return null;
        }
        
        return attribute.toDate();
    }

	/**
	 * Convert a Date value, that has been read from a database, into a DateTime.
	 * 
	 * @param dbData the Date that has been read from the database
	 * @return the converted DateTime
	 */
    @Override
    public DateTime convertToEntityAttribute(Date dbData) {
        if (dbData == null) {
            return null;
        }

        return new DateTime(dbData.getTime());
    }
}