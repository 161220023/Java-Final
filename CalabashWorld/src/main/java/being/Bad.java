package being;

import javafx.scene.paint.Color;

public abstract class Bad extends Creature{

	public Bad(String name, String imgpath, char c, int hp, int attackpower, int defensepower, int movespeed, int around, int attackspeed, int bulletmovespeed, Color bulletcolor) {
		super(name, imgpath, c, hp, attackpower, defensepower, movespeed, around, attackspeed, bulletmovespeed, bulletcolor);
		// TODO 自动生成的构造函数存根
		p=Property.BAD;
	}
	
}
