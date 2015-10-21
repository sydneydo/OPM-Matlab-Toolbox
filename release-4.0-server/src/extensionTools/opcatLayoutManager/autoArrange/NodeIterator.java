
package extensionTools.opcatLayoutManager.autoArrange;

import java.util.*;

import javax.swing.JComponent;

/**
 * Class that implements the iterator on OPD nodes
 */
public class NodeIterator implements Iterator
{
    Iterator generalIterator = null;
    
    Object curElement = null;
        
    public NodeIterator(Iterator iterator)
    {
        generalIterator = iterator;
    }

    public boolean hasNext()
    {
        if (curElement != null)
            return true;
        
        while (generalIterator.hasNext()) {
            Object o = generalIterator.next();
            if (o instanceof JComponent) {
                curElement = o;
                return true;
            }
        }
        
        return false;
    }

    public Object next()
    {
        if (curElement != null) {
            Object o = curElement;
            curElement = null;
            return o;
        }
        
        while (generalIterator.hasNext()) {
            Object o = generalIterator.next();
            if (o instanceof JComponent)
                return o;
        }
        
        throw new NoSuchElementException();
    }
    
    public void remove()
    {
        throw new UnsupportedOperationException();
    }
}
