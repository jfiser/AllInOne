
import java.util.ArrayList;

public class TestCaseSetup {



public static ArrayList<TestCase> createTestCaseArr() {
	ArrayList<TestCase> testCaseArr = new ArrayList<TestCase>();
	
	// TestCase 1
	ArrayList<Steps> stepsList1 = new ArrayList<Steps>();
	stepsList1.add(new Steps("http://players.brightcove.net/1660646664/HydB02Dyb_default/index.html?playlistId=5047277540001", 
	//stepsList1.add(new Steps("http://engtestsite.com/bsdk/testsuite/",
								"className",
								//"//*[@id='wrapper']/nav/div[2]/ul/li[2]/a",
								"vjs-big-play-button",  //*[@id="vjs_video_3"]/button
								15,
								"leftClick"));
	/*stepsList1.add(new Steps("sameUrl", 
								"xpath", 
								"/html/body/ol/li[3]",
								10,
								"leftClick"));

	stepsList1.add(new Steps("http://discovery.com", 
								"id", 
								"menu-item-192593",
								10,
								"leftClick"));*/
	stepsList1.add(new Steps("endTestCase", 
			"", 
			"",  //*[@id="vjs_video_3"]/button
			0,
			""));
	// Now add to the array of testCases
	testCaseArr.add(new TestCase("jfTestCase1", 
								stepsList1, 
								"base_testCase1.txt", 
								"dcr",
								"c27,c51,c52,cr,rnd",
								null));
	
	////////////////////
	// TestCase 2
	ArrayList<Steps> stepsList2 = new ArrayList<Steps>();
	//stepsList2.add(new Steps("http://engtestsite.com/joel/autoTest/autoTestPage.html", 
	stepsList2.add(new Steps("http://players.brightcove.net/1660646664/HydB02Dyb_default/index.html?playlistId=5047277540001", 
								"className",
								//"//*[@id='wrapper']/nav/div[2]/ul/li[2]/a",
								"vjs-big-play-button",  //*[@id="vjs_video_3"]/button
								35,
								"leftClick"));
	stepsList2.add(new Steps("killBrowser", 
								"", 
								"",  //*[@id="vjs_video_3"]/button
								0,
								""));
	
	// Now add to the array of testCases
	testCaseArr.add(new TestCase("jfTestCase2", 
								stepsList2, 
								"base_testCase2.txt", 
								"dcr",
								"c27,c51,c52,cr",
								null));
	
	return(testCaseArr);
}

}
