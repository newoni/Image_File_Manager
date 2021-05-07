package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class FileNameManager extends Manager{
	
	String extension="";
	
	public String getExtension(String name) {
		
		extension = name.substring(name.length()-3, name.length());
		
		return extension;
	}
	
	public boolean checkImg(String name) {
		boolean result=false;
		
		String buffExtension = getExtension(name);
		
		if(buffExtension.equals("jpg")|| buffExtension.equals("png")){
			return true;
		}else {
			return false;
		}
	}
	
	//��� ���Ͽ� �ش� �̸��� ������ �����ϸ� �ȵ�.
	//move�� ���� ���� �ս� ��� ���
	public boolean checkBeforeChangeName(String[] fileList, String name) {
		for(String fileName : fileList) {
			if(fileName.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	
	public String renameAll(File file, String text, Boolean withBracket) {
		if(text==null&&withBracket) {
			return renameAllWithLastModifiedDayAndBracket(file);
		}else if(text==null && !(withBracket)) {
			return renameAllWithLastModifiedDayAndUnderBar(file);
		}else if(text!=null && withBracket) {
			return renameAllWithTextAndBracket(file, text);
		}else if(text!=null && !(withBracket)) {
			return renameAllWithTextAndUnderBar(file, text);
		}else {
			return "������ �� �ƽ��ϴ�";
		}
		
	}
	
	
	public String renameAllWithLastModifiedDayAndBracket(File file) {
		System.out.println("Execute rename All With Last Modified Day And Bracket");

		boolean stop = false;
		int cnt = 1;
		String[] fileList = file.list();
		for(String fileName: fileList) {
			//���� Ȯ���� üũ
			if(!(checkImg(fileName))) {
				continue;
			}
			
			
			String path = file.getPath() + "\\"+fileName;
			File targetFile = new File(path);
			
			LocalDate fileDate = Instant.ofEpochMilli(targetFile.lastModified())
		            .atZone(ZoneId.systemDefault())
		            .toLocalDate();
		    
			getExtension(fileName);
			stop = checkBeforeChangeName(fileList, fileDate.toString()+ "(" + cnt + ")." + extension);
			
			if(stop) {
				System.out.println("�ٲ� ����� ��ġ�� ���� �̸��� �̹� �����մϴ�.");
				return "�ٲ� ����� ��ġ�� ���� �̸��� �̹� �����մϴ�.";
			}
			
			String command = useCmd("move \"" + file.getPath() + "\\" +  fileName + "\" \"" + file.getPath() + "\\" +  fileDate.toString() + "(" + cnt + ")." + extension + "\"");
			String result = execCommand(command);
			
			cnt++;
		}
		
		return "���������� �̸��� �ٲ���ϴ�.";
	}
	
	public String renameAllWithLastModifiedDayAndUnderBar(File file){
		System.out.println("Execute rename All With Last Modified Day And Underbar");
		boolean stop = false;
		
		int cnt = 1;
		String[] fileList = file.list();
		for(String fileName: fileList) {
			
			//���� Ȯ���� üũ
			if(!(checkImg(fileName))) {
				continue;
			}
			
			String path = file.getPath() + "\\"+fileName;
			File targetFile = new File(path);
			
			LocalDate fileDate = Instant.ofEpochMilli(targetFile.lastModified())
		            .atZone(ZoneId.systemDefault())
		            .toLocalDate();
			
			getExtension(fileName);
			stop = checkBeforeChangeName(fileList, fileDate.toString()+ "_" + cnt + "." + extension);
			
			if(stop) {
				System.out.println("�ٲ� ����� ��ġ�� ���� �̸��� �̹� �����մϴ�.");
				return "�ٲ� ����� ��ġ�� ���� �̸��� �̹� �����մϴ�.";
			}
			
			String command = useCmd("move \"" + file.getPath() + "\\" +  fileName + "\" \"" + file.getPath() + "\\" +  fileDate.toString() + "_" + cnt + "." + extension + "\"");
			String result = execCommand(command);
			
			cnt++;
		}
		return "���������� �̸��� �ٲ���ϴ�.";
	}
	
	public String renameAllWithTextAndBracket(File file, String text) {
		System.out.println("Execute rename All With Text And Bracket");
		
		if(text.contains("/") || text.contains("\\") || text.contains("*") || text.contains(":") || text.contains("*")|| text.contains("?") || text.contains("\"")|| text.contains("<") || text.contains(">")|| text.contains("|") ) {
			return "�ùٸ� ���� ������ �ƴմϴ�.";
		}
		
		boolean stop = false;
		int cnt= 1;
		String[] fileList = file.list();
		for(String fileName: fileList){
			
			//���� Ȯ���� üũ
			if(!(checkImg(fileName))) {
				continue;
			}
			
			getExtension(fileName);
			stop = checkBeforeChangeName(fileList, text + "(" + cnt + ")." + extension);
			
			if(stop) {
				System.out.println("�ٲ� ����� ��ġ�� ���� �̸��� �̹� �����մϴ�.");
				return "�ٲ� ����� ��ġ�� ���� �̸��� �̹� �����մϴ�.";
			}
			
			String command = useCmd("move \"" + file.getPath() + "\\" +  fileName + "\" \"" + file.getPath() + "\\" + text + "(" + cnt + ")." + extension +"\"");
			
			String result = execCommand(command);
			
			cnt++;
		}
		return "���������� �̸��� �ٲ���ϴ�.";
	}
	
	public String renameAllWithTextAndUnderBar(File file, String text) {
		System.out.println("Execute rename All With Text And Underbar");
		
		boolean stop = false;
		int cnt= 1;
		String[] fileList = file.list();
		for(String fileName: fileList){
			
			//���� Ȯ���� üũ
			if(!(checkImg(fileName))) {
				continue;
			}
			
			getExtension(fileName);
			getExtension(fileName);
			stop = checkBeforeChangeName(fileList, text + "_" + cnt +"." + extension);
			
			if(stop) {
				System.out.println("�ٲ� ����� ��ġ�� ���� �̸��� �̹� �����մϴ�.");
				return "�ٲ� ����� ��ġ�� ���� �̸��� �̹� �����մϴ�.";
			}
			
			String command = useCmd("move \"" + file.getPath() + "\\" +  fileName + "\" \"" + file.getPath() + "\\" + text + "_" + cnt +"." + extension +"\"");
			
			String result = execCommand(command);
			
			cnt++;
		}
		return "���������� �̸��� �ٲ���ϴ�.";
	}
	
}

