package gui;

import java.io.*;
import java.util.ArrayList;

import being.*;
import formation.*;
import gui.Cell;
import javafx.application.Platform;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import thread.*;

public class World {
	
	//场景
	Stage stage;
	Scene scene;
	//主布局
	GridPane cellpane;
	MenuBar menubar;
	VBox leftbox;
	VBox rightbox;
	BottomBox bottombox;
	
	//中间布局
	final int ROWS=11;
	final int COLS=20;
	Cell[][] cells;
	Position[][] positions;
	
	//阵形
	final String[] formationname={"锋矢阵","鹤翼阵","偃月阵","衡轭阵","鱼鳞阵","长蛇阵","方圆阵","雁行阵"};
	Menu start;
	Menu goodformation;
	Menu badformation;
	MenuItem[] startitem;
	MenuItem[] gformation;
	MenuItem[] bformation;
	Formation[] formations= {new Arrow(), new CraneWing(), 
	new CrescentMoon(), new CrossBar(), new FishScale(), 
	new SerpentArray(), new SquareCircle(), new WildGooseRow()};
	
	//生物集合
	ArrayList<Creature> goods;
	ArrayList<Creature> bads;
	
	BattleThread bt;
	ReplayThread rt;
	//回放线程
	
	//flag
	public static boolean goodform;//是否设置了葫芦娃阵形
	public static boolean badform;//是否设置了妖怪阵形
	public static boolean newgame;//是否开始了新游戏
	public static boolean replay;//是否在回放
	
	World(Stage stage, GridPane pane, MenuBar menubar, VBox leftbox, VBox rightbox, BottomBox bottombox){
		
		this.stage=stage;
		this.scene=stage.getScene();
		this.cellpane=pane;
		this.menubar=menubar;
		this.leftbox=leftbox;
		this.rightbox=rightbox;
		this.bottombox=bottombox;
		
		//初始化中间布局
		cells=new Cell[ROWS][COLS];
		positions=new Position[ROWS][COLS];
		for(int i=0;i<ROWS;i++) {
			for(int j=0;j<COLS;j++) {
				cells[i][j]=new Cell(i,j);
				positions[i][j]=new Position(i,j);
				cellpane.add(cells[i][j], j, i);
			}
		}
		
		//初始化菜单栏
		start=new Menu("开始");
		goodformation=new Menu("葫芦娃阵形");
		badformation=new Menu("妖怪阵形");
		int types=formationname.length;
		startitem=new MenuItem[2];
		gformation=new MenuItem[types];
		bformation=new MenuItem[types];
		for(int i=0;i<types;i++) {
			gformation[i]=new MenuItem(formationname[i]);
			bformation[i]=new MenuItem(formationname[i]);
		}
		startitem[0]=new MenuItem("新游戏");
		startitem[1]=new MenuItem("重放");
		start.getItems().addAll(startitem);
		goodformation.getItems().addAll(gformation);
		badformation.getItems().addAll(bformation);
		this.menubar.getMenus().addAll(start,goodformation,badformation);
		
		//初始化生物
		goods=new ArrayList<>();
		bads=new ArrayList<>();
		goods.add(new FirstBrother());
		goods.add(new SecondBrother());
		goods.add(new ThirdBrother());
		goods.add(new FourthBrother());
		goods.add(new FifthBrother());
		goods.add(new SixBrother());
		goods.add(new SeventhBrother());
		for(Creature good:goods)
			good.setback(positions);
		bads.add(new Serpent());
		bads.add(new Scorpion());
		bads.add(new Centipede());
		bads.add(new Centipede());
		bads.add(new Toad());
		bads.add(new Toad());
		bads.add(new Toad());
		for(Creature bad:bads)
			bad.setback(positions);
		
		//初始化左边栏
		for(Creature c:goods)
			leftbox.getChildren().add(new CreatureLabel(c));
		//初始化右边栏
		for(Creature c:bads)
			rightbox.getChildren().add(new CreatureLabel(c));
		
		//初始化flag
		goodform=false;
		badform=false;
		newgame=false;
		replay=false;
		
		
		addaction();
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	        @Override
	        public void handle(WindowEvent event) {
	        	if(bt!=null)      bt.closethreads();
	        	else if(rt!=null) rt.closefile();
	        	stage.close();
        		Platform.exit();
        		System.exit(0);
	        }
	    });
		
		cellpane.setOnKeyPressed((KeyEvent event)->{
			if(event.getCode()!=KeyCode.L) return;
			replay();
		});
	}
	
	public void replay() {
		if(newgame) {
			bottombox.add(new Label("游戏过程中,请等待游戏结束"));
			return;
		}
		else if(replay) {
			bottombox.add(new Label("回放过程中,请等待回放结束"));
			return;
		}
		FileChooser filechooser=new FileChooser();
		filechooser.setTitle("选择战斗文件");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        filechooser.getExtensionFilters().add(extFilter);
		File file=filechooser.showOpenDialog(stage);
		if(file!=null) {
			replay=true;
			//创建回放对象
			rt=new ReplayThread(file,cellpane,cells,goods,bads,leftbox,rightbox,bottombox);
			rt.run();
		}
	}
	
	//初始化生物的状态
	public void initcreature() {
		int len=goods.size();
		for(int i=0;i<len;i++) {
			Creature c=goods.get(i);
			c.init();
		}
		len=bads.size();
		for(int i=0;i<len;i++) {
			Creature c=bads.get(i);
			c.init();
		}
	}

	public void addaction() {
		//开始菜单添加事件
		int len=startitem.length;
		for(int i=0;i<len;i++) {
			MenuItem curitem=startitem[i];
			String itemname=curitem.getText();
			if(itemname.equals("新游戏")) {
				curitem.setOnAction((ActionEvent event)->{
					//如果是正在战斗或者重放则不处理
					if(newgame) {
						bottombox.add(new Label("游戏过程中,请等待游戏结束"));
						return;
					}
					else if(replay) {
						bottombox.add(new Label("回放过程中,请等待回放结束"));
						return;
					}
					//如果双方中至少有一方没有选择好阵形则不处理
					if(!goodform) {
						bottombox.add(new Label("你还没有设置葫芦娃阵形,不能开始游戏"));
						return;
					}
					if(!badform) {
						bottombox.add(new Label("你还没有设置妖怪阵形,不能开始游戏"));
						return;
					}
					FileChooser filechooser=new FileChooser();
					filechooser.setTitle("保存战斗文件");
					FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
			        filechooser.getExtensionFilters().add(extFilter);
			        File file=filechooser.showSaveDialog(stage);
			        OutputStreamWriter osw=null;
			        try {
			        	if(file!=null) {
			        		osw = new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath()), "UTF-8");
							osw.write("");
							osw.close();
			        	}
			        	else {
			        		bottombox.add(new Label("请重新选择文件"));
							return;
			        	}
					} catch (FileNotFoundException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
			        newgame=true;
			        bt=new BattleThread(cellpane, cells, positions, goods,bads,file,leftbox,rightbox,bottombox);
			        bt.run();
				});
			}
			else {//回放
				curitem.setOnAction((ActionEvent event)->{
					//如果是正在战斗或者重放则不处理
					replay();
				});
			}
		}
		
		len=gformation.length;
		for(int i=0;i<len;i++) {
			MenuItem item=gformation[i];
			final int j=i;
			item.setOnAction((ActionEvent event)->{
				//如果是正在战斗或者正在回放,则不处理
				if(newgame) {
					bottombox.add(new Label("游戏过程中,请等待游戏结束"));
					return;
				}
				else if(replay) {
					bottombox.add(new Label("回放过程中,请等待回放结束"));
					return;
				}
				formations[j].clear(cells, goods);
				formations[j].setbeings(cells, goods, true);
				goodform=true;
			});
		}
		
		len=bformation.length;
		for(int i=0;i<len;i++) {
			MenuItem item=bformation[i];
			final int j=i;
			item.setOnAction((ActionEvent event)->{
				//如果是正在战斗或者正在回放,则不处理
				if(newgame) {
					bottombox.add(new Label("游戏过程中,请等待游戏结束"));
					return;
				}
				else if(replay) {
					bottombox.add(new Label("回放过程中,请等待回放结束"));
					return;
				}
				formations[j].clear(cells, bads);
				formations[j].setbeings(cells, bads, false);
				badform=true;
			});
		}
	}
}