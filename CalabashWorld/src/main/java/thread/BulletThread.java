package thread;

import java.util.concurrent.TimeUnit;

import being.*;
import formation.Position;
import javafx.animation.*;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class BulletThread extends Thread{

	
	Bullet bullet;
	GridPane pane;
	int row;
	int fromcol;
	int tocol;
	int topixel;
	Position[][] positions;

	
	public BulletThread(GridPane pane, Position[][] positions, Bullet bullet){
		this.bullet=bullet;
		this.pane=pane;
		this.row=bullet.getrow();
		this.fromcol=bullet.getfromcol();
		this.tocol=bullet.gettocol();
		this.topixel=tocol*50+25;
		this.positions=positions;

		
		//播放动画
		pane.add(bullet.getcirc(), 0, row);
		int fromcol=bullet.getfromcol();
		int tocol=bullet.gettocol();
		Circle curcircle=bullet.getcirc();
		int curspeed=bullet.getspeed();
		TranslateTransition translateTransition=new TranslateTransition(Duration.millis(curspeed*Math.abs(fromcol-tocol)), curcircle);
		translateTransition.setFromX(25+fromcol*50);
		translateTransition.setToX(20+tocol*50);
		FadeTransition fadeTransition=new FadeTransition(Duration.millis(100), curcircle);
		fadeTransition.setFromValue(1.0f);
		fadeTransition.setToValue(0.0f);		
		SequentialTransition sequentialTransition=new SequentialTransition(translateTransition,fadeTransition);
        sequentialTransition.play();
	}

	@Override
	public void run() {
		//从(row,fromcol)到(row,tocol)途中若遇到其他阵营的生物则对其造成伤害
		int curpixel=(int)bullet.getcirc().localToParent(0,0).getX();
		int curcol=curpixel/50;
		int precol=-1;
		while(curcol!=tocol) {
			//计算子弹的位置
			if(curcol<0||curcol>=20) return;
			if(curcol!=precol) {
				if(curcol<0||curcol>=20) {
					System.out.println("异常 curcol"+curcol);
					System.out.println("所属生物:"+bullet.getcreature().getname()+", 位置: 从"+bullet.getrow()+", "+bullet.getfromcol()+" 到 "+bullet.gettocol());
				}
				Creature targetcreature=(Creature)positions[row][curcol].getbeing();
				if(targetcreature!=null) {
					Property pro=targetcreature.getproperty();
					Creature attachedcreature=bullet.getcreature();
					if(attachedcreature.getproperty()!=pro){
						targetcreature.hurt(bullet);
					}
				}
				precol=curcol;
			}
			else {
				try {
					TimeUnit.MILLISECONDS.sleep(bullet.getspeed());
				} catch (InterruptedException e) {
					//e.printStackTrace();
				}
			}
			curpixel=(int)bullet.getcirc().localToParent(0,0).getX();
			curcol=curpixel/50;
		}
		if(curcol<0||curcol>=20) return;
		Creature targetcreature=(Creature)positions[row][curcol].getbeing();
		if(targetcreature!=null) {
			Property pro=targetcreature.getproperty();
			Creature attachedcreature=bullet.getcreature();
			if(attachedcreature.getproperty()!=pro){
				targetcreature.hurt(bullet);
			}
		}
		
		
	}
	
	
}