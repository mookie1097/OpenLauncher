package es.amadornes.openlauncher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import net.minecraft.login.MojangAuth;
import es.amadornes.openlauncher.util.Util;

public class LastLogin {

	public static String UUID = "";
	public static String ACCESS_TOKEN = "";
	public static String CLIENT_TOKEN = "";
	
	public static void tryLoading(){
		File f = new File(Util.getWorkingDirectory(), "lastLogin.dat");
		
		if(f.exists()){
			System.out.println("LastLogin file detected. Logging in...");
			FileReader fr = null;
			BufferedReader br = null;
			try {
				fr = new FileReader(f);
				br = new BufferedReader(fr);
				
				if(!br.ready())
					throw new Exception();
				
				UUID = br.readLine();
				
				if(!br.ready())
					throw new Exception();
				
				ACCESS_TOKEN = br.readLine();
				
				if(!br.ready())
					throw new Exception();
				
				CLIENT_TOKEN = br.readLine();
				
				if(!br.ready())
					throw new Exception();
				
				OpenLauncher.username = br.readLine();
				
				if(MojangAuth.isValid(ACCESS_TOKEN)){
					System.out.println("Logged in!");
					OpenLauncher.loggedIn = true;
				}else{
					System.err.println("Could not log in!");
				}

			} catch (Exception e) {
				System.err.println("Could not load the data!");
			}finally{
				try{
					fr.close();
				}catch(Exception ex){}
				try{
					br.close();
				}catch(Exception ex){}
			}
		}else{
			System.err.println("LastLogin file didn't exist. Creating it...");
			boolean crash = false;
			try {
				f.createNewFile();
			} catch (Exception e) {crash = true;}
			if(crash){
				System.err.println("LastLogin file couldn't be created!");
			}else{
				System.out.println("LastLogin file created correctly!");
			}
		}
	}
	
	public static void save(){
		File f = new File(Util.getWorkingDirectory(), "lastLogin.dat");
		f.delete();
		PrintWriter pw = null;
		try {
			f.createNewFile();
			pw = new PrintWriter(f);
			pw.println(UUID);
			pw.println(ACCESS_TOKEN);
			pw.println(CLIENT_TOKEN);
			pw.print(OpenLauncher.username);
		} catch (IOException e) {e.printStackTrace();f.delete();}finally{
			try{
				pw.close();
			}catch(Exception ex){}
		}
	}
	
	public static void clear(){
		File f = new File(Util.getWorkingDirectory(), "lastLogin.dat");
		f.delete();
		try {
			f.createNewFile();
		}catch(Exception e){}
	}

}
