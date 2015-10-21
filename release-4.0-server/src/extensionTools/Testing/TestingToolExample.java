package extensionTools.Testing;

import javax.swing.JPanel;
import exportedAPI.OpcatExtensionToolX;
import exportedAPI.opcatAPIx.IXSystem;

public class TestingToolExample implements OpcatExtensionToolX {
	public TestingToolExample() {
	}

	public String getName() {
		return "ET Testing Example";
	}

	public JPanel getAboutBox() {
		return new TestingToolExampleAbout();
	}

	public String getHelpURL() {
		return null;
	}

	public JPanel execute(IXSystem opcatSystem) {
		return new TestingGUIPanel(opcatSystem);
	}

}
