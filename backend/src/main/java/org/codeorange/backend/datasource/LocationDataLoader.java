package org.codeorange.backend.datasource;

import java.util.List;

import org.codeorange.backend.api.data.Location;

public interface LocationDataLoader {
	public List<Location> load();
}