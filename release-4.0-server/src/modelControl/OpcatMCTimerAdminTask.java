package modelControl;

import java.util.TimerTask;

public class OpcatMCTimerAdminTask extends TimerTask {

	@Override
	public void run() {
		try {
			OpcatMCManager.getInstance().updateAdmin();
		} catch (Exception ex) {

		}

	}

}
