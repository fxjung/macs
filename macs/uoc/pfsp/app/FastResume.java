/** 
 * MACS - Multi-Agent Cooperative Search is a framework to develop cooperating agents using 
 * different Metaheuristics Copyright (C) 2016 Angel Alejandro Juan Perez. This file is part of MACS. 
 * 
 * MACS is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 * 
 * MACS is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with MACS. 
 * If not, see <http://www.gnu.org/licenses/>.
 */
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
