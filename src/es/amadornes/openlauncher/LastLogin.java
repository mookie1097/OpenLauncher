package es.amadornes.openlauncher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class LastLogin {

	public static String LOGIN = "";
	public static String UUID = "";
	public static String ACCESS_TOKEN = "";
	public static String CLIENT_TOKEN = "";
	
	public static void tryLoading(){
		File f = new File(OpenLauncher.getWorkingDirectory(), "lastLogin.dat");
		
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(f);
			br = new BufferedReader(fr);
			
			LOGIN = br.readLine();
			UUID = br.readLine();
			ACCESS_TOKEN = br.readLine();
			CLIENT_TOKEN = br.readLine();
		} catch (Exception e) {e.printStackTrace();}finally{
			try{
				fr.close();
			}catch(Exception ex){}
			try{
				br.close();
			}catch(Exception ex){}
		}
	}
	
	public static void save(){
		File f = new File(OpenLauncher.getWorkingDirectory(), "lastLogin.dat");
		f.delete();
		PrintWriter pw = null;
		try {
			f.createNewFile();
			pw = new PrintWriter(f);
			pw.println(LOGIN);
			pw.println(UUID);
			pw.println(ACCESS_TOKEN);
			pw.print(CLIENT_TOKEN);
		} catch (IOException e) {e.printStackTrace();f.delete();}finally{
			try{
				pw.close();
			}catch(Exception ex){}
		}
	}

}
