package visualiser.leap.event;

public interface LeapListener {	
	public void update(LeapEvent event);
	public void statusChanged(LeapEvent event);
}