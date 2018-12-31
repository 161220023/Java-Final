package gui;


import javafx.application.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import annotation.*;


@Author(name="Caroline",number="161220023",email="161220023@smail.nju.edu.cn")
public class Main extends Application {
	
	final int HEIGHT=750;
	final int WIDTH=1350;

	//主要面板、布局
	Scene scene;
	BorderPane border;
	GridPane gp;
	MenuBar menubar;
	VBox leftbox;
	HBox rightbox;
	VBox rightleftbox;
	VBox rightrightbox;
	HBox wholebottombox;
	BottomBox bottombox;
	
	
	@Override
	public void start(Stage primaryStage) {

		Image image=new Image("/葫芦仙境.jpg");
		BackgroundPosition backpos=new BackgroundPosition(null, 0,false, null, 0, false);
		BackgroundSize backsize=new BackgroundSize(1350,750, false, false, false, false);
		BackgroundImage background=new BackgroundImage(image,null,null,backpos,backsize);
		border=new BorderPane();
		border.setBackground(new Background(background));
		scene = new Scene(border,WIDTH,HEIGHT);
		
		
		//设置菜单栏
		menubar=new MenuBar();
		border.setTop(menubar);
		
		
		//设置中间的格子
		gp=new GridPane();
		border.setCenter(gp);
		
		
		//左边的面板
		leftbox=new VBox();
		leftbox.setPrefSize(150, 735);
		border.setLeft(leftbox);
		
		//右边的面板
		rightbox=new HBox();
		rightbox.setPrefSize(200, 735);
		rightleftbox=new VBox();
		rightleftbox.setPrefSize(150, 735);
		rightrightbox=new VBox();
		rightrightbox.setPrefSize(50, 735);
		
		rightbox.getChildren().addAll(rightleftbox,rightrightbox);
		border.setRight(rightbox);
		
		//底部面板
		wholebottombox=new HBox();
		wholebottombox.setPrefSize(1000, 185);
		Label tmplabel=new Label();
		tmplabel.setPrefWidth(500);
		tmplabel.setPrefHeight(185);
		bottombox=new BottomBox(5);
		bottombox.setPrefSize(300, 185);
		Label tmplabel2=new Label();
		tmplabel2.setPrefWidth(200);
		tmplabel2.setPrefHeight(185);
		border.setBottom(wholebottombox);
		wholebottombox.getChildren().addAll(tmplabel,bottombox,tmplabel2);
		
		
		
		primaryStage.setTitle("葫芦娃大战妖精");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setX(0);
		primaryStage.setY(0);
		primaryStage.show();
		

		World world=new World(primaryStage, gp, menubar, leftbox, rightleftbox, bottombox);
		
		scene.setOnKeyPressed((KeyEvent e)->{
			if(e.getCode()!=KeyCode.L) return;
			if(world!=null)
				world.replay();
		});
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
