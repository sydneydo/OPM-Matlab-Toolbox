package gui.util;

import exportedAPI.OpcatExtensionToolBase;

public class ExtensionToolInfo
{
	private String name;
	private OpcatExtensionToolBase extension;

	ExtensionToolInfo(OpcatExtensionToolBase extension)
	{
		this.extension = extension;
		this.name = extension.getName();
	}

	public String getName()
	{
		return this.name;
	}

    public void setName(String name)
    {
        this.name = name;
    }


    public OpcatExtensionToolBase getExtension()
    {
        return this.extension;
    }

    public void setExtension(OpcatExtensionToolBase extension)
    {
        this.extension = extension;
    }
}