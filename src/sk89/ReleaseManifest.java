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

import java.net.URL;
import java.util.Date;
import java.util.List;

import net.technicpack.launchercore.minecraft.Library;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReleaseManifest {

	private String id;
	private Date time;
	private Date releaseTime;
	private String type;
	private String processArguments;
	private String minecraftArguments;
	private String mainClass;
	private int minimumLauncherVersion;
	public List<Library> libraries;

	@JsonIgnore
	public URL getJarUrl() {
		return HttpRequest.url(String.format(
				"http://s3.amazonaws.com/Minecraft.Download/versions/%s/%s.jar",
				getId(), getId()));
	}

}
