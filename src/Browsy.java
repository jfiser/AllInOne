

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

//import gui.JScrollPaneDemo;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.filters.ResponseFilter;
import net.lightbody.bmp.proxy.CaptureType;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


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

static WebDriver driver;
static BrowserMobProxy bmpProxy;
static ArrayList<TestCase> testCaseArr;

//static Array<Steps> stepsList = new Array<Steps>();

//stepsList[0] = "className|vjs-big-play-button";
//stepsList.push("className|vjs-big-play-button");
	//guiPane.addTextToPane("str"); 

public static void main(String[] args) {	
	//TestCaseSetup testCaseSetup = new TestCaseSetup();
	testCaseArr = TestCaseSetup.createTestCaseArr();
	guiPane = new GuiPane(testCaseArr);

	//System.setProperty("webdriver.chrome.driver", "/ChromeDriver/chromedriver.exe");
	System.setProperty("webdriver.chrome.driver", "chromedriver");
   
	Comparator.getPingFileCreateArray("BSDK_BC_plugin_requestPings.txt", "prev");
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
						requestPingFile = new File("BSDK_BC_plugin_requestPings.txt"); // only open file after Comparator has read previous one
						FileUtils.writeStringToFile(requestPingFile, urlStr + System.getProperty("line.separator"), Charset.defaultCharset()); //Charset.defaultCharset());
					}
					else{ // now append
		        		FileUtils.writeStringToFile(requestPingFile, urlStr + System.getProperty("line.separator"), Charset.defaultCharset(), true); //Charset.defaultCharset());
					}
					System.out.println("handlePing>: " + urlStr);
					guiPane.addTextToPane(urlStr + "\n");
					//FileUtils.writeStringToFile(requestPingFile, str + '\n', Charset.defaultCharset());
				} catch (IOException e) {
					e.printStackTrace();
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
			
     		if(!gotPlayerVersion && urlStr.indexOf("metrics.brightcove.com") != -1){
    			gotPlayerVersion = true;
     			playerVersion = thirdPartyPlayer.getPlayerVersion(urlStr);
     			
    			//guiPane.addTextToPane(">>>>>>>>>> Player Version: " + playerVersion + '\n');
				System.out.println(">>>>>>>>>> Player Version: " + playerVersion);
    			try {
					FileUtils.writeStringToFile(bcPlayerVersionFile, playerVersion, Charset.defaultCharset());
				} catch (IOException e) {
					e.printStackTrace();
				} 
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
    //bmpProxy.stop();
    //driver.quit();
    //Comparator.getPingFileCreateArray("BSDK_BC_plugin_requestPings.txt", "cur");
    //curSessionPingArr = Comparator.compare();
    //if(hasId3){
    	//ID3_Validator.check_D_Pings(curSessionPingArr);
    //}
 }
 public static void startBrowser(){
	try {
		setProxy();
	} catch (IOException e) {
		e.printStackTrace();
	}
 }
 public static void killBrowser(){
    bmpProxy.stop();
    driver.quit();
    Comparator.getPingFileCreateArray("BSDK_BC_plugin_requestPings.txt", "cur");
    curSessionPingArr = Comparator.compare();
    guiPane.enableBtnSetBaseline();
 }
 public static void setTimeout(Runnable runnable, int delay){
    new Thread(() -> {
        try {
            Thread.sleep(delay);
            runnable.run();
        }
        catch (Exception e){
            System.err.println(e);
        }
    }).start();
}
 public static void doTest(String _testCaseToRun){
	 ArrayList<Steps> stepsList = null;
	 startBrowser();
	 
	 WebElement btnToClick = null;
	 for(int i = 0; i < testCaseArr.size(); i++){
		 if(testCaseArr.get(i).testCaseId == _testCaseToRun){
			 stepsList = testCaseArr.get(i).stepsList;
		 }
	 }
	 if(stepsList == null){
		 System.out.println("Cannot find testCase: " + _testCaseToRun);
		 return;
	 }
	 
	 for (int i = 0; i < stepsList.size(); i++) {
		 System.out.println(stepsList.get(i));
		 // go to the URL to test unless the URL param is set to "sameUrl"
		 if(stepsList.get(i).urlToTest != "sameUrl" && stepsList.get(i).urlToTest != curUrlBeingTested){
			 driver.get(stepsList.get(i).urlToTest);
		 }
		 // Can use class, id, or xpath as selector
		 if(stepsList.get(i).accessorType == "className"){
			 btnToClick = (new WebDriverWait(driver, 20))
		    		  .until(ExpectedConditions.elementToBeClickable(By.className(stepsList.get(i).accessorName)));
			 
		 }
		 else
		 if(stepsList.get(i).accessorType == "id"){
			 btnToClick = (new WebDriverWait(driver, 20))
		    		  .until(ExpectedConditions.elementToBeClickable(By.id(stepsList.get(i).accessorName)));
			 
		 }
		 else
		 if(stepsList.get(i).accessorType == "xpath"){
			 btnToClick = (new WebDriverWait(driver, 20))
		    		  .until(ExpectedConditions.elementToBeClickable(By.xpath(stepsList.get(i).accessorName)));
			 
		 }
		 // Haven't yet added rightClick stuff - haven't needed it yet			 
		 btnToClick.click();
		// Now thread sleep for however long specified while testing occurs 
		 setTimeout(() -> killBrowser(), stepsList.get(i).duration * 1000);
	    /*try {
			Thread.sleep(stepsList.get(i).duration * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} */ 
			
	}
	 
 }
	 
 
}
       