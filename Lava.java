import processing.core.PImage;
import java.util.List;
import java.util.Random;

public class Lava
    extends AnimatedActor
{
    private static final int DEFAULT_DISTANCE = 1;
    private int resourceDistance = 1;

    private static final int LAVA_MONSTER_RATE = 1000;
    private static final int LAVA_MONSTER_ANIM_RATE = 300;

    private static final Random rand = new Random();


    public Lava(String name, Point position, int rate, int animation_rate,
                List<PImage> imgs)
	{
        super(name, position, rate, animation_rate, imgs);
	}

   public Action createAction(WorldModel world, ImageStore imageStore)
   {
      Action[] action = { null };
      action[0] = ticks -> {
         removePendingAction(action[0]);
         long nextTime = ticks + getRate();

          Point openPt = findOpenAround(world, getPosition(), resourceDistance);
          if (openPt != null)
          {
              LavaMonster monster = createLavaMonster(world, "LavaMon - " + getName() + " - " + ticks,
                      openPt, ticks, imageStore);
              world.addEntity(monster);
              //monster.schedule(world,nextTime,imageStore);
          }

         scheduleAction(world, this, createAction(world, imageStore),
                        nextTime);
      };
      return action[0];
   }

    private LavaMonster createLavaMonster(WorldModel world, String name, Point pt,
                          long ticks, ImageStore imageStore)
    {
        LavaMonster monster = new LavaMonster(name, pt,
                LAVA_MONSTER_RATE, LAVA_MONSTER_ANIM_RATE,
                imageStore.get("fire"));
        monster.schedule(world, ticks, imageStore);
        return monster;
    }

    private Point findOpenAround(WorldModel world, Point pt, int distance)
    {
        for (int dy = -distance; dy <= distance; dy++)
        {
            for (int dx = -distance; dx <= distance; dx++)
            {
                Point newPt = new Point(pt.x + dx, pt.y + dy);
                if (world.withinBounds(newPt) && !world.isOccupied(newPt))
                {
                    return newPt;
                }
            }
        }

        return null;
    }
}