package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ImageDuplicationManager extends Manager{
	boolean isEqual = false;
	public HashMap<String, HashSet<String>> banMap = new HashMap<String, HashSet<String>>();

	// Compare ��� ����� ���� String���� ��ȯ�ϱ� �� ���ڿ� ��ó��	
	public String hashMap2String(HashMap<String, HashSet<String>> hashMap) {
		String strFromhashMap = hashMap.toString();
		
		String[] resultStringArr = strFromhashMap.split("], ");
		
		int cnt=0;
		String result="";
		
		for(String str : resultStringArr) {
			resultStringArr[cnt] = str + "]\n";
			result += resultStringArr[cnt];
			cnt++;
		}
		
		return result.substring(1,result.length()-3);
	}
	
	
	public boolean checkExtensionBeforeCompare(String name) {
		String extensionOfName = name.substring(name.length()-3, name.length());
		if((extensionOfName.equals("jpg")) || (extensionOfName.equals("png"))) {
			return false;
		}else {
			return true;
		}
	}
	
	// Compare ��� ����
	public HashMap<String, HashSet<String>> compareAll(File file) throws IOException {
		//��ü ���� ����Ʈ �ҷ�����
		for(File f : file.listFiles()){
			Boolean innerStop = false;
			// ���� Ȯ���� ���� ���� Ȯ���ϰ� �̹��� ������ �ƴ� ��� ����
			innerStop = checkExtensionBeforeCompare(f.getName());
			
			//���� ���� f�� ������ �˻���  ���� ���� �˻�.
			Set<String> banMapKeys = banMap.keySet();
			Iterator<String> banMapKeyItr = banMapKeys.iterator();
			
			while(banMapKeyItr.hasNext()){
				//value�� ������ �ִ� ��� ���̻� �˻��� �ʿ䰡 ����.
				if(banMap.get(banMapKeyItr.next()).contains(f.getName())) {
					innerStop = true;
					System.out.println("�̹� �ٸ� ���ϰ� �ߺ��Ǵ°����� Ȯ�εǾ����ϴ�." + f.getName() + " ������ �ߺ��˻縦 skip�մϴ�.");
					break;
				}
			}
			if(innerStop) {
				innerStop = false;
				continue;
			}
			
			//f �� �������� target file �ҷ�����
			 for(File targetFile: file.listFiles()) {
				 
				//���� ���� ��� skip
				if(f.getName().equals(targetFile.getName()) ) {
					//value�� ������ �ִ� ��� ���̻� �˻��� �ʿ䰡 ����.
					System.out.println("�̹� �ٸ� ���ϰ� �ߺ��Ǵ°����� Ȯ�εǾ����ϴ�." + targetFile.getName() + " ������ �ߺ��˻縦 skip�մϴ�.");
					System.out.println(f.getName() + "��" + targetFile.getName() + "���� ������ ��ŵ�մϴ�.");
					continue;
				}
				
				//Ȯ���ڰ� �̹��������� �ƴ� ��� ��� skip 
				innerStop = checkExtensionBeforeCompare(targetFile.getName());
				
				
				//���� ��� �ݺ��� �����ϱ����� �ڵ�
				banMapKeys = banMap.keySet(); 
				banMapKeyItr = banMapKeys.iterator();
				while( banMapKeyItr.hasNext() ){
					if(banMap.get(banMapKeyItr.next()).contains(targetFile.getName())){
						innerStop = true;
						continue;
					}
				}
				if(innerStop) {
					innerStop=false;
					continue;
				}
				
				FileInputStream fis1 = new FileInputStream(f.getPath());
				FileInputStream fis2 = new FileInputStream(targetFile.getPath());
				
				int readByteNo1;
				int readByteNo2;
	
				byte[] readBytes1 = new byte[100];
				byte[] readBytes2 = new byte[100];
				
				int cnt = 0;
				
				//2���� ���� byte ���� ��
				System.out.println(f.getName() + "�� " + targetFile.getName() + " ���ϰ� ����Ʈ ���� �� ����");
				
				while((readByteNo1 = fis1.read(readBytes1)) != -1) {
					readByteNo2 = fis2.read(readBytes2);
					for(int i=0 ; i<readByteNo1; i++) {
						if(readBytes1[i] == readBytes2[i]) {
							cnt +=1;
						}else {
							continue;
						}
					}
				}
				
				if(cnt >=f.length()*0.8) {
					isEqual = true;
				}else {
					isEqual=false;
				}
				
				// ���� ��� hashSet�� ��� �־�α�
				if(isEqual) {
					//�� �̻� �ݺ����� �ʴ� ����Ʈ �߰�.
					try {
						HashSet<String> bufferSet = banMap.get(f.getName());
						bufferSet.add(targetFile.getName());
						banMap.put(f.getName(), bufferSet);
					}catch(Exception e){
						HashSet<String> bufferSet = new HashSet<String>(); 
						bufferSet.add(targetFile.getName());
						banMap.put(f.getName(), bufferSet);
					}
				}
				
				fis1.close();
				fis2.close();
				
				
			}
		}
		return banMap;
	}
	
	public String deleteDuplicatedImages(File file, HashMap<String, HashSet<String>> hashMap) {
		
		Set keySet = new HashSet<String>();
		
		keySet = hashMap.keySet();
		
		Iterator itr = keySet.iterator();
		
		while(itr.hasNext()){
			HashSet<String> duplicatedImgs = hashMap.get(itr.next());
			
			Iterator targetFileNameItr = duplicatedImgs.iterator();
			
			while(targetFileNameItr.hasNext()) {
				String command = useCmd("del \"" + file.getPath() + "\\" +  targetFileNameItr.next()+ "\"");
				String result = execCommand(command);
				System.out.println(result);
			}
			
		}
		return "���������� �ߺ������� �������ϴ�.";
	}
}


