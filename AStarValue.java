import java.util.LinkedList;
import java.util.List;

/**
 * Created by David Ritter offline on 5/25/2015.
 */
public class AStarValue
{
    private int g_value;
    private int h_value;
    private Point prev_point;
    private Point curr_point;

    //initial AStar
    public AStarValue(Point final_point, Point orig_point)
    {
        set_g_value(0);
        set_h_value(final_point,orig_point);

        this.curr_point = orig_point;
        this.prev_point = new Point(-1,-1); //out of bounds point because no previous point
    }

    //regular AStar init
    public AStarValue(Point final_point, Point curr_point, AStarValue previous)
    {
        set_g_value(previous);
        set_h_value(final_point, curr_point);

        this.curr_point = curr_point;
        this.prev_point = previous.get_curr_point();
    }

    public Point get_prev_point()
    {
        return this.prev_point;
    }

    public Point get_curr_point()
    {
        return this.curr_point;
    }

    public int get_f_value()
    {
        return (g_value + h_value);
    }

    public int get_g_value()
    {
        return g_value;
    }

    public int get_h_value()
    {
        return h_value;
    }

    public void set_g_value(int g_value)
    {
        this.g_value = g_value;
    }

    public void set_h_value(int h_value)
    {
        this.h_value = h_value;
    }

    public void set_h_value(Point final_point, Point curr_point)
    {
        this.h_value = Math.abs(curr_point.x - final_point.x) +
                      Math.abs(curr_point.y - final_point.y) - 1;
    }

    public void set_g_value(AStarValue prev_A)
    {
        this.g_value = prev_A.get_g_value() + 1;
    }
}
