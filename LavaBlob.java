import processing.core.PImage;
import java.util.List;

public class LavaBlob
   extends Blob
{
   public LavaBlob(String name, Point position, int rate, int animation_rate,
      List<PImage> imgs)
   {
	   super(name, position, rate, animation_rate, imgs, Blacksmith.class);
   }

}