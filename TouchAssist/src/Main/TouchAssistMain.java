package Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFileChooser;

import Model.TapAssistAnalysis;
import Model.TapAssistDataEntry;
import Model.TapDistanceComparator;

public class TouchAssistMain {
	
	
	
	
	  public static void main(String args[]) 
	  {
		  JFileChooser fileChooser = new JFileChooser();
		  //fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		  int result = fileChooser.showOpenDialog(null);
		  if (result == JFileChooser.APPROVE_OPTION) {
			  File selectedFile = fileChooser.getSelectedFile();
			  
			  String name = selectedFile.getParentFile().getAbsolutePath() + "/CDF_result.csv";
	      	
			  ArrayList<TapAssistDataEntry> list = TapAssistAnalysis.LoadFile(selectedFile, TapAssistDataEntry.DistancePolicy.Dist, 26, 27);
			  
			  
			  Collections.sort(list, new TapDistanceComparator());
			  
			  for(int i = 0 ; i < list.size();i++)
			  {
				 System.out.println(list.get(i).toString());
			  }
			 
			  float max = list.get(list.size()-1).getDistance();
			  
			  ArrayList<Float> CDFList = TapAssistAnalysis.GetCDF(list, max);
			  
			  System.out.println(name);
			  
			  File file = new File(name);
			  try {
				FileWriter fw = new FileWriter(file);
				
				fw.write("Dist,%\n");
				
				for(int i = 0; i < CDFList.size();i++)
				{
					fw.write(i+","+CDFList.get(i)+"\n");
				}
				
				fw.close();
			  } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			  }
		  }
		
		  
		 // Frame1 frame = new Frame1();
		  
		  
		  
		 
		  
	  }
}
