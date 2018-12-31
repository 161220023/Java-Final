package gui;


import being.Creature;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CreatureLabel extends GridPane{

	Creature creature;
	final int fullhp;
	
	Label label;
	VBox vbox;
	
	Image image;
	ImageView view;
	Rectangle hp;
	double hpratio;
	Rectangle energy;
	double energyratio;
	Label attackpower;
	Label defensepower;
	
	boolean die;
	
	CreatureLabel(Creature c){
		this.creature=c;
		fullhp=creature.gethp();
		
		this.label=new Label();
		this.vbox=new VBox();
		
		image=creature.getview().getImage();
		view=new ImageView(image);
		view.setFitHeight(80);
		view.setFitWidth(75);
		label.setGraphic(view);
		
		hp=new Rectangle(70,15);
		hp.setFill(Color.RED);
		hpratio=1;
		energy=new Rectangle(70,15);
		energy.setFill(Color.BLUE);
		energyratio=1;
		attackpower=new Label("攻击力:"+creature.getattackpower());
		defensepower=new Label("防御力:"+creature.getdefensepower());
		attackpower.setPrefSize(75, 20);
		defensepower.setPrefSize(75, 20);
		
		vbox.getChildren().addAll(hp,energy,attackpower,defensepower);
		this.add(label, 0, 0);
		this.add(vbox, 1, 0);
		
		die=false;
	}
	
	public void die() {
		die=true;
		//血量设置成空,能量设置成空
		hpratio=0;
		energyratio=0;
		hp.setWidth(0);
		energy.setWidth(0);
		//图片设置成黑白
		creature.newimage();
		int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        WritableImage grayImage = new WritableImage(width,height);
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriterGray = grayImage.getPixelWriter();
        for(int i=0;i<width;i++) {
            for(int j=0;j<height;j++) {
                Color color = pixelReader.getColor(i, j);
                color = color.grayscale();
                pixelWriterGray.setColor(i,j,color);
            }
        }
		view.setImage(grayImage);
		label.setGraphic(view);
	}
	
	public void refresh() {//刷新
		if(die) return;
		int remainedhp=creature.getremainedhp();
		hpratio=(double)remainedhp/fullhp;
		hp.setWidth(70*hpratio);
	}
	
	public void refresh(int remainedhp) {
		hpratio=(double)remainedhp/fullhp;
		hp.setWidth(70*hpratio);
	}
	
	public void init() {//初始化
		image=creature.getview().getImage();
		view.setImage(image);
		label.setGraphic(view);
		hp.setWidth(70);
		energy.setWidth(70);
		hpratio=1;
		energyratio=1;
		die=false;
	}
}
