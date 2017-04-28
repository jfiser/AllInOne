
import java.util.ArrayList;

public class TestCaseSetup {



public static ArrayList<TestCase> createTestCaseArr() {
	ArrayList<TestCase> testCaseArr = new ArrayList<TestCase>();
	
	// TestCase 1
	ArrayList<Steps> stepsList1 = new ArrayList<Steps>();
	stepsList1.add(new Steps("http://players.brightcove.net/1660646664/BJvGXLtr_default/index.html?playlistId=5047277540001", 
										"className", 
										"vjs-big-play-button",  //*[@id="vjs_video_3"]/button
										600,
										"leftClick"));
			/*stepsList.add(new Steps("sameUrl", 
										"xpath", 
										"/html/body/ol/li[3]",
										10,
										"leftClick"));

			stepsList.add(new Steps("http://discovery.com", 
										"id", 
										"menu-item-192593",
										10,
										"leftClick"));*/
	testCaseArr.add(new TestCase("testCase1", 
								stepsList1, 
								"base_testCase1.txt", 
								"dcr",
								"c27,c51,c52,cr,rnd"));
	
	// TestCase 2
	ArrayList<Steps> stepsList2 = new ArrayList<Steps>();
	stepsList2.add(new Steps("http://engtestsite.com/joel/autoTest/autoTestPage.html", 
										"className", 
										"vjs-big-play-button",  //*[@id="vjs_video_3"]/button
										25,
										"leftClick"));
	testCaseArr.add(new TestCase("testCase2", 
								stepsList2, 
								"base_testCase2.txt", 
								"dcr",
								"c27,c51,c52,cr"));
	
	return(testCaseArr);
}

}
