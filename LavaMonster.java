import processing.core.PImage;

import java.util.List;
import java.util.Random;

/**
 * Created by David Ritter offline on 6/3/2015.
 */
public class LavaMonster
extends MobileAnimatedActor
{
    private static final Random rand = new Random();

    public LavaMonster(String name, Point position, int rate, int animation_rate,
                       List<PImage> imgs)
    {
        super(name, position, rate, animation_rate, imgs);
    }

    protected boolean canPassThrough(WorldModel world, Point pt)
    {
        return !world.isOccupied(pt);
    }

    protected boolean move(WorldModel world, WorldEntity destination)
    {
        if (destination == null)
        {
            return false;
        }

        if (adjacent(getPosition(), destination.getPosition()))
        {
            return true;
        }
        else
        {
            world.moveEntity(this, nextPosition(world, destination.getPosition()));
            return false;
        }
    }

    public Action createAction(WorldModel world, ImageStore imageStore)
    {
        Action[] action = { null };
        action[0] = ticks -> {
            removePendingAction(action[0]);

            //find nearest mobile animated actor, it is a target now
            WorldEntity target = world.findNearest(getPosition(), Miner.class);


            if (move(world, target))
            {
                Lava newLava = new Lava("lava1", target.getPosition(), Main.LAVA_RATE_MIN
                        + rand.nextInt(Main.LAVA_RATE_MAX - Main.LAVA_RATE_MIN), 400,
                        imageStore.get("lava"));
                target.remove(world);
                newLava.schedule(world, System.currentTimeMillis()
                        + newLava.getRate(), imageStore);
                world.addEntity(newLava);
                /*scheduleAction(world, newLava,
                        newLava.createAction(world, imageStore),
                        ticks + newLava.getRate());*/
            }
            scheduleAction(world, this,
                    this.createAction(world, imageStore),
                    ticks + this.getRate());

        };
        return action[0];
    }

}
