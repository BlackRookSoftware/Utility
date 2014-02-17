/*******************************************************************************
 * Copyright (c) 2013-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.utility;

/**
 * Version info object. Holds version info.
 * @author Matthew Tropiano
 */
public final class Version
{
	private int major;
	private int minor;
	private int release;
	private int build;
	private String suffix;
	
	/**
	 * Create new version object.
	 */
	public Version()
	{
		this(0, 0, 0, 0, "");
	}
	
	/**
	 * Create new version object.
	 */
	public Version(int major, int minor, int release, int build)
	{
		this(major, minor, release, build, "");
	}
	
	/**
	 * Create new version object.
	 */
	public Version(int major, int minor, int release, int build, String suffix)
	{
		this.major = major;
		this.minor = minor;
		this.release = release;
		this.build = build;
		this.suffix = suffix;
	}
	
	public int getMajor()
	{
		return major;
	}

	public void setMajor(int major)
	{
		this.major = major;
	}

	public int getMinor()
	{
		return minor;
	}

	public void setMinor(int minor)
	{
		this.minor = minor;
	}

	public int getRelease()
	{
		return release;
	}

	public void setRelease(int release)
	{
		this.release = release;
	}

	public int getBuild()
	{
		return build;
	}

	public void setBuild(int build)
	{
		this.build = build;
	}

	public String getSuffix()
	{
		return suffix;
	}

	public void setSuffix(String suffix)
	{
		this.suffix = suffix;
	}

	/**
	 * Gets the full string version of this version, leaving nothing out.
	 */
	public String getFullString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(major).append('.');
		sb.append(minor).append('.');
		sb.append(release).append('.');
		sb.append(build);
		if (suffix != null) sb.append(suffix);
		return sb.toString();
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(major).append('.').append(minor);
		if (build != 0)
			sb.append('.').append(release).append('.').append(build);
		else if (release != 0)
			sb.append('.').append(release);
		if (suffix != null) sb.append(suffix);
		return sb.toString();
	}
}
