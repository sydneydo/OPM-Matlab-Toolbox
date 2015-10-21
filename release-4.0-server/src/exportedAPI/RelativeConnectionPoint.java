package exportedAPI;

/**
 * A point representing a relative location on some graphical object.
 * A point composed from two slots - side and param.
 * Size is one of constants defined in OpcatConstants interface :
 * N_BORDER, S_BORDER, W_BORDER or E_BORDER.
 * Param is float number between 0 and 1 that defines a relative location of this point
 * In order to get absolute point of (in coordinates of above graphical object)
 * you should use getAbsoluteConnectionPoint method of IConnectionEdgeInstance or
 * IXConnectionEdgeInstance interfaces.
 * @see exportedAPI.opcatAPI.IConnectionEdgeInstance#getAbsoluteConnectionPoint(RelativeConnectionPoint relPoint)
 * @see exportedAPI.opcatAPIx.IXConnectionEdgeInstance#getAbsoluteConnectionPoint(RelativeConnectionPoint relPoint )
 */

public class RelativeConnectionPoint
{
    int side;
    double param;

    public RelativeConnectionPoint(int side, double param)
    {
        this.side = side;
        this.param = param;
    }

    public int getSide()
    {
        return this.side;
    }

    public double getParam()
    {
        return this.param;
    }

    public void setParam(double newParam)
    {
        this.param = newParam;
    }

    public void setSide(int newSide)
    {
        this.side = newSide;
    }

    public String toString()
    {
        return "RelativeConnectionPoint[side="+this.side+",param="+this.param+"]";
    }

}