package extensionTools.opcatLayoutManager;

import exportedAPI.*;
import exportedAPI.opcatAPIx.*;

import javax.swing.*;

public class LayoutManagerExtensionTool implements OpcatExtensionToolX
{
        public LayoutManagerExtensionTool()
        {
        }
        public String getName()
        {
                return "Layout Manager";
        }
        public JPanel getAboutBox()
        {
                return new LayoutManagerExtensionToolAbout();
        }
        public String getHelpURL()
        {
                return null;
        }
        public JPanel execute(IXSystem opcatSystem)
        {
                return new GUIPanel(opcatSystem);
        }
}

