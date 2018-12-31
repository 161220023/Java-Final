package formation;

import java.util.ArrayList;

import being.Creature;
import gui.Cell;

public class WildGooseRow extends Formation{

	public WildGooseRow() {
		super(FormationType.WildGooseRow);
	}

	@Override
	public void setbeings(Cell[][] cells, ArrayList<Creature> creature, boolean good) {
		if(good) {
			int i;
			for(i=0;i<7;i++) {
				Creature cala=creature.get(i);
				cala.moveto(2+i, 2+i);
				cells[2+i][2+i].setbeing(cala);
			}
		}
		else {
			int i;
			for(i=0;i<7;i++) {
				Creature cala=creature.get(i);
				cala.moveto(2+i, 17-i);
				cells[2+i][17-i].setbeing(cala);
			}
		}
	}

}
