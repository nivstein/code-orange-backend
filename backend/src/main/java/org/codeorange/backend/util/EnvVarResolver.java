package org.codeorange.backend.util;

public class EnvVarResolver {

	private static final int NONE	= 0;
	private static final int ESC_1	= 1;
	private static final int ESC_2	= 2;
	
	public static String resolve(String str) {
		StringBuilder sb = new StringBuilder();
		
		int state = NONE;
		int startIndex = -1;

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			switch (state) {
				case NONE: {
					if (c == '$') {
						state = ESC_1;
					} else {
						sb.append(c);
					}
				}
				break;

				case ESC_1: {
					if (c == '{') {
						state = ESC_2;
						startIndex = i + 1;
					} else if (c == '$') {
						sb.append('$');
					} else {
						state = NONE;
						sb.append('$').append(c);
					}
				}
				break;

				case ESC_2: {
					if (c == '}') {
						String name = str.substring(startIndex, i);
						String value = StringUtil.nonNull(System.getenv(name));

						state = NONE;
						sb.append(value);
					}
				}
				break;
			}
		}

		switch (state) {
			case NONE: {
				// do nothing
			}
			break;

			case ESC_1: {
				sb.append('$');
			}
			break;

			case ESC_2: {
				sb.append("${").append(str.substring(startIndex));
			}
			break;
		}

		return sb.toString();
	}

}
