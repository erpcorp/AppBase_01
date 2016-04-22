package excel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReplaceString {

	private static enum Lufthansa {
		LH_U("LH"), LH_L("lh"), LUFTHANSA_U("LUFTHANSA"), LUFTHANSA_L(
				"Lufthansa"), BOOKAGROUP("Book a Group"), BOOKAGROUP_L(
				"book a group"), BOOKAGROUP_HYP("Book-a-Group"),LGT("LGT"),BookaGroup("BookaGroup"),Bookagroup("Bookagroup");
		private String value;

		private Lufthansa(String value) {
			this.value = value;
		}

		public String getLHValue() {
			return value;
		}
	}

	private enum Austrian {
		OS_U("OS"), OS_L("os"), AUSTRIAN_U("AUSTRIAN"), AUSTRIAN_L("Austrian"), MY_GROUP_U(
				"MY GROUP"), MY_GROUP_L("myGroup") , AUS("Austrian myGroup");
		private String value;

		private Austrian(String value) {
			this.value = value;
		}

		public String getOSValue() {
			return value;
		}

	}

	private static final String FOLDER_PATH = "E:\\Projects\\LGT_SVN_NEW_HP\\lgt\\lgt-web\\src\\main\\resources\\i18n\\OS";

	public static void main(String arr[]) throws IOException {
		ReplaceString thisObject = new ReplaceString();
		String thisLine = null;
		BufferedReader bufferedReader=null;
		BufferedWriter bufferedWriter=null;
		List<String> linesInFile=null;
		List<File> propertyFiles = new ArrayList<>();
		thisObject.listFile(FOLDER_PATH, propertyFiles);
		for (File file : propertyFiles) {
			System.out.println("Processing File :> " + file.getName());
			System.out
					.println("============================================================================");
			      bufferedReader = new BufferedReader(new FileReader(
					file));
			      linesInFile=new ArrayList<String>();
			while ((thisLine = bufferedReader.readLine()) != null) {
				System.out.println("Processing Line :> " + thisLine);
				thisLine=thisObject.replaceString(thisLine);
				linesInFile.add(thisLine);
				System.out.println("New Line :> " + thisLine);
			}
			bufferedReader.close();
			bufferedWriter = new BufferedWriter(new FileWriter(file));
			for (String newLine : linesInFile) {
				bufferedWriter.write(newLine);
				bufferedWriter.newLine();
			}
			bufferedWriter.flush();
			bufferedWriter.close();
			System.out
					.println("============================================================================");
		}
	}

	private String replaceString(String line) {
		String[] oldLines=line.split("=");
		if(oldLines.length<2){
			return line;
		}
		String newLine= oldLines[0].concat("=");
	 
		
		if (oldLines[1].contains(Lufthansa.LH_U.getLHValue())) {
			oldLines[1]=oldLines[1].replace(Lufthansa.LH_U.getLHValue(),
					Austrian.OS_U.getOSValue());
		}

		if (oldLines[1].contains(Lufthansa.LH_L.getLHValue())) {
			oldLines[1]=oldLines[1].replace(Lufthansa.LH_L.getLHValue(),
					Austrian.OS_L.getOSValue());
		}

		if (oldLines[1].contains(Lufthansa.LUFTHANSA_U.getLHValue())) {
			oldLines[1]=oldLines[1].replace(Lufthansa.LUFTHANSA_U.getLHValue(),
					Austrian.AUSTRIAN_U.getOSValue());
		}

		if (oldLines[1].contains(Lufthansa.LUFTHANSA_L.getLHValue())) {
			oldLines[1]=oldLines[1].replace(Lufthansa.LUFTHANSA_L.getLHValue(),
					Austrian.AUSTRIAN_L.getOSValue());
		}
		if (oldLines[1].contains(Lufthansa.BOOKAGROUP.getLHValue())) {
			oldLines[1]=oldLines[1].replace(Lufthansa.BOOKAGROUP.getLHValue(),
					Austrian.MY_GROUP_L.getOSValue());
		}
		if (oldLines[1].contains(Lufthansa.BOOKAGROUP_L.getLHValue())) {
			oldLines[1]=oldLines[1].replace(Lufthansa.BOOKAGROUP_L.getLHValue(),
					Austrian.MY_GROUP_L.getOSValue());
		}

		if (oldLines[1].contains(Lufthansa.BOOKAGROUP_HYP.getLHValue())) {
			oldLines[1]=oldLines[1].replace(Lufthansa.BOOKAGROUP_HYP.getLHValue(),
					Austrian.MY_GROUP_L.getOSValue());
		}
		if (oldLines[1].contains(Lufthansa.LGT.getLHValue())) {
			oldLines[1]=oldLines[1].replace(Lufthansa.LGT.getLHValue(),
					Austrian.AUS.getOSValue());
		}
		if (oldLines[1].contains(Lufthansa.BookaGroup.getLHValue())) {
			oldLines[1]=oldLines[1].replace(Lufthansa.BookaGroup.getLHValue(),
					Austrian.MY_GROUP_L.getOSValue());
		}
		if (oldLines[1].contains(Lufthansa.Bookagroup.getLHValue())) {
			oldLines[1]=oldLines[1].replace(Lufthansa.Bookagroup.getLHValue(),
					Austrian.MY_GROUP_L.getOSValue());
		}
		newLine=newLine.concat(oldLines[1]);
		return newLine;

	}

	private void listFile(String directoryName, List<File> files) {
		File directory = new File(directoryName);
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				files.add(file);
			} else if (file.isDirectory()) {
				listFile(file.getAbsolutePath(), files);
			}
		}
	}

}
