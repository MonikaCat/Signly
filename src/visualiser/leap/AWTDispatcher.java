package visualiser.leap;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import visualiser.leap.event.PointDraggListener;
import visualiser.leap.event.PointEvent;
import visualiser.leap.event.PointEvent.Zone;
import visualiser.leap.event.PointMotionListener;
import visualiser.leap.event.PointZoneListener;

//TODO: Use Robot instead?
public class AWTDispatcher extends WindowAdapter implements PointZoneListener,
		PointMotionListener, PointDraggListener {
	private Window window;
	
	private void dispatchMouseEvent(PointEvent event, int id) {
		dispatchMouseEvent(event, id, 0, 0);
	}

	private void dispatchMouseEvent(PointEvent event, int id, int modifiers,
			int button) {
		if (window != null) {
			Component component = window.findComponentAt((int) event.getX(),
					(int) event.getY());
			if (component != null) {
				window.dispatchEvent(new MouseEvent(component, id, System
						.currentTimeMillis(), modifiers, (int) event.getX(),
						(int) event.getY(), 0, false, button));
			}
		}
	}

	@Override
	public void enteredViewoport(PointEvent event) {}
	
	@Override
	public void moved(PointEvent event) {
		dispatchMouseEvent(event, MouseEvent.MOUSE_MOVED);
	}
	
	@Override
	public void leftViewport(PointEvent event) {}

	@Override
	public void pointDragged(PointEvent event) {
		dispatchMouseEvent(event, MouseEvent.MOUSE_DRAGGED);
	}

	@Override
	public void zoneChanged(PointEvent event) {
		if (event.isInZone(Zone.BACK)) {
			dispatchMouseEvent(event, MouseEvent.MOUSE_PRESSED,
					MouseEvent.BUTTON1_DOWN_MASK, MouseEvent.BUTTON1);
		} else if ((event.leftViewPort() && event.wasInClickZone()) ||
				(event.wasInClickZone() && !event.isInClickZone())) {
			dispatchMouseEvent(event, MouseEvent.MOUSE_RELEASED,
					MouseEvent.BUTTON1_DOWN_MASK, MouseEvent.BUTTON1);
			dispatchMouseEvent(event, MouseEvent.MOUSE_CLICKED,
					MouseEvent.BUTTON1_DOWN_MASK, MouseEvent.BUTTON1);
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
		window = e.getWindow();
	}

	
	@Override
	public void windowClosing(WindowEvent e) {
		LeapApp.destroy();
	}
}
