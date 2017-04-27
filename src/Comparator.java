import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Comparator {
	
//static List<String> prevPingValuesArr[] = new ArrayList<String>();
static String[][] prevPingValuesArr = new String[50][];
static String[][] curPingValuesArr = new String[50][];

public Comparator() {
	
}
public static String[][] compare(){
	String prevVal = "", curVal = "";
	for(int i = 0; i < prevPingValuesArr.length; i++){
		if(prevPingValuesArr[i] != null){
			for(int j = 0; j < prevPingValuesArr[i].length; j++){
				if(prevPingValuesArr[i][j] != null){
					prevVal = prevPingValuesArr[i][j];
				}
				else{
					prevVal = "NONE";
				}
				
				if(curPingValuesArr[i][j] != null){
					curVal = curPingValuesArr[i][j];
				}
				else{
					curVal = "NONE";
				}
	
				System.out.println("[" + prevVal + "]" 
								+ " : "
								+ "[" + curVal + "]" 
								+ (prevVal.equals(curVal) ? "" : ">>>>>>DIFF"));
			}
		}
	}
	return(curPingValuesArr);
}
public static void getPingFileCreateArray(String _fileName, String _prevOrCur) {
	File file = new File(_fileName);
	//List<String> lines = new ArrayList<String>();
	List<String> lines = new ArrayList<String>();
	String tmpArr1[]; // just get the query string
	String tmpArr2[];
	String pingStr;
	Integer j = 0;
	String[][] arrayToUse;
	
	if(_prevOrCur.equals("prev")){
		arrayToUse = prevPingValuesArr;
	}
	else{ // "cur"
		arrayToUse = curPingValuesArr;
	}

	try {
		lines = FileUtils.readLines(file, "UTF-8");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	//System.out.println("handlePing>: " + urlStr);
	for (int i = 0; i < lines.size(); i++) {
		pingStr = lines.get(i);
		System.out.println("===" + lines.get(i));
		if(pingStr.indexOf('?') != -1 && pingStr.indexOf("cgi-bin") != -1){
			tmpArr1 = pingStr.split("\\?");
			System.out.println("tmpArr1: " + tmpArr1[1]);
			tmpArr2 = tmpArr1[1].split("\\&");
			
			System.out.println("tmpArr2: " + tmpArr2);
			//prevPingValuesArr[prevPingValuesArr.length] = tmpArr2;
			arrayToUse[j++] = tmpArr2;
		}			
	}
}


}
