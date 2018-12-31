package formation;

import java.util.ArrayList;

import being.Creature;
import gui.Cell;

public class CrescentMoon extends Formation{

	public CrescentMoon() {
		super(FormationType.CrescentMoon);
	}

	@Override
	public void setbeings(Cell[][] cells, ArrayList<Creature> creature, boolean good) {
		if(good) {
			int i;
			for(i=0;i<3;i++) {
				Creature cala=creature.get(i);
				cala.moveto(3+i, 3+i);
				cells[3+i][3+i].setbeing(cala);
			}
			for(;i<5;i++) {
				Creature cala=creature.get(i);
				cala.moveto(3+i, 7-i);
				cells[3+i][7-i].setbeing(cala);
			}
			Creature cala=creature.get(5);
			cala.moveto(5, 3);
			cells[5][3].setbeing(cala);
			cala=creature.get(6);
			cala.moveto(5, 4);
			cells[5][4].setbeing(cala);
		}
		else {
			int i;
			for(i=0;i<3;i++) {
				Creature cala=creature.get(i);
				cala.moveto(3+i, 16-i);
				cells[3+i][16-i].setbeing(cala);
			}
			for(;i<5;i++) {
				Creature cala=creature.get(i);
				cala.moveto(3+i, 12+i);
				cells[3+i][12+i].setbeing(cala);
			}
			Creature cala=creature.get(5);
			cala.moveto(5, 16);
			cells[5][16].setbeing(cala);
			cala=creature.get(6);
			cala.moveto(5, 15);
			cells[5][15].setbeing(cala);
		}
	}
}
