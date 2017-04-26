import java.util.ArrayList;

public class BasicTests extends Thread{
	
static ArrayList<ValuePair> urlParams = new ArrayList<ValuePair>();
static long startTime = 0;
static Boolean gotDcrVideoView = false;
static Boolean gotDcrVideoImpression = false;
static Integer curPingNum = 1;


public BasicTests() {
	// TODO Auto-generated constructor stub
}
public static void handlePing(String _urlStr){
	String tmpArr[] = _urlStr.split("&");
	String valPair[], _val; 
	Integer i;
	//System.out.println("handlePing: " + _urlStr);
	//Browsy.myGui.addTextToPane(curPingNum++ + ". " + _urlStr + "\n\n");
	// store the ping values pairs 
	urlParams.clear();
	for(i = 0; i < tmpArr.length; i++){
		valPair = tmpArr[i].split("=");
		urlParams.add(new ValuePair(valPair[0], valPair[1]));
		//System.out.println(valPair[0] + ": " + valPair[1]);
		//Browsy.myGui.addTextToPane(valPair[0] + ": " + valPair[1] + '\n');
	}
	
	_val = getValueFromName(urlParams, "rt");
	System.out.println("handlePing - _val: " + _val);
	if(_val.equals("text")){
		handleStaticPing(urlParams);
	}
	else
	if(_val.equals("video")){
		handleVideoPing(urlParams);
	}
}
private static void handleStaticPing(ArrayList<ValuePair> _urlParams){
	 System.out.println("handleStaticPing: " + _urlParams);
	 //Browsy.myGui.addTextToPane(">>>>>>>>>> handleStaticPing\n");
}
private static void handleVideoPing(ArrayList<ValuePair> _urlParams){
	Integer i;
	String _name, _val;
	System.out.println("handleVideoPing: " + _urlParams);
	
	_val = getValueFromName(_urlParams, "at");
	System.out.println("handleVideoPing - at: " + _val);
	
	if(_val.equals("start")){ // Impression ping
		gotDcrVideoImpression = true;
		setStartTime();
		System.out.println(">>>>>>>>>> gotDcrVideoImpression (I ping): " + getValueFromName(_urlParams, "cr") + '\n');
		//Browsy.myGui.addTextToPane(">>>>>>>>>> gotDcrVideoImpression (I ping): " + getValueFromName(_urlParams, "cr") + '\n');
		
	}
	else
	if(_val.equals("view")){ //got video view ping
		gotDcrVideoView = true;
		//System.out.println("gotDcrVideoView = true");
		System.out.println(">>>>>>>>>> gotDcrVideoView (V ping): " + getValueFromName(_urlParams, "cr") + '\n');
		//Browsy.myGui.addTextToPane(">>>>>>>>>> gotDcrVideoView (V ping): " + getValueFromName(_urlParams, "cr") + '\n');
	}
	else
	if(_val.equals("timer")){  // D ping
		System.out.println(">>>>>>>>>> D Ping: " + getValueFromName(_urlParams, "cr") + '\n');
		//Browsy.myGui.addTextToPane(">>>>>>>>>> D Ping: " + getValueFromName(_urlParams, "cr") + '\n');
	}
	
	_val = getValueFromName(_urlParams, "c13");
	System.out.println("appId: " + _val);

	System.out.println("========================================\n");
}
private static String getValueFromName(ArrayList<ValuePair> _urlParams, String _nameToGet){
	Integer i;
	
	for (i = 0; i < urlParams.size(); i++) {
		 if(urlParams.get(i).name.toLowerCase().equals(_nameToGet)){
			 return(urlParams.get(i).value.toLowerCase());
		 }
	}
	return(null);
}
private static void setStartTime(){
	startTime = System.nanoTime();    
}
private long getElapsedTime(){
	return(System.nanoTime() - startTime);
}

}
