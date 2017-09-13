package algo;
/*    */ import java.util.Comparator;
/*    */ 
/*    */ class Edge
/*    */   implements Comparator
/*    */ {
/*    */   public int from;
/*    */   public int to;
/*    */   public int cost;
/*    */   
/*    */   public Edge() {}
/*    */   
/*    */   public Edge(int f, int t, int c)
/*    */   {
/* 14 */     this.from = f;this.to = t;this.cost = c;
/*    */   }
/*    */   
/*    */   public int compare(Object o1, Object o2)
/*    */   {
/* 19 */     int cost1 = ((Edge)o1).cost;
/* 20 */     int cost2 = ((Edge)o2).cost;
/* 21 */     int from1 = ((Edge)o1).from;
/* 22 */     int from2 = ((Edge)o2).from;
/* 23 */     int to1 = ((Edge)o1).to;
/* 24 */     int to2 = ((Edge)o2).to;
/*    */     
/* 26 */     if (cost1 < cost2)
/* 27 */       return -1;
/* 28 */     if ((cost1 == cost2) && (from1 == from2) && (to1 == to2))
/* 29 */       return 0;
/* 30 */     if (cost1 == cost2)
/* 31 */       return -1;
/* 32 */     if (cost1 > cost2) {
/* 33 */       return 1;
/*    */     }
/* 35 */     return 0;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 39 */     return (Integer.toString(this.cost) + Integer.toString(this.from) + Integer.toString(this.to)).hashCode();
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj)
/*    */   {
/* 44 */     Edge e = (Edge)obj;
/* 45 */     return (this.cost == e.cost) && (this.from == e.from) && (this.to == e.to);
/*    */   }
/*    */ }


/* Location:              D:\New folder\SOP\DARP\DARP.jar!\Edge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */