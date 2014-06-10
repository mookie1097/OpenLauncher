/*
 * SK's Minecraft Launcher
 * Copyright (C) 2010, 2011 Albert Pham <http://www.sk89q.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package sk89;


/**
 * Represents information about the current environment.
 */

public class Environment {

	private final Platform platform;
	private final String platformVersion;

	/**
	 * Get an instance of the current environment.
	 *
	 * @return the current environment
	 */
	public static Environment getInstance() {
		return new Environment(detectPlatform(), System.getProperty("os.version"));
	}

	/**
	 * Detect the current platform.
	 *
	 * @return the current platform
	 */
	public static Platform detectPlatform() {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("win"))
			return Platform.WINDOWS;
		if (osName.contains("mac"))
			return Platform.MAC_OS_X;
		if (osName.contains("solaris") || osName.contains("sunos"))
			return Platform.SOLARIS;
		if (osName.contains("linux"))
			return Platform.LINUX;
		if (osName.contains("unix"))
			return Platform.LINUX;

		return Platform.UNKNOWN;
	}

}
