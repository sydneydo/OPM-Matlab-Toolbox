package user;

import gui.util.OpcatLogger;
import modelControl.OpcatMCManager;

import org.jdesktop.swingx.auth.LoginService;

public class OpcatLoginService extends LoginService {

	@Override
	public boolean authenticate(String name, char[] password, String server)
			throws Exception {
		try {
			OpcatMCManager.authenticate(name, new String(password));
		} catch (Exception ex) {
			// OpcatLogger.logError(ex);
			OpcatLogger.logError("While logging into : "
					+ OpcatMCManager.server, false);
			OpcatLogger.logError(ex, false);
			return false;
		}
		return true;
	}

}
