package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

import application.FileNameManager;
import application.ImageDuplicationManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Controller implements Initializable   {

	@FXML private Button changeButton;
	
//	This is only for directory chooser
	@FXML private AnchorPane anchorId; //--check have to change name
	@FXML private Text fileLocation;
	
//	dicide the name name of file
	@FXML private CheckBox dayCheckBox;
	@FXML private CheckBox nameCheckBox;
	@FXML private TextField textField;
	
//	decide how to naming
	@FXML private CheckBox numberWithBracketCheckBox;
	@FXML private CheckBox numberWithUnderBarCheckBox;
	
// this is for directory chooser of duplicated img searcher
	@FXML private AnchorPane anchorOfDuplicatedImgFinder;
	@FXML private Text fileLocation4DuplicatedImgFinder;
	@FXML private TextArea textArea4result;
	
// Manager에 넣어주는 역할
	public File targetFile;
	
// 결과 출력
	@FXML private Text alertOfNameChanger;
	@FXML private Text alertOfImageCompare;
	
//	progress bar 
	@FXML private ProgressBar progressBarOfNameChanger;
	@FXML private ProgressBar progressBarOfImageCompare;
	
	FileNameManager fileNameManager = new FileNameManager();
	ImageDuplicationManager imageDuplicationManager = new ImageDuplicationManager();
	
	
//	파일 탐색 동작
	@FXML
	public void openFileChooser4rename(ActionEvent event) {
		final DirectoryChooser dirChooser = new DirectoryChooser();
		
		Stage stage = (Stage) anchorId.getScene().getWindow();
		
		targetFile = dirChooser.showDialog(stage);
		
		if(targetFile == null) { //파일 존재 여부 확인
			alertOfNameChanger.setText("파일을 설정해주세요");
		}else {
			fileLocation.setText(targetFile.getPath());
		}
	}
	
	//check box 동작 확인
	@FXML
	public void setStringNameWithNameCheckBox(ActionEvent event) {
		
		if(nameCheckBox.isSelected()) {
			dayCheckBox.setSelected(false);
			textField.clear();
			textField.setEditable(true);
		}else {
			dayCheckBox.setSelected(true);
			textField.clear();
			textField.setText(null);
			textField.setEditable(false);
		}
	}
	
	@FXML
	public void setStringNameWithDayCheckBox(ActionEvent event) {
		
		if(dayCheckBox.isSelected()) {
			nameCheckBox.setSelected(false);
			textField.clear();
			textField.setText(null);
			textField.setEditable(false);
			
		}else {
			nameCheckBox.setSelected(true);
			textField.clear();
			textField.setEditable(true);
		}
	}
	
	@FXML
	public void how2writeWithNumberWithUnderBarCheckBox(ActionEvent event) {
		if(numberWithUnderBarCheckBox.isSelected()) {
			numberWithBracketCheckBox.setSelected(false);
		}else {
			numberWithBracketCheckBox.setSelected(true);
		}
	}
	
	@FXML
	public void how2writeWithNumberWithBracketCheckBox(ActionEvent event) {
		if(numberWithBracketCheckBox.isSelected()) {
			numberWithUnderBarCheckBox.setSelected(false);
		}else {
			numberWithUnderBarCheckBox.setSelected(true);
		}
	}

	@FXML
	public void openFileChooser4searchDuplicatedImg(ActionEvent event) {
		final DirectoryChooser dirChooser = new DirectoryChooser();
		
		Stage stage = (Stage) anchorOfDuplicatedImgFinder.getScene().getWindow();
		
		targetFile = dirChooser.showDialog(stage);
		
		if(targetFile == null) { //파일 존재 여부 확인
			alertOfImageCompare.setText("파일을 설정해주세요");
		}else {
			if(targetFile.isDirectory()) {// 디렉토리 여부 확인
				fileLocation4DuplicatedImgFinder.setText(targetFile.getPath());
				
			}else {
				alertOfImageCompare.setText("디렉토리가 아닙니다.");
			}
		}
	}
	
	//Image compare start
	@FXML
	public void compareImage(ActionEvent event) throws IOException {
		
		try {
			progressBarOfImageCompare.setProgress(0);
			System.out.println("Image Compare Start");
			progressBarOfImageCompare.setProgress(0.5);
			progressBarOfImageCompare.setProgress(0.7);
			String result = imageDuplicationManager.hashMap2String(imageDuplicationManager.compareAll(targetFile));
			progressBarOfImageCompare.setProgress(0.9);
			textArea4result.setText(result);
			progressBarOfImageCompare.setProgress(1);
		}catch(NullPointerException e){
			progressBarOfImageCompare.setProgress(0);
			String alert ="파일 위치를 설정해주세요.";
			alertOfImageCompare.setText(alert);
			fileLocation4DuplicatedImgFinder.setText("src...");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@FXML
	public void deleteDuplicatedImage(ActionEvent event) {
		String result = imageDuplicationManager.deleteDuplicatedImages(targetFile, imageDuplicationManager.banMap);
		alertOfImageCompare.setText(result);
	}

//	File name change button 동작
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		changeButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				System.out.println("File Name Changing Start");
				progressBarOfNameChanger.setProgress(0);
				
				try {
					progressBarOfNameChanger.setProgress(0.5);
					String result = fileNameManager.renameAll(targetFile, textField.getText(), numberWithBracketCheckBox.isSelected());
					progressBarOfNameChanger.setProgress(0.9);
					alertOfNameChanger.setText(result);
					progressBarOfNameChanger.setProgress(1);
					
				}catch(NullPointerException e){
					progressBarOfNameChanger.setProgress(0);
					String alert = "파일 위치를 설정해주세요";
					fileLocation.setText("src...");
					alertOfNameChanger.setText(alert);
				}
				catch (Exception e) {
					progressBarOfNameChanger.setProgress(0);
					e.printStackTrace();
				}
			}
		});
	}

}

