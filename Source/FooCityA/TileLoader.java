import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


class TileLoader
{
	private boolean m_valid;
    private BufferedImage grassImg;
    private BufferedImage waterImg;
    private BufferedImage dirtImg;
    private BufferedImage beachImg;
    private BufferedImage forrestImg;    
    private String TileSet;
    
	public TileLoader()
	{
		m_valid = true;
		TileSet = "images/" + "SolidColors/";
    	String grassFileName = TileSet + "grass.png";
		String waterFileName = TileSet + "water.png";
		String dirtFileName = TileSet + "dirt.png";
		String beachFileName = TileSet + "beach.png";
		String forrestFileName = TileSet + "forrest.png";
		
        grassImg = null;
        waterImg = null;
        dirtImg = null;
        beachImg = null;
        try
        {
        	grassImg = ImageIO.read(new File(grassFileName));
            waterImg = ImageIO.read(new File(waterFileName));
            dirtImg = ImageIO.read(new File(dirtFileName));
            beachImg = ImageIO.read(new File(beachFileName));
            forrestImg = ImageIO.read(new File(forrestFileName));
        }
        catch (Exception e)
        {
        	m_valid = false;
        }
	}
	public BufferedImage GetTitle(char c)
	{
		if (m_valid)
		{
			switch (c)
			{
			case 'G':
				return grassImg;
			case 'W':
				return waterImg;
			case 'D':
				return dirtImg;
			case 'B':
				return beachImg;
			case 'T':
				return forrestImg;
			default:
				System.out.print(c + " ");
				break;        				
			}
		}
		return null;
	}
}