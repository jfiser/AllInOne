import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Report {
	
private static int curTestCaseNum = 1;
static Document htmlTemplate = null;

public Report() {
	// TODO Auto-generated constructor stub
}
// getTemplateAndParse("http://engtestsite.com/joel/autoTest/report.html");
public static void getTemplate(String _fileToGet){
    //String url = _fileToGet;
    Elements curTestCase = null;
    String tr = null, newVal = null;
    
    print("Fetching %s...", _fileToGet);

	try {
		htmlTemplate = Jsoup.connect(_fileToGet).get();
	} catch (IOException e) {
		e.printStackTrace();
	}
    
	/*Elements curTestCaseId2 = htmlTemplate.select("#tr1 > .testCaseId");
	print("row: (%s)", curTestCaseId2.html());
	curTestCaseId2.html("newContent");
	print("row: (%s)", curTestCaseId2.html());
	//for(Element row : tr){
        //print("row: (%s)", row.id());
        //row#
	//}
	
	/for(int i = 1; i < strArr.length; i++){
		tr = "#tr" + i + " > .testCaseId";
		curTestCase = htmlTemplate.select(tr);
		
		print("row: (%s)", curTestCase.html());
		//newVal = 
		//curTestCase.html(""
	}*/
	
	
}
public static void addLine(TestCase _curTestCase, String _pingType, ArrayList<String> _baseSelectedValArr, ArrayList<String> _curSelectedValArr){
	//curTestCaseNum;
	//String tr = "#tr" + curTestCaseNum;
	//String _baseValStr = _baseSelectedValArr.join(",");
	String _baseValStr = String.join("</br>", _baseSelectedValArr);
	String _curValStr = String.join("</br>", _curSelectedValArr);
	
	/*Elements curRow = htmlTemplate.select("#tr" + curTestCaseNum++);
	curRow.html("<td class='testCaseId'>" + _curTestCase.testCaseId + "</td>"
				+ "<td class='pingType'>" + _pingType + "</td>"
				+ "<td class='baseVals'>" + _baseValStr + "</td>"
				+ "<td class='curVals'>" + _curValStr + "</td>"			
			);*/
	
	Element myTable = htmlTemplate.select("table").first();
	myTable.append("<td class='testCaseId'>" + _curTestCase.testCaseId + "</td>"
				+ "<td class='pingType'>" + _pingType + "</td>"
				+ "<td class='baseVals'>" + _baseValStr + "</td>"
				+ "<td class='curVals'>" + _curValStr + "</td>"			
			);
	
	
	try {
		savePage(htmlTemplate);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}
private static void print(String msg, Object... args) {
    System.out.println(String.format(msg, args));
}

private static String trim(String s, int width) {
    if (s.length() > width)
        return s.substring(0, width-1) + ".";
    else
        return s;
}


public static void savePage(Document _htmlTemplate) throws Exception {
    final File f = new File("report2.html");
    FileUtils.writeStringToFile(f, _htmlTemplate.outerHtml(), "UTF-8");
}


}
