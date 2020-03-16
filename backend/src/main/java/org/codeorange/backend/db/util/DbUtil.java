package org.codeorange.backend.db.util;

import java.util.ArrayList;
import java.util.List;

import org.codeorange.backend.data.Country;

public class DbUtil {
	
	public static final String TABLE_NAME_RECORDED_LOCATIONS		= "recorded_locations";
	public static final String TABLE_NAME_IMPORTED_LOCATIONS_IL_MOH	= "imported_locations_il_moh";

	public static List<String> getTablesByCountry(String country) {
		List<String> result = new ArrayList<>();

		result.add(TABLE_NAME_RECORDED_LOCATIONS);

		if (Country.ISRAEL.getCode().equalsIgnoreCase(country)) {
			result.add(TABLE_NAME_IMPORTED_LOCATIONS_IL_MOH);
		}

		return result;
	}

}