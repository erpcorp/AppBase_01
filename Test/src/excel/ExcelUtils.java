package excel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 * This class append additional labels to the resource bundle.
 * @author shubhamkumar01
 *
 */
public class ExcelUtils {
	
	/*Common constant*/
	private static final String ENG_LANGUAGE_CODE= "en";
	private static final String EQUAL="=";
	private static final String EMPTY_STRING= "";
	private static final String FWD_SLASH="\\";
	private static final String INPUT_FILE_NAME ="input.properties";
	
	/*Input parameters required*/
	private static String INPUT_EXCEL_FILE_PATH = EMPTY_STRING ; 
	private static String INPUT_PROJECT_LOCATION = EMPTY_STRING; 
	private static String INPUT_I18_N_LOC = EMPTY_STRING; 
	private static String INPUT_HEADER = EMPTY_STRING; 
	private static int[] INPUT_COLUMN_INDEX = new int[4];
	private static String INPUT_LANGUAGE_SUFFIX = EMPTY_STRING; 
	

	/*Excel column information*/
	private enum EXCEL_COLUMN {
		A(0), B(1), C(2), D(3), E(4), F(5), G(6), H(7), I(8), J(9), K(10), L(11), M(
				12), N(13), O(14), P(15), Q(16), R(17), S(18), T(19), U(20), V(
				21), W(22), X(23), Y(24), Z(25);

		private int index;

		EXCEL_COLUMN(int index) {
			this.index = index;
		}

		public int index() {
			return index;
		}

	}

	/**
	 * The method is used to append the new entries from a given excel to the given property file.
	 * @param excelFilePath The excel file path.
	 * @throws IOException The IOexception that can be thrown by the method.
	 */
	private static void processFile(String excelFilePath) throws IOException{
	        FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
	        Workbook workbook = new XSSFWorkbook(inputStream);
	        int propertiesCount = 0;
	        for (Sheet sheet : workbook) {
	        boolean start =false;
			if (sheet.getSheetName().split("_").length > 1
					&& sheet.getSheetName().split("_")[sheet.getSheetName()
							.split("_").length - 1].equalsIgnoreCase(ENG_LANGUAGE_CODE)) {
				System.out.println("");
				System.out.println("******************"+" Processing SHEET with name  :> "+sheet.getSheetName()+"*******************");
				System.out.println("");
				File fileFromProject=ExcelUtils.findFileWithName(new File(INPUT_PROJECT_LOCATION+INPUT_I18_N_LOC),sheet.getSheetName(),null);
				if(null==fileFromProject){
					System.out.println("Warning !, Could not find the file in I18n for sheet with name :> "+sheet.getSheetName());	
					}else{
				Writer fileWritter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileFromProject,true), "UTF-8"));
				Properties properties=new Properties();
				properties.load(new FileInputStream(fileFromProject));
				fileWritter.append("\n");
				fileWritter.append("#"+INPUT_HEADER+" Onwords Entries .");
				fileWritter.append("\n");
				Iterator<Row> iterator = sheet.iterator();
				while (iterator.hasNext()) {
					Row nextRow = iterator.next();
					if (nextRow.getCell(0) != null
							&& Cell.CELL_TYPE_STRING  == nextRow.getCell(0).getCellType()
								&&  nextRow.getCell(0).getStringCellValue().equalsIgnoreCase(INPUT_HEADER) || start) {
						if (start) {
							System.out.println("Processing after HEADER , row number : "+nextRow.getRowNum()+" ,For Sheet : "+sheet.getSheetName());
							
							Cell keyCell= null;
							Cell value1Cell=null;
							Cell value2Cell=null;
							Cell value3Cell=null;
							
							if (null != (keyCell = nextRow.getCell(EXCEL_COLUMN.A.index()))) {
								keyCell.setCellType(Cell.CELL_TYPE_STRING);
							}
							if (null != (value1Cell = nextRow.getCell(INPUT_COLUMN_INDEX[1]))) {
								value1Cell.setCellType(Cell.CELL_TYPE_STRING);
							}
							if (null != (value2Cell = nextRow.getCell(INPUT_COLUMN_INDEX[2]))) {
								value2Cell.setCellType(Cell.CELL_TYPE_STRING);
							}
							if (null != (value3Cell = nextRow.getCell(INPUT_COLUMN_INDEX[3]))) {
								value3Cell.setCellType(Cell.CELL_TYPE_STRING);
							}
							
							String key = keyCell != null ? keyCell.getStringCellValue() : EMPTY_STRING;
							if(EMPTY_STRING.equals(key.trim())){
								System.out.println("Invalid row , could not find key at 'A'  column . , Line NO : "+nextRow.getRowNum()+" , For Sheet : "+sheet.getSheetName());
							}
							String value=value1Cell!=null ? value1Cell.getStringCellValue() : EMPTY_STRING;
							if(EMPTY_STRING.equals(value.trim())){
								  value=value2Cell!=null ? value2Cell.getStringCellValue() : EMPTY_STRING;
							}else if(EMPTY_STRING.equals(value.trim())){
								value=value3Cell!=null ? value3Cell.getStringCellValue() : EMPTY_STRING;
							}else if(EMPTY_STRING.equals(value.trim())){
								System.out.println("Invalid row , could not find value at "+INPUT_COLUMN_INDEX[1] +" OR , "+INPUT_COLUMN_INDEX[2] +" OR , "+INPUT_COLUMN_INDEX[3] + " column . Line NO : "+nextRow.getRowNum()+" ,For Sheet :"+sheet.getSheetName());
							}
							String newEntry = key.concat(EQUAL).concat(value);
							if(newEntry.split(EQUAL).length == 2){
								Object propValue=properties.contains(key.trim());
								if(null!=propValue) {
								  System.out.println("Duplicate Entry for key : > "+key);
								}
								System.out.println("Inserting !, new Entry : > " +newEntry +" , TO File  : > "+fileFromProject.getName());
								fileWritter.append(newEntry+"\n");
								propertiesCount++;
							}else{
								System.out.println("Warning !, no new valid Entry : > " +newEntry +" , For File  : > "+fileFromProject.getName() +" , From sheet : "+sheet.getSheetName());
							}
						} else {
							System.out.println("Header row arrived ! , For Sheet : "+sheet.getSheetName());
							start = true;
						}
					}
				}
				fileWritter.flush();
				fileWritter.close();
			}
		}else{
				System.out.println("Warning ! ,Skipping SHEET with Name : "+sheet.getSheetName());
			}
	   }       
	        workbook.close();
	        inputStream.close();
	        System.out.println("Total New Entries inserted : > "+propertiesCount);
	    }
	
	/**
	 * The method is used get the file with given tabName from excel file
	 * @param root The base location for I18 folder in file system
	 * @param tabName The tab name from the excel
	 * @param searchedFile The searched file
	 * @return The searched file
	 */
	public static File findFileWithName(File root,String tabName,File searchedFile){
		if(searchedFile !=null){
			return searchedFile;
		}
	    File[] files = root.listFiles(); 
	    String tabsString[]=tabName.split("_");
	    String searchString="";
	    if(tabsString.length == 2){
	    	searchString=tabsString[0]+INPUT_LANGUAGE_SUFFIX;
	    }else if(tabsString.length == 3){
	    	searchString=tabsString[1]+INPUT_LANGUAGE_SUFFIX;
	    }
	    for (File file : files) {
	        if (file.isFile() && file.getName().toLowerCase().contains(searchString.toLowerCase())) {
	        	searchedFile=file;
	        	break;
	        } else if (file.isDirectory()) {
	        	File search=ExcelUtils.findFileWithName(file,tabName,searchedFile);
	        	if(search!=null){
	        		searchedFile=search;
	        		break;
	        	}
	        }
	    }
	    return searchedFile;
	}

	
	public static void main(String[] args) {
		System.out.println("Starting process..");
		Long timeInMiliesOnStart=Calendar.getInstance().getTimeInMillis();
		if(args[0].equalsIgnoreCase("search_v1")) {
			ListDirectoryRecurisve.this_main(args);
		}
		else if(args[0].equalsIgnoreCase("search_v2")){
			try {
				TextFileIndexer.this_main(args);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
		if(args.length==0){throw new IllegalArgumentException("Error !, please provide config file path with name INPUT.");}
		String configFilePath=args[0];
		Properties properties=new Properties();
		try {
			InputStream inputASstream= new FileInputStream(new File(configFilePath.concat(FWD_SLASH).concat(INPUT_FILE_NAME)));
			if(inputASstream!=null){
			properties.load(inputASstream);
			INPUT_EXCEL_FILE_PATH =  properties.getProperty("INPUT_EXCEL_FILE_PATH");
			if(INPUT_EXCEL_FILE_PATH == null || EMPTY_STRING.equals(INPUT_EXCEL_FILE_PATH)){throw new IllegalArgumentException("Error !, INPUT_EXCEL_FILE_PATH can not be null or empty.");}
			else{INPUT_EXCEL_FILE_PATH.trim();}
			
			INPUT_PROJECT_LOCATION= properties.getProperty("INPUT_PROJECT_LOCATION");
			if(INPUT_PROJECT_LOCATION==null || EMPTY_STRING.equals(INPUT_PROJECT_LOCATION)){throw new IllegalArgumentException("Error !, INPUT_PROJECT_LOCATION can not be null or empty.");}
			else{INPUT_PROJECT_LOCATION.trim();}
			
			INPUT_I18_N_LOC = properties.getProperty("INPUT_I18_N_LOC");
			if(INPUT_I18_N_LOC==null || EMPTY_STRING.equals(INPUT_I18_N_LOC)){throw new IllegalArgumentException("Error !, INPUT_I18_N_LOC can not be null or empty.");}
			else{INPUT_I18_N_LOC.trim();}
			
			INPUT_HEADER = properties.getProperty("INPUT_HEADER");
			if(INPUT_HEADER ==null || EMPTY_STRING.equals(INPUT_HEADER)){throw new IllegalArgumentException("Error !, INPUT_HEADER can not be null or empty.");}
			else{INPUT_HEADER.trim();}
			
			INPUT_LANGUAGE_SUFFIX= properties.getProperty("INPUT_LANGUAGE_SUFFIX");
			if(INPUT_LANGUAGE_SUFFIX==null || EMPTY_STRING.equals(INPUT_LANGUAGE_SUFFIX)){throw new IllegalArgumentException("Error !, INPUT_LANGUAGE_SUFFIX can not be null or empty.");}
			else{INPUT_LANGUAGE_SUFFIX.trim();}
			
			String columnInfo[] = properties.getProperty("INPUT_COLUMN_INDEX").split(",");
			if(columnInfo.length < 4){throw new IllegalArgumentException("Error !, INPUT_COLUMN_INDEX length can not be less then 4 .");}
			INPUT_COLUMN_INDEX[0] = Integer.parseInt(columnInfo[0].trim());
			INPUT_COLUMN_INDEX[1] = Integer.parseInt(columnInfo[1].trim());
			INPUT_COLUMN_INDEX[2] = Integer.parseInt(columnInfo[2].trim());
			INPUT_COLUMN_INDEX[3] = Integer.parseInt(columnInfo[3].trim());
			
			System.out.println("With Input parameters : >");
			System.out.println("INPUT_EXCEL_FILE_PATH : "+INPUT_EXCEL_FILE_PATH);
			System.out.println("INPUT_PROJECT_LOCATION : "+INPUT_PROJECT_LOCATION);
			System.out.println("INPUT_I18_N_LOC : "+INPUT_I18_N_LOC);
			System.out.println("INPUT_HEADER : "+INPUT_HEADER);
			System.out.println("INPUT_LANGUAGE_SUFFIX : "+INPUT_LANGUAGE_SUFFIX);
			System.out.println("");
			ExcelUtils.processFile(INPUT_EXCEL_FILE_PATH);
			} 
		} catch (IOException e) {
			System.out.println("Error ! , Input parameters not correct .????");
			e.printStackTrace();
		}catch (Exception e) {
			System.out.println("Error ! , Unknown error accoured .????");
			e.printStackTrace();
		}
		Long timeInMiliesOnEnd=Calendar.getInstance().getTimeInMillis();
		Long timDiff=timeInMiliesOnEnd-timeInMiliesOnStart;
		System.out.println("Compleated ! , Time taken : > "+timDiff +" MS");
	}
}

}
