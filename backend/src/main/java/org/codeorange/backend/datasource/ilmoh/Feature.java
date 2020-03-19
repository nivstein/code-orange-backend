package org.codeorange.backend.datasource.ilmoh;

/*package*/ class Feature {

	private Geometry geometry;
	private Properties properties;

	public Feature() {

	}

	public Feature(Geometry geometry, Properties properties) {
		this.geometry = geometry;
		this.properties = properties;
	}

	public Geometry getGeometry() {
		return this.geometry;
	}
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public Properties getProperties() {
		return this.properties;
	}
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}