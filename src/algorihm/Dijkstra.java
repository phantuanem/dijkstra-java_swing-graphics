package algorihm;

import java.util.ArrayList;
import java.util.List;

public class Dijkstra {
	private int size;
	private int matran[][];
	private int vocung=9999;
	
	
	
	public Dijkstra() {
		this.size=0;
		this.matran = new int[0][0];
	}
	
	public Dijkstra(int[][] mt, int size) {
		this.size = size;
		this.matran = mt;
	}
	
	public List<Integer> runDijkstra(int s,int d) {
		int[] mark=new int[this.size +1];
		int[] pi=new int[this.size +1];
		int[] p=new int[this.size+1];
		List<Integer> listdd = new ArrayList<Integer>();
		for(int i=1;i<=this.size;i++) {
			mark[i]=0;
			pi[i]=this.vocung;
			p[i]=0;
		}
		
		pi[s]=0;
		
		for(int i=1;i<this.size;i++) {
			
			int min_pi=this.vocung;
			int u=-1;
					
			for(int j=1;j<=this.size;j++) {
				if(mark[j]==0 && pi[j]<min_pi) {
					min_pi=pi[j];
					u=j;
				}
			}
			
			if(u==-1) break;
			
			mark[u]=1;
			
			for(int v=1;v<this.size;v++) {
				if(this.matran[u][v] != 0 && mark[v]==0 && pi[u] + this.matran[u][v] < pi[v]) {
					pi[v]=pi[u] + this.matran[u][v];
					p[v]=u;
				}
			}
			
			if(u==d) break;
		}
		int i=d;
		while(i>0 || p[i]>0) {
			listdd.add(i);
			i=p[i];
		}
		List<Integer> listdd2 = new ArrayList<Integer>();
		for(i=listdd.size()-1;i>=0;i--) listdd2.add(listdd.get(i));
		return listdd2;
	}
	
	public int KTLienThong() {
		int count=1;
		int[] mark = new int[this.size+1];
		for(int i=0;i<this.size+1;i++) {
			mark[i]=0;
		}
		
		for(int i=0;i<=this.size;i++) {
			if(mark[i] == 0) mark[i] = count;
			Boolean check=false;
			List<Integer> hangxom = new ArrayList<Integer>();
			for(int j=0;j<this.size+1;j++) {
				if(this.matran[i][j] != 0 || this.matran[j][i] != 0) {
					if(mark[j]==0) {
						hangxom.add(j);
					}
				}
			}
			
			while(hangxom.size() != 0) {
				check=true;
				int temp = hangxom.get(hangxom.size()-1);
				mark[temp] = count;
				hangxom.remove(hangxom.size()-1);
				
				for(int j=0;j<this.size+1;j++) {
					if(this.matran[temp][j] != 0 || this.matran[j][temp] != 0) {
						if(mark[j]==0) {
							hangxom.add(j);
						}
					}
				}
			}
			if(check) count++;
		}
		return count-1;
	}
}
