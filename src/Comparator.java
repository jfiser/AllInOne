import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Comparator {
	
//static List<String> basePingValuesArr[] = new ArrayList<String>();
static String[][] basePingValuesArr;
static String[][] curPingValuesArr;

public Comparator() {
	
}
private static Boolean getCompareFiles(){
    // If a baseline doesn't exist for this testCase, don't try to compare
	if(!getPingFileCreateArray("base_" + GuiPane.comboSelected + ".txt", "base")){
		GuiPane.addTextToPane("\n==================================================================\n");
		GuiPane.addTextToPane("No baseline file has been set for " + GuiPane.comboSelected + "\n");
		GuiPane.addTextToPane("Click the \"Set as Baseline\" button to set the current pings as baseline"  + "\n");
		GuiPane.addTextToPane("====================================================================\n\n");
		System.out.println("in getCompareFiles2");
		return(false);
    }
    // Get the current file (the one I just ran) for comparison
    getPingFileCreateArray("currentRunPings.txt", "cur");
    return(true);
}
public static String[][] compare(){
	String baseVal = "", curVal = "";
	String[] baseValArr;
	// If there's no baseline file - nothing to do here
	basePingValuesArr = new String[50][];
	curPingValuesArr = new String[50][];

	if(!getCompareFiles()){
		return(null);
	}
	System.out.println("in compare2");
	GuiPane.addTextToPane("\n==================================================================\n");
	GuiPane.addTextToPane("Comparing current testCase with stored baseline for: " + GuiPane.comboSelected + "\n");
	GuiPane.addTextToPane("==================================================================\n\n");
	
	// Clear these arrays out each time
	String valsToCheck = Browsy.curTestCase.valsToCheck;
	System.out.println("valsToCheck: " + valsToCheck);

	
	for(int i = 0; i < basePingValuesArr.length && basePingValuesArr[i] != null; i++){
		getPingType(basePingValuesArr[i]);
		for(int j = 0; j < basePingValuesArr[i].length; j++){
			if(basePingValuesArr[i][j] != null){
				baseVal = basePingValuesArr[i][j];
				baseValArr = baseVal.split("=");
				if(baseValArr.length < 2){
					continue;
				}
				// Check to see if this key is defined in the TestCase as one I need to check 
				if(valsToCheck.indexOf(baseValArr[0]) == -1){
					continue;
				}
			}
			else{
				baseVal = "NONE";
			}
			
			if(curPingValuesArr != null && curPingValuesArr[i] != null && curPingValuesArr[i][j] != null){
				curVal = curPingValuesArr[i][j];
			}
			else{
				curVal = "NONE";
			}
			
			if(!baseVal.equals(curVal)){
				GuiPane.addTextToPane("DIFF - Baseline: " + baseVal + " ; " + "Current: " + curVal + "\n");
			}
			else{
				GuiPane.addTextToPane("SAME - " + baseVal + "\n");					
			}
			System.out.println("[" + baseVal + "]" 
							+ " : "
							+ "[" + curVal + "]" 
							+ (baseVal.equals(curVal) ? "" : ">>>>>>DIFF"));
		}
	}
	return(curPingValuesArr);
}
private static void getPingType(String[] _pingValuesArr){
	for(int i = 0; i < _pingValuesArr.length; i++){
		System.out.println("getType: " + _pingValuesArr[i]);
		
		if(_pingValuesArr[i].equals("at=start")){
			GuiPane.addTextToPane("_______________________\nComparing 'I' ping...\n_______________________\n");	
			break;
		}
		else
		if(_pingValuesArr[i].equals("at=view")){
			GuiPane.addTextToPane("_______________________\nComparing 'V' ping...\n_______________________\n");	
			break;
		}
		else
		if(_pingValuesArr[i].equals("at=timer")){
			GuiPane.addTextToPane("_______________________\nComparing 'D' ping...\n_______________________\n");	
			break;
		}
	}
}
private static void checkNumberOfPings(){
	
}
public static Boolean getPingFileCreateArray(String _fileName, String _baseOrCur) {
	File file = new File(_fileName);
	if(!file.exists()) { 
	    return(false);
	}
	//List<String> lines = new ArrayList<String>();
	List<String> lines = new ArrayList<String>();
	String tmpArr1[]; // just get the query string
	String tmpArr2[];
	String pingStr;
	Integer j = 0;
	String[][] arrayToUse;
	
	if(_baseOrCur.equals("base")){
		arrayToUse = basePingValuesArr;
	}
	else{ // "cur"
		arrayToUse = curPingValuesArr;
	}

	try {
		lines = FileUtils.readLines(file, "UTF-8");
	} catch (IOException e) {
		e.printStackTrace();
	}
	//System.out.println("handlePing>: " + urlStr);
	for (int i = 0; i < lines.size(); i++) {
		pingStr = lines.get(i);
		System.out.println("===" + lines.get(i));
		// Only look at pings with cgi-bin in the URL - but we don't want the cfg
		if(pingStr.indexOf('?') != -1 && pingStr.indexOf("cgi-bin") != -1 && pingStr.indexOf("cgi-bin/cfg") == -1){
			tmpArr1 = pingStr.split("\\?");
			System.out.println("tmpArr1: " + tmpArr1[1]);
			tmpArr2 = tmpArr1[1].split("\\&");
			
			System.out.println("tmpArr2: " + tmpArr2);
			//basePingValuesArr[basePingValuesArr.length] = tmpArr2;
			arrayToUse[j++] = tmpArr2;
		}			
	}
	System.out.println("getPingFileCreateArray - return");
	return(true);
}


}
