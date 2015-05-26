import processing.core.PImage;
import java.util.List;
import java.util.ArrayList;
import static java.lang.Math.abs;

public abstract class MobileAnimatedActor
    extends AnimatedActor
{
    private List<Point> path;
    private List<Point> checked;

    public MobileAnimatedActor(String name, Point position, int rate,
        int animation_rate, List<PImage> imgs)
    {
        super(name, position, rate, animation_rate, imgs);
        this.path = new ArrayList<>();
        this.checked = new ArrayList<>();
        this.checked.add(new Point(1, 1));
        this.path.add(new Point(1, 1));
        this.path.add(new Point(1, 2));
    }

    public List<Point> get_path()
	{
        return this.path;
	}

    public List<Point> get_checked()
	{
        return this.checked;
	}

    public void add_to_path(Point p)
	{
        this.path.add(p);
	}

    public void add_to_checked(Point p)
	{
        this.checked.add(p);
	}

    protected Point nextPosition(WorldModel world, Point dest_pt)
    {
        int horiz = Integer.signum(dest_pt.x - getPosition().x);
        Point new_pt = new Point(getPosition().x + horiz, getPosition().y);

        if (horiz == 0 || !canPassThrough(world, new_pt))
        {
            int vert = Integer.signum(dest_pt.y - getPosition().y);
            new_pt = new Point(getPosition().x, getPosition().y + vert);

            if (vert == 0 || !canPassThrough(world, new_pt))
            {
                new_pt = getPosition();
            }
        }

        return new_pt;
    }

    protected static boolean adjacent(Point p1, Point p2)
    {
        return (p1.x == p2.x && abs(p1.y - p2.y) == 1) ||
            (p1.y == p2.y && abs(p1.x - p2.x) == 1);
    }

    protected abstract boolean canPassThrough(WorldModel world, Point new_pt);
}
