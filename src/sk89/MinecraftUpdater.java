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

import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.technicpack.launchercore.minecraft.Library;

import org.apache.commons.codec.digest.DigestUtils;
import org.omg.CORBA.Environment;

import es.amadornes.openlauncher.util.Downloader;
import es.amadornes.openlauncher.util.Util;

/**
 * Updates an installation of Minecraft.
 */
class MinecraftUpdater extends AbstractWorker<MinecraftInstall> {

	private static final String VERSION_MANIFEST_URL =
			"https://s3.amazonaws.com/Minecraft.Download/versions/%s/%s.json";
	private static final String ASSETS_URL =
			"https://s3.amazonaws.com/MinecraftResources/";

	private static final Logger logger = LauncherUtils.getLogger(MinecraftUpdater.class);
	private final ExecutorService executor = Executors.newFixedThreadPool(6);
	private final MinecraftInstall instance;
	private final Environment environment;
	private final InstallerRuntime installer;

	/**
	 * Create a new instance.
	 *
	 * @param instance the installation instance
	 */
	public MinecraftUpdater(MinecraftInstall instance) {
		this.instance = instance;
		this.environment = instance.getEnvironment();

		File temporaryDir = instance.getProfile().getTemporaryDir();

		installer = new InstallerRuntime(executor, environment);
		installer.setTemporaryDir(temporaryDir);
	}

	/**
	 * Get the URL of the JSON file that tells information about the desired version.
	 *
	 * @return the URL
	 */
	private URL getManifestUrl() {

		return new URL(VERSION_MANIFEST_URL.replace("%s", "1.7.2"));
	}

	/**
	 * Get the URL where assets can be found.
	 *
	 * @param marker the bucket marker indicating the entry to start at
	 * @return the URL
	 */
	private URL getAssetsUrl(String marker) {
		if (marker.length() == 0) {
			return getAssetsUrl();
		} else {
			return url(ASSETS_URL + "?" + form().add("marker", marker).toString());
		}
	}

	/**
	 * Get the base URL where assets can be found.
	 *
	 * @return the URL
	 */
	private URL getAssetsUrl() {
		return url(ASSETS_URL);
	}

	@Override
	public MinecraftInstall call() throws Exception {
		try {
			WorkUnit step1 = split(0.02),
					step2 = split(0.02),
					step3 = split(0.96);

			if (!hasSystemProperty(MinecraftUpdater.class, "skipAssets")) {
				installAssets(step1);
				checkInterrupted();
			}

			installGame(step2);
			checkInterrupted();

			logger.log(Level.INFO, "Install tasks enumerated; now installing...");

			installer.addObserver(step3);
			installer.call();

			return instance;
		} finally {
			executor.shutdownNow();
		}
	}

	/**
	 * Install the game.
	 *
	 * @param workUnit split for progress tacking
	 * @throws IOException thrown on I/O error
	 * @throws InterruptedException thrown on interruption
	 */
	protected void installGame() throws IOException, InterruptedException {
		logger.log(Level.INFO, "Checking for game updates...");
		System.out.println("minecraftUpdate.checkingGameUpdates");

		File contentDir = Util.getInstancesFolder();
		File librariesDir = Util.getInstancesFolder();
		File jarPath = Util.getInstancesFolder();
		File manifestPath = Util.getInstancesFolder();

		// Obtain the release manifest, save it, and parse it
		ReleaseManifest manifest = HttpRequest
				.get(getManifestUrl())
				.execute()
				.expectResponseCode(200)
				.returnContent()
				.saveContent(manifestPath)
				.asJson(ReleaseManifest.class);

		// If the JAR does not exist, install it
		if (!jarPath.exists()) {
			Downloader.download((manifest.getJarUrl()), jarPath);
		}

		// Install all the missing libraries
		for (Library library : manifest.getLibraries()) {
			if (library.matches(environment)) {
				URL url = library.getUrl(environment);
				File file = new File(librariesDir, library.getPath(environment));

				if (!file.exists()) {
					Downloader.download(url, file);
				}

				//checkInterrupted();
			}
		}
	}

	/**
	 * Add shared Minecraft assets to the installer.
	 *
	 * @param workUnit split for progress tacking
	 * @throws IOException on I/O error
	 * @throws InterruptedException on interruption
	 */
	protected void installAssets(WorkUnit workUnit) throws IOException, InterruptedException {
		logger.log(Level.INFO, "Checking for asset downloads...");

		File assetsDir = instance.getAssetsDir();
		String marker = "";

		while (marker != null) {
			URL bucketUrl = getAssetsUrl(marker);
			workUnit.push(0, _("minecraftUpdate.checkingAssets", bucketUrl.toString()));
			logger.log(Level.INFO, "Enumerating assets from {0}...", bucketUrl);
			checkInterrupted();

			// Obtain the assets manifest
			AWSBucket bucket = HttpRequest
					.get(bucketUrl)
					.execute()
					.returnContent()
					.asXml(AWSBucket.class);

			// Install all the missing assets
			for (AWSBucket.Item item : bucket.getContents()) {
				String key = item.getKey();
				String hash = item.getEtag();

				if (item.isDirectory()) {
					continue; // skip directories
				}

				URL url = item.getUrl(getAssetsUrl());
				File file = new File(assetsDir, key);

				if (!file.exists() || !getFileETag(file).equals(hash)) {
					logger.log(Level.INFO, "Need to get {0}", key);
					String id = hash + file.toString();
					installer.copyTo(new HttpResource(url).withId(id), file);
				}

				marker = item.getKey();
			}

			// If the last bucket list is not truncated, then we're done
			if (!bucket.isTruncated()) {
				marker = null;
			}
		}
	}

	/**
	 * Generate the Etag hash string that is returned by the assets location.
	 *
	 * @param file the file
	 * @return the etag hash string
	 */
	protected String getFileETag(File file) {
		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
			return "\"" + DigestUtils.md5Hex(is) + "\"";
		} catch (IOException e) {
			return "";
		} finally {
			closeQuietly(is);
		}
	}

}
