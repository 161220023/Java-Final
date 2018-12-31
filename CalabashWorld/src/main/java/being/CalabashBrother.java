package being;

import javafx.scene.paint.Color;

public abstract class CalabashBrother extends Good implements Battle{
	public CalabashBrother(String name, String imgpath, char c, int hp, int attackpower, int defensepower, int movespeed, int around, int attackspeed, int bulletmovespeed, Color bulletcolor) {
		super(name, imgpath, c, hp, attackpower, defensepower, movespeed, around, attackspeed, bulletmovespeed, bulletcolor);
	}
}
