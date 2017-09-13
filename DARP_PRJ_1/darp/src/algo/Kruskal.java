package algo;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashSet;
/*     */ import java.util.TreeSet;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ public class Kruskal
/*     */ {
/*     */   private HashSet[] nodes;
/*     */   private TreeSet allEdges;
/*     */   private Vector allNewEdges;
/*     */   private int MAX_NODES;
/*     */   
/*     */   Kruskal(int maxN)
/*     */   {
/*  16 */     this.MAX_NODES = maxN;
/*  17 */     this.nodes = new HashSet[this.MAX_NODES];
/*  18 */     this.allEdges = new TreeSet(new Edge());
/*  19 */     this.allNewEdges = new Vector(this.MAX_NODES);
/*     */   }
/*     */   
/*     */   public void initializeGraph(boolean[][] A, boolean connect4) {
/*  23 */     int rows = A.length;
/*  24 */     int cols = A[0].length;
/*     */     
/*  26 */     for (int i = 0; i < rows; i++) {
/*  27 */       for (int j = 0; j < cols; j++) {
/*  28 */         if (A[i][j] != false)
/*     */         {
/*  30 */           if ((i > 0) && (A[(i - 1)][j] != false)) AddToAllEdges(i * cols + j, (i - 1) * cols + j, 1);
/*  31 */           if ((i < rows - 1) && (A[(i + 1)][j] != false)) AddToAllEdges(i * cols + j, (i + 1) * cols + j, 1);
/*  32 */           if ((j > 0) && (A[i][(j - 1)] != false)) AddToAllEdges(i * cols + j, i * cols + j - 1, 1);
/*  33 */           if ((j < cols - 1) && (A[i][(j + 1)] != false)) { AddToAllEdges(i * cols + j, i * cols + j + 1, 1);
/*     */           }
/*  35 */           if (!connect4) {
/*  36 */             if ((i > 0) && (j > 0) && (A[(i - 1)][(j - 1)] != false)) AddToAllEdges(i * cols + j, (i - 1) * cols + j - 1, 1);
/*  37 */             if ((i < rows - 1) && (j < cols - 1) && (A[(i + 1)][(j + 1)] != false)) AddToAllEdges(i * cols + j, (i + 1) * cols + j + 1, 1);
/*  38 */             if ((i > rows - 1) && (j > 0) && (A[(i + 1)][(j - 1)] != false)) AddToAllEdges(i * cols + j, (i + 1) * cols + j - 1, 1);
/*  39 */             if ((i > 0) && (j < cols - 1) && (A[(i - 1)][(j + 1)] != false)) AddToAllEdges(i * cols + j, (i - 1) * cols + j + 1, 1);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void AddToAllEdges(int from, int to, int cost)
/*     */   {
/*  48 */     this.allEdges.add(new Edge(from, to, cost));
/*  49 */     if (this.nodes[from] == null)
/*     */     {
/*  51 */       this.nodes[from] = new HashSet(2 * this.MAX_NODES);
/*  52 */       this.nodes[from].add(new Integer(from));
/*     */     }
/*     */     
/*  55 */     if (this.nodes[to] == null)
/*     */     {
/*  57 */       this.nodes[to] = new HashSet(2 * this.MAX_NODES);
/*  58 */       this.nodes[to].add(new Integer(to));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void performKruskal()
/*     */   {
/*  65 */     int size = this.allEdges.size();
/*  66 */     for (int i = 0; i < size; i++) {
/*  67 */       Edge curEdge = (Edge)this.allEdges.first();
/*  68 */       if (this.allEdges.remove(curEdge))
/*     */       {
/*     */ 
/*  71 */         if (nodesAreInDifferentSets(curEdge.from, curEdge.to)) {
/*     */           HashSet dst;
/*     */           HashSet src;
/*     */           int dstHashSetIndex;
///*     */           HashSet dst;
/*  76 */           if (this.nodes[curEdge.from].size() > this.nodes[curEdge.to].size())
/*     */           {
/*  78 */             src = this.nodes[curEdge.to];
/*  79 */            dst = this.nodes[(dstHashSetIndex = curEdge.from)];
/*     */           }
/*     */           else {
/*  82 */             src = this.nodes[curEdge.from];
/*  83 */             dst = this.nodes[(dstHashSetIndex = curEdge.to)];
/*     */           }
/*     */           
/*  86 */           Object[] srcArray = src.toArray();
/*  87 */           int transferSize = srcArray.length;
/*  88 */           for (int j = 0; j < transferSize; j++)
/*     */           {
/*     */ 
/*  91 */             if (src.remove(srcArray[j])) {
/*  92 */               dst.add(srcArray[j]);
/*  93 */               this.nodes[((Integer)srcArray[j]).intValue()] = this.nodes[dstHashSetIndex];
/*     */             }
/*     */             else {
/*  96 */               System.out.println("Something wrong: set union");
/*  97 */               System.exit(1);
/*     */             }
/*     */           }
/*     */           
/* 101 */           this.allNewEdges.add(curEdge);
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/* 109 */         System.out.println("TreeSet should have contained this element!!");
/* 110 */         System.exit(1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean nodesAreInDifferentSets(int a, int b)
/*     */   {
/* 119 */     return !this.nodes[a].equals(this.nodes[b]);
/*     */   }
/*     */   
/*     */   private void printFinalEdges() {
/* 123 */     System.out.println("The minimal spanning tree generated by \nKruskal's algorithm is: ");
/*     */     
/* 125 */     while (!this.allNewEdges.isEmpty())
/*     */     {
/* 127 */       Edge e = (Edge)this.allNewEdges.firstElement();
/* 128 */       System.out.println("Nodes: (" + e.from + ", " + e.to + ") with cost: " + e.cost);
/*     */       
/* 130 */       this.allNewEdges.remove(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 135 */   public Vector getAllNewEdges() { return this.allNewEdges; }
/* 136 */   public TreeSet getAllEdges() { return this.allEdges; }
/*     */ }


/* Location:              D:\New folder\SOP\DARP\DARP.jar!\Kruskal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */