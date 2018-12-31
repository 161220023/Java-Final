package being;

import java.util.ArrayList;
import formation.Position;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import thread.CreatureThread;

public abstract class Creature extends Being implements Battle{
	
	Property p;//生物属性,有好有坏
	
	int hp;             		//血量
	int remainedhp;     		//剩余血量
	int attackpower;    		//攻击力
	int defensepower;   		//防御力
	int movespeed;				//移动速度:每移动一格休息的时间,ms
	int around;					//攻击范围,单位:格
	int attackspeed;			//攻击速度:每发出一颗子弹休息的时间,ms
	int bulletmovespeed; 		//子弹移动速度
	Color bulletcolor;			//子弹颜色
	boolean alive;				//是否还活着
	boolean playanimation;		//决定FX线程是否播放动画
	ArrayList<Bullet> bullets;	//待发子弹
	CreatureThread cthread;				//对应的生物线程
	
	public Creature(String name, String imgpath, char c, int hp, int attackpower, int defensepower, int movespeed, int around, int attackspeed, int bulletmovespeed, Color color){
		super(name, imgpath, c);
		this.hp=hp;
		this.remainedhp=hp;
		this.attackpower=attackpower;
		this.defensepower=defensepower;
		this.movespeed=movespeed;
		this.around=around;
		this.attackspeed=attackspeed;
		this.bulletmovespeed=bulletmovespeed;
		this.bulletcolor=color;
		this.alive=true;
		this.playanimation=false;
		bullets=new ArrayList<>();
	}

	public void setthread(CreatureThread t) {
		this.cthread=t;
	}
	
	public boolean ifwin() {
		return cthread.getwin();
	}

	public void hurt(Bullet enemybullet) {
		if(!alive) return;
		Creature sourcecreature=enemybullet.getcreature();
		int sourceattackpower=sourcecreature.getattackpower();
		if(sourceattackpower>defensepower) {
			remainedhp-=(sourceattackpower-defensepower);
		}
		else {
			if((defensepower-sourceattackpower)>10)
				remainedhp-=5;
			else
				remainedhp-=10;
		}
		if(remainedhp<=0) {
			remainedhp=0;
			die();
		}
	}
	
	public void battle(int tocol) {
		bullets.add(new Bullet(this,bulletmovespeed,bulletcolor,row,col,tocol));
	}
	
	public void prepareattack(int tocol) {
		if(Math.abs(tocol-col)<=around) {
			battle(tocol);
		}
		else{
			System.out.println("异常情况:手不够长 in prepareattack, tocol:"+tocol);
			System.exit(0);
		}
	}
	
	public void init() {
		super.init();
		remainedhp=hp;
		this.alive=true;
		this.playanimation=false;
		bullets.clear();
		cthread=null;
	}
	
	public int locateenemy() {
		//上半空间
		int len=20;
		Position[] tmppos;
		for(int i=0;i<row;i++) {
			tmppos=positions[i];
			for(int j=0;j<len;j++) {
				Creature otherb=(Creature)tmppos[j].getbeing();
				if(otherb==null) continue;
				Property otherp=otherb.getproperty();
				if(otherp!=p) {
					if(j<col) 		return 1;
					else if(col==j) return 2;
					else 			return 3;
				}
			}
		}
		//同一行
		tmppos=positions[row];
		for(int j=0;j<len;j++) {
			if(j==col) continue;
			Creature otherb=(Creature)tmppos[j].getbeing();
			if(otherb==null) continue;
			Property otherp=otherb.getproperty();
			if(otherp!=p) {
				if(j<col) return 4;
				else return 5;
			}
		}
		//下半空间
		for(int i=row+1;i<11;i++) {
			tmppos=positions[i];
			for(int j=0;j<len;j++) {
				Creature otherb=(Creature)tmppos[j].getbeing();
				if(otherb==null) continue;
				Property otherp=otherb.getproperty();
				if(otherp!=p) {
					if(j<col) return 6;
					else if(col==j) return 7;
					else return 8;
				}
			}
		}
		return -1;
	}
	
	public int enemycolpos() {
		//同一行
		Position[] tmppos=positions[row];
		int len=20;
		//System.out.println(name+" "+row+", "+col+" 在定位敌人");
		for(int i=1;i<=around;i++) {//在(col-around,col+around)的攻击范围内
			if(col-i>=0) {
				Creature otherb=(Creature)tmppos[col-i].getbeing();
				//System.out.println(row+", "+(col-i)+"的位置:"+otherb);
				if(otherb!=null) {
					Property otherp=otherb.getproperty();
					if(otherp!=p) return col-i;
				}
			}
			if(col+i<len) {
				Creature otherb=(Creature)tmppos[col+i].getbeing();
				//System.out.println(row+", "+(col+i)+"的位置:"+otherb);
				if(otherb!=null) {
					Property otherp=otherb.getproperty();
					if(otherp!=p) return col+i;
				}
			}
		}
		return -1;
	}

	public void die() {
		if(!this.alive){
			System.out.println("double die:"+this.getname());
			System.exit(0);
			//return;
		}
		this.alive=false;
		//要打断相应的生物线程
        if(cthread!=null) {
        	cthread.interrupt();
        }
        else {
        	System.out.println("异常 in die, 没有设置 cthread");
        	System.exit(0);
        }
		
		/*//生物消失
		FadeTransition fadeTransition=new FadeTransition(Duration.millis(500), this.getview());
		fadeTransition.setFromValue(1.0f);
        fadeTransition.setToValue(0.0f);
        fadeTransition.play();
        this.moveto(-1, -1);
        this.alive=false;*/
	}
	
	public void setanimation(boolean animation) {
		this.playanimation=animation;
	}
	
	public Property getproperty() {
		return p;
	}
	
	public Circle getbullet() {
		Circle circle=new Circle();
		circle.setFill(bulletcolor);
		circle.setRadius(5);
		return circle;
	}
	
	public int gethp() {
		return hp;
	}
	
	public int getremainedhp() {
		return remainedhp;
	}
	
	public int getattackpower() {
		return attackpower;
	}
	
	public int getdefensepower() {
		return defensepower;
	}
	
	public int getmovespeed() {
		return movespeed;
	}
	
	public int getbulletmovespeed() {
		return bulletmovespeed;
	}
	
	public int getattackspeed() {
		return attackspeed;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public ArrayList<Bullet> getbullets(){
		return bullets;
	}

	public boolean getanimation() {
		return this.playanimation;
	}
}