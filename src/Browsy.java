

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

//import gui.JScrollPaneDemo;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.proxy.CaptureType;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

import org.apache.commons.io.FileUtils;


public class Browsy {

static GuiPane guiPane;
//static ArrayList<Steps> stepsList = new ArrayList<Steps>();
static String curUrlBeingTested = "";
static File requestPingFile;
static File bcPlayerVersionFile = new File("BSDK_BC_plugin_playerVersion.txt");
static File sdkVersionFile = new File("BSDK_BC_plugin_sdkVersion.txt");
static ConfigChecker configChecker;
static ThirdPartyPlayer thirdPartyPlayer = new ThirdPartyPlayer();
//static BasicTests basicTests = new BasicTests();
static Boolean gotPlayerVersion = false;
static Boolean firstPing = true;
static String[][] curSessionPingArr;
static boolean hasId3 = false;
static TestCase curTestCase = null;

static WebDriver driver;
static BrowserMobProxy bmpProxy;
static ArrayList<TestCase> testCaseArr;
static ArrayList<Steps> curStepsList = null;
static int curStepsListIndx = 0;
static ArrayList<String> curTestCasePingArr = new ArrayList<String>();
static Boolean batchRun = false;

public static void main(String[] args) {	
	//TestCaseSetup testCaseSetup = new TestCaseSetup();
	testCaseArr = TestCaseSetup.createTestCaseArr();
	guiPane = new GuiPane(testCaseArr);
	Report.getTemplate("http://engtestsite.com/joel/autoTest/reportTemplate.html");

	//System.setProperty("webdriver.chrome.driver", "/ChromeDriver/chromedriver.exe");
	System.setProperty("webdriver.chrome.driver", "chromedriver");
   
	//Comparator.getPingFileCreateArray("currentRunPings.txt", "prev");
 }
 public static void setProxy() throws IOException{
	 bmpProxy = new BrowserMobProxyServer();
	 bmpProxy.start(0);

	 bmpProxy.addRequestFilter(new RequestFilter() {
         //int cnt = 1;
                  
		 @Override
         public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents, HttpMessageInfo messageInfo) {
			 String urlStr = messageInfo.getOriginalUrl();
			 String playerVersion = "";
			 String bldv = "";

			 if(urlStr.indexOf("imrworld") != -1){ // || urlStr.indexOf("facebook") != -1){
	        	//guiPane.addTextToPane(cnt++ + ". " + urlStr + "\n\n");
	        	try {
					if(firstPing){ // open a new file - don't just append
						firstPing = false;
						requestPingFile = new File("currentRunPings.txt"); // only open file after Comparator has read previous one
						FileUtils.writeStringToFile(requestPingFile, urlStr + System.getProperty("line.separator"), Charset.defaultCharset()); //Charset.defaultCharset());
					}
					else{ // now append
		        		FileUtils.writeStringToFile(requestPingFile, urlStr + System.getProperty("line.separator"), Charset.defaultCharset(), true); //Charset.defaultCharset());
					}
					System.out.println("handlePing>: " + urlStr);
					guiPane.addTextToPane(urlStr + "\n\n");
					//FileUtils.writeStringToFile(requestPingFile, str + '\n', Charset.defaultCharset());
				} catch (IOException e) {
					e.printStackTrace();
				}
	        	// save PINGS only in array so I can write to a baseline file if requested (not config, ggcmb.js, etc)
	        	if(urlStr.indexOf("imrworldwide.com/cgi-bin/") != -1 && urlStr.indexOf("imrworldwide.com/cgi-bin/cfg?") == -1){
	        		curTestCasePingArr.add(urlStr);
	        		System.out.println("Browsy - curTestCasePingArr: " + curTestCasePingArr.get(0));
	        	}
	        	
	        	// grab the config file to record the params
	        	if(urlStr.indexOf("cgi-bin/cfg") != -1){
					System.out.println("GOT cfg");

	        		bldv = ConfigChecker.getValFromUrl(urlStr, "bldv");
					System.out.println("bsdkv>: " + bldv);
	        		
	        		try {
    					FileUtils.writeStringToFile(sdkVersionFile, bldv, Charset.defaultCharset());
    				} catch (IOException e) {
    					e.printStackTrace();
    				} 
	        	}
	        	//basicTests.handlePing(urlStr);
        	}
        	
            return null;
         }
     });
	 	 
	 
	 /*bmpProxy.addResponseFilter(new ResponseFilter() {
         @Override
         public void filterResponse(HttpResponse response, HttpMessageContents contents, HttpMessageInfo messageInfo) {
        	 String urlStr = messageInfo.getOriginalUrl();
			 //if(urlStr.indexOf("imrworld") != -1 || urlStr.indexOf("facebook") != -1){
		        	//guiPane.addTextToPane("response::: " + urlStr + '\n');
			 //}
        	 
        	 if(urlStr.indexOf("imrworld") != -1 && urlStr.indexOf("configs/glcfg") != -1){
        		 String str = contents.getTextContents();
        		 guiPane.addTextToPane("response from :" + urlStr + '\n' + urlStr + '\n');
			 }
         }
     });*/
	 
	 System.out.println("BrowserMobProxy: " + bmpProxy);

    //proxy.start(0);
    // get the JVM-assigned port and get to work!
    int port = bmpProxy.getPort();
    System.out.println("port: " + port);

    Proxy seleniumProxy = ClientUtil.createSeleniumProxy(bmpProxy);
    System.out.println("seleniumProxy: " + seleniumProxy);
    
    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

    // start the browser up
    //WebDriver driver = new FirefoxDriver(capabilities);
    driver = new ChromeDriver(capabilities);

    // enable more detailed HAR capture, if desired (see CaptureType for the complete list)
    bmpProxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);

    // create a new HAR 
    bmpProxy.newHar("Browsy.har");
    //doTest(driver);
    //driver.get("http://google.com");    
    
    // get the HAR data
    Har har = bmpProxy.getHar();  
   
	File harFile = new File("BSDK_BC_plugin_canary.har");
	har.writeTo(harFile);
    
    System.out.println("har: " + har);
 }
 public static void startBrowser(){
	try {
		curTestCasePingArr.clear(); // clear out any pings from previous test cases
		setProxy();
	} catch (IOException e) {
		e.printStackTrace();
	}
 }
 public static void doTest(String _testCaseToRun){
	Thread browserThread = new Thread(new Runnable() {
        public void run() {
        	//startBrowser();			 
			WebElement btnToClick = null;
			// Iterate through the test cases
			for(int i = 0; i < testCaseArr.size(); i++){
				System.out.println("testCaseId: " + testCaseArr.get(i).testCaseId);
				if(_testCaseToRun.equals("batch")){
					batchRun = true;
					curStepsList = testCaseArr.get(i).stepsList;
					curTestCase = testCaseArr.get(i);
					startBrowser();			 
				}
				else{
					batchRun = false;
					if(testCaseArr.get(i).testCaseId.equals(_testCaseToRun)){
						curStepsList = testCaseArr.get(i).stepsList;
						curTestCase = testCaseArr.get(i);
						startBrowser();	
					}
					else{
						continue; // iterating through looking for specific testcase
					}
				}
				//}
				if(curStepsList == null){
					System.out.println("Cannot find testCase: " + _testCaseToRun);
					return;
				}
				 
				// Execute the steps one by one
				for (int curStepsListIndx = 0; curStepsListIndx < curStepsList.size(); curStepsListIndx++) {
					System.out.println(curStepsList.get(curStepsListIndx));
					if(curStepsList.get(curStepsListIndx).urlToTest.equals("killBrowser")){						
						killBrowser();
						//curSessionPingArr = Comparator.compare();
						if(_testCaseToRun.equals("batch")){ // && i != testCaseArr.size()-1){
							continue;
						}
						else{
							return;
						}
					}
						 
					 // go to the URL to test unless the URL param is set to "sameUrl"
					if(!curStepsList.get(curStepsListIndx).urlToTest.equals("") 
												&& !curStepsList.get(curStepsListIndx).urlToTest.equals("sameUrl") 
												&& !curStepsList.get(curStepsListIndx).urlToTest.equals(curUrlBeingTested)){
						driver.get(curStepsList.get(curStepsListIndx).urlToTest);
					    //driver.navigate().to(curStepsList.get(curStepsListIndx).urlToTest);
					    //driver.manage().window().maximize();
					    //driver.switchTo().window(driver.getWindowHandle());

					}
					// Can use class, id, or xpath as selector
					if(curStepsList.get(curStepsListIndx).accessorType.equals("className")){
						btnToClick = (new WebDriverWait(driver, 20))
					    		  .until(ExpectedConditions.elementToBeClickable(By.className(curStepsList.get(curStepsListIndx).accessorName)));
						 
					}
					else
					if(curStepsList.get(curStepsListIndx).accessorType.equals("id")){
						btnToClick = (new WebDriverWait(driver, 20))
					    		  .until(ExpectedConditions.elementToBeClickable(By.id(curStepsList.get(curStepsListIndx).accessorName)));
						 
					}
					else
					if(curStepsList.get(curStepsListIndx).accessorType.equals("xpath")){
						btnToClick = (new WebDriverWait(driver, 20))
					    		  .until(ExpectedConditions.elementToBeClickable(By.xpath(curStepsList.get(curStepsListIndx).accessorName)));
						System.out.println("AFTER XPath>>>>>>>>>>>>>>>>>>>>>>>>>");
					}
					else // comboBox is done in 2 steps because we don't know what type of accessor will be passed (id, xpath, classname)
					if(curStepsList.get(curStepsListIndx).accessorType.equals("select")){
						//Select selectBox = new Select(driver.findElement(By.id(elementId)));
						Select selectBox = new Select(btnToClick);
						//selectBox.selectByValue(curStepsList.get(curStepsListIndx).accessorName);
						if(curStepsList.get(curStepsListIndx).clickType.equals("index")){
							selectBox.selectByIndex(Integer.parseInt(curStepsList.get(curStepsListIndx).accessorName));
						}
						else{ // Must be accessing the comboBox by string
							selectBox.selectByValue(curStepsList.get(curStepsListIndx).accessorName);
						}
					}

					// Haven't yet added rightClick stuff - haven't needed it yet			 
					if(btnToClick != null){
						btnToClick.click();
					}
					// Now thread sleep for however long specified while testing occurs 
					//setTimeout(() -> checkForNextStep(), curStepsList.get(curStepsListIndx).duration * 1000);
				    try {
				    	System.out.println(">>>>>>>>>Sleeping for: " + curStepsList.get(curStepsListIndx).duration);
				    	Thread.sleep(curStepsList.get(curStepsListIndx).duration * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
				 } 
			 }
         }
    });  
    browserThread.start();

 }
 public static void killBrowser(){
 	driver.navigate().refresh(); // refresh the browser so we get the final pings
 	// Now sleep to give the onunload ping time to fire so it gets associated with the correct test case
 	try{
		Thread.sleep(4000);
		System.out.println(">>>>>>=>>>>AFTER THREAD SLEEP 4000");
	}
 	catch (InterruptedException e){
		e.printStackTrace();
	}
 	
 	bmpProxy.stop();
 	driver.quit();
	if(!batchRun){
	    guiPane.enableBtnSetBaseline();
	}
	curSessionPingArr = Comparator.compare();
}
	 
 
}
       