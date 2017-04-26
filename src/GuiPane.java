

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class GuiPane
{
static JFrame frame = new JFrame("Browsy4"); 
static JTextArea textArea = new JTextArea();
static JTextArea infoTextArea = new JTextArea();

		
 public GuiPane()
  {
	  addFrame();
	  addScrollPane(140,20,25,20, BorderLayout.CENTER);
	  //addScrollPane(20,20,25,20, BorderLayout.NORTH);
  }
  public void addScrollPane(Integer __marginTop, Integer _marginRight, Integer _marginBottom, Integer _marginLeft, String _loc){
	  SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        //JTextArea textArea = new JTextArea();
    	  GuiPane.textArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
    	  GuiPane.textArea.setForeground(new Color(0x444444));
    	  GuiPane.textArea.setSize(400,400);    

    	  GuiPane.textArea.setLineWrap(true);
    	  GuiPane.textArea.setEditable(true);
    	  GuiPane.textArea.setVisible(true);
        // add text to it; we want to make it scroll
        //textArea.setText("xx\nxx\nxx\nxx\nxx\nxx\nxx\nxx\nxx\nxx\nxx\nxx\nxx\nxx\n");
        // create a scrollpane, givin it the textarea as a constructor argument
    	      	      	  
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(50, 450));
        /*scrollPane.setBorder(BorderFactory.createCompoundBorder(
					            BorderFactory.createCompoundBorder(
					                            BorderFactory.createTitledBorder("Plain Text"),
					                            BorderFactory.createEmptyBorder(255,20,20,20)),
					            scrollPane.getBorder()));*/
        //scrollPane.setBorder(BorderFactory.createEmptyBorder(350,20,20,20));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(__marginTop, _marginRight, _marginBottom, _marginLeft));

        
        
        //JFrame frame = new JFrame("Browsy");
        frame.getContentPane().add(scrollPane, _loc);
        /*frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // set the frame size (you'll usually want to call frame.pack())
        frame.setSize(new Dimension(840, 680));      
        // center the frame
        frame.setLocationRelativeTo(null);  */   
        frame.setVisible(true);
      }
    });
  }
  public void addFrame(){
	  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      // set the frame size (you'll usually want to call frame.pack())
      frame.setSize(new Dimension(840, 680));      
      // center the frame
      frame.setLocationRelativeTo(null);      
      frame.setVisible(true);
  }
  public void addTextToPane(String str){
	  GuiPane.textArea.append(str);
  }

}