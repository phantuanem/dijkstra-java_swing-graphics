package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import algorihm.Dijkstra;
import graph.Point;
import models.DataGraph;
import models.Edge;
import models.GraphFile;
import models.Node;

public class PanelDraw extends JPanel implements MouseListener, MouseMotionListener{
	private Graphics2D g;
	
	private List<Node> nodes;
	private List<Edge> edges;
	private int countNode;
	private int radius;
	private Node nodeSelected=null;
	private MouseEvent drag=null;
	private JFrame frameMain;
	private int[][] matrix;
	private List<Integer> listNodePath; // chứa các đỉnh của đường đi ngắn nhất
	private JLabel lblPath, lblCost,lblLT;
	private String pathFileSave=null;
	private int desNode, startNode;
	
	public PanelDraw() {
		this.nodes=new ArrayList<Node>();
		this.edges=new ArrayList<Edge>();
		this.countNode=1;
		this.radius=36;
		this.startNode=1;
		this.desNode=1;
		
		addMouseListener(this);
        addMouseMotionListener(this);
	}
	
	@Override
	public void paintComponent(Graphics g1) {
		super.paintComponent(g1);
		this.g = (Graphics2D) g1;
		
		this.handleAlDijkstra();
		
		if(this.nodeSelected != null && this.drag != null) {// vẽ đường thảng khi đang drag
			this.g.setColor(new Color(128, 138, 176));
			this.g.setStroke(new BasicStroke(3));
			this.g.drawLine(this.nodeSelected.getX(), this.nodeSelected.getY(), this.drag.getX(), this.drag.getY());
		}
		
		// xoa cung hai dau chung mot nut
		for(int i=0;i<this.edges.size();i++) {
			Edge edge = this.edges.get(i);
			if(edge.getID1() == edge.getID2()) {
				this.edges.remove(i);
			}
		}
		this.drawEdges();		
		this.drawNodes();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.isControlDown()) {
			this.handleDragNodeCtrl(e);
		} else {
			// vẽ đường thẳng khi drag
			if(this.nodeSelected != null) {
				this.drag=e;
				repaint();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getClickCount()==1 && !e.isShiftDown() && !e.isAltDown()) {
			this.handleOneClick(e);
		} else if(e.getClickCount()==2 && !e.isShiftDown() && !e.isAltDown()) {
			this.handleDoubleClick(e);
		} else if(e.getClickCount()==1 && !e.isShiftDown() && e.isAltDown() && !e.isControlDown()) {
			this.handleDeleteEdge(e);
		} else if(e.getClickCount()==1 && e.isShiftDown() && !e.isAltDown() && !e.isControlDown()) {
			this.handleDeleteNode(e);
		} else if(e.getClickCount() == 1 && e.isControlDown() && e.isShiftDown() && !e.isAltDown()) {
			this.setNodeStart(e);
		} else if(e.getClickCount() == 1 && e.isControlDown() && !e.isShiftDown() && e.isAltDown()) {
			this.setNodeEnd(e);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub// in
		for(int i=this.nodes.size()-1;i>=0;i--) {
			Node node=this.nodes.get(i);
			if(this.calculateCH(node.getX(),node.getY(),e.getX(),e.getY()) <= this.radius/2) {
				this.nodeSelected=node;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub// out
		if(this.nodeSelected != null && this.drag != null) {
			for(int i=this.nodes.size()-1;i>=0;i--) {
				Node node=this.nodes.get(i);
				if(this.calculateCH(node.getX(),node.getY(),e.getX(),e.getY()) <= this.radius/2) {
					Edge temp_edge = new Edge(this.nodeSelected.getX(),this.nodeSelected.getY(),node.getX(),node.getY(),this.nodeSelected.getID(),node.getID());
					Boolean check=true;
					for(Edge ed: this.edges) {
						if((ed.getID1()==temp_edge.getID1() && ed.getID2()==temp_edge.getID2())||(ed.getID1()==temp_edge.getID2() && ed.getID2()==temp_edge.getID1())) {
							check=false;
						}
					}
					if(check) {
						this.edges.add(temp_edge);
					} else {
						System.out.println("Khong the them cung");
					}
					break;
				}
			}
		}
		this.nodeSelected=null;
		this.drag=null;
		repaint();
	}
	
	private void handleOneClick(MouseEvent e) {
		Boolean check=true;
		for(int i=this.edges.size()-1;i>=0;i--) {
			Edge edge = this.edges.get(i);
			int r=(this.radius-10)/2;
			int ch = calculateCH(edge.getNodeCost().getX(), edge.getNodeCost().getY(), e.getX(), e.getY());
			if(ch<=r) {
				check=false;
				break;
			}
		}
		if(check) {
			this.nodes.add(new Node(e.getX(),e.getY(),this.countNode,String.valueOf(this.countNode),false,false));
			if(this.desNode == this.countNode-1) {
				this.desNode=this.countNode;
			}
			this.countNode++;
		}
		repaint();
	}
	
	private void handleDoubleClick(MouseEvent e) {
		for(int i=this.edges.size()-1;i>=0;i--) {
			Edge edge = this.edges.get(i);
			int r=(this.radius-10)/2;
			int ch = calculateCH(edge.getNodeCost().getX(), edge.getNodeCost().getY(), e.getX(), e.getY());
			if(ch<=r) {
				JDialog dia = new JDialog(this.frameMain, true);
				dia.setLayout(null);
				dia.setTitle("Cập nhật chi phí");
				
				JSeparator separator = new JSeparator();
				separator.setBounds(0, 0, 300, 2);
				dia.add(separator);
				
				JLabel lbl = new JLabel("Nhập chi phí");
				lbl.setFont(new Font("Arial", Font.PLAIN, 14));
				lbl.setBounds(10, 5, 260, 30);
				dia.add(lbl);
				
				JTextField txtCost = new JTextField();
				txtCost.setBackground(Color.white);
				txtCost.setFont(new Font("Arial", Font.PLAIN, 14));
				txtCost.setBounds(10, 37, 260, 30);
				txtCost.addKeyListener(new KeyAdapter() {
			         public void keyPressed(KeyEvent e) {
			             int key = e.getKeyCode();
			             if (key == KeyEvent.VK_ENTER) {
			            	 try {
									if(txtCost.getText() != null) {
										int cost = Integer.parseInt(txtCost.getText());
										if(cost >= 0) {
											edge.getNodeCost().setCost(cost);
											dia.dispose();
										}
									}
							    } catch (NumberFormatException nfe) {
							    }
			                }
			             }
			           });
				dia.add(txtCost);
				
				JButton btnSetCost = new JButton("Lưu");
				btnSetCost.setFont(new Font("Tahoma", Font.PLAIN, 14));
				btnSetCost.setBounds(10, 71, 127, 29);
				btnSetCost.setFocusable(false);
				btnSetCost.setRolloverEnabled(false);
				btnSetCost.setBackground(Color.white);
				btnSetCost.setCursor(new Cursor(Cursor.HAND_CURSOR));
				btnSetCost.setBounds(10, 75, 80, 30);
				btnSetCost.addActionListener(eventbtn->{
					try {
						if(txtCost.getText() != null) {
							int cost = Integer.parseInt(txtCost.getText());
							if(cost >= 0) {
								edge.getNodeCost().setCost(cost);
								dia.dispose();
							}
						}
				    } catch (NumberFormatException nfe) {
				    }
					
				});
				dia.add(btnSetCost);
				
				JButton btnCancel = new JButton("Hủy");
				btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 14));
				btnCancel.setBounds(10, 71, 127, 29);
				btnCancel.setFocusable(false);
				btnCancel.setRolloverEnabled(false);
				btnCancel.setBackground(Color.white);
				btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
				btnCancel.setBounds(100, 75, 80, 30);
				btnCancel.addActionListener(eventbtn->{
					dia.dispose();
				});
				dia.add(btnCancel);
				
				dia.setBounds(this.frameMain.getX() + 100, this.frameMain.getY() + 100, 297, 155);
				dia.getContentPane().setBackground(Color.white);
				dia.setVisible(true);
				break;
			}
		}
		repaint();
	}
	
	private void drawNodes() {
		this.nodes.forEach(node->{
			int r=this.radius/2;
			
			this.g.setStroke(new BasicStroke(2));
			this.g.setColor(new Color(245, 255, 105));
			this.g.fillOval(node.getX()-r, node.getY()-r, this.radius, this.radius);
			
			if(node.getID()==this.startNode || node.getID()==this.desNode) {
				this.g.setColor(new Color(94, 86, 7));
				this.g.setStroke(new BasicStroke(4));
			} else {
				this.g.setColor(new Color(89, 145, 255));
				this.g.setStroke(new BasicStroke(3));
			}
			
			this.g.drawOval(node.getX()-r, node.getY()-r, this.radius, this.radius);
			this.g.setStroke(new BasicStroke(3));
			
			FontMetrics fm = g.getFontMetrics();
		    g.setColor(new Color(48, 48, 37));
	        double t_width = fm.getStringBounds(node.getName(), g).getWidth();
		    g.drawString(node.getName(),(int)(node.getX() - t_width / 2), (node.getY() + fm.getMaxAscent() / 2));
		});
	}
	
	public void drawEdges() {
		this.edges.forEach(edge -> {
			if(edge.getIsShortestPath()) {
				this.g.setStroke(new BasicStroke(5));
				this.g.setColor(new Color(0, 255, 68));
			} else {
				this.g.setStroke(new BasicStroke(3));
				this.g.setColor(new Color(38, 44, 66));
			}
			this.g.drawLine(edge.getX1(),edge.getY1(),edge.getX2(),edge.getY2());
			
			Node node = edge.getNodeCost();
			int r=(this.radius-10)/2;
			
			this.g.setStroke(new BasicStroke(2));
			this.g.setColor(new Color(204, 255, 253));
			this.g.fillOval(node.getX()-r, node.getY()-r, this.radius-10, this.radius-10);
			
			this.g.setColor(new Color(43, 43, 43));
			this.g.setStroke(new BasicStroke(2 ));
			this.g.drawOval(node.getX()-r, node.getY()-r, this.radius-10, this.radius-10);
			
			FontMetrics fm = g.getFontMetrics();
		    g.setColor(new Color(48, 48, 37));
	        double t_width = fm.getStringBounds(String.valueOf(node.getID()), g).getWidth();
		    g.drawString(String.valueOf(node.getID()),(int)(node.getX() - t_width / 2), (node.getY() + fm.getMaxAscent() / 2));
		});
	}
	
	private void handleDragNodeCtrl(MouseEvent e) {
		for(int i=this.nodes.size()-1;i>=0;i--) {
			Node node = this.nodes.get(i);
			if(this.calculateCH(node.getX(), node.getY(), e.getX(), e.getY())<=(this.radius/2)){
				node.setPosition(e.getX(),e.getY());
				for(Edge edge: this.edges) {
					if(edge.getID1() == node.getID()) {
						edge.setPosition(e.getX(),e.getY(),edge.getX2(),edge.getY2());
					} else if(edge.getID2() == node.getID()){
						edge.setPosition(edge.getX1(),edge.getY1(),e.getX(),e.getY());
					}
				}
				repaint();
				break;
			}
		}
	}
	
	private void handleDeleteNode(MouseEvent e) {
		for(int i=this.nodes.size()-1;i>=0;i--) {
			Node node=this.nodes.get(i);
			int ch = this.calculateCH(node.getX(),node.getY(),e.getX(),e.getY());
			if(ch <= this.radius/2) {
				for(int j=this.edges.size()-1;j>=0;j--) {
					Edge edge = this.edges.get(j);
					if(edge.getID1() == node.getID()) {
						this.edges.remove(j);
					}
					if(edge.getID2() == node.getID()) {
						this.edges.remove(j);
					}
				}
				Boolean isStartNode=false;
				Boolean isDesNode=false;
				if(this.nodes.get(i).getID()==this.startNode) {
					isStartNode=true;
				}
				if(this.nodes.get(i).getID()==this.desNode) {
					isDesNode=true;
				}
				this.nodes.remove(i);
				if(isStartNode && this.nodes.size() > 0) {
					this.startNode=this.nodes.get(0).getID();
				}
				if(isDesNode && this.nodes.size() > 0) {
					this.desNode=this.nodes.get(this.nodes.size()-1).getID();
				}
				if(this.nodes.size()==0) {
					this.startNode=this.countNode;
					this.desNode=this.countNode;
				}
				repaint();
				break;
			}
		}
	}
	
	private void handleDeleteEdge(MouseEvent e) {
		if(this.edges.size() > 0) {
			for(int i=this.edges.size()-1;i>=0;i--) {
				Node lable = this.edges.get(i).getNodeCost();
				int ch = calculateCH(lable.getX(),lable.getY(),e.getX(),e.getY());
				if(ch <= (this.radius-10)/2) {
					this.edges.remove(i);
					break;
				}
			}
			repaint();
		}
	}
	
	private int calculateCH(int x1,int y1, int x2, int y2) {
		int c1=Math.abs(x1-x2);
		int c2 = Math.abs(y1-y2);
		return (int) Math.sqrt(c1*c1 + c2*c2);
	}
	
	private void handleAlDijkstra() {
		if(this.edges.size() > 0) {
			this.matrix = new int[this.countNode + 1][this.countNode + 1];
			for(int i=0;i<=this.countNode;i++)
				for(int j=0;j<=this.countNode;j++)
					this.matrix[i][j]=0;
			this.edges.forEach(e->{
				this.matrix[e.getID1()][e.getID2()] = e.getNodeCost().getCost();
				this.matrix[e.getID2()][e.getID1()] = e.getNodeCost().getCost();
			});
			
			Dijkstra dij = new Dijkstra(this.matrix,this.countNode);
			
			this.listNodePath = dij.runDijkstra(this.startNode, this.desNode);
			
			int countLT = dij.KTLienThong();
			
			for(int i=0;i<this.nodes.size();i++) {
				Node node=this.nodes.get(i);
				Boolean check=true;
				for(int j=0;j<this.edges.size();j++) {
					Edge edge=this.edges.get(j);
					if(node.getID() == edge.getID1() || node.getID()==edge.getID2()) check=false;
				}
				if(check) countLT++;
			}
			this.lblLT.setText( countLT + ""); 
			
			for(int j=0;j<this.edges.size();j++) {
				Edge edge = this.edges.get(j);
				edge.setIsShortestPath(false);
			}
			
			int sum=0;
			String path = "";
			for(int i=0;i<this.listNodePath.size()-1;i++) {
				int n1 = this.listNodePath.get(i);
				int n2 = this.listNodePath.get(i+1);
				for(int j=0;j<this.edges.size();j++) {
					Edge edge = this.edges.get(j);
					if((n1==edge.getID1() && n2==edge.getID2()) || (n2==edge.getID1() && n1==edge.getID2())) {
						sum+=edge.getNodeCost().getCost();
						edge.setIsShortestPath(true);
					}
				}
			}
			for(int i=0;i<this.listNodePath.size();i++) {
				if(i<this.listNodePath.size()-1) {
					path+= this.listNodePath.get(i) + " > ";
				} else {
					path+= this.listNodePath.get(i);
				}
			}
			if(sum!=0) {
				lblCost.setText(String.valueOf(sum));
				lblPath.setText(path);
			} else {
				lblCost.setText("");
				lblPath.setText("");
			}
		}
	}
	
	public void setFrameMain(JFrame f) {
		this.frameMain = f;
	}
	
	public void setLblPathAndCost(JLabel lblpath,JLabel lblcost,JLabel lbllt) {
		this.lblPath=lblpath;
		this.lblCost=lblcost;
		this.lblLT=lbllt;
	}
	
	public void handleSave(String pathFile) {
		if(this.nodes.size() >1 && this.edges.size() > 0) {
			this.pathFileSave = pathFile;
			if(this.pathFileSave.indexOf(".graph") <0) {
				this.pathFileSave = this.pathFileSave + ".graph";
			}
			try {
		        File myObj = new File(pathFileSave);
		        if (myObj.createNewFile()) {
		          try {					
						FileWriter fw = new FileWriter(pathFileSave);
						BufferedWriter bw = new BufferedWriter(fw);
						bw.write(this.startNode + " " + this.desNode);
						bw.newLine();
						
						bw.write("nodes");
						bw.newLine();
						
						for(Node p: this.nodes) {
							bw.write(p.getX() + " " + p.getY() + " " + p.getID());
							bw.newLine();
						}
						
						bw.write("edges");
						bw.newLine();
						
						for(Edge e: this.edges) {
							bw.write(e.getX1() + " " + e.getY1() + " " + e.getX2() + " " + e.getY2() + " " + e.getID1() + " " + e.getID2() + " " + e.getNodeCost().getCost());
							bw.newLine();
						}
						bw.close();
						fw.close();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
		        } else {
		          System.out.println("File already exists.");
		        }
		      } catch (IOException e1) {
		        System.out.println("An error occurred.");
		        e1.printStackTrace();
		      }
		}
	}
	
	public void handleOpenFileGtaph(String pathFile) {
		this.pathFileSave=pathFile;
		File file = new File(pathFile);
		try {
			List<String> conText = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
			String st = conText.get(0);
			this.startNode = Integer.parseInt(st.substring(0, st.indexOf(" ")));
			this.desNode = Integer.parseInt(st.substring(st.indexOf(" ")+1, st.length()));
			for(int i=1;i<conText.size();i++) {
				String line=conText.get(i);
				if(line.indexOf("nodes") >= 0) i++;
				List<Node> temp_point = new ArrayList<Node>();
				for(int j=i;j<conText.size();j++) {
					line = conText.get(j);
					if(!line.equals("edges")) {
						int x=Integer.parseInt(line.substring(0, line.indexOf(" ")));
						int y=Integer.parseInt(line.substring(line.indexOf(" ") + 1, line.lastIndexOf(" ")));
						int id1 = Integer.parseInt(line.substring(line.lastIndexOf(" ") + 1, line.length()));
						temp_point.add(new Node(x,y,id1,id1+"",false,false));
					} else {
						i=j+1;
						break;
					}
				}
				this.nodes=temp_point;
				this.countNode = this.nodes.get(this.nodes.size()-1).getID()+1;
				List<Edge> temp_edge = new ArrayList<Edge>();
				
				for(int j=i;j<conText.size();j++) {
					line = conText.get(j);
					if(line.length() > 0) {							
						int x1=Integer.parseInt(line.substring(0, line.indexOf(" ")));
						line = line.substring(line.indexOf(" ") + 1,line.length());
						int y1=Integer.parseInt(line.substring(0, line.indexOf(" ")));
						line = line.substring(line.indexOf(" ") + 1,line.length());
						
						int x2 = Integer.parseInt(line.substring(0, line.indexOf(" ")));
						line = line.substring(line.indexOf(" ") + 1,line.length());
						int y2 = Integer.parseInt(line.substring(0, line.indexOf(" ")));
						line = line.substring(line.indexOf(" ") + 1,line.length());
						
						int id1 = Integer.parseInt(line.substring(0, line.indexOf(" ")));
						line = line.substring(line.indexOf(" ") + 1,line.length());
						int id2 = Integer.parseInt(line.substring(0, line.indexOf(" ")));
						
						int idLabel = Integer.parseInt(line.substring(line.indexOf(" ") + 1,line.length()));
						
						Edge edge = new Edge(x1,y1,x2,y2,id1,id2);
						edge.getNodeCost().setCost(idLabel);
						temp_edge.add(edge);
					}
				}
				this.edges=temp_edge;
			}
		}catch(Exception e) {
			System.out.println(e);
		}
		repaint();
	}
	
	private void setNodeStart(MouseEvent e) {
		for(int i=this.nodes.size()-1;i>=0;i--) {
			Node node=this.nodes.get(i);
			int ch = this.calculateCH(node.getX(),node.getY(),e.getX(),e.getY());
			if(ch<=this.radius/2) {
				if(node.getID()< this.desNode) {
					this.startNode = node.getID();
				}
				repaint();
				break;
			}
		}
	}
	
	private void setNodeEnd(MouseEvent e) {
		for(int i=this.nodes.size()-1;i>=0;i--) {
			Node node=this.nodes.get(i);
			int ch = this.calculateCH(node.getX(),node.getY(),e.getX(),e.getY());
			if(ch<=this.radius/2) {
				if(node.getID() > this.startNode) {
					this.desNode = node.getID();
				}
				repaint();
				break;
			}
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void clear() {
		this.nodes = new ArrayList<Node>();
		this.edges = new ArrayList<Edge>();
		this.countNode=1;
		this.desNode=1;
		this.startNode=1;
		this.nodeSelected=null;
		this.drag=null;
		this.matrix=new int[0][];
		this.listNodePath = new ArrayList<Integer>();
		this.lblPath.setText("");
		this.lblCost.setText("");
		this.pathFileSave=null;
		repaint();
	}

}
