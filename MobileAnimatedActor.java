import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import static java.lang.Math.abs;
import processing.core.PImage;

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

   public void add_to_checked(Point p) {
      this.checked.add(p);
   }

   protected Point nextPosition(WorldModel world, Point dest_pt)
   {
      List<Point> closed_set = new ArrayList<>();
      OrderedList<Point> open_set = new OrderedList<>();
      AStarValue[][] came_from = new AStarValue[world.getNumRows()][world.getNumCols()];

      //initial point initialization (special)
      AStarValue init_pos = new AStarValue(dest_pt,this.getPosition());
      open_set.insert(init_pos.get_curr_point(),init_pos.get_f_value());
      came_from[this.getPosition().y][this.getPosition().x] = init_pos;

      while(open_set.size() > 0)// && open_set.size() < 100) //while open set is not empty
      {
         Point current = open_set.head().item; //obtain current point at top of open set

         //check if at goal, if so, return path
         if (current.equals(dest_pt))
         {
            List<Point> path = reconstruct_path(dest_pt, came_from); //return the path after the original point
            //System.out.println(path);
            this.path = path;
            this.checked = closed_set;
            return path.get(0); //return the next point
         }

         open_set.pop();   //remove the top node
         closed_set.add(current); //add the current point to closed set (point now has been current)

         for (Point neighbor : neighbor_nodes(current, world,dest_pt))
         {
            if (closed_set.contains(neighbor)){continue;}
            else
            {
               AStarValue prev_node = came_from[neighbor.y][neighbor.x];
               AStarValue new_node = new AStarValue(dest_pt, neighbor, came_from[current.y][current.x]);

               if (!open_set.contains(neighbor) || (prev_node != null &&
               new_node.get_g_value() < prev_node.get_g_value()))
               {
                  came_from[neighbor.y][neighbor.x] = new_node;
               if (!open_set.contains(neighbor))
               {
                  open_set.insert(neighbor, new_node.get_f_value());
               }
               }
            }
         }
      }
      //System.out.println("no movement error");
      return this.getPosition(); //if cannot get to goal, stay put
   }

   protected static boolean adjacent(Point p1, Point p2)
   {
      return (p1.x == p2.x && abs(p1.y - p2.y) == 1) ||
         (p1.y == p2.y && abs(p1.x - p2.x) == 1);
   }

   protected abstract boolean canPassThrough(WorldModel world, Point new_pt);

   public List<Point> reconstruct_path(Point dest_pt, AStarValue[][] came_from)
   {
      Point final_point = new Point(-1,-1); //final point is at -1,-1 for all
      int i=dest_pt.y; int j=dest_pt.x; //initial came_from point

      List<Point> path = new LinkedList<>();

      while(!came_from[i][j].get_prev_point().equals(final_point))
      {
         path.add(0,came_from[i][j].get_curr_point()); //add current point to beginning of path
         Point prev_point = came_from[i][j].get_prev_point();
         i = prev_point.y;
         j = prev_point.x;
      }
      return path;
   }

   public List<Point> neighbor_nodes(Point current, WorldModel world, Point dest_pt)
   {
      List<Point> neighbors = new ArrayList<>();

      Point[] all_neighbors = {new Point(current.x+1,current.y),
                               new Point(current.x-1,current.y),
                               new Point(current.x,current.y+1),
                               new Point(current.x,current.y-1)};

      for(int i=0;i<4;i++)
      {
         if(world.withinBounds(all_neighbors[i]) && (all_neighbors[i].equals(dest_pt) || canPassThrough(world,all_neighbors[i])))
         {
            neighbors.add(all_neighbors[i]);
         }
      }
      return neighbors;
   }

}
