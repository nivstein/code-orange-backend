package org.codeorange.backend.datasource;

import java.util.List;

import org.codeorange.backend.data.Location;

public interface LocationDataLoader {
	public List<Location> load();
}