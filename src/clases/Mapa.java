package clases;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Mapa {
	private int indexMapa;
	private String lugarMapa;
	private int[][] mapa;
	private int[][] visor;
	private boolean buffered;
	
	private BufferedReader reader;
	private FileReader freader;
	
	private int width;
	private int height;
	
	public Mapa(int width, int height, String path, int tileSize){
		this.width = width*tileSize;
		this.height = height*tileSize;
		String lugarMapa = path;
		visor = new int[height][width];
		cargarMapa();
	}
	
	private void cargarMapa(){
		try {
			freader = new FileReader("mapas//mapa1.csv");
			reader = new BufferedReader(freader);
			String line = "";
			ArrayList<int[]> lines = new ArrayList<>();
			
			while ((line = reader.readLine()) != null) {
				String[] temp = line.split(",");
				int[] arreglo = new int[temp.length];
				for(int index = 0; index < temp.length; index++){
					arreglo[index] = Integer.valueOf(temp[index]);
				}
				lines.add(arreglo);
			}
			
			mapa = new int[height][lines.get(0)[0]];
			for(int i = 1; i < lines.size(); i++) {
				for(int j = 0; j < lines.get(i).length; j++) {
					mapa[i-1][j] = lines.get(i)[j];
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int[][] getVisor(int t){
		if(width + t < mapa[0].length) {
			for(int i = 0; i< mapa.length; i++) {
				for(int j = 0; j < width; j++) {
					visor[i][j] = mapa[i][j+t];
				}
			}
		}
		return visor;
	}
	
	public int[][] getMapa()
	{
		return mapa;
	}
	
	public int getWidth()
	{
		return width;
	}
}
