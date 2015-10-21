package extensionTools.opcatLayoutManager;

import java.text.MessageFormat;
import exportedAPI.opcatAPIx.*;

/**
 * Encapsulates OPCAT edge classes with some additional layout information.
 */
public class EdgeContext
{
    /**
     * Initializes an instance of the class.
     *
     * @param edge An instance of an OPCAT edge class.
     * @param node1 The first node of this edge.
     * @param node2 The second node of this edge.
     */
    public EdgeContext(IXLine edge, NodeContext node1, NodeContext node2)
    {
        this.m_edge  = edge;
        this.m_node1 = node1;
        this.m_node2 = node2;
    }

    /**
     * Returns the name of the OPCAT edge instance.
     *
     * @return the name of the OPCAT edge instance.
     */
    public String GetName()
    {
        if (this.m_edge instanceof IXInstance)
        {
          return ((IXInstance)this.m_edge).getIXEntry().getName();
        }
        else
        {
            return "?";
        }
    }

    /**
     * Returns a string representation of the object.
     * @see java.lang.Object#toString()
     *
     * @return a string representation of the object
     */
    public String toString()
    {
        return MessageFormat.format( "[{0} - {1}]",
                                     new Object[] {this.m_node1, this.m_node2});
    }

    /**
     * Returns the first node.
     *
     * @return the first node
     */
    public NodeContext GetNode1()
    {
        return this.m_node1;
    }

    /**
     * Returns the second node.
     *
     * @return the second node
     */
    public NodeContext GetNode2()
    {
        return this.m_node2;
    }

    /**
     * Returns the line, corresponding to the edge.
     *
     * @return the line, corresponding to the edge.
     */
    public Line GetLine()
    {

        //return new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        return new Line( this.m_node1.GetCenterX(),
                         this.m_node1.GetCenterY(),
                         this.m_node2.GetCenterX(),
                         this.m_node2.GetCenterY());
    }

    /**
     * Restores the auto-arrange for the OPCAT edge instance.
     */
    public void RestoreAutoArrange()
    {
      this.m_edge.makeStraight();
      this.m_edge.setAutoArranged(true);
    }

    private IXLine      m_edge;
    private NodeContext m_node1;
    private NodeContext m_node2;
}