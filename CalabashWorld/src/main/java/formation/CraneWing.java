package formation;

import java.util.ArrayList;

import being.Creature;
import gui.Cell;

public class CraneWing extends Formation{

	public CraneWing() {
		super(FormationType.CraneWing);
	}

	@Override
	public void setbeings(Cell[][] cells, ArrayList<Creature> creature, boolean good) {
		if(good) {
			int i;
			for(i=0;i<4;i++) {
				Creature cala=creature.get(i);
				cala.moveto(2+i, 2+i);
				cells[2+i][2+i].setbeing(cala);
			}
			for(;i<7;i++) {
				Creature cala=creature.get(i);
				cala.moveto(2+i, 8-i);
				cells[2+i][8-i].setbeing(cala);
			}
		}
		else {
			int i;
			for(i=0;i<4;i++) {
				Creature cala=creature.get(i);
				cala.moveto(2+i, 17-i);
				cells[2+i][17-i].setbeing(cala);
			}
			for(;i<7;i++) {
				Creature cala=creature.get(i);
				cala.moveto(2+i, 11+i);
				cells[2+i][11+i].setbeing(cala);
			}
		}
	}
	
}
