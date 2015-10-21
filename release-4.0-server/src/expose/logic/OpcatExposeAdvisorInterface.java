package expose.logic;

import gui.projectStructure.ConnectionEdgeInstance;
import gui.projectStructure.Instance;

import java.util.ArrayList;

interface OpcatExposeAdvisorInterface {

	public ArrayList<OpcatExposeAdvice> getAdvices();

	public void changeSourceInstance(ConnectionEdgeInstance newSourceInstance);
}
