package thread;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.locks.*;

import being.Creature;
import gui.*;
import gui.Cell;
import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class ReplayUIThread extends Thread {
	File infile;
	BufferedReader reader;
	Cell[][] cells;
	GridPane pane;
	ArrayList<Creature> goods;
	ArrayList<Creature> bads;
	VBox leftbox;
	VBox rightbox;
	BottomBox bottombox;
	
	ImageView goodswin;
	ImageView badswin;
	
	boolean finish;
	
	Lock lock;
	
	public ReplayUIThread(File infile, GridPane pane, Cell[][] cells, 
			ArrayList<Creature> goods, ArrayList<Creature> bads, 
			VBox leftbox, VBox rightbox, BottomBox bottombox){
		//读取文件，进行重放
		this.infile=infile;
		try {
			this.reader=new BufferedReader(new FileReader(infile));
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		this.cells=cells;
		this.pane=pane;
		this.goods=goods;
		this.bads=bads;
		this.leftbox=leftbox;
		this.rightbox=rightbox;
		this.bottombox=bottombox;
		this.goodswin=new ImageView(new Image("/goodswin.png"));
		this.badswin=new ImageView(new Image("/badswin.png"));
		this.finish=false;
		
		this.lock=new ReentrantLock();
	}
	
	public void run() {
		String str;
		lock.lock();
		try {
			if((str=reader.readLine())!=null) {
				//解析当前的场景
				
				//先清空cells上的生物
				for(int i=0;i<11;i++) {
					for(int j=0;j<20;j++) {
						cells[i][j].clear();
					}
				}
				
				if(str.equals("winner:C")||str.equals("winner:S")) {
					//关闭文件流
					reader.close();
					//删除pane上的所有物体,播放胜利动画
					pane.getChildren().clear();
					bottombox.clear();
					FadeTransition fadeTransition;
					ScaleTransition scaleTransition;
					if(str.equals("winner:C")) {
						fadeTransition=new FadeTransition(Duration.millis(1000), goodswin);
						scaleTransition = new ScaleTransition(Duration.millis(1000), goodswin);
						pane.getChildren().add(goodswin);
					}
					else {
						fadeTransition=new FadeTransition(Duration.millis(1000), badswin);
						scaleTransition = new ScaleTransition(Duration.millis(1000), badswin);
						pane.getChildren().add(badswin);
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
			        	removeall();
			        	init();
			        });
				}
				
				else {
					//放置生物、添加子弹、更新面板
					String[] strstr=str.split(" ");
					for(String tmpstr:strstr) {
						int len=tmpstr.length();
						if(len>0) {
							char type=tmpstr.charAt(0);
							String posbullet=tmpstr.substring(3);
							String[] subsubstring=posbullet.split(",");
							int row=Integer.parseInt(subsubstring[0]);
							int col=Integer.parseInt(subsubstring[1]);
							int index=tmpstr.charAt(1)-'0';
							Creature c=null;
							switch(type) {
							case 'C':
								c=goods.get(index);break;
							case 'S':
								c=bads.get(index);break;
							default:
								System.out.println("不存在的类型");
							}
							if(subsubstring[2].equals("die")) {
								//播放动画
								pane.add(c.getview(), col, row);
								FadeTransition fadeTransition=new FadeTransition(Duration.millis(100), c.getview());
								fadeTransition.setFromValue(1.0f);
								fadeTransition.setToValue(0.0f);
								fadeTransition.play();
								//将生物阵亡消息添加到底部面板
								bottombox.add(new Label(c.getname()+"阵亡..."));
								//更新左右面板
								switch(type) {
								case 'C':
									((CreatureLabel)leftbox.getChildren().get(index)).die();break;
								case 'S':
									((CreatureLabel)rightbox.getChildren().get(index)).die();break;
								default:
									System.out.println("不存在的生物类型");
								}
							}
							else{//添加生物到格子上
								cells[row][col].setbeing(c);
								int remainedhp=Integer.parseInt(subsubstring[2]);
								switch(type) {
								case 'C':
									((CreatureLabel)leftbox.getChildren().get(index)).refresh(remainedhp);break;
								case 'S':
									((CreatureLabel)rightbox.getChildren().get(index)).refresh(remainedhp);break;
								default:
									System.out.println("不存在的生物类型");
								}
								if(subsubstring.length>3) {
									for(int i=3;i<subsubstring.length;i++) {
										int tocol=Integer.parseInt(subsubstring[i].substring(1));
										Circle bullet=c.getbullet();
										pane.add(bullet, 0, row);
										TranslateTransition translateTransition=new TranslateTransition(Duration.millis(c.getbulletmovespeed()*Math.abs(col-tocol)), bullet);
										translateTransition.setFromX(25+col*50);
										translateTransition.setToX(20+tocol*50);
										FadeTransition fadeTransition=new FadeTransition(Duration.millis(100), bullet);
										fadeTransition.setFromValue(1.0f);
										fadeTransition.setToValue(0.0f);		
										SequentialTransition sequentialTransition=new SequentialTransition(translateTransition,fadeTransition);
								        sequentialTransition.play();
									}
								}	
							}
						}
					}
				}
			}
			else {
				finish=true;
				removeall();
				init();
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			finish=true;
			return;
		}
		finally {
			lock.unlock();
		}
	}
	
	public void removeall() {
		pane.getChildren().clear();
		bottombox.clear();
	}
	
	public void closefile() {
		try {
			reader.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	public void init() {
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

		//初始化flag
		World.replay=false;
		World.goodform=false;
		World.badform=false;
	}
	
	public boolean isfinished() {
		return finish;
	}
}
