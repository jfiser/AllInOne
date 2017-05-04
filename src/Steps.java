
public class Steps {
	public String urlToTest; // values: a valid URL, "endTestCase", "killBrowser", "sameUrl" (don't navigate away)
	public String accessorType;
	public String accessorName;
	public Integer duration;
	public String clickType; // values: "leftClick", "rightClick"
	
	public Steps(String _urlToTest, String _accessorType, String _accessorName, Integer _duration, String _clickType){
		urlToTest = _urlToTest;
		accessorType = _accessorType;
		accessorName = _accessorName;
		duration = _duration;
		clickType = _clickType;
	}

}


