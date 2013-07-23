package JFrame;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Model.TapAssistAnalysis;
//notice javax
public class Frame1 extends JFrame
{
	//public static String TapFile = "resource/M5_tap.csv";
	//public static String ScrollFile = "resource/M5_scroll.csv";
	
	public static String TapFile = "";
	public static String ScrollFile = "";
	
	
  JPanel pane = new JPanel();
  
  public Frame1() // the frame constructor method
  {
    super("資料分析小工具"); setBounds(100,100,600,100);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container con = this.getContentPane(); // inherit main frame
    con.add(pane); // add the panel to frame
    // customize panel here
    // pane.add(someWidget);
   
    
    
    {
	    final JButton pressme = new JButton("select Tap File");
	    pane.add(pressme);
	    
	    pressme.addActionListener(new ActionListener()
	    {
	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				
				// TODO Auto-generated method stub
				  JFileChooser fileChooser = new JFileChooser();
				  fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				  int result = fileChooser.showOpenDialog(null);
				  if (result == JFileChooser.APPROVE_OPTION) {
					  File selectedFile = fileChooser.getSelectedFile();
			      	//System.out.println("Selected file: " + selectedFile.getAbsolutePath());
					  pressme.setText(selectedFile.getAbsolutePath());
					  
					  TapFile = selectedFile.getAbsolutePath();
				  }
				  
				  
			}
	    	
	    });
    }
    
    
    {
	    final JButton pressme2 = new JButton("select Scroll File");
	    pressme2.setLocation(0,100);
	    pane.add(pressme2);
	    
	    pressme2.addActionListener(new ActionListener()
	    {
	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				
				// TODO Auto-generated method stub
				  JFileChooser fileChooser = new JFileChooser();
				  fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				  int result = fileChooser.showOpenDialog(null);
				  if (result == JFileChooser.APPROVE_OPTION) {
					  File selectedFile = fileChooser.getSelectedFile();
			      	//System.out.println("Selected file: " + selectedFile.getAbsolutePath());
					  pressme2.setText(selectedFile.getAbsolutePath());
					  
					  ScrollFile = selectedFile.getAbsolutePath();
				  }
				  
				  
			}
	    	
	    });
    }
    
    
    {
	    final JButton pressme3 = new JButton("Start");
	    pressme3.setLocation(0,100);
	    pane.add(pressme3);
	    
	    pressme3.addActionListener(new ActionListener()
	    {
	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				
				// TODO Auto-generated method stub
				  JFileChooser fileChooser = new JFileChooser();
				  //fileChooser.setCurrentDirectory(new File("test.csv"));
				  fileChooser.setSelectedFile(new File("test.csv"));
				  int result = fileChooser.showSaveDialog(null);
				  if (result == JFileChooser.APPROVE_OPTION) {
					  File selectedFile = fileChooser.getSelectedFile();
					  
					  
					  if(TapFile.contains(".csv")&&ScrollFile.contains(".csv"))
					  {
						  
						  File tapFile = new File(TapFile);
						  File scrollFile = new File(ScrollFile);
						  
						  TapAssistAnalysis analysis = new TapAssistAnalysis(tapFile,scrollFile);
						  analysis.SaveOutFile(selectedFile);
						  
					  }
					  else
					  {
						  pressme3.setText("all File should be (.csv) file");
					  }
			      	
				  }
				  
				  
			}
	    	
	    });
    }

    
    
    setVisible(true); // display this frame
    
  }

}

