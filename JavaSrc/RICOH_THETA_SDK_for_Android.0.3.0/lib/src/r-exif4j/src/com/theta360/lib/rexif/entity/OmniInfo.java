package com.theta360.lib.rexif.entity;

/**
 * Body information at time of capture
 */
public class OmniInfo {

	// If does not retrieved, will be null.
	private Double horizontalAngle = null;
	private Double elevationAngle = null;
	private Double orientationAngle = null;

	/**
	 * Acquires the horizontal angle of the body at the time of capture
	 * 
	 * @return Horizontal angle [0 to 360] or "null" if this information cannot be acquired
	 */
	public Double getHorizontalAngle() {
		return horizontalAngle;
	}

	/**
	 * Sets the horizontal angle of the body at the time of capture
	 * 
	 * @param horizontalAngle
	 *            Horizontal angle of the body at the time of capture
	 */
	public void setHorizontalAngle(Double horizontalAngle) {
		this.horizontalAngle = horizontalAngle;
	}

	/**
	 * Acquires the elevation angle of the body at the time of capture
	 * 
	 * @return Elevation angle [-90 to 90] or "null" if this information cannot be acquired
	 */
	public Double getElevationAngle() {
		return elevationAngle;
	}

	/**
	 * Sets the elevation angle of the body at the time of capture
	 * 
	 * @param elevationAngle
	 *            Elevation angle of the body at the time of capture
	 */
	public void setElevationAngle(Double elevationAngle) {
		this.elevationAngle = elevationAngle;
	}

	/**
	 * Acquires the elevation angle of the body at the time of capture
	 * 
	 * @return Elevation angle [0 to 360] or "null" if this information cannot be acquired
	 */
	public Double getOrientationAngle() {
		return orientationAngle;
	}

	/**
	 * Sets the elevation angle of the body at the time of capture
	 * 
	 * @param orientationAngle
	 *            Orientation angle of the body at the time of capture
	 */
	public void setOrientationAngle(Double orientationAngle) {
		this.orientationAngle = orientationAngle;
	}
}
