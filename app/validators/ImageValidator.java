package validators;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import play.data.validation.Check;
import play.db.jpa.Blob;

public class ImageValidator extends Check {
	
	public static final long MAX_SIZE = 4048 * 1000 * 1000;
	public static final int MAX_HEIGHT = 3000;

	@Override
	public boolean isSatisfied(Object validatedObject, Object value) {
		
		if (!(value instanceof Blob)) {
			return false;
		}

		Blob image = (Blob) value;
		
		if (!"image/jpeg".equals(image.type()) && !"image/png".equals(image.type())) {
			return false;
		}
		
		if (image.getFile().length() > MAX_SIZE) {
			return false;
		}
				
		try {
			BufferedImage source = ImageIO.read(image.get());
			int w = source.getWidth();
			int h = source.getHeight();
			
			if (w > MAX_HEIGHT || h > MAX_HEIGHT) {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
		
		return false;
	}

}
