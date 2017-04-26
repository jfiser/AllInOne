
public class ThirdPartyPlayer {

public ThirdPartyPlayer() {
	
}
public String getPlayerVersion(String _urlStr){
	String urlParams[] = _urlStr.split("&");
	String valPair[]; 
	Integer i;
	
	for(i = 0; i < urlParams.length; i++){
		valPair = urlParams[i].split("=");
		//System.out.println("valPair: " + valPair[0] + ":" + valPair[1]);
		if(valPair[0].equals("platform_version")){
			return(valPair[1]);
		}
	}
	return(null);

	//urlParams = _urlStr.split('&')
}

}
