package formation;


import java.util.ArrayList;
import being.Creature;
import gui.Cell;


public class Arrow extends Formation{
	
	public Arrow() {
		super(FormationType.Arrow);
	}
	
	public void setbeings(Cell[][] cells, ArrayList<Creature> creature, boolean good) {
		//System.out.println("设置为锋矢阵");
		if(good) {
			int i;
			for(i=0;i<5;i++) {
				Creature cala=creature.get(i);
				cala.moveto(5, 2+i);
				cells[5][2+i].setbeing(cala);
			}
			Creature cala=creature.get(5);
			cala.moveto(4, 5);
			cells[4][5].setbeing(cala);
			cala=creature.get(6);
			cala.moveto(6, 5);
			cells[6][5].setbeing(cala);
		}
		else {
			int i;
			for(i=0;i<5;i++) {
				Creature cala=creature.get(i);
				cala.moveto(5, 17-i);
				cells[5][17-i].setbeing(cala);
			}
			Creature cala=creature.get(5);
			cala.moveto(4, 14);
			cells[4][14].setbeing(cala);
			cala=creature.get(6);
			cala.moveto(6, 14);
			cells[6][14].setbeing(cala);
		}
	}

}
