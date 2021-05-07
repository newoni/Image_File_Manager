package application;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public abstract class Manager {

	public StringBuffer buffer;
	public Process process;
	private BufferedReader bufferedReader;
	private StringBuffer readBuffer;
	
	private String path;
	
	
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path =path;
	}

	public String useCmd(String cmd) {
		buffer = new StringBuffer();

		buffer.append("cmd.exe ");
		buffer.append("/c ");
		buffer.append(cmd);
		
		return buffer.toString();
	}
	
	public String execCommand(String cmd) {
		try {
			
			process = Runtime.getRuntime().exec(cmd);
			
			bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			String line = null;
			readBuffer = new StringBuffer();
			
			while((line = bufferedReader.readLine()) !=null) {
				readBuffer.append(line);
				readBuffer.append("\n");
			}
			
			return readBuffer.toString();
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}
	
}
