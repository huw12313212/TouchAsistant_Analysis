package Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class TapAssistAnalysis {
	
	public static int TapDistIndexX = 28;
	public static int TapDistIndexY = 29;
	
	public static int ScrollDistIndexX = 16;
	public static int ScrollDistIndexY = 17;
	
	public static int TapMaxV = 28;
	public static int TapMinV = 29;
	
	public static int ScrollMaxV = 18;
	public static int ScrollMinv = 19;
	
	public static int ScrollPath = 23;
	public static int ZERO = 0;
	
	public static int TargetDistXIndex = 7;
	public static int TargetDistYIndex = 8;
	
	private ArrayList<TapAssistDataEntry> tapList;
	private ArrayList<TapAssistDataEntry> scrollList;
	private ArrayList<TapAssistDataEntry> scrollListYOnly;
	private ArrayList<TapAssistDataEntry> scrollPathList;
	
	

	
	private ArrayList<Float> tapCDF;
	private ArrayList<Float> scrollCDF;
	private ArrayList<Float> scrollListYOnlyCDF;
	private ArrayList<Float> scrollPathListCDF;
	
	private ArrayList<Float> tapPDF;
	private ArrayList<Float> scrollPDF;
	private ArrayList<Float> scrollListYOnlyPDF;
	private ArrayList<Float> scrollPathListPDF;

	private ArrayList<Float> tapPDFWithCount;
	private ArrayList<Float> scrollPDFWithCount;
	private ArrayList<Float> scrollListYOnlyPDFWithCount;
	private ArrayList<Float> scrollPathListPDFWithCount;
	
	
	
	private ArrayList<TapAssistDataEntry> tapDifList;
	private ArrayList<Float> tapDifCDF;
	private ArrayList<Float> tapDifPDF;
	private ArrayList<Float> tapDifPDFWithCount;
	
	
	
	private ArrayList<TapAssistDataEntry> ScrollSpeedMaxList;
	private ArrayList<TapAssistDataEntry> TapSpeedMaxList;
	
	private ArrayList<Float> ScrollSpeedCDF;
	private ArrayList<Float> TapSpeedCDF;
	
	private ArrayList<Float> ScrollSpeedPDF;
	private ArrayList<Float> TapSpeedPDF;
	
	
	public TapAssistAnalysis(File TapFile,File ScrollFile)
	{
		//CDF,PDF,PDF with Count
		tapList = LoadFile(TapFile,TapAssistDataEntry.DistancePolicy.Dist,TapDistIndexX,TapDistIndexY);
		scrollList = LoadFile(ScrollFile,TapAssistDataEntry.DistancePolicy.Dist,ScrollDistIndexX,ScrollDistIndexY);
		scrollListYOnly = LoadFile(ScrollFile,TapAssistDataEntry.DistancePolicy.YOnly,ScrollDistIndexX,ScrollDistIndexY);
		tapDifList = LoadFile(TapFile,TapAssistDataEntry.DistancePolicy.Dist,TargetDistXIndex,TargetDistYIndex); 
		scrollPathList = LoadFile(ScrollFile,TapAssistDataEntry.DistancePolicy.XOnly,ScrollPath,ZERO); 
		

		
		Collections.sort(tapList, new TapDistanceComparator());
		Collections.sort(scrollList, new TapDistanceComparator());
		Collections.sort(scrollListYOnly, new TapDistanceComparator());
		Collections.sort(scrollPathList, new TapDistanceComparator());
		
		
		
		float RangeMax = GetMaximum();
		
		
		tapCDF = GetCDF(tapList,RangeMax);
		scrollCDF = GetCDF(scrollList,RangeMax);
		scrollListYOnlyCDF = GetCDF(scrollListYOnly,RangeMax);
		scrollPathListCDF = GetCDF(scrollPathList,RangeMax);
		
		
		
		tapPDF = GetPDF(tapList,RangeMax,true);
		scrollPDF = GetPDF(scrollList,RangeMax,true);
		scrollListYOnlyPDF = GetPDF(scrollListYOnly,RangeMax,true);
		scrollPathListPDF = GetPDF(scrollPathList,RangeMax,true);
		
		
		tapPDFWithCount = GetPDF(tapList,RangeMax,false);
		scrollPDFWithCount = GetPDF(scrollList,RangeMax,false);
		scrollListYOnlyPDFWithCount = GetPDF(scrollListYOnly,RangeMax,false);
		scrollPathListPDFWithCount = GetPDF(scrollPathList,RangeMax,false);
		
		//CDF,PDF,PDF with Count
		
		//EXTRA
		Collections.sort(tapDifList, new TapDistanceComparator());
		float TapDifMax = tapDifList.get(tapDifList.size()-1).getDistance();
		tapDifCDF = GetCDF(tapDifList,TapDifMax);
		tapDifPDF = GetPDF(tapDifList,TapDifMax,true);
		tapDifPDFWithCount = GetPDF(tapDifList,TapDifMax,false);
		//EXTRA
		
		/*
		for(int i = 0 ; i < tapCDF.size();i++)
		{
			System.out.println(i+":"+tapCDF.get(i));
		}*/
	
		/*
		for(int i = 0 ; i < scrollList.size();i++)
		{
			System.out.println(i+":"+scrollList.get(i));
		}*/
		/*
		
		for(int i = 0 ; i < scrollListYOnlyCDF.size();i++)
		{
			System.out.println(i+":"+scrollListYOnlyCDF.get(i));
		}*/
		
		/*
		for(int i = 0 ; i < scrollPathList.size();i++)
		{
			System.out.println(i+":"+scrollPathList.get(i));
		}
		
		*/
		
		ScrollSpeedMaxList = LoadFile(ScrollFile,TapAssistDataEntry.DistancePolicy.XOnly,ScrollMaxV,ScrollMinv);
		TapSpeedMaxList = LoadFile(TapFile,TapAssistDataEntry.DistancePolicy.XOnly,TapDistIndexX,TapDistIndexY);
		
		Collections.sort(ScrollSpeedMaxList, new TapDistanceComparator());
		Collections.sort(TapSpeedMaxList, new TapDistanceComparator());
		
		for(int i = 0 ; i < TapSpeedMaxList.size();i++)
		{
			TapSpeedMaxList.get(i).DistX *= 100;
		}
		
		float max = TapSpeedMaxList.get(TapSpeedMaxList.size()-1).getDistance();
		float temp2 = ScrollSpeedMaxList.get(ScrollSpeedMaxList.size()-1).getDistance();
		
		if(max<temp2)max = temp2;
		
		TapSpeedCDF = GetCDF(TapSpeedMaxList,max);
		ScrollSpeedCDF = GetCDF(ScrollSpeedMaxList,max);
		
		TapSpeedPDF = GetPDF(TapSpeedMaxList,max,false);
		ScrollSpeedPDF = GetPDF(ScrollSpeedMaxList,max,false);
		
		//printAll();
	}
	
	public ArrayList<Float> CDFtoPDF(ArrayList<Float> CDF)
	{
		ArrayList<Float> PDF = new ArrayList<Float>();
		
		PDF.add(CDF.get(0));
		
		for(int i=1;i<CDF.size();i++)
		{
			PDF.add(CDF.get(i)-CDF.get(i-1));
		}
		
		
		
		return PDF;
	}
	
	public void SaveOutFile(File f) 
	{
		try
		{
		FileWriter fw = new FileWriter(f);
		
		fw.write("CDF,,,,,,PDF,,,,,,PDFWithCount,,,,,,TapDist:,CDF,PDF,PDFWithCount\n");
	    fw.write("�Z��,tap,scroll,scrollY,scrollPath,,�Z��,tap,scroll,scrollY,scrollPath,,�Z��,tap("+tapList.size()+"),scroll("+scrollList.size()+"),scrollY("+scrollList.size()+"),scrollPath("+scrollPathList.size()+"),,�Z��,tap,tap,tap("+tapDifList.size()+")\n");
	    //fw.write(",,,,,,,,,,,"+tapList.size()+","+scrollList.size()+","+scrollList.size()+"\n");
	    int i;
	    
		for(i = 0; i < tapCDF.size();i++)
		{
			
				String line = i+","+tapCDF.get(i)+","+scrollCDF.get(i)+","+scrollListYOnlyCDF.get(i)+","+scrollPathListCDF.get(i)+",,"+
				i+","+tapPDF.get(i)+","+scrollPDF.get(i)+","+scrollListYOnlyPDF.get(i)+","+scrollPathListPDF.get(i)+",,"+
				i+","+tapPDFWithCount.get(i)+","+scrollPDFWithCount.get(i)+","+scrollListYOnlyPDFWithCount.get(i)+","+scrollPathListPDFWithCount.get(i)+",,";
		
				if(i<tapDifCDF.size())
				{
					line += i+","+tapDifCDF.get(i)+","+tapDifPDF.get(i)+","+tapDifPDFWithCount.get(i)+",\n";
				}
				else
				{
					line += "\n";
				}
		
		fw.write(line);
		}
		
		for(;i<tapDifCDF.size();i++)
		{
			String line = ",,,,,,,,,,,,,,,,,,";
			

			line += i+","+tapDifCDF.get(i)+","+tapDifPDF.get(i)+","+tapDifPDFWithCount.get(i)+",\n";

					
			fw.write(line);
		}
		
		fw.write("\n");
		fw.write("CDF,,,,PDF,\n");
		fw.write("MaxSpeed,Tap,Scroll,,MaxSpeed,Tap,Scroll,\n");
	
		for(int j=0;j<TapSpeedCDF.size();j++)
		{
			String line = "";
			
			line += j+","+TapSpeedCDF.get(j)+","+ScrollSpeedCDF.get(j)+",,";
			line += j+","+TapSpeedPDF.get(j)+","+ScrollSpeedPDF.get(j)+",,\n";
			
			fw.write(line);
		}
		
		fw.close();
		}
		catch(Exception e)
		{
			System.out.println("save error");
		}
	}
	
	public static ArrayList<Float> GetCDF (ArrayList<TapAssistDataEntry> distList,float max)
	{
		ArrayList<Float> CDFList = new ArrayList<Float>();
		
		int nowIndex = 0;
		
		for(int i = 0; i < max ; i++)
		{
			
			while(nowIndex<distList.size()&&distList.get(nowIndex).getDistance()<=i)
			{
				nowIndex++;
			}
			
			CDFList.add(nowIndex/(float)distList.size());
			
		}
		
		CDFList.add(1.0f);
		return CDFList;

	}
	
	
	public ArrayList<Float> GetPDF (ArrayList<TapAssistDataEntry> distList,float max,boolean percentage)
	{
		ArrayList<Float> PDFList = new ArrayList<Float>();
		
		int nowIndex = 0;
		
		int lastIndex = 0;
		
		
		for(int i = 0; i < max ; i++)
		{
			
			while(nowIndex<distList.size()&&distList.get(nowIndex).getDistance()<=i)
			{
				nowIndex++;
			}
			
			
			
			if(percentage)
			{
				PDFList.add((nowIndex-lastIndex)/(float)distList.size());
			}
			else
			{
				PDFList.add((float)(nowIndex-lastIndex));
			}
			
			lastIndex = nowIndex;
			
		}
		
		
		if(percentage)
		{
			PDFList.add((distList.size()-lastIndex)/(float)distList.size());
		}
		else
		{
			PDFList.add((float)(distList.size()-lastIndex));
		}
		
		return PDFList;

	}
	
	
	
	public float GetMaximum()
	{
		float result = tapList.get(tapList.size()-1).getDistance();
		
		float temp = 0;
		
		temp = scrollList.get(scrollList.size()-1).getDistance();
		
		if(temp>result)result = temp;
		
		temp = scrollListYOnly.get(scrollListYOnly.size()-1).getDistance();
		
		if(temp>result)result = temp;
		
		temp = scrollPathList.get(scrollPathList.size()-1).getDistance();
		
		if(temp>result)result = temp;
		
		return result;
	}
	
	public void printAll()
	{
		
		for(TapAssistDataEntry entry : tapList)
		{
			System.out.println(entry.toString());
		}
		System.out.println("tap data length:"+tapList.size());
		
		
		for(TapAssistDataEntry entry : scrollList)
		{
			System.out.println(entry.toString());
		}
		System.out.println("scroll data length:"+scrollList.size());
		
		for(TapAssistDataEntry entry : scrollListYOnly)
		{
			System.out.println(entry.toString());
		}
		System.out.println("scrollY data length:"+scrollListYOnly.size());	
	}
	
	

	

	static public ArrayList<TapAssistDataEntry> LoadFile(File file,TapAssistDataEntry.DistancePolicy policy,int indexX,int indexY) {
		
		ArrayList<TapAssistDataEntry> data = new ArrayList<TapAssistDataEntry>();
		
		try
		{
		
		 BufferedReader reader = new BufferedReader(new FileReader(file));
		 String headLine = reader.readLine();
		 String line = reader.readLine();
		 
		 while(line != null)
		 {
			 String[] strArray = line.split(",");
			 
			 
			 if(strArray[indexX]!=null&&strArray[indexX].length()>0)
			 {
				 float distX = Float.valueOf(strArray[indexX]);
				 float distY = Float.valueOf(strArray[indexY]);
				 
				 data.add(new TapAssistDataEntry(distX,distY,policy));
			 }
		
			 line = reader.readLine();
		 } 
		 
		 reader.close();
		 
		}
		catch(Exception e)
		{
			System.out.println("can NOT read file");
		}
		
		return data;
	}
	

}
