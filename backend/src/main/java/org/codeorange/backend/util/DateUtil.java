package org.codeorange.backend.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static final String ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

	public static long iso8601ToTimestamp(String iso) throws ParseException {
		return new SimpleDateFormat(ISO8601_FORMAT)
				.parse(iso)
				.getTime();
	}

}