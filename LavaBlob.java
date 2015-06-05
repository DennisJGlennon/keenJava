import processing.core.PImage;
import java.util.List;

public class LavaBlob
   extends Blob
{
  private static final int QUAKE_ANIMATION_RATE = 100;
   public LavaBlob(String name, Point position, int rate, int animation_rate,
      List<PImage> imgs)
   {
	   super(name, position, rate, animation_rate, imgs, Blacksmith.class);
   }




}