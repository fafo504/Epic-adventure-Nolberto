package implementacion;

import java.util.ArrayList;
import java.util.HashMap;

import clases.Item;
import clases.JugadorAnimado;
import clases.Mapa;
import clases.Tile;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Juego extends Application{
	private Scene escena;
	private Group root;
	private Canvas canvas;
	private Canvas gui;
	private GraphicsContext graficos;
	private int puntuacion = 0;
	private AnimationTimer loop;
	//private Jugador jugador;
	private JugadorAnimado jugadorAnimado;
	public static boolean derecha=false;
	public static boolean izquierda=false;
	public static boolean arriba=false;
	public static boolean abajo=false;
	public static HashMap<String, Image> imagenes; //Shift+Ctrl+O
	private Item item;
	private Item item2;
	//private ArrayList<Image> imagenes;
	
	private ArrayList<Tile> tiles;
	Mapa map = new Mapa(64,8, "x", 64);
	private int[][] mapa ;
	
	/*private int[][] mapa = {
			{6,4,4,4,4,4,5,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,2,0,0,0},
			{0,0,0,0,0,1,666,0,0,0},
			{0,0,0,0,6,4,4,5,0,0},
			{0,0,0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0,0,0},
	};*/
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage ventana) throws Exception {
		inicializarComponentes();
		graficos = canvas.getGraphicsContext2D();
		root.getChildren().add(canvas);
		root.getChildren().add(gui);
		ventana.setScene(escena);
		ventana.setTitle("Inge lo quiero xd--------------Epic adventure");
		gestionarEventos();
		ventana.show();
		cicloJuego();		
	}
	
	public void inicializarComponentes() {
		
		mapa = map.getMapa();
		//jugador = new Jugador(-50,400,"goku",1);
		jugadorAnimado = new JugadorAnimado(64,320,"megaman",1, "correr");		
		jugadorAnimado.getJugador().translateYProperty().addListener((obs, viejo, nuevo) -> 
		{
			int dif = nuevo.intValue();
			if(dif >= escena.getHeight()-80) {
				System.out.println(dif);
				loop.stop();
			}
		});
		root = new Group();
		escena = new Scene(root,640,480);
		canvas  = new Canvas(map.getWidth(),480);
		gui = new Canvas(escena.getWidth(),escena.getHeight());
		imagenes = new HashMap<String,Image>();
		item = new Item(100,200,0,0,"item");
		item2 = new Item(200,200,0,0,"item");
		
		cargarImagenes();
		cargarTiles();
	}
	
	public void cargarImagenes() {
		imagenes.put("tilemap", new Image("Assets64.png"));
		imagenes.put("megaman", new Image("pj.png"));
		imagenes.put("item", new Image("item.png"));
		imagenes.put("bg", new Image("bg.png"));
		imagenes.put("bg2", new Image("bg2.png"));
	}
	
	public void pintar() {
		graficos = gui.getGraphicsContext2D();
		graficos.setFill(Color.WHITE);
		graficos.fillRect(0, 0, escena.getWidth(), 10);
		graficos.setFill(Color.BLACK);
		graficos.fillText("Puntuacion: " + puntuacion, 10, 10);
		graficos = canvas.getGraphicsContext2D();
		graficos.setFill(Color.WHITE);
		graficos.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		graficos.drawImage(imagenes.get("bg2"), 0, 0, canvas.getWidth(), canvas.getHeight());
		graficos.drawImage(imagenes.get("bg"), 0, 0, canvas.getWidth(), canvas.getHeight());
		//graficos.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		//jugador.pintar(graficos);
		jugadorAnimado.pintar(graficos);
		///Pintar tiles
		for (int i=0;i<tiles.size();i++)
			tiles.get(i).pintar(graficos);
		
		item.pintar(graficos);
		item2.pintar(graficos);

	}
	
	public void cargarTiles() {
		tiles = new ArrayList<Tile>();
		for(int i=0; i<mapa.length; i++) {
			for(int j=0; j<mapa[i].length; j++) {
				if (mapa[i][j]!=0)
					tiles.add(new Tile(mapa[i][j], j*64, i*64, "tilemap",0));
			}
		}
	}
	
	public void gestionarEventos() {
		//Evento cuando se presiona una tecla
		escena.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent evento) {
					//Aqui tengo que poner el codigo para identificar cuando se presiono una tecla
					switch (evento.getCode().toString()) {
						case "RIGHT": //derecha
							derecha=true;
							break;
						case "LEFT": //derecha
							izquierda=true;
						break;
						case "UP":
							arriba=true;
							break;
						case "DOWN":
							abajo=true;
							break;
						case "SPACE":
							//jugador.setVelocidad(10);
							//jugador.setIndiceImagen("goku-furioso");
							break;
					}
			}			
		});
		
		escena.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent evento) {
				//Aqui tengo que poner el codigo para identificar cuando se soltó una tecla
				switch (evento.getCode().toString()) {
				case "RIGHT": //derecha
					derecha=false;
					break;
				case "LEFT": //derecha
					izquierda=false;
				break;
				case "UP":
					arriba=false;
					break;
				case "DOWN":
					abajo=false;
					break;
				case "SPACE":
					//jugador.setVelocidad(1);
					//jugador.setIndiceImagen("goku");
					break;
			}
				
			}
			
		});
		
	}
	
	public void cicloJuego() {
		final long tiempoInicial = System.nanoTime();
		AnimationTimer animationTimer = new AnimationTimer() {
			//Esta rutina simula un ciclo de 60FPS
			@Override
			public void handle(long tiempoActualNanoSegundos) {
				double t = (tiempoActualNanoSegundos - tiempoInicial) / 1000000000.0;
				pintar();
				actualizar(t);
				
			}
			
		};
		loop = animationTimer;
		loop.start(); //Inicia el ciclo
	}
	
	public void actualizar(double t) {
		puntuacion = jugadorAnimado.getPuntuacion();
		for(Tile tile : tiles) {
			jugadorAnimado.verificarColisionesTiles(tile);
		}
		jugadorAnimado.mover(t);
		jugadorAnimado.actualizarAnimacion(t);
		jugadorAnimado.verificarColisiones(item);
		jugadorAnimado.verificarColisiones(item2);
		canvas.setLayoutX(-(t*(128 + jugadorAnimado.getVelocidad()*15)));
		
	}
}
