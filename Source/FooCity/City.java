import java.awt.Color;

public class City {
	private CityTile[][] tile;
	
	public City(){
		tile = new CityTile[128][128];
	}
	
	public CityTile getTile(int x, int y){
		return tile[x][y];
	}
	
	public void initFromTerrain(int[] mapData){
		for (int y = 0; y < 128; y++){
			for (int x = 0; x < 128; x++){
				tile[x][y] = new CityTile();
				switch(mapData[y * 128 + x]){
					case 'W':
						tile[x][y].setTileType(TileType.WATER);
						break;
					case 'B':
						tile[x][y].setTileType(TileType.BEACH);
						break;
					case 'G':
						tile[x][y].setTileType(TileType.GRASS);
						break;
					case 'D':
						tile[x][y].setTileType(TileType.DIRT);
						break;
					case 'T':
						tile[x][y].setTileType(TileType.FORREST);
						break;
				}
			}
		}
	}
}
