import processing.core.PImage;
import java.util.List;

public class OreBlob
   extends Blob
{
   private static final int QUAKE_ANIMATION_RATE = 100;
   public OreBlob(String name, Point position, int rate, int animation_rate,
      List<PImage> imgs)
   {
	   super(name, position, rate, animation_rate, imgs, Vein.class);
   }
}
