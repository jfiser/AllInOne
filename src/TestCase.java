import java.util.ArrayList;

public class TestCase {
	public String testCaseId;
	public ArrayList<Steps> stepsList; 

	public TestCase(String _testCaseId, ArrayList<Steps> _stepsList) {
		testCaseId = _testCaseId;
		stepsList = _stepsList;
	}
}


