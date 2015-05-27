public class Point
{
   public final int x;
   public final int y;

   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;
   }

   public String toString()
   {
      return "(" + x + "," + y + ")";
   }

   public boolean equals(Object obj)
   {
      Point pt  = (Point)obj;
      return (this.x == pt.x && this.y==pt.y);
   }
}
