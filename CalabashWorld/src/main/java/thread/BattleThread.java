package thread;

import java.io.*;
import java.util.*;

import annotation.Author;
import being.*;
import formation.*;
import gui.*;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

@Author(name="chy",number="161220023",email="161220023@smail.nju.edu.cn")
public class BattleThread{
	
	File outfile;
	GridPane pane;
	Cell[][] cells;
	Position[][] positions;
	ArrayList<Creature> goods;
	ArrayList<Creature> bads;
	ArrayList<CreatureThread> threads;
	UIFileThread uithread;
	VBox leftbox;
	VBox rightbox;
	BottomBox bottombox;
	
	Timer timer;
	
	public BattleThread(GridPane pane, Cell[][] cells, Position[][] positions, 
			ArrayList<Creature> goods, ArrayList<Creature> bads, File outfile, 
			VBox leftbox, VBox rightbox, BottomBox bottombox){//管理战场,记录文件
		this.outfile=outfile;
		this.pane=pane;
		this.cells=cells;
		this.positions=positions;
		this.goods=goods;
		this.bads=bads;
		this.leftbox=leftbox;
		this.rightbox=rightbox;
		this.bottombox=bottombox;
		this.threads=new ArrayList<>();
		bottombox.add(new Label("新游戏"));
		for(Creature c:goods)
			threads.add(new CreatureThread(c));
		for(Creature c:bads)
			threads.add(new CreatureThread(c));
		for(Thread t:threads)
			t.start();
		
		timer=new Timer();
	}

	public void run() {
		uithread=new UIFileThread(this, outfile,pane,cells,positions,goods,bads,leftbox,rightbox,bottombox);
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if(uithread.gameover) {
					timer.cancel();
				}
				else
					Platform.runLater(uithread);
			}
			
		}, 0, 50);
	}
	
	public void cancle() {
		timer.cancel();
	}
	
	public void removeall() {//清空pane上的所有物体(包括cell),清空bottombox
		pane.getChildren().clear();
		bottombox.clear();
	}
	
	public void init() {//初始化所有生物、面板、flag,达到游戏初始状态
		removeall();
		
		//添加cell
		for(int i=0;i<11;i++) {
			for(int j=0;j<20;j++) {
				cells[i][j].clear();
				pane.add(cells[i][j], j, i);
			}
		}
		//初始化左右底部的面板
		for(Node cl:leftbox.getChildren())
			((CreatureLabel)cl).init();
		for(Node cl:rightbox.getChildren())
			((CreatureLabel)cl).init();
		bottombox.clear();
		
		//初始化各个生物
		for(Creature c:goods) c.init();
		for(Creature c:bads)  c.init();
		
		//初始化flag
		World.newgame=false;
		World.goodform=false;
		World.badform=false;
	}
	
	public void closethreads() {//关闭线程及与线程相关的一切资源
		//关闭UIFileThread(包括了关闭子弹线程池)
		uithread.exit();
		
		//关闭生物线程
		for(CreatureThread t:threads) {
			t.exit();
			t.interrupt();
		}
		
	}
}
