package org.codeorange.backend.util;

public class StringUtil {
	
	public static String nonNull(String str) {
		return ((str != null) ? str : "");
	}

}