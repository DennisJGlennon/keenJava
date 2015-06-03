import processing.core.PImage;
import java.util.List;

public class Lava
    extends AnimatedActor
{
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
         scheduleAction(world, this, createAction(world, imageStore),
                        nextTime);
      };
      return action[0];
   }

}