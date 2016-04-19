package macs.uoc.pfsp.app;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class FastResume {

	private final String resumePath;
	private final BufferedWriter bw;
	
	private static FastResume instance = null;
	private FastResume(String fileName) {
		resumePath = "outputs"+File.separator+"simple_resume_"+fileName+".txt"; 
		
		BufferedWriter tmp = null;
		try {
			tmp = new BufferedWriter(new FileWriter(resumePath));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		bw = tmp;
	}
	
	public static void append(String line) { 
		try {
			instance.bw.append(line+"\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void flush() { 
		try {
			instance.bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void finish() { 
		try {
			instance.bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void genResumer(String name) { 
		instance = new FastResume(name); 
	}

}
