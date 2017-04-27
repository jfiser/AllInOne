

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class GuiPane extends JPanel {
	
static String comboSelected = "";
static String testCaseToMakeValid = "";
static String testCaseToRun = "";
static String[] testCases = new String[50]; // = { "BC_TestPlayer", "testCase2", "testCase3", "testCase4", "testCase5" };


public GuiPane(ArrayList<TestCase> _testCaseArr) {
    //super(new BorderLayout());
	for (int i = 0; i < _testCaseArr.size(); i++) {
		testCases[i] = _testCaseArr.get(i).testCaseId;
		System.out.println("testCaseArr>: " + _testCaseArr.get(i));
	}
    comboSelected = testCases[0];

    createAndShowGUI(this);
}
private static void createAndShowGUI(GuiPane guiPane) {
	javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run(){
		    JFrame frame = new JFrame("All In One Ping Validator");
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		    guiPane.setOpaque(true); //content panes must be opaque
		    frame.setContentPane(guiPane);
		    
		    addBtnRunTest(guiPane);
		    addComboBox(guiPane);
		    addBtnBaseline(guiPane);
		    
		    //testCaseCombo.setPrototypeDisplayValue("text here");
		    frame.setSize(650, 500);
		    //frame.setMinimumSize(new Dimension(600, 600));
		    //frame.pack();
		    frame.setVisible(true);
        }
	});
}
private static void addComboBox(GuiPane _guiPane){
    JComboBox testCaseCombo = new JComboBox(testCases);
    _guiPane.add(testCaseCombo);	
    testCaseCombo.setSelectedIndex(0);
    // Set up the event listener for comboBox
    testCaseCombo.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
        	JComboBox cb = (JComboBox)e.getSource();
            comboSelected = (String)cb.getSelectedItem();
            //updateLabel(petName);
            System.out.println("chosenTestCase: " + comboSelected);
        }
    });  
}
private static void addBtnBaseline(GuiPane _guiPane){
    JButton btnSetBaseline = new JButton("Set as Baseline");
    _guiPane.add(btnSetBaseline);
    btnSetBaseline.setBounds(60, 20, 20, 30);
    //btnSetBaseline.addActionListener(new BtnListener(comboSelected));
    // Set up the event listener for baseline button
    btnSetBaseline.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
        	testCaseToMakeValid = comboSelected;
        	System.out.println("testCaseToMakeValid: " + testCaseToMakeValid);
        }
    });
	
}
private static void addBtnRunTest(GuiPane _guiPane){
    JButton btnRunTest = new JButton("Run Test");
    _guiPane.add(btnRunTest);
    btnRunTest.setBounds(60, 20, 20, 30);
    //btnSetBaseline.addActionListener(new BtnListener(comboSelected));
    // Set up the event listener for baseline button
    btnRunTest.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
        	testCaseToRun = comboSelected;
        	System.out.println("testCaseToRun: " + testCaseToRun);
        	Browsy.doTest(testCaseToRun);
        }
    });
	
}


}




