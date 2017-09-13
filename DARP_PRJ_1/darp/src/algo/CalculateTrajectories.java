package algo;

/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.TreeSet;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class CalculateTrajectories
/*     */ {
/*     */   private TreeSet[] nodes;
/*     */   private int MAX_NODES;
/*     */   private int rows;
/*     */   private int cols;
/*     */   private Vector MSTvector;
/*     */   private int MSTedges;
/*     */   private HashSet<Edge> AllEdges;
/*     */   private ArrayList<Integer[]> PathSequence;
/*     */   
/*     */   CalculateTrajectories(int r, int c, Vector MST)
/*     */   {
/*  20 */     this.MAX_NODES = (4 * r * c);
/*  21 */     this.PathSequence = new ArrayList();
/*  22 */     this.rows = r;
/*  23 */     this.cols = c;
/*  24 */     this.MSTvector = MST;
/*  25 */     this.MSTedges = this.MSTvector.size();
/*  26 */     this.AllEdges = new HashSet();
/*  27 */     this.nodes = new TreeSet[this.MAX_NODES];
/*     */   }
/*     */   
/*     */ 
/*     */   public void initializeGraph(boolean[][] A, boolean connect4)
/*     */   {
/*  33 */     for (int i = 0; i < 2 * this.rows; i++) {
/*  34 */       for (int j = 0; j < 2 * this.cols; j++) {
/*  35 */         if (A[i][j] != false)
/*     */         {
/*  37 */           if ((i > 0) && (A[(i - 1)][j] != false)) AddToAllEdges(i * 2 * this.cols + j, (i - 1) * 2 * this.cols + j, 1);
/*  38 */           if ((i < 2 * this.rows - 1) && (A[(i + 1)][j] != false)) AddToAllEdges(i * 2 * this.cols + j, (i + 1) * 2 * this.cols + j, 1);
/*  39 */           if ((j > 0) && (A[i][(j - 1)] != false)) AddToAllEdges(i * 2 * this.cols + j, i * 2 * this.cols + j - 1, 1);
/*  40 */           if ((j < 2 * this.cols - 1) && (A[i][(j + 1)] != false)) { AddToAllEdges(i * 2 * this.cols + j, i * 2 * this.cols + j + 1, 1);
/*     */           }
/*  42 */           if (!connect4) {
/*  43 */             if ((i > 0) && (j > 0) && (A[(i - 1)][(j - 1)] != false)) AddToAllEdges(i * 2 * this.cols + j, (i - 1) * 2 * this.cols + j - 1, 1);
/*  44 */             if ((i < 2 * this.rows - 1) && (j < 2 * this.cols - 1) && (A[(i + 1)][(j + 1)] != false)) AddToAllEdges(i * 2 * this.cols + j, (i + 1) * 2 * this.cols + j + 1, 1);
/*  45 */             if ((i > 2 * this.rows - 1) && (j > 0) && (A[(i + 1)][(j - 1)] != false)) AddToAllEdges(i * 2 * this.cols + j, (i + 1) * 2 * this.cols + j - 1, 1);
/*  46 */             if ((i > 0) && (j < 2 * this.cols - 1) && (A[(i - 1)][(j + 1)] != false)) { AddToAllEdges(i * 2 * this.cols + j, (i - 1) * 2 * this.cols + j + 1, 1);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void AddToAllEdges(int from, int to, int cost)
/*     */   {
/*  56 */     this.AllEdges.add(new Edge(from, to, cost));
/*  57 */     if (this.nodes[from] == null)
/*     */     {
/*  59 */       this.nodes[from] = new TreeSet();
/*     */     }
/*     */     
/*  62 */     this.nodes[from].add(new Integer(to));
/*     */     
/*  64 */     if (this.nodes[to] == null)
/*     */     {
/*  66 */       this.nodes[to] = new TreeSet();
/*     */     }
/*     */     
/*  69 */     this.nodes[to].add(new Integer(from));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void RemoveTheAppropriateEdges()
/*     */   {
/*  78 */     for (int i = 0; i < this.MSTedges; i++) {
/*  79 */       Edge e = (Edge)this.MSTvector.get(i);
/*  80 */       int maxN = Math.max(e.from, e.to);
/*  81 */       int minN = Math.min(e.from, e.to);
/*     */       Edge eToRemove2Mirr;
/*  83 */       Edge eToRemove; Edge eToRemoveMirr; Edge eToRemove2; if (Math.abs(e.from - e.to) == 1) {
/*  84 */         int alpha = 4 * minN + 3 - 2 * (maxN % this.cols);
/*  85 */        eToRemove = new Edge(alpha, alpha + 2 * this.cols, 1);
/*  86 */          eToRemoveMirr = new Edge(alpha + 2 * this.cols, alpha, 1);
/*  87 */       eToRemove2 = new Edge(alpha + 1, alpha + 1 + 2 * this.cols, 1);
/*  88 */         eToRemove2Mirr = new Edge(alpha + 1 + 2 * this.cols, alpha + 1, 1);
/*     */       }
/*     */       else {
/*  91 */         int alpha = 4 * minN + 2 * this.cols - 2 * (maxN % this.cols);
/*  92 */         eToRemove = new Edge(alpha, alpha + 1, 1);
/*  93 */         eToRemoveMirr = new Edge(alpha + 1, alpha, 1);
/*  94 */         eToRemove2 = new Edge(alpha + 2 * this.cols, alpha + 1 + 2 * this.cols, 1);
/*  95 */         eToRemove2Mirr = new Edge(alpha + 1 + 2 * this.cols, alpha + 2 * this.cols, 1);
/*     */       }
/*     */       
/*     */ 
/*  99 */       if (this.AllEdges.contains(eToRemove)) {
/* 100 */         SafeRemoveEdge(eToRemove);
/*     */       }
/* 102 */       if (this.AllEdges.contains(eToRemoveMirr)) {
/* 103 */         SafeRemoveEdge(eToRemoveMirr);
/*     */       }
/* 105 */       if (this.AllEdges.contains(eToRemove2)) {
/* 106 */         SafeRemoveEdge(eToRemove2);
/*     */       }
/* 108 */       if (this.AllEdges.contains(eToRemove2Mirr)) {
/* 109 */         SafeRemoveEdge(eToRemove2Mirr);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void SafeRemoveEdge(Edge curEdge)
/*     */   {
/* 117 */     if (this.AllEdges.remove(curEdge))
/*     */     {
/*     */ 
/* 120 */       this.nodes[curEdge.from].remove(Integer.valueOf(curEdge.to));
/* 121 */       this.nodes[curEdge.to].remove(Integer.valueOf(curEdge.from));
/*     */     }
/*     */     else
/*     */     {
/* 125 */       System.out.println("TreeSet should have contained this element!!");
/* 126 */       System.exit(1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void CalculatePathsSequence(int StartingNode)
/*     */   {
/* 132 */     int currentNode = StartingNode;
/* 133 */     HashSet<Integer> RemovedNodes = new HashSet();
/*     */     
/*     */ 
/* 136 */     ArrayList<Integer> movement = new ArrayList();
/* 137 */     movement.add(Integer.valueOf(2 * this.cols));
/* 138 */     movement.add(Integer.valueOf(-1));
/* 139 */     movement.add(Integer.valueOf(-2 * this.cols));
/* 140 */     movement.add(Integer.valueOf(1));
/*     */     
/* 142 */     boolean found = false;
/* 143 */     int prevNode = 0;
/* 144 */     for (int idx = 0; idx < 4; idx++) {
/* 145 */       if (this.nodes[currentNode].contains(Integer.valueOf(currentNode + ((Integer)movement.get(idx)).intValue()))) {
/* 146 */         prevNode = currentNode + ((Integer)movement.get(idx)).intValue();
/* 147 */         found = true;
/* 148 */         break;
/*     */       }
/*     */     }
/* 151 */     if (!found) return;
/*     */     for (;;)
/*     */     {
/* 154 */       RemovedNodes.add(Integer.valueOf(currentNode));
/*     */       
/* 156 */       int offset = movement.indexOf(Integer.valueOf(prevNode - currentNode));
/*     */       
/* 158 */       prevNode = currentNode;
/*     */       
/* 160 */       found = false;
/* 161 */       for (int idx = 0; idx < 4; idx++) {
/* 162 */         if ((this.nodes[prevNode].contains(Integer.valueOf(prevNode + ((Integer)movement.get((idx + offset) % 4)).intValue()))) && 
/* 163 */           (!RemovedNodes.contains(Integer.valueOf(prevNode + ((Integer)movement.get((idx + offset) % 4)).intValue())))) {
/* 164 */           currentNode = prevNode + ((Integer)movement.get((idx + offset) % 4)).intValue();
/* 165 */           found = true;
/* 166 */           break;
/*     */         }
/*     */       }
/* 169 */       if (!found) { return;
/*     */       }
/* 171 */       if (this.nodes[currentNode].contains(Integer.valueOf(prevNode))) this.nodes[currentNode].remove(Integer.valueOf(prevNode));
/* 172 */       if (this.nodes[prevNode].contains(Integer.valueOf(currentNode))) { this.nodes[prevNode].remove(Integer.valueOf(currentNode));
/*     */       }
/* 174 */       int i = currentNode / (2 * this.cols);
/* 175 */       int j = currentNode % (2 * this.cols);
/* 176 */       int previ = prevNode / (2 * this.cols);
/* 177 */       int prevj = prevNode % (2 * this.cols);
/* 178 */       this.PathSequence.add(new Integer[] { Integer.valueOf(previ), Integer.valueOf(prevj), Integer.valueOf(i), Integer.valueOf(j) });
/*     */     }
/*     */   }
/*     */   
/*     */   public ArrayList<Integer[]> getPathSequence() {
/* 183 */     return this.PathSequence;
/*     */   }
/*     */ }


/* Location:              D:\New folder\SOP\DARP\DARP.jar!\CalculateTrajectories.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */