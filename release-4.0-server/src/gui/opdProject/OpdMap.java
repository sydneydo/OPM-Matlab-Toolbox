package gui.opdProject;


import java.util.ArrayList;

public class OpdMap {

	private static ArrayList opds = new ArrayList();

	private static int currentPlace = -1;

	public OpdMap() {

	}

	public static long getBackOpd() {
		if (currentPlace > 0)
			currentPlace--;

		if (currentPlace >= 0) {
			return ((Long) opds.get(currentPlace)).longValue();
		} else {
			return -1;
		}
	}

	public static long getForwordOpd() {
		if (currentPlace < opds.size() - 1)
			currentPlace++;
		if ((currentPlace <= opds.size() - 1) && (currentPlace >= 0)) {
			return ((Long) opds.get(currentPlace)).longValue();
		} else {
			return -1;
		}
	}

	public static void UpdateOpdMap(Opd opd) {
		if (opds.size() > 0) {
			if (opd.getOpdId() == ((Long) opds.get(opds.size() - 1))
					.longValue()) {
				return;
			}
		}
		opds.add(new Long(opd.getOpdId()));
		currentPlace = opds.size() - 1;

	}

}
