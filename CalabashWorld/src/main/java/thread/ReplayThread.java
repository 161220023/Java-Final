package thread;

import java.io.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import being.Creature;
import gui.*;
import javafx.application.Platform;
import javafx.scene.layout.*;

public class ReplayThread{
	
	ReplayUIThread uithread;
	
	public ReplayThread(File infile, GridPane pane, Cell[][] cells, ArrayList<Creature> goods, ArrayList<Creature> bads, VBox leftbox, VBox rightbox, BottomBox bottombox){
		
		uithread=new ReplayUIThread(infile,pane,cells,goods,bads,leftbox,rightbox,bottombox);
	}
	
	public void run() {
		Timer timer=new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if(!uithread.isfinished())
					Platform.runLater(uithread);
				else
					timer.cancel();
			}
			
		}, 0, 50);
	}
	
	public void closefile() {
		uithread.closefile();
	}
}
