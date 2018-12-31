package being;

import annotation.Author;
import formation.Position;
import javafx.scene.image.*;

@Author(name="chy",number="161220023",email="161220023@smail.nju.edu.cn")
public class Being {
	
	String name;
	char c;
	Image image;
	ImageView view;
	String imgpath;

	Position[][] positions;
	
	int row,col;

	public Being(String name, String imgpath, char c){
		view=new ImageView(new Image("/"+imgpath));
		this.name=name;
		this.imgpath=imgpath;
		this.c=c;
		view.setFitHeight(50);
		view.setFitWidth(50);
		row=-1;
		col=-1;
	}
	
	public Being(Position[][] pos) {
		this.positions=pos;
		row=-1;
		col=-1;
	}
	
	public void setback(Position[][] pos) {
		this.positions=pos;
	}
	
	public boolean hasbeing(int row, int col) {
		Being being=positions[row][col].getbeing();
		return being!=null;
	}

	public void moveto(int newrow, int newcol) {
		//先离开原来的格子,再前进到下一个格子
		int prerow=row;
		int precol=col;
		if(newrow>=0&&newcol>=0&&newrow<=10&&newcol<=19) {
			if(positions[newrow][newcol].setbeing(this)) {
				if(prerow>=0&&precol>=0&&prerow<=10&&precol<=19)
					positions[prerow][precol].clear();
			}
		}
	}
	
	public void leave() {
		if(row>=0&&col>=0&&row<=10&&col<=19)
			positions[row][col].clear();
		row=-1;
		col=-1;
	}
	
	public void moveright() {
		if(col==19) {
			System.out.println("已经到最右边了,不能再往右了");
			return;
		}
		moveto(row,col+1);
	}
	
	public void moveleft() {
		if(col==0) {
			System.out.println("已经到最左边了,不能再往左了");
			return;
		}
		moveto(row,col-1);
	}
	
	public void moveup() {
		if(row==0) {
			System.out.println("已经到最上边了,不能再往上了");
			return;
		}
		moveto(row-1,col);
	}
	
	public void movedown() {
		if(row==10) {
			System.out.println("已经到最下边了,不能再往下了");
			return;
		}
		moveto(row+1,col);
	}

	public void init() {
		moveto(-1,-1);
	}
	
	public void setrow(int row) {
		this.row=row;
	}
	
	public void setcol(int col) {
		this.col=col;
	}
	
	public Position[][] getpositions(){
		return positions;
	}
	
	public void newimage() {
		/*File directory = new File("");
		String path=null;
        try {
			path = directory.getCanonicalPath();
			path+="\\src\\main\\additions\\";
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		view=new ImageView(new Image("file:"+path+imgpath));*/
		view=new ImageView(new Image("/"+imgpath));
		view.setFitHeight(50);
		view.setFitWidth(50);
	}
	
	public int getrow() {
		return row;
	}
	
	public int getcol() {
		return col;
	}
	
	public String getname() {
		return name;
	}
	
	public char getchar() {
		return c;
	}
	
	public ImageView getview() {
		return view;
	}
}
