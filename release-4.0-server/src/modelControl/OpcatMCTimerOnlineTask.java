package modelControl;

import java.net.InetAddress;
import java.util.TimerTask;

public class OpcatMCTimerOnlineTask extends TimerTask {

	@Override
	public void run() {
		try {
			InetAddress.getByName(OpcatMCManager.server);
			OpcatMCManager.setOnline(true);
		} catch (Exception ex) {
			OpcatMCManager.setOnline(false);
		}
	}
}
