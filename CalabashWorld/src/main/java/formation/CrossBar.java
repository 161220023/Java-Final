package formation;

import java.util.ArrayList;

import being.Creature;
import gui.Cell;

public class CrossBar extends Formation{

	public CrossBar() {
		super(FormationType.CrossBar);
	}

	@Override
	public void setbeings(Cell[][] cells, ArrayList<Creature> creature, boolean good) {
		if(good) {
			int i;
			for(i=0;i<4;i++) {
				Creature cala=creature.get(i);
				cala.moveto(2*(i+1), 3);
				cells[2*(i+1)][3].setbeing(cala);
			}
			for(;i<7;i++) {
				Creature cala=creature.get(i);
				cala.moveto(2*i-5, 4);
				cells[2*i-5][4].setbeing(cala);
			}
		}
		else {
			int i;
			for(i=0;i<4;i++) {
				Creature cala=creature.get(i);
				cala.moveto(2*(i+1), 16);
				cells[2*(i+1)][16].setbeing(cala);
			}
			for(;i<7;i++) {
				Creature cala=creature.get(i);
				cala.moveto(2*i-5, 15);
				cells[2*i-5][15].setbeing(cala);
			}
		}
	}

}
