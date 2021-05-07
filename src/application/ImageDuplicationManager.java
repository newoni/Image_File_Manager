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

	// Compare 결과 출력을 위해 String으로 변환하기 전 문자열 전처리	
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
	
	// Compare 기능 구현
	public HashMap<String, HashSet<String>> compareAll(File file) throws IOException {
		//전체 파일 리스트 불러오기
		for(File f : file.listFiles()){
			Boolean innerStop = false;
			// 파일 확장자 명을 번저 확인하고 이미지 파일이 아닌 경우 제외
			innerStop = checkExtensionBeforeCompare(f.getName());
			
			//기준 파일 f를 가지고 검사할  건지 여부 검사.
			Set<String> banMapKeys = banMap.keySet();
			Iterator<String> banMapKeyItr = banMapKeys.iterator();
			
			while(banMapKeyItr.hasNext()){
				//value로 가지고 있는 경우 더이상 검사할 필요가 없음.
				if(banMap.get(banMapKeyItr.next()).contains(f.getName())) {
					innerStop = true;
					System.out.println("이미 다른 파일과 중복되는것으로 확인되었습니다." + f.getName() + " 파일은 중복검사를 skip합니다.");
					break;
				}
			}
			if(innerStop) {
				innerStop = false;
				continue;
			}
			
			//f 를 기준으로 target file 불러오기
			 for(File targetFile: file.listFiles()) {
				 
				//동일 파일 계산 skip
				if(f.getName().equals(targetFile.getName()) ) {
					//value로 가지고 있는 경우 더이상 검사할 필요가 없음.
					System.out.println("이미 다른 파일과 중복되는것으로 확인되었습니다." + targetFile.getName() + " 파일은 중복검사를 skip합니다.");
					System.out.println(f.getName() + "과" + targetFile.getName() + "같은 파일은 스킵합니다.");
					continue;
				}
				
				//확장자가 이미지파일이 아닌 경우 계산 skip 
				innerStop = checkExtensionBeforeCompare(targetFile.getName());
				
				
				//동일 계산 반복을 지양하기위한 코드
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
				
				//2개의 파일 byte 단위 비교
				System.out.println(f.getName() + "과 " + targetFile.getName() + " 파일간 바이트 단위 비교 시작");
				
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
				
				// 같을 경우 hashSet에 담고 넣어두기
				if(isEqual) {
					//더 이상 반복하지 않는 리스트 추가.
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
		return "성공적으로 중복파일을 지웠습니다.";
	}
}


