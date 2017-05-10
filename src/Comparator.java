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
	//if(!getBaseFileCreateArray("base_" + GuiPane.comboSelected + ".txt", "base")){
    // If a baseline doesn't exist for this testCase, don't try to compare
	if(Browsy.curTestCase.baseFileName.equals("") || Browsy.curTestCase.baseFileName.equals(null)){
		GuiPane.addTextToPane("\n==================================================================\n");
		GuiPane.addTextToPane("No baseline file has been set for " + GuiPane.comboSelected + "\n");
		GuiPane.addTextToPane("Click the \"Set as Baseline\" button to set the current pings as baseline"  + "\n");
		GuiPane.addTextToPane("====================================================================\n\n");
		System.out.println("in getCompareFiles2");
		return(false);
    }
	else{
		getBaseFileCreateArray(Browsy.curTestCase.baseFileName);
		
	}
    // Get the current pings
	getCurPingsCreateArray();
    return(true);
}
public static String[][] compare(){
	String baseVal = "", curVal = "", pingType = "";
	String[] baseValArr;
	ArrayList<String> baseSelectedValsArr = new ArrayList<String>();
	ArrayList<String> curSelectedValsArr = new ArrayList<String>();
	Boolean pingDiff = false;
	
	// If there's no baseline file - nothing to do here
	basePingValuesArr = new String[500][]; // 500 pings should be enough
	curPingValuesArr = new String[500][];

	if(!getCompareFiles()){
		return(null);
	}
	System.out.println("in compare2");
	GuiPane.addTextToPane("\n==================================================================\n");
	GuiPane.addTextToPane("Comparing current testCase with stored baseline for: " + GuiPane.comboSelected + "\n");
	GuiPane.addTextToPane("==================================================================\n\n");
	
	// Clear these arrays out each time
	String valsToCheck = Browsy.curTestCase.valsToCheck;
	//System.out.println("valsToCheck: " + valsToCheck);

	Boolean pingDiff_testCase = false;
	
	for(int i = 0; i < basePingValuesArr.length && basePingValuesArr[i] != null; i++){
		baseSelectedValsArr = new ArrayList<String>(); // init these arrays each time
		curSelectedValsArr = new ArrayList<String>();
		
		pingType = getPingType(basePingValuesArr[i]);
		pingDiff = false;
		
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
			
			//System.out.println("curPingValuesArr[" + i + "]: " + curPingValuesArr[i]);
			
			if(curPingValuesArr != null 
								&& i < curPingValuesArr.length 
								&& curPingValuesArr[i] != null 
								&& j < curPingValuesArr[i].length 
								&& curPingValuesArr[i][j] != null){
				curVal = curPingValuesArr[i][j];
			}
			else{
				curVal = "NONE";
			}
			
			if(!baseVal.equals(curVal)){
				GuiPane.addTextToPane("DIFF - Baseline: " + baseVal + " ; " + "Current: " + curVal + "\n");
				pingDiff = true;
				pingDiff_testCase = true;
			}
			else{
				GuiPane.addTextToPane("SAME - " + baseVal + "\n");					
			}
			// These contain values like cr=4_00_99_V1_00000,  c51=adl,0, c52=noad,0,  etc
			baseSelectedValsArr.add(baseVal);
			curSelectedValsArr.add(curVal);
			System.out.println("[" + baseVal + "]" 
							+ " : "
							+ "[" + curVal + "]" 
							+ (baseVal.equals(curVal) ? "" : ">>>>>>DIFF"));
		}
		System.out.println("AddLine>>>>>>>>>>>: " + i + "; lastOne: " + (basePingValuesArr[i+1] == null) + " ; length: " + basePingValuesArr.length);
		System.out.println("basePingValuesArr[i+1]: " + basePingValuesArr[i+1]);
		Report.addLine(Browsy.curTestCase, pingType, baseSelectedValsArr, curSelectedValsArr, pingDiff, pingDiff_testCase, (basePingValuesArr[i+1] == null));
		
	}
	return(curPingValuesArr);
}
private static String getPingType(String[] _pingValuesArr){
	for(int i = 0; i < _pingValuesArr.length; i++){
		//System.out.println("getType: " + _pingValuesArr[i]);
		
		if(_pingValuesArr[i].equals("at=start")){
			GuiPane.addTextToPane("_______________________\nComparing 'I' ping...\n_______________________\n");
			return("I");
		}
		else
		if(_pingValuesArr[i].equals("at=view")){
			GuiPane.addTextToPane("_______________________\nComparing 'V' ping...\n_______________________\n");	
			return("V");
		}
		else
		if(_pingValuesArr[i].equals("at=timer")){
			GuiPane.addTextToPane("_______________________\nComparing 'D' ping...\n_______________________\n");	
			return("D");
		}
	}
	return null;
}
private static void checkNumberOfPings(){
	
}
private static Boolean getCurPingsCreateArray(){
	//List<String> lines = new ArrayList<String>();
	List<String> lines = new ArrayList<String>();
	String tmpArr1[]; // just get the query string
	String tmpArr2[];
	String pingStr;
	Integer j = 0;
	String[][] arrayToUse;
	
	//System.out.println("handlePing>: " + urlStr);
	lines = Browsy.curTestCasePingArr;
	for (int i = 0; i < lines.size(); i++) {
		pingStr = lines.get(i);
		System.out.println("curPings ===" + lines.get(i));
		// Only look at pings with cgi-bin in the URL - but we don't want the cfg
		if(pingStr.indexOf('?') != -1 && pingStr.indexOf("cgi-bin") != -1 && pingStr.indexOf("cgi-bin/cfg") == -1){
			tmpArr1 = pingStr.split("\\?");
			tmpArr2 = tmpArr1[1].split("\\&");
			
			//System.out.println("tmpArr2: " + tmpArr2);
			//basePingValuesArr[basePingValuesArr.length] = tmpArr2;
			curPingValuesArr[j++] = tmpArr2;
		}			
	}
	return(true);
	
}
private static Boolean getBaseFileCreateArray(String _fileName) {
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
	
	try {
		lines = FileUtils.readLines(file, "UTF-8");
	} catch (IOException e) {
		e.printStackTrace();
	}
	//System.out.println("handlePing>: " + urlStr);
	for (int i = 0; i < lines.size(); i++) {
		pingStr = lines.get(i);
		//System.out.println("===" + lines.get(i));
		// Only look at pings with cgi-bin in the URL - but we don't want the cfg
		if(pingStr.indexOf('?') != -1 && pingStr.indexOf("cgi-bin") != -1 && pingStr.indexOf("cgi-bin/cfg") == -1){
			tmpArr1 = pingStr.split("\\?");
			//System.out.println("tmpArr1: " + tmpArr1[1]);
			tmpArr2 = tmpArr1[1].split("\\&");
			
			//System.out.println("tmpArr2: " + tmpArr2);
			//basePingValuesArr[basePingValuesArr.length] = tmpArr2;
			basePingValuesArr[j++] = tmpArr2;
		}			
	}
	System.out.println("getBaseFileCreateArray - return");
	return(true);
}


}
