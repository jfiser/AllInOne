
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class TestCaseSetup {
	
public static ArrayList<TestCase> createTestCaseArr() {
	//ArrayList<TestCase> testCaseArr = new ArrayList<TestCase>();
	Type typeOfT = new TypeToken<ArrayList<TestCase>>(){}.getType();
	
	Gson gson = new Gson();
	//Gson gson = new GsonBuilder().setPrettyPrinting().create();
	JsonReader reader = null;
	try {
		reader = new JsonReader(new FileReader("./testCases.json"));
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	}
	// keep testCaseArr as a local var in this func. Use Browsy.testCaseArr for official testCaseArr
	ArrayList<TestCase> testCaseArr = gson.fromJson(reader, typeOfT); // contains the whole reviews list
	/*for(int i = 0; i < testCaseArr.size(); i++){
		System.out.println("size: " + testCaseArr.size());
		System.out.println("JSON: <" + testCaseArr.get(i) + ">");
		System.out.println("JSON: <" + testCaseArr.get(i).stepsList.get(i) + ">");
		System.out.println("JSON: <" + testCaseArr.get(i).stepsList.get(i).accessorType + ">");
	}*/
	return testCaseArr;
}
public static void saveTestCaseArr(){
	try (Writer writer = new FileWriter("./testCases.json")) {
	    //Gson gson = new GsonBuilder().create();
	    Gson gson = new GsonBuilder().setPrettyPrinting().create();
	    gson.toJson(Browsy.testCaseArr, writer); // need to save to Browsy.testCaseArr - the official testCaseArr
	} catch (IOException e) {
		e.printStackTrace();
	}
}
/*public static ArrayList<TestCase> createTestCaseArr2() {
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
}*/

}
