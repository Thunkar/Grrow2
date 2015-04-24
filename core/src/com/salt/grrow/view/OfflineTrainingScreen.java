package com.salt.grrow.view;




import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.salt.grrow.controller.MainController;
import com.salt.grrow.model.Creature;
import com.salt.grrow.model.DNA;
import com.salt.grrow.model.OfflineTank;




public class OfflineTrainingScreen implements Screen {

	final GrrowMain game;
	private OrthographicCamera camera;
	private static OfflineTank tank = new OfflineTank(1366,768,60,15,7);
	private TextButton exit;
	private TextButton start;
	private TextButton stahp;
	private TextButton continueSim;
	private Table table;
	private Label maxlabel;
	private Label minlabel;
	private Label generationlabel;
	public static Array<Integer> firstPoints; 
	public static int generation;
	Thread simulation;
	boolean runit;
	boolean isnew;
	boolean hasStarted;
	private int max;
	private int min;
	public static Array<Integer> secondPoints;
	public static Array<Integer> thirdPoints;
	public static Array<Integer> fourthPoints;
	public static Array<Integer> fifthPoints;
	public static Array<Integer> sixthPoints;
	public static Array<Integer> seventhPoints;
	public static Array<Integer> eighthPoints;
	public static Array<Integer> ninthPoints;
	public static Array<Integer> tenthPoints;
	public static Array<Integer> medianPoints;
	public static Array<File> files;


	public OfflineTrainingScreen(final GrrowMain game){

		this.game = game; 
		runit = true;
		isnew = true;
		hasStarted = false;
		firstPoints = new Array<Integer>();
		secondPoints = new Array<Integer>();
		thirdPoints = new Array<Integer>();
		fourthPoints = new Array<Integer>();
		fifthPoints = new Array<Integer>();
		sixthPoints = new Array<Integer>();
		seventhPoints = new Array<Integer>();
		eighthPoints = new Array<Integer>();
		ninthPoints = new Array<Integer>();
		tenthPoints = new Array<Integer>();
		medianPoints = new Array<Integer>();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.width, game.height);
		Gdx.input.setInputProcessor(game.stage);
		game.stage.setViewport(new FitViewport(game.width, game.height, camera));
		this.table=new Table();
		exit = new TextButton("Show me my minions!", game.uiskin);
		start = new TextButton("Start!", game.uiskin);
		stahp = new TextButton("Stahp!", game.uiskin);
		maxlabel = new Label(null, game.uiskin);
		minlabel = new Label(null, game.uiskin);
		generationlabel = new Label(null, game.uiskin);
		continueSim = new TextButton("Continue!", game.uiskin);
		table.add(start).width(300).padTop(10).padBottom(3);
		table.row();
		table.add(continueSim).width(300).padTop(10).padBottom(3);
		table.row();
		table.add(exit).width(300).padTop(10).padBottom(3);
		table.row();
		table.bottom();
		table.center();
		table.setPosition(700, 100);
		minlabel.setPosition(50, 180);
		maxlabel.setPosition(50, 720);
		generationlabel.setPosition(650, 710);
		continueSim.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				isnew = false;
				if(files == null)
						loadStatistics();
				simulation = new Thread(simrun);
				simulation.start();
			}
		});
		start.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				generation = 0;
				stahp.setDisabled(false);
				if(!start.isDisabled()){
					resetStatistics();
					tank.clearLastGeneration();
					tank.Creatures.clear();
					tank.CheckPoints.clear();
					isnew = true;
					simulation = new Thread(simrun);
					simulation.start();
				}}});
		stahp.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				if(!stahp.isDisabled()){
					runit = false;
					try {
						simulation.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		exit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				game.stage.clear();
				runit = false;
				try {
					simulation.join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				Array<Creature> newCreatures = new Array<Creature>();
				for(DNA dna : tank.extractDNA(tank.getLastGeneration())){
					Creature c = new Creature("SmartAss", 0, -330, 90, 10, dna);
					newCreatures.add(c);
				}
				MainController.setTank(newCreatures, tank.CheckPoints);
				game.setScreen(new GameScreen(game));
			}
		});
		game.stage.addActor(table);
		game.stage.addActor(maxlabel);
		game.stage.addActor(minlabel);
		game.stage.addActor(generationlabel);
	}
	
	public void loadStatistics(){
		files = new Array<File>();
	   	File dir = new File(System.getProperty("user.dir")+"\\Statistics\\");
    	for(File f : dir.listFiles()){
    		files.add(f);
    	}
	}
	
	public void resetStatistics(){
	   	File dir = new File(System.getProperty("user.dir")+"\\Statistics\\");
    	for(File f : dir.listFiles()){
    		f.delete();
    	}
		files = new Array<File>();
		int counter = 0;
		for(int i = 0; i<11; i++){
			File file = new File(System.getProperty("user.dir")+"\\Statistics\\Creature"+counter+".csv");
			files.add(file);
			counter++;
		}
	}

	Runnable simrun = new Runnable(){

		@Override
		public void run() {
			generation = 0;
			max = -10000000;
			min = 10000000;
			start.setDisabled(true);
			firstPoints.clear();
			secondPoints.clear();
			thirdPoints.clear();
			fourthPoints.clear();
			fifthPoints.clear();
			sixthPoints.clear();
			seventhPoints.clear();
			eighthPoints.clear();
			ninthPoints.clear();
			tenthPoints.clear();
			medianPoints.clear();
			if(isnew)
				tank.firstGeneration();
			else
				try {
					tank.Creatures.addAll(tank.reproduce(OfflineTank.creaturesFromFiles()));
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			while(runit){
				try {
					tank.newGeneration();
					Iterator<Creature> it = tank.getLastGeneration().iterator();
					int p1 = it.next().getHealth();
					int p2 = it.next().getHealth();
					int p3 = it.next().getHealth();
					int p4 = it.next().getHealth();
					int p5 = it.next().getHealth();
					int p6 = it.next().getHealth();
					int p7 = it.next().getHealth();
					int p8 = it.next().getHealth();
					int p9 = it.next().getHealth();
					int p10 = it.next().getHealth();
					firstPoints.add(p1);
					secondPoints.add(p2);
					thirdPoints.add(p3);
					fourthPoints.add(p4);
					fifthPoints.add(p5);
					sixthPoints.add(p6);
					seventhPoints.add(p7);
					eighthPoints.add(p8);
					ninthPoints.add(p9);
					tenthPoints.add(p10);
					medianPoints.add(calculateMedian(p1, p2, p3, p4, p5, p6,
							p7, p8, p9, p10));
					generation++;
					if(firstPoints.size >=500) firstPoints.clear();
					if(secondPoints.size >=500) secondPoints.clear();
					if(thirdPoints.size >=500) thirdPoints.clear();
					if(fourthPoints.size >=500) fourthPoints.clear();
					if(fifthPoints.size >=500) fifthPoints.clear();
					if(sixthPoints.size >=500) sixthPoints.clear();
					if(seventhPoints.size >=500) seventhPoints.clear();
					if(eighthPoints.size >=500) eighthPoints.clear();
					if(ninthPoints.size >=500) ninthPoints.clear();
					if(tenthPoints.size >=500) tenthPoints.clear();
					if(medianPoints.size >=500) medianPoints.clear();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				tank.saveLastGeneration();
				tank.generateCheckPoints();
				flushStatistics();
				start.setDisabled(false);
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		}};
		
		public int calculateMedian(int p1, int p2, int p3, int p4,
				int p5, int p6, int p7, int p8, int p9, int p10) throws IOException{
			float median = (p1+p2+p3+p4+p5+p6+p7+p8+p9+p10)/10;
			return (int)median;
		}
		
		public void flushStatistics() throws IOException{
			Iterator<File> itr = files.iterator();
			BufferedWriter BW = new BufferedWriter(new FileWriter(itr.next(), true));
			for(Integer i : firstPoints){
				BW.append(i+",");
			}
			BW.close();
			BW = new BufferedWriter(new FileWriter(itr.next(), true));
			for(Integer i : secondPoints){
				BW.append(i+",");
			}
			BW.close();
			BW = new BufferedWriter(new FileWriter(itr.next(), true));
			for(Integer i : thirdPoints){
				BW.append(i+",");
			}
			BW.close();
			BW = new BufferedWriter(new FileWriter(itr.next(), true));
			for(Integer i : fourthPoints){
				BW.append(i+",");
			}
			BW.close();
			BW = new BufferedWriter(new FileWriter(itr.next(), true));
			for(Integer i : fifthPoints){
				BW.append(i+",");
			}
			BW.close();
			BW = new BufferedWriter(new FileWriter(itr.next(), true));
			for(Integer i : sixthPoints){
				BW.append(i+",");
			}
			BW.close();
			BW = new BufferedWriter(new FileWriter(itr.next(), true));
			for(Integer i : seventhPoints){
				BW.append(i+",");
			}
			BW.close();
			BW = new BufferedWriter(new FileWriter(itr.next(), true));
			for(Integer i : eighthPoints){
				BW.append(i+",");
			}
			BW.close();
			BW = new BufferedWriter(new FileWriter(itr.next(), true));
			for(Integer i : ninthPoints){
				BW.append(i+",");
			}
			BW.close();
			BW = new BufferedWriter(new FileWriter(itr.next(), true));
			for(Integer i : tenthPoints){
				BW.append(i+",");
			}
			BW.close();
			BW = new BufferedWriter(new FileWriter(itr.next(), true));
			for(Integer i : medianPoints){
				BW.append(i+",");
			}
			BW.close();
		}

		public int[] convertToPlot(int x, Array<Integer> y, int yaxis, int yoffset){
			while(x>=500) x-=500;
			int[] result  = new int[x];
			Iterator<Integer> it = y.iterator();
			int next = 0;
			generationlabel.setText("Generation: "+ generation);
			for(int i = 0; i<result.length; i++){
				if(it.hasNext()){
					try{
					next = it.next().intValue();
					}
					catch(Exception e){
						System.out.println("WTF?!");
					}
				}
				if(next>max){
					max = next;
				}
				if(next<min){
					min = next;
				}
				maxlabel.setText(""+max);
				minlabel.setText(""+min);
				double axisHeight = max-min;
				double nextNormalized = next-min;
				result[i] =yoffset + (int)((double)yaxis*(nextNormalized/axisHeight));
				//System.out.println(result[i] + " " + next + " " + Math.abs(max-min));

			}
			return result;
		}

		@Override
		public void render(float delta) {
			Gdx.gl.glClearColor(0, 0, 0.2f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			camera.update();
			game.stage.getViewport().update(game.width, game.height, true);
			//game.stage.setCamera(camera);
			game.srenderer.begin(ShapeType.Line);
			int[] result = convertToPlot(generation, firstPoints,  500, 200);
			for(int i = 0; i<result.length-2 ; i++){
				int counter = i;
				game.srenderer.setColor(Color.WHITE);
				game.srenderer.line((float)2.4*counter+50, (float)result[i], (float)2.4*(counter+1)+50, (float)result[i+1]);
			}
			result = convertToPlot(generation, secondPoints,  500, 200);
			for(int i = 0; i<result.length-2 ; i++){
				int counter = i;
				game.srenderer.setColor(Color.CYAN);
				game.srenderer.line((float)2.4*counter+50, (float)result[i], (float)2.4*(counter+1)+50, (float)result[i+1]);			}
			result = convertToPlot(generation, thirdPoints,  500, 200);
			for(int i = 0; i<result.length-2 ; i++){
				int counter = i;
				game.srenderer.setColor(Color.DARK_GRAY);
				game.srenderer.line((float)2.4*counter+50, (float)result[i], (float)2.4*(counter+1)+50, (float)result[i+1]);			}
			result = convertToPlot(generation, fourthPoints, 500,  200);
			for(int i = 0; i<result.length-2 ; i++){
				int counter = i;
				game.srenderer.setColor(Color.GREEN);
				game.srenderer.line((float)2.4*counter+50, (float)result[i], (float)2.4*(counter+1)+50, (float)result[i+1]);			}
			result = convertToPlot(generation, fifthPoints,  500, 200);
			for(int i = 0; i<result.length-2 ; i++){
				int counter = i;
				game.srenderer.setColor(Color.ORANGE);
				game.srenderer.line((float)2.4*counter+50, (float)result[i], (float)2.4*(counter+1)+50, (float)result[i+1]);			}
			result = convertToPlot(generation, sixthPoints,  500, 200);
			for(int i = 0; i<result.length-2 ; i++){
				int counter = i;
				game.srenderer.setColor(Color.MAGENTA);
				game.srenderer.line((float)2.4*counter+50, (float)result[i], (float)2.4*(counter+1)+50, (float)result[i+1]);			}
			result = convertToPlot(generation, seventhPoints,  500, 200);
			for(int i = 0; i<result.length-2 ; i++){
				int counter = i;
				game.srenderer.setColor(Color.YELLOW);
				game.srenderer.line((float)2.4*counter+50, (float)result[i], (float)2.4*(counter+1)+50, (float)result[i+1]);			}
			result = convertToPlot(generation, eighthPoints,  500, 200);
			for(int i = 0; i<result.length-2 ; i++){
				int counter = i;
				game.srenderer.setColor(Color.GRAY);
				game.srenderer.line((float)2.4*counter+50, (float)result[i], (float)2.4*(counter+1)+50, (float)result[i+1]);			}
			result = convertToPlot(generation, ninthPoints,  500, 200);
			for(int i = 0; i<result.length-2 ; i++){
				int counter = i;
				game.srenderer.setColor(Color.PINK);
				game.srenderer.line((float)2.4*counter+50, (float)result[i], (float)2.4*(counter+1)+50, (float)result[i+1]);			}
			result = convertToPlot(generation, tenthPoints,  500, 200);
			for(int i = 0; i<result.length-2 ; i++){
				int counter = i;
				game.srenderer.setColor(Color.BLACK);
				game.srenderer.line((float)2.4*counter+50, (float)result[i], (float)2.4*(counter+1)+50, (float)result[i+1]);			}
			result = convertToPlot(generation, medianPoints,  500, 200);
			for(int i = 0; i<result.length-2 ; i++){
				int counter = i;
				game.srenderer.setColor(Color.RED);
				game.srenderer.line((float)2.4*counter+50, (float)result[i], (float)2.4*(counter+1)+50, (float)result[i+1]);			}
			game.srenderer.setColor(Color.WHITE);
			game.srenderer.line(50, 200, 50, 700);
			game.srenderer.end();
			game.stage.act();
			game.stage.draw();
		}

		@Override
		public void resize(int width, int height) {
			// TODO Auto-generated method stub

		}

		@Override
		public void show() {
			// TODO Auto-generated method stub

		}

		@Override
		public void hide() {
			// TODO Auto-generated method stub

		}

		@Override
		public void pause() {
			// TODO Auto-generated method stub

		}

		@Override
		public void resume() {
			// TODO Auto-generated method stub

		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub

		}





}
