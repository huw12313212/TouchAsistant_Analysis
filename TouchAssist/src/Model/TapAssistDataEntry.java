package Model;

public class TapAssistDataEntry {
	
	
	public enum DistancePolicy
	{
		XOnly,
		YOnly,
		Dist,
	}
	public DistancePolicy _distancePolicy;
	
	public float DistX;
	public float DistY;
	
	public TapAssistDataEntry(float distX,float distY,DistancePolicy distancePolicy)
	{
		this.DistX = distX;
		this.DistY = distY;

		_distancePolicy = distancePolicy;
	}
	
	
	
	@Override 
	public String toString()
	{
		String data ="";
		
		data += "mode("+_distancePolicy+") ";
		
		data += "distXY("+DistX+","+DistY+") ";
		
		
		data += "dist("+this.getDistance()+")";
		
		
		return data;
	}
	
	
	
	public float getDistance()
	{
		float distance = 0;
		
		switch(_distancePolicy)
		{
		case XOnly:
			
			float difXOnly = DistX;
			distance = Math.abs(difXOnly);
			break;
		case YOnly:
			

			float difYOnly = DistY;
			distance = Math.abs(difYOnly);
			break;
			
		case Dist:
			
			distance = (float)Math.sqrt(((DistX*DistX)+(DistY*DistY)));
			break;
		}
		
		return distance;
	}
	
	
	
}
