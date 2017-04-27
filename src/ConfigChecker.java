import java.util.ArrayList;

public class ConfigChecker {
	
static ArrayList<ValuePair> urlParams = new ArrayList<ValuePair>();

public static String getValFromUrl(String _urlStr, String _nameToGet){	
	String tmpArr[] = _urlStr.split("&");
	String valPair[]; 
	Integer i;
	//System.out.println("handlePing: " + _urlStr);
	// store the ping values pairs 
	urlParams.clear();
	//System.out.println("_urlStr: " + _urlStr);

	for(i = 0; i < tmpArr.length; i++){
		valPair = tmpArr[i].split("=");
		//System.out.println(valPair[0] + ": " + valPair[1]);
		if(valPair[0].equals(_nameToGet)){
			return(valPair[1]);
		}
	}
	return(null);
}




}
