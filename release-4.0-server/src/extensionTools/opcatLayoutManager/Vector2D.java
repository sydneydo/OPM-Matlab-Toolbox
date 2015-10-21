package extensionTools.opcatLayoutManager;

/**
 * Represents a two-dimensional vector.
 */
public class Vector2D
{
    /**
     * Initializes an instance of the class.
     */
    public Vector2D()
    {
        this(0, 0);
    }

    /**
     * Initializes an instance of the class as a copy of a given instance.
     *
     * @param vector The instance to create a copy of.
     */
    public Vector2D(Vector2D vector)
    {
        this(vector.x, vector.y);
    }

    /**
     * Initializes an instance of the class connecting two points.
     *
     * @param x1 The x coordinate of the start point.
     * @param y1 The y coordinate of the start point.
     * @param x2 The x coordinate of the end point.
     * @param y2 The y coordinate of the end point.
     */
    public Vector2D(double x1, double y1, double x2, double y2)
    {
        this(x2 - x1, y2 - y1);
    }

    /**
     * Initializes an instance of the class with given coordinates.
     *
     * @param x The x coordinate of the vector.
     * @param y The y coordinate of the vector.
     */
    public Vector2D(double _x, double _y)
    {
        this.x = _x;
        this.y = _y;
    }

    /**
     * Returns a string representation of the object.
     * @see java.lang.Object#toString()
     *
     * @return a string representation of the object
     */
    public String toString()
    {
        return "(" + this.x + ", " + this.y + ")";
    }

    /**
     * Returns the vector module.
     *
     * @return the vector module.
     */
    public double GetModule()
    {
        return Math.sqrt(this.GetModuleSquare());
    }

    /**
     * Returns the square of the vector module.
     *
     * @return the square of the vector module.
     */
    public double GetModuleSquare()
    {
        return Util.Square(this.x) + Util.Square(this.y);
    }

    /**
     * Increases this vector by the amount specified by a given vector.
     *
     * @param vector Specifies the amount to add to this vector.
     *
     * @return This (updated) vector.
     */
    public Vector2D Add (Vector2D vector)
    {
        this.x += vector.x;
        this.y += vector.y;
        return this;
    }

    /**
     * Multiples this vector by a given scalar.
     *
     * @param d the scalar
     *
     * @return This (updated) vector.
     */
    public Vector2D Multiply (double d)
    {
        this.x *= d;
        this.y *= d;
        return this;
    }

    /**
     * Calculates the sum of two vectors.
     *
     * @param vector1 the first vector
     * @param vector2 the second vector
     *
     * @return the sum of two vectors
     */
    public static Vector2D Add(Vector2D vector1, Vector2D vector2)
    {
        Vector2D result = new Vector2D(vector1);
        return result.Add(vector2);
    }

    public double x;
    public double y;
}
