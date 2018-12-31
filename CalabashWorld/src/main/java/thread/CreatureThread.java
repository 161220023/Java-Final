package thread;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import being.*;


public class CreatureThread extends Thread{
	
	Creature creature;
	Random r=new Random();
	boolean exit;
	boolean win;

	public CreatureThread(Creature creature) {
		this.creature=creature;
		creature.setthread(this);
		this.exit=false;
		this.win=false;
	}
	
	public boolean moveleft() {
		int creaturerow=creature.getrow();
		int creaturecol=creature.getcol();
		if(creaturecol>0&&!creature.hasbeing(creaturerow, creaturecol-1)) {
			creature.moveleft();
			return true;
		}
		return false;
	}
	
	public boolean moveright() {
		int creaturerow=creature.getrow();
		int creaturecol=creature.getcol();
		if(creaturecol<19&&!creature.hasbeing(creaturerow, creaturecol+1)) {
			creature.moveright();
			return true;
		}
		return false;
	}
	
	public boolean moveup() {
		int creaturerow=creature.getrow();
		int creaturecol=creature.getcol();
		if(creaturerow>0&&!creature.hasbeing(creaturerow-1, creaturecol)) {
			creature.moveup();
			return true;
		}
		return false;
	}
	
	public boolean movedown() {
		int creaturerow=creature.getrow();
		int creaturecol=creature.getcol();
		if(creaturerow<10&&!creature.hasbeing(creaturerow+1, creaturecol)) {
			creature.movedown();
			return true;
		}
		return false;
	}
	
	public void exit() {
		exit=true;
	}
	
	public boolean getwin() {
		return win;
	}
	
	@Override
	public void run() {
		//当自己还存活时,判断跟自己同一行是否有敌人.发射子弹
		//发射出的子弹新开一个线程
		//如果没有敌人则进行移动,移动不能快于移动的速度
		while(creature.isAlive()&&!exit) {
			//如果前方没有敌人则判断敌人在哪个方向从而决定向哪个方向走
			int col=creature.enemycolpos();
			if(col!=-1) {//判断方向进行攻击
				creature.prepareattack(col);
				//睡眠
				try {
					TimeUnit.MILLISECONDS.sleep(creature.getattackspeed());
				} catch (InterruptedException e) {
					
				}
			}
			else {//同一行没有敌人,判断哪个方向有敌人
				int colpos=creature.locateenemy();
				boolean move=false;
				if(colpos!=-1) {
					//有一定的概率随机走
					int randomint=r.nextInt(100);
					if(randomint<80) {
						switch(colpos) {
						case 1:
							randomint=r.nextInt(100);
							if(randomint<50) {//先向上走再向左走
								if(!moveup())
									move=moveleft();
								else
									move=true;
							}
							else {//先向左走再向上走
								if(!moveleft())
									move=moveup();
								else
									move=true;
							}
							break;
						case 2:
							//先向上走
							if(!moveup()) {
								randomint=r.nextInt(100);
								if(randomint<50) {//先向左走再向右走
									if(!moveleft())
										move=moveright();
									else
										move=true;
								}
								else {//先向右走再向左走
									if(!moveright())
										move=moveleft();
									else
										move=true;
								}
							}
							else
								move=true;
							break;
						case 3:
							randomint=r.nextInt(100);
							if(randomint<50) {//先向上走再向右走
								if(!moveup())
									move=moveright();
								else
									move=true;
							}
							else {//先向右走再向上走
								if(!moveright())
									move=moveup();
								else
									move=true;
							}
							break;
						case 4:
							//先向左走
							if(!moveleft()) {
								randomint=r.nextInt(100);
								if(randomint<50) {//先向上走再向下走
									if(!moveup())
										move=movedown();
									else
										move=true;
								}
								else {//先向下走再向上走
									if(!movedown())
										move=moveup();
									else
										move=true;
								}
							}
							else
								move=true;
							break;
						case 5:
							//先向右走
							if(!moveright()) {
								randomint=r.nextInt(100);
								if(randomint<50) {//先向上走再向下走
									if(!moveup())
										move=movedown();
									else
										move=true;
								}
								else {//先向下走再向上走
									if(!movedown())
										move=moveup();
									else
										move=true;
								}
							}
							else
								move=true;
							break;
						case 6:
							randomint=r.nextInt(100);
							if(randomint<50) {//先向下走再向左走
								if(!movedown())
									move=moveleft();
								else
									move=true;
							}
							else {//先向左走再向下走
								if(!moveleft())
									move=movedown();
								else
									move=true;
							}
							break;
						case 7:
							//先向下走
							if(!movedown()) {
								randomint=r.nextInt(100);
								if(randomint<50) {//先向左走再向右走
									if(!moveleft())
										move=moveright();
									else
										move=true;
								}
								else {//先向右走再向左走
									if(!moveright())
										move=moveleft();
									else
										move=true;
								}
							}
							else
								move=true;
							break;
						case 8:
							randomint=r.nextInt(100);
							if(randomint<50) {//先向下走再向右走
								if(!movedown())
									move=moveright();
								else
									move=true;
							}
							else {//先向右走再向下走
								if(!moveright())
									move=movedown();
								else
									move=true;
							}
							break;
						}
					}
					if(!move) {
						randomint=r.nextInt(4)+1;
						switch(randomint) {
						case 1:
							moveup();
							break;
						case 2:
							movedown();
							break;
						case 3:
							moveleft();
							break;
						case 4:
							moveright();
						}
					}
					try {
						TimeUnit.MILLISECONDS.sleep(creature.getmovespeed());
					} catch (InterruptedException e) {
						
					}
				}
				else {
					win=true;
				}
			}
		}
		if(!exit)
			creature.setanimation(true);
	}
}
