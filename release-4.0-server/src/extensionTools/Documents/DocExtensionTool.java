package extensionTools.Documents;

import javax.swing.JPanel;

import exportedAPI.OpcatExtensionTool;
import exportedAPI.opcatAPI.ISystem;

public class DocExtensionTool implements OpcatExtensionTool
{
	public DocExtensionTool()
	{
	}
	public String getName()
	{
		return "Doc Generator";
	}
	public JPanel getAboutBox()
	{
		return null;
	}
	public String getHelpURL()
	{
		return null;
	}
	public JPanel execute(ISystem opcatSystem)
	{
		return new DocPanel(opcatSystem);
	}

}

