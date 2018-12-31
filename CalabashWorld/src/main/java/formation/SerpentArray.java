package formation;

import java.util.ArrayList;

import being.Creature;
import gui.Cell;


public class SerpentArray extends Formation{

	public SerpentArray() {
		super(FormationType.SerpentArray);
	}

	@Override
	public void setbeings(Cell[][] cells, ArrayList<Creature> creature, boolean good) {
		if(good) {
			int i;
			for(i=0;i<7;i++) {
				Creature cala=creature.get(i);
				cala.moveto(2+i, 4);
				cells[2+i][4].setbeing(cala);
			}
		}
		else {
			int i;
			for(i=0;i<7;i++) {
				Creature cala=creature.get(i);
				cala.moveto(2+i, 15);
				cells[2+i][15].setbeing(cala);
			}
		}
	}

}
