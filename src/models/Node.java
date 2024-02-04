package models;

public class Node {
	private int x,y,id;
	private String name;
	private Boolean start,end;
	
	public Node() {
		this.x=this.y=this.id=0;
		this.name=new String();
		this.start=this.end=false;
	}
	
	public Node(int x1, int y1, int id1, String name1, Boolean start1, Boolean end1) {
		this.x=x1;
		this.y=y1;
		this.id=id1;
		this.name = new String(name1);
		this.start=start1;
		this.end=end1;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Boolean getStart() {
		return this.start;
	}
	
	public Boolean getEnd() {
		return this.end;
	}
	
	public void setPosition(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	public void setCost(int cost) {
		this.id=cost;
	}
	
	public int getCost() {
		return this.id;
	}
}
