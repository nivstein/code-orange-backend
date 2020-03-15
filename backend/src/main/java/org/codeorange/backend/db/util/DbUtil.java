package org.codeorange.backend.db.util;

import java.util.ArrayList;
import java.util.List;

public class DbUtil {
	
	public static final String TABLE_NAME_RECORDED_LOCATIONS		= "recorded_locations";
	public static final String TABLE_NAME_IMPORTED_LOCATIONS_IL_MOH	= "imported_locations_il_moh";

	//NNNTODO refactor
	public static List<String> getTablesByCountry(String country) {
		List<String> result = new ArrayList<>();

		result.add(TABLE_NAME_RECORDED_LOCATIONS);

		if ("il".equalsIgnoreCase(country)) {
			result.add(TABLE_NAME_IMPORTED_LOCATIONS_IL_MOH);
		}

		return result;
	}

}