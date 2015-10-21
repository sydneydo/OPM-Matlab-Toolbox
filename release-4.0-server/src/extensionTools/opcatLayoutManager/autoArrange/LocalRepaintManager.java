
package extensionTools.opcatLayoutManager.autoArrange;

import javax.swing.JComponent;
import javax.swing.RepaintManager;

import gui.opdGraphics.DrawingArea;


/**
 * This class overrides the javax.swing.RepaintManager to take care
 * of "painting" of the screen in the way that will be the most 
 * efficient for this specific application.
 * 
 * Normally, each component is repainted once updated. In our case,
 * because updating of many components is involved, it makes sense
 * to postpone painting of all of them until the whole system has
 * been updated.
 * 
 */
public class LocalRepaintManager extends RepaintManager
{
    private DrawingArea drawingArea = null;
    
    /**
     * The constructor - saves off the DrawingArea, which will later 
     * be repainted in whole.
     * @param drawingArea - the reference to the DrawingArea
     */
    public LocalRepaintManager(DrawingArea drawingArea)
    {
        this.drawingArea = drawingArea;
    }

    /**
     * In the default RepaintManager, this function saves the component
     * for repainting. Because we are going to repaint the whole DrawingArea
     * at the very end of each step, this function will do nothing.
     */
    public synchronized void addDirtyRegion(JComponent c, int x, int y, int w, int h)
    {
        //do nothing
    }

    /**
     * Now, in this function, that will be called manually at the end
     * of each operation, we repaint the whole drawing area.
     */
    public void paintAll()
    {
        super.addDirtyRegion(drawingArea, drawingArea.getX(), drawingArea.getY(),
                drawingArea.getWidth(), drawingArea.getHeight());
        super.paintDirtyRegions();
    }
}
