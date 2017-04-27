

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GuiPane extends JPanel {
	
static String comboSelected = "";
static String testCaseToMakeValid = "";

public GuiPane() {
    //super(new BorderLayout());
    String[] testCases = { "BC_TestPlayer", "testCase2", "testCase3", "testCase4", "testCase5" };
    comboSelected = testCases[0];
    JComboBox testCaseCombo = new JComboBox(testCases);
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
    
    add(testCaseCombo, BorderLayout.PAGE_START);
    createAndShowGUI(this);
}
private static void createAndShowGUI(GuiPane guiPane) {
	javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run(){
		    JFrame frame = new JFrame("All In One Ping Validator");
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		    guiPane.setOpaque(true); //content panes must be opaque
		    frame.setContentPane(guiPane);
		    
		    JButton btnSetBaseline = new JButton("Set as Baseline");
		    guiPane.add(btnSetBaseline);
		    btnSetBaseline.setBounds(60, 20, 20, 30);
		    //btnSetBaseline.addActionListener(new BtnListener(comboSelected));
		    // Set up the event listener for baseline button
		    btnSetBaseline.addActionListener(new ActionListener(){
		        public void actionPerformed(ActionEvent e){
		        	testCaseToMakeValid = comboSelected;
		        	System.out.println("testCaseToMakeValid: " + testCaseToMakeValid);
		        }
		    });
		    
		    //testCaseCombo.setPrototypeDisplayValue("text here");
		    frame.setSize(600, 600);
		    //frame.setMinimumSize(new Dimension(600, 600));
		    //frame.pack();
		    frame.setVisible(true);
        }
	});
}

}




