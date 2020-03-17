package org.codeorange.backend.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

	public static final String ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

	public static long iso8601ToTimestamp(String iso) throws ParseException {
		return new SimpleDateFormat(ISO8601_FORMAT)
				.parse(iso)
				.getTime();
	}

	public static boolean isSameDay(Date date1, Date date2, ZoneId zoneId) {
		LocalDate localDate1 = date1.toInstant().atZone(zoneId).toLocalDate();
		LocalDate localDate2 = date2.toInstant().atZone(zoneId).toLocalDate();

		return localDate1.isEqual(localDate2);
	}

}