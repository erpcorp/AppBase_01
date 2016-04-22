package excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.channels.FileChannel;
import java.util.Scanner;



public class ListDirectoryRecurisve {
	
	
	private static String SEARCH_STRING = "";
	private static String DIRECTORY_PATH = "";

	public void listDirectory(String dirPath, int level) {
		File dir = new File(dirPath);
		File[] firstLevelFiles = dir.listFiles();
		if (firstLevelFiles != null && firstLevelFiles.length > 0) {
			for (File aFile : firstLevelFiles) {
				for (int i = 0; i < level; i++) {
					System.out.print("\t");
				}
				if (aFile.isDirectory()) {
					System.out.println("[" + aFile.getName() + "]");
					listDirectory(aFile.getAbsolutePath(), level + 1);
				} else {
					printFile(aFile);
				}
			}
		}
	}
	
	private void search(File file, String serchStr) {
		int count = 0, tot = 0;
		String result = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			FileChannel fc = fis.getChannel();
			Scanner scan = new Scanner(fc);
			while (scan.hasNext()) {
				scan.next();
				result = scan.findWithinHorizon(serchStr, 0);
				if (result != null) {
					tot++;
				}
				count++;
			}
			scan.close();
			fc.close();
			fis.close();

			System.out.println("Results found: " + tot + " in " + count
					+ " words "+" in File : "+file.getAbsolutePath());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	
	private void printFile(File file){
		try {
		    Scanner scanner = new Scanner(file);
		    int lineNum = 0;
		    while (scanner.hasNextLine()) {
		        String line = scanner.nextLine();
		        lineNum++;
		        if(line.contains(SEARCH_STRING)) { 
		            System.out.println("i found it on line " +lineNum + " At file path : "+file.getAbsolutePath());
		            break;
		        }
		    }
		} catch(FileNotFoundException e) { 
		}
	}
	
	public static void this_main(String[] args) {
		DIRECTORY_PATH=args[1];
		SEARCH_STRING =args[2];
		ListDirectoryRecurisve test = new ListDirectoryRecurisve();
		String dirToList =DIRECTORY_PATH;
		test.listDirectory(dirToList, 0);
	}

}
