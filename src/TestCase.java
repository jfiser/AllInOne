import java.util.ArrayList;

public class TestCase {
	public String testCaseId;
	public ArrayList<Steps> stepsList;
	public String baselineFile;
	public String prodsToCheck;
	public String valsToCheck; // comma delimited list like:  "c27,c51,c52"
	public String baseFileName;

	public TestCase(String _testCaseId, ArrayList<Steps> _stepsList, String _baselineFile, String _prodsToCheck, String _valsToCheck, String _baseFileName) {
		testCaseId = _testCaseId;
		stepsList = _stepsList;
		baselineFile = _baselineFile;
		prodsToCheck = _prodsToCheck;
		valsToCheck = _valsToCheck;
		baseFileName = _baseFileName;
	}
}


