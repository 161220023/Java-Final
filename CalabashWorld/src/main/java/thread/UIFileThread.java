package thread;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

import being.*;
import formation.*;
import gui.*;
import gui.Cell;
import javafx.animation.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class UIFileThread extends Thread{
	
	BattleThread bt;
	
	File outfile;
	OutputStreamWriter writer;
	GridPane pane;
	Cell[][] cells;
	Position[][] positions;
	
	ArrayList<Creature> goods;
	ArrayList<Creature> bads;
	
	VBox leftbox;
	VBox rightbox;
	BottomBox bottombox;
	
	ImageView goodswin;
	ImageView badswin;
	
	ExecutorService bulletpool;
	
	boolean gameover;
	boolean winnergoods;
	
	StringBuffer sb;
	
	Lock lock;
	
	
	UIFileThread(BattleThread bt, File outfile, GridPane pane, Cell[][] cells, Position[][] positions, ArrayList<Creature> goods, ArrayList<Creature> bads, VBox leftbox, VBox rightbox, BottomBox bottombox){
		this.bt=bt;
		this.outfile=outfile;
		try {
			this.writer=new OutputStreamWriter(new FileOutputStream(outfile.getAbsolutePath()),"UTF-8");
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		this.pane=pane;
		this.cells=cells;
		this.positions=positions;
		this.goods=goods;
		this.bads=bads;
		
		this.leftbox=leftbox;
		this.rightbox=rightbox;
		this.bottombox=bottombox;

		this.goodswin=new ImageView(new Image("/goodswin.png"));
		this.badswin=new ImageView(new Image("/badswin.png"));
		
		this.bulletpool=Executors.newCachedThreadPool();
		
		this.gameover=false;
		this.lock=new ReentrantLock();
	}
	
	public void gameover() {
		bt.removeall();
		FadeTransition fadeTransition;
		ScaleTransition scaleTransition;
		if(winnergoods) {
			pane.getChildren().add(goodswin);
			fadeTransition=new FadeTransition(Duration.millis(1000), goodswin);
			scaleTransition = new ScaleTransition(Duration.millis(1000), goodswin);
		}
		else {
			pane.getChildren().add(badswin);
			fadeTransition=new FadeTransition(Duration.millis(1000), badswin);
			scaleTransition = new ScaleTransition(Duration.millis(1000), badswin);
		}
		fadeTransition.setFromValue(1.0f);
		fadeTransition.setToValue(1.0f);
		scaleTransition.setFromX(0.0f);
		scaleTransition.setFromY(0.0f);
		scaleTransition.setToX(1.0f);
		scaleTransition.setToY(1.0f);
		SequentialTransition sequentialTransition=new SequentialTransition(scaleTransition,fadeTransition);
		sequentialTransition.play();
		Button b=new Button("确定");
		bottombox.add(b);
		b.setOnMouseClicked((MouseEvent)->{
			bt.init();
		});
		bt.closethreads();
		bt.cancle();
	}
	
	public void run() {
		//刷新UI
		if(gameover) return;
		lock.lock();
		try {
			if(gameover) return;
			sb=new StringBuffer();
			for(int i=0;i<11;i++) {
				for(int j=0;j<20;j++) {
					//根据每个position的情况设置cell的图片
					cells[i][j].clear();
				}
			}
			int len=goods.size();
			for(int i=0;i<len;i++) {
				Creature c=goods.get(i);
				if(c.isAlive()) {
					if(c.ifwin()) {
						bulletpool.shutdownNow();
						winnergoods=true;
						gameover=true;
						try {
							writer.write("winner:C");
						} catch (IOException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
						gameover();
						return;
					}
					//更新坐标、血量
					int row=c.getrow();
					int col=c.getcol();
					cells[row][col].setbeing(c);
					sb.append("C"+i+":"+row+","+col+","+c.getremainedhp());
					//更新子弹
					ArrayList<Bullet> bullets=c.getbullets();
					int tmplen=bullets.size();
					for(int j=0;j<tmplen;j++) {//创建子弹线程
						Bullet curbullet=bullets.remove(0);
						bulletpool.execute(new BulletThread(pane,positions,curbullet));
						sb.append(",b"+curbullet.gettocol());
					}
					sb.append(" ");
					//更新血量
					CreatureLabel cl=(CreatureLabel)leftbox.getChildren().get(i);
					cl.refresh();
				}
				else if(c.getanimation()) {
					//播放动画
					//if(gameover) return;
					int currow=c.getrow();
					int curcol=c.getcol();
					sb.append("C"+i+":"+currow+","+curcol+",die ");
					c.leave();
					pane.add(c.getview(), curcol, currow);
					FadeTransition fadeTransition=new FadeTransition(Duration.millis(1000), c.getview());
					fadeTransition.setFromValue(1.0f);
					fadeTransition.setToValue(0.0f);
					fadeTransition.play();
					c.setanimation(false);
					CreatureLabel cl=(CreatureLabel)leftbox.getChildren().get(i);
					cl.die();
					//bottombox播报战死消息
					bottombox.add(new Label(c.getname()+"阵亡..."));
				}
			}	
			len=bads.size();
			for(int i=0;i<len;i++) {
				Creature c=bads.get(i);
				if(c.isAlive()) {
					if(c.ifwin()) {
						bulletpool.shutdownNow();
						winnergoods=false;
						gameover=true;
						try {
							writer.write("winner:S");
						} catch (IOException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
						gameover();
						return;
					}
					//更新坐标
					int row=c.getrow();
					int col=c.getcol();
					cells[row][col].setbeing(c);
					sb.append("S"+i+":"+row+","+col+","+c.getremainedhp());
					//更新子弹
					ArrayList<Bullet> bullets=c.getbullets();
					int tmplen=bullets.size();
					for(int j=0;j<tmplen;j++) {//创建子弹线程
						Bullet curbullet=bullets.remove(0);
						bulletpool.execute(new BulletThread(pane,positions,curbullet));
						sb.append(",b"+curbullet.gettocol());
					}
					sb.append(" ");
					//更新血量
					CreatureLabel cl=(CreatureLabel)rightbox.getChildren().get(i);
					cl.refresh();
				}
				else if(c.getanimation()) {
					//播放动画
					int currow=c.getrow();
					int curcol=c.getcol();
					c.leave();
					sb.append("S"+i+":"+currow+","+curcol+",die ");
					pane.add(c.getview(), curcol, currow);
					FadeTransition fadeTransition=new FadeTransition(Duration.millis(1000), c.getview());
					fadeTransition.setFromValue(1.0f);
					fadeTransition.setToValue(0.0f);
					fadeTransition.play();
					c.setanimation(false);
					CreatureLabel cl=(CreatureLabel)rightbox.getChildren().get(i);
					cl.die();
					
					//bottombox播报战死消息
					bottombox.add(new Label(c.getname()+"阵亡..."));
				}
			}
			sb.append("\n");
			try {
				writer.write(sb.toString());
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		finally {
			lock.unlock();
		}
	}
	
	public void exit() {//关闭打开的文件资源,关闭子弹线程池
		bulletpool.shutdownNow();
		try {
			writer.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
