package Model;

import java.util.Comparator;

public class TapDistanceComparator implements Comparator{

	 public int compare(Object arg0, Object arg1) {
		 
		TapAssistDataEntry entry = (TapAssistDataEntry)arg0;

		TapAssistDataEntry entry2 = (TapAssistDataEntry)arg1;
		
	    float result = entry.getDistance() - entry2.getDistance();
	    
	    if(result >0) return 1;
	    else if(result < 0) return -1;
	    else return 0;
	 }
	 
	}