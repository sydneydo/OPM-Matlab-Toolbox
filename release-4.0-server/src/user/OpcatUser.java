package user;

import gui.Opcat2;
import gui.images.misc.MiscImages;
import gui.util.OpcatLogger;

import modelControl.OpcatMCManager;

import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.auth.LoginService;

public class OpcatUser {

	private String name = null;
	private String password = null;
	private static OpcatUser user = null;

	private OpcatUserStatus myStatus;

	private OpcatUser() {
		super();
		myStatus = new OpcatUserStatus(OpcatUserStatus.NOT_TRIED);
	}

	public static OpcatUser getCurrentUser() {
		if ((user == null)) {
			user = new OpcatUser();
		}
		return user;
	}

	public static void removeCurrentUser() {
		user = null;
	}

	private boolean tryLogin() {
		return ((myStatus.myStatus == OpcatUserStatus.NOT_TRIED)
				|| (myStatus.myStatus == OpcatUserStatus.LOGIN_FAIL) || (myStatus.myStatus == OpcatUserStatus.LOGIN_CANCEL));
	}

	public String getName() {
		if (tryLogin()) {
			myStatus = new OpcatUserStatus(login());
		}
		return name;
	}

	public OpcatUserStatus getCurrentStatus() {
		return myStatus;

	}

	public String getDecodedPassword() {
		if (tryLogin()) {
			myStatus = new OpcatUserStatus(login());
		}
		return password;
	}

	private int login() {

		try {

			name = "not.logged.in";
			password = "not.logged.in";

			if (!OpcatMCManager.isOnline()) {
				return OpcatUserStatus.NOT_ONLINE;
			}

			LoginService svc = new OpcatLoginService();

			JXLoginPane login = new JXLoginPane();

			login.setLoginService(svc);
			login.setBanner(MiscImages.SPLASH.getImage());

			JXLoginPane.Status status = JXLoginPane.showLoginDialog(Opcat2
					.getFrame(), login);

			if (status == JXLoginPane.Status.SUCCEEDED) {
				name = login.getUserName();
				password = new String(login.getPassword());
				return OpcatUserStatus.LOGIN_OK;
			} else if (status == JXLoginPane.Status.CANCELLED) {
				return OpcatUserStatus.LOGIN_CANCEL;
			}

			return OpcatUserStatus.LOGIN_FAIL;

		} catch (Exception ex) {
			OpcatLogger.logError(ex);
		}

		return OpcatUserStatus.LOGIN_FAIL;

	}

	public class OpcatUserStatus {
		public final static int LOGIN_OK = 0;
		public final static int LOGIN_FAIL = 1;
		public final static int NOT_ONLINE = 2;
		public final static int NOT_TRIED = 3;
		public final static int LOGIN_CANCEL = 4;

		int myStatus;

		public int currentStatus() {
			return myStatus;
		}

		public OpcatUserStatus(int status) {
			myStatus = status;
		}
	}

}
