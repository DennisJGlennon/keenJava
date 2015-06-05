import processing.core.PImage;
import java.util.List;
import java.util.Random;

public abstract class Blob
   extends MobileAnimatedActor
{
   private static final int DEFAULT_RATE = 1000;//5000;
   private static final int BLOB_RATE_SCALE = 4;
   private static final int BLOB_ANIMATION_RATE_SCALE = 50;
   private static final int BLOB_ANIMATION_MIN = 1;
   private static final int BLOB_ANIMATION_MAX = 3;
   private static final int QUAKE_ANIMATION_RATE = 100;

   private static final Random rand = new Random();
   private Class<?> seeking;
   
   public Blob(String name, Point position, int rate, int animation_rate,
			   List<PImage> imgs, Class<?> seeking)
   {
      super(name, position, rate, animation_rate, imgs);
	  this.seeking = seeking;
   }

   protected boolean canPassThrough(WorldModel world, Point pt)
   {
      return !world.isOccupied(pt) || world.getTileOccupant(pt) 
             instanceof Ore;
   }

	protected boolean move(WorldModel world, WorldEntity target)
	{
      if (target == null)
      {
         return false;
      }

      if (adjacent(getPosition(), target.getPosition()))
      {
         target.remove(world);
         return true;
      }
      else
      {
         Point new_pt = nextPosition(world, target.getPosition());
         WorldEntity old_entity = world.getTileOccupant(new_pt);
         if (old_entity != null && old_entity != this)
         {
            old_entity.remove(world);
         }
         world.moveEntity(this, new_pt);
         return false;
      }
	}

   public Action createAction(WorldModel world, ImageStore imageStore)
   {
      Action[] action = { null };
      if (!(nearLava(world) && this instanceof OreBlob))
	  {
      action[0] = ticks -> {
         removePendingAction(action[0]);

         WorldEntity target = world.findNearest(getPosition(), seeking);
         long nextTime = ticks + getRate();

         if (target != null)
         {
            Point tPt = target.getPosition();

            if (this.move(world, target))
            {
               Quake quake = createQuake(world, tPt, ticks, imageStore);
               world.addEntity(quake);
               nextTime = nextTime + getRate();
            }
         }

         scheduleAction(world, this, createAction(world, imageStore),
            nextTime);
         
      };
	  }
      else
	  {
         action[0] = ticks -> {
            removePendingAction(action[0]);
            Blob blob = createBlob(world, "lavablob",
            getPosition(), getRate() / BLOB_RATE_SCALE, ticks, imageStore);

            remove(world);
            world.addEntity(blob);
         };
	  }
      return action[0];
   }

   private Quake createQuake(WorldModel world, Point pt, long ticks,
      ImageStore imageStore)
   {
      Quake quake = new Quake("quake", pt, QUAKE_ANIMATION_RATE,
         imageStore.get("quake"));
      quake.schedule(world, ticks, imageStore);
      return quake;
   }

   private static Blob createBlob(WorldModel world, String name,
      Point pt, int rate, long ticks, ImageStore imageStore)
   {
      Blob blob = new LavaBlob(name, pt, rate,
         BLOB_ANIMATION_RATE_SCALE * (BLOB_ANIMATION_MIN +
            rand.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN)),
         imageStore.get("lavablob"));
      blob.schedule(world, ticks, imageStore);
      return blob;
   }

	protected boolean nearLava(WorldModel world)
	{
	   Point[] pts = new Point[4];
	   int x = getPosition().x;
       int y = getPosition().y;
	   pts[0] = new Point(x - 1, y);
       pts[1] = new Point(x, y - 1);
       pts[2] = new Point(x + 1, y);
       pts[3] = new Point(x, y + 1);
       for(int i = 0; i < 4; i ++)
	   {
           if (world.getTileOccupant(pts[i]) instanceof Lava)
		   {
               return true;
		   }
	   }
       return false;
	}
}


