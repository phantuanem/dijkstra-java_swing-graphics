package models;

public class Edge {
	private int x1,y1,x2,y2,id1,id2;
	private Node nodeCost;
	private Boolean is_shortest_path;
	
	public Edge(int x1, int y1, int x2,int y2,int id1,int id2) {
		this.x1=x1;
		this.y1=y1;
		this.x2=x2;
		this.y2=y2;
		this.id1=id1;
		this.id2=id2;
		this.is_shortest_path=false;
		this.nodeCost = new Node((x1+x2)/2,(y1+y2)/2,1,"",false,false);
	}
	
	public int getX1() {
		return this.x1;
	}
	
	public int getX2() {
		return this.x2;
	}
	
	public int getY1() {
		return this.y1;
	}
	
	public int getY2() {
		return this.y2;
	}
	
	public int getID1() {
		return this.id1;
	}
	
	public int getID2() {
		return this.id2;
	}
	
	public Boolean isShortestPath() {
		return this.is_shortest_path;
	}
	
	public Node getNodeCost() {
		return this.nodeCost;
	}
	
	public void setPosition(int x1, int y1, int x2, int y2) {
		this.x1=x1;
		this.x2=x2;
		this.y1=y1;
		this.y2=y2;
		this.nodeCost = new Node((x1+x2)/2,(y1+y2)/2,this.nodeCost.getID(),"",false,false);
	}
	
	public void setIsShortestPath(Boolean ok) {
		this.is_shortest_path=ok;
	}
	
	public Boolean getIsShortestPath() {
		return this.is_shortest_path;
	}
}
