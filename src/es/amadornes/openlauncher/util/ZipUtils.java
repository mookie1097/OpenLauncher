package es.amadornes.openlauncher.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {
	
	public static void unzip(String zipFile, String outputFolder) {
		new ZipUtils().unzip2(zipFile, outputFolder);
	}

	List<String> fileList;
	
	private void unzip2(String zipFile, String outputFolder){
		byte[] buffer = new byte[1024];
		try {
			File folder = new File(outputFolder);
			if (!folder.exists()) {
				folder.mkdir();
			}
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
			
				String fileName = ze.getName();
				File newFile = new File(outputFolder + File.separator + fileName);

				new File(newFile.getParent()).mkdirs();
				
				if(ze.isDirectory()){
					newFile.mkdir();
				}else{
					if(newFile.exists())
						newFile.delete();
					newFile.createNewFile();
					FileOutputStream fos = new FileOutputStream(newFile);
					
					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}

					fos.close();
				}
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}