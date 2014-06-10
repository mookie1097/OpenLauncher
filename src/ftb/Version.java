package ftb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.technicpack.launchercore.minecraft.Library;

public class Version {
	public String id;
	public Date time;
	public Date releaseTime;
	public String type;
	public String minecraftArguments;
	public List<Library> libraries;
	public String mainClass;
	public int minimumLauncherVersion;
	public String incompatibilityReason;
	public List<OSRule> rules;
	public String assets;

	private List<Library> _libraries;

	public List<Library> getLibraries () {
		if (_libraries == null) {
			_libraries = new ArrayList<Library>();
			if (libraries == null)
				return _libraries;
			for (Library lib : libraries) {
				if (lib.applies()) {
					_libraries.add(lib);
				}
			}
		}
		return _libraries;
	}

	public String getAssets () {
		return assets == null ? "legacy" : assets;
	}
}