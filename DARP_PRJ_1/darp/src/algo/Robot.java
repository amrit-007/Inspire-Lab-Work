package algo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class Robot {
	public static final int MAX = 1001; // MAX Limit
	public static final int inf = (int)1e9; 
	public static	int[] dx = {1, -1, 0, 0}; // Restriction of motion along X axis
	public static	int[] dy = {0, 0, 1, -1}; // Restriction of motion along Y axis
	public int RoboId; // Unique for each robot
	public pair RoboLoc; // Current Location Coordinates of Robot on grid
	public ArrayList<pair> Goals; // Alloted Goals to the robot
	public  pair p[][]=new pair[1001][1001];  // Stores parent of x y
	public  Queue<pair> Q=new ArrayDeque<>(); // Queue of graph points
	public static int countObs2=0; // Number of obstacles
	public static ArrayList<pair> ObsArray=new ArrayList<>(); // Coordinates of obstacles
	public ArrayList<pair> path; // final calculated path
	public ArrayList<pair> goalnrobot; // combined array list of robots and goals to be traversed in order
	public void addGoal(pair p){ // Append goal to array list of goals
		Goals.add(p);
	}
	public Robot(pair p){  // Constructor
		RoboLoc=p;
		Goals=new ArrayList<>();
		path=new ArrayList<>();
	}

	public void setPath(){  // BFS algorithm to determine shortest distance for a robot via all its goals
		
		goalnrobot=new ArrayList<>();
		goalnrobot.add(RoboLoc);
		goalnrobot.addAll(Goals);
		int m=MainGUI.rows;
		int n=MainGUI.cols;
		for(int i = 0;i<MainGUI.rows;i++){
			for(int j = 0;j<MainGUI.cols;j++){
					if(MainGUI.EnvironmentGrid[i][j]==1||MainGUI.EnvironmentGrid[i][j]==2){
						countObs2++;
						ObsArray.add(new pair(i,j));
					}
			}
		}
		int dp[][]=new int[n][m];
		for(int j1=0;j1<Goals.size();j1++){
			
		
		for(int i = 0; i < n; ++i) {
			for(int j = 0; j < m; ++j) {
				dp[i][j] =  inf;
			}
		}
    int u, v;
    for(int i = 0; i < countObs2; ++i) {
        u=ObsArray.get(i).first;
        v=ObsArray.get(i).second;
        if(u==RoboLoc.first&&v==RoboLoc.second){}
        else  dp[u][v] = -1;
    }
    int S1, S2;
    S1=goalnrobot.get(j1+1).first;
    S2=goalnrobot.get(j1+1).second;
    dp[S1][S2] = 0;
	p[S1][S2] = new pair(-1,-1);
	Q.offer(new pair(S1,S2));
	while(!Q.isEmpty()) {
		pair u1 = Q.poll();
		for(int i = 0; i < 4; ++i) {
			int x =  dx[i] + u1.first, y =  dy[i] + u1.second;
			if (x >= 0 && x < n && y >= 0 && y < m) {
				if (dp[x][y] ==  inf && dp[x][y] != -1) {
                    dp[x][y] = dp[u1.first][u1.second] + 1;
					p[x][y] = new pair(u1.first, u1.second);
					Q.offer(new pair(x,y));
				}
			}
		}
	}
	int D1, D2;
	pair s=new pair(5,5);
	   	D1=goalnrobot.get(j1).first;
	    D2=goalnrobot.get(j1).second;
	    s.first=D1;s.second=D2;
		if(dp[D1][D2]== inf){
			MainGUI.EnvironmentGrid[D1][D2]=9999;
		}
		else {
			while(s.first != -1 && s.second != -1) {
				if(MainGUI.EnvironmentGrid[s.first][s.second]<2)
					MainGUI.EnvironmentGrid[s.first][s.second]=RoboId;
					path.add(new pair(s.first,s.second));
				s = p[s.first][s.second];
			}
		}
	}
	}
}
