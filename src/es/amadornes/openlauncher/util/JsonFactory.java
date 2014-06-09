package es.amadornes.openlauncher.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 *CRIT  from ftb launcher should replace!
 * @author FTB team
 *
 */
public class JsonFactory {
	public static final Gson GSON;
	static {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapterFactory(new EnumAdaptorFactory());
		builder.registerTypeAdapter(Date.class, new DateAdapter());
		builder.registerTypeAdapter(File.class, new FileAdapter());
		builder.enableComplexMapKeySerialization();
		builder.setPrettyPrinting();
		GSON = builder.create();
	}

	public static Version loadVersion (File json) throws JsonSyntaxException, JsonIOException, IOException {
		FileReader reader = new FileReader(json);
		Version v =  GSON.fromJson(reader, Version.class);
		reader.close();
		return v;
	}

	public static AssetIndex loadAssetIndex (File json) throws JsonSyntaxException, JsonIOException, IOException {
		FileReader reader = new FileReader(json);
		AssetIndex a =  GSON.fromJson(reader, AssetIndex.class);
		reader.close();
		return a;
	}
}