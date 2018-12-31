package being;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Bullet{
	
	Creature attachedcreature;
	int movespeed;//移动一格的时间,单位ms
	Circle bulletcirc;
	int row;
	int fromcol;
	int tocol;
	
	public Bullet(Creature c, int movespeed, Color color, int row, int fromcol, int tocol){
		this.attachedcreature=c;
		this.movespeed=movespeed;
		bulletcirc=new Circle();
		bulletcirc.setRadius(5);
		bulletcirc.setFill(color);
		this.row=row;
		this.fromcol=fromcol;
		this.tocol=tocol;
	}
	
	public Creature getcreature() {
		return attachedcreature;
	}
	
	public int getspeed() {
		return movespeed;
	}
	
	public Circle getcirc() {
		return bulletcirc;
	}
	
	public int getrow() {
		return row;
	}
	
	public int getfromcol() {
		return fromcol;
	}
	
	public int gettocol() {
		return tocol;
	}
}
