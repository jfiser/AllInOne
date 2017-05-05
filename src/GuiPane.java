

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.swing.*;

import org.apache.commons.io.FileUtils;

public class GuiPane extends JPanel {
	
static String comboSelected = "";
static String testCaseToMakeBaseline = "";
static String testCaseToRun = "";
static String[] testCases = new String[50]; // = { "BC_TestPlayer", "testCase2", "testCase3", "testCase4", "testCase5" };
static JButton btnSetBaseline;
static JTextField pingTextHolder;
static JTextArea pingTextArea = new JTextArea();
static JFrame frame;
static JComboBox testCaseCombo;
static DefaultComboBoxModel comboModel;
//static ArrayList<TestCase> testCaseArr;

public GuiPane(ArrayList<TestCase> _testCaseArr) {
    //super(new BorderLayout());
	//testCaseArr = _testCaseArr;
	for (int i = 0; i < _testCaseArr.size(); i++) {
		testCases[i] = _testCaseArr.get(i).testCaseId;
		if(!_testCaseArr.get(i).baseFileName.equals(null) && !_testCaseArr.get(i).baseFileName.equals("")){
			testCases[i] = testCases[i] + " (Has baseline)";
		}
		else{
			testCases[i] = testCases[i] + " (No baseline)";
		}
		System.out.println("testCaseArr>>: " + _testCaseArr.get(i).testCaseId);
	}
    comboSelected = stripBaselineComment(testCases[0]);

    createAndShowGUI(this);
}
private static void createAndShowGUI(GuiPane guiPane) {
	javax.swing.SwingUtilities.invokeLater(new Runnable() {
        @Override
		public void run(){
		    frame = new JFrame("All In One Ping Validator");
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		    guiPane.setOpaque(true); //content panes must be opaque
		    frame.setContentPane(guiPane);
		    
		    addBtnRunTest(guiPane);
		    addComboBox(guiPane);
		    addBtnBaseline(guiPane);
		    addPingTextHolder(guiPane);
		    //testCaseCombo.setPrototypeDisplayValue("text here");
		    frame.setSize(1050, 700);
		    //frame.setMinimumSize(new Dimension(600, 600));
		    //frame.pack();
		    frame.setVisible(true);
        }
	});
}
private static void addComboBox(GuiPane _guiPane){
    testCaseCombo = new JComboBox(testCases);
    _guiPane.add(testCaseCombo);	
    
    testCaseCombo.setSelectedIndex(0);
    
    // Set up the event listener for comboBox
    testCaseCombo.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
        	System.out.println("getActionCommand: " + e.getActionCommand());
        	System.out.println("getActionCommand: " + e.paramString());
        	
        	String[] tmpArr;
        	String str;
        	
        	JComboBox cb = (JComboBox)e.getSource();
            comboSelected = stripBaselineComment((String) cb.getSelectedItem());
            System.out.println("chosenTestCase: " + comboSelected);
        }
    });  
}
private static String stripBaselineComment(String _str){
	if(_str != null && _str.indexOf("(") != -1){
		return(_str.split(" \\(")[0]);
	}
	return(_str);
}
private static void resetComboBox(){
	testCaseCombo.removeAllItems();
	for(int i = 0; i < testCases.length && testCases[i] != null; i++){
		testCaseCombo.addItem(testCases[i]);
    }
}
private static void addBtnBaseline(GuiPane _guiPane){
    btnSetBaseline = new JButton("Set as Baseline");
    _guiPane.add(btnSetBaseline);
    btnSetBaseline.setBounds(60, 20, 20, 30);
    disableBtnSetBaseline();
    //btnSetBaseline.addActionListener(new BtnListener(comboSelected));
    // Set up the event listener for baseline button
    btnSetBaseline.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
        	String _tmpStr;
        	
        	testCaseToMakeBaseline = comboSelected;
        	System.out.println("testCaseToMakeBaseline: " + testCaseToMakeBaseline);
        	//Files.move(source, source.resolveSibling("base_" + comboSelected + ".txt"), StandardCopyOption.REPLACE_EXISTING);
			saveCurPingsToBaselineFile();
			// reset ComboBox to show that this testCase now has a baseline
			for(int i = 0; i < testCases.length && testCases[i] != null; i++){
				System.out.println("testCaseId: " + testCases[i]);
				_tmpStr = stripBaselineComment(testCases[i]);
				if(_tmpStr.equals(testCaseToMakeBaseline)){
					testCases[i] = _tmpStr + " (Has baseline)";
					resetComboBox();
					//testCaseCombo.setModel(comboModel); // reset comboBox
				}
			}
        }
    });	
}
private static void saveCurPingsToBaselineFile(){
	String _fileToCreate = "base_" + comboSelected + ".txt";
	File _curTestCasePingFile = new File(_fileToCreate); 
	System.out.println("Save to baseline: " + Browsy.curTestCasePingArr.get(0));
	for (int i = 0; i < Browsy.curTestCasePingArr.size(); i++) {
		System.out.println("Save to baseline: " + Browsy.curTestCasePingArr.get(i));
		try {
    		FileUtils.writeStringToFile(_curTestCasePingFile, Browsy.curTestCasePingArr.get(i) + System.getProperty("line.separator"), 
    								Charset.defaultCharset(), (i == 0 ? false : true)); //Charset.defaultCharset());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// add the baseline name to the testCase
	if(Browsy.curTestCasePingArr.size() > 0){
		for(int i = 0; i < Browsy.testCaseArr.size(); i++){
			if(Browsy.testCaseArr.get(i).testCaseId.equals(comboSelected)){
				Browsy.testCaseArr.get(i).baseFileName = _fileToCreate;
				TestCaseSetup.saveTestCaseArr();
				break;
			}
		}
	}
	//_fileToCreate
}
private static void addBtnRunTest(GuiPane _guiPane){
    JButton btnRunTest = new JButton("Run Test");
    _guiPane.add(btnRunTest);
    btnRunTest.setBounds(60, 20, 20, 30);
    //btnSetBaseline.addActionListener(new BtnListener(comboSelected));
    // Set up the event listener for baseline button
    btnRunTest.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
        	//testCaseToRun = comboSelected.split(" \\(")[0];
        	testCaseToRun = stripBaselineComment(comboSelected);
        	//((String) cb.getSelectedItem()).split(" \\(")[0];
        	System.out.println("testCaseToRun: " + testCaseToRun);
        	Browsy.curTestCasePingArr.clear();
            disableBtnSetBaseline();
        	Browsy.doTest(testCaseToRun);
        }
    });
}

public static void disableBtnSetBaseline(){
	System.out.println(">>>>>>>>>>>>>>>>>>>>disableBtnSetBaseline ");
	btnSetBaseline.setEnabled(false); 
}
public void enableBtnSetBaseline(){
	System.out.println(">>>>>>>>>>>>>>>>>>>>enableBtnSetBaseline ");
	btnSetBaseline.setEnabled(true); 
}
public static void addPingTextHolder(GuiPane _guiPane){
	SwingUtilities.invokeLater(new Runnable()
	{
    public void run()
    {
		//JTextArea pingTextArea = new JTextArea();
		GuiPane.pingTextArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
		GuiPane.pingTextArea.setForeground(new Color(0x444444));
		GuiPane.pingTextArea.setSize(400,400);    
		
		GuiPane.pingTextArea.setLineWrap(true);
		GuiPane.pingTextArea.setEditable(true);
		GuiPane.pingTextArea.setVisible(true);
		  // add text to it; we want to make it scroll
		  //pingTextArea.setText("xx\nxx\nxx\nxx\nxx\nxx\nxx\nxx\nxx\nxx\nxx\nxx\nxx\nxx\n");
		  // create a scrollpane, givin it the textarea as a constructor argument
		      	      	  
		JScrollPane scrollPane = new JScrollPane(pingTextArea);
		scrollPane.setPreferredSize(new Dimension(1000, 450));
		frame.getContentPane().add(scrollPane);
  /*frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  // set the frame size (you'll usually want to call frame.pack())
  frame.setSize(new Dimension(840, 680));      
  // center the frame
  frame.setLocationRelativeTo(null);  */   
		//frame.setVisible(true);
    }
  });
}

/*public static void addPingTextHolder(GuiPane _guiPane){
	pingTextHolder = new JTextField(108);
    //textField.setBounds(5, 5, 280, 50); // to get height, set large font
	pingTextHolder.setFont(pingTextHolder.getFont().deriveFont(50f));
    _guiPane.add(pingTextHolder);
}*/
public static void addTextToPane(String str){
	  GuiPane.pingTextArea.append(str);
}

/*public void addTextToPane(String str){
	pingTextArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
	pingTextArea.setForeground(new Color(0x444444));
	pingTextArea.setSize(400,400);    

	pingTextArea.setLineWrap(true);
	pingTextArea.setEditable(true);
	pingTextArea.setVisible(true);
	      	      	  
	JScrollPane scrollPane = new JScrollPane(pingTextArea);
	
	GuiPane.pingTextArea.append(str);
}*/

}




