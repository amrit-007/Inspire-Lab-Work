package algo;
/*     */ 
import java.awt.Dimension;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConnectComponent
/*     */ {
/*     */   int MAX_LABELS;
/*  16 */   int next_label = 1;
/*     */   
/*     */   private int[][] label2d;
/*     */   private int[][] BinaryRobot;
/*     */   private int[][] BinaryNonRobot;
/*     */   private int rows;
/*     */   private int cols;
/*     */   
/*     */   public int[][] compactLabeling(int[][] m, Dimension d, boolean zeroAsBg)
/*     */   {
/*  26 */     int[] image = TransformImage2Dto1D(m, d);
/*  27 */     this.label2d = new int[d.height][d.width];
/*  28 */     this.MAX_LABELS = (d.height * d.width);
/*  29 */     this.rows = d.height;
/*  30 */     this.cols = d.width;
/*     */     
/*  32 */     int[] label = labeling(image, d, zeroAsBg);
/*  33 */     int[] stat = new int[this.next_label + 1];
/*  34 */     for (int i = 0; i < image.length; i++)
/*     */     {
/*     */ 
/*  37 */       stat[label[i]] += 1;
/*     */     }
/*     */     
/*  40 */     stat[0] = 0;
/*     */     
/*  42 */     int j = 1;
/*  43 */     for (int i = 1; i < stat.length; i++) {
/*  44 */       if (stat[i] != 0) { stat[i] = (j++);
/*     */       }
/*     */     }
/*     */     
/*  48 */     this.next_label = (j - 1);
/*  49 */     int locIDX = 0;
/*  50 */     for (int i = 0; i < d.height; i++) {
/*  51 */       for (int ii = 0; ii < d.width; ii++) {
/*  52 */         label[locIDX] = stat[label[locIDX]];
/*  53 */         this.label2d[i][ii] = label[locIDX];
/*  54 */         locIDX++;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  59 */     return this.label2d;
/*     */   }
/*     */   
/*     */   public int[] TransformImage2Dto1D(int[][] a, Dimension d) {
/*  63 */     int[] ret = new int[d.height * d.width];
/*  64 */     int k = 0;
/*  65 */     for (int i = 0; i < d.height; i++) {
/*  66 */       for (int j = 0; j < d.width; j++) {
/*  67 */         ret[k] = a[i][j];
/*  68 */         k++;
/*     */       }
/*     */     }
/*     */     
/*  72 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getMaxLabel()
/*     */   {
/*  79 */     return this.next_label;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] labeling(int[] image, Dimension d, boolean zeroAsBg)
/*     */   {
/*  94 */     int w = d.width;int h = d.height;
/*  95 */     int[] rst = new int[w * h];
/*  96 */     int[] parent = new int[this.MAX_LABELS];
/*  97 */     int[] labels = new int[this.MAX_LABELS];
/*     */     
/*     */ 
/* 100 */     int next_region = 1;
/* 101 */     for (int y = 0; y < h; y++) {
/* 102 */       for (int x = 0; x < w; x++) {
/* 103 */         if ((image[(y * w + x)] != 0) || (!zeroAsBg)) {
/* 104 */           int k = 0;
/* 105 */           boolean connected = false;
/*     */           
/* 107 */           if ((x > 0) && (image[(y * w + x - 1)] == image[(y * w + x)])) {
/* 108 */             k = rst[(y * w + x - 1)];
/* 109 */             connected = true;
/*     */           }
/*     */           
/* 112 */           if ((y > 0) && (image[((y - 1) * w + x)] == image[(y * w + x)]) && ((!connected) || (image[((y - 1) * w + x)] < k))) {
/* 113 */             k = rst[((y - 1) * w + x)];
/* 114 */             connected = true;
/*     */           }
/* 116 */           if (!connected) {
/* 117 */             k = next_region;
/* 118 */             next_region++;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 128 */           rst[(y * w + x)] = k;
/*     */           
/* 130 */           if ((x > 0) && (image[(y * w + x - 1)] == image[(y * w + x)]) && (rst[(y * w + x - 1)] != k))
/* 131 */             uf_union(k, rst[(y * w + x - 1)], parent);
/* 132 */           if ((y > 0) && (image[((y - 1) * w + x)] == image[(y * w + x)]) && (rst[((y - 1) * w + x)] != k)) {
/* 133 */             uf_union(k, rst[((y - 1) * w + x)], parent);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 139 */     this.next_label = 1;
/* 140 */     for (int i = 0; i < w * h; i++) {
/* 141 */       if ((image[i] != 0) || (!zeroAsBg)) {
/* 142 */         rst[i] = uf_find(rst[i], parent, labels);
/*     */         
/*     */ 
/* 145 */         if (!zeroAsBg) rst[i] -= 1;
/*     */       }
/*     */     }
/* 148 */     this.next_label -= 1;
/* 149 */     if (!zeroAsBg) { this.next_label -= 1;
/*     */     }
/*     */     
/*     */ 
/* 153 */     return rst;
/*     */   }
/*     */   
/*     */   void uf_union(int x, int y, int[] parent) {
/* 157 */     while (parent[x] > 0)
/* 158 */       x = parent[x];
/* 159 */     while (parent[y] > 0)
/* 160 */       y = parent[y];
/* 161 */     if (x != y) {
/* 162 */       if (x < y)
/* 163 */         parent[x] = y; else {
/* 164 */         parent[y] = x;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int uf_find(int x, int[] parent, int[] label)
/*     */   {
/* 177 */     while (parent[x] > 0)
/* 178 */       x = parent[x];
/* 179 */     if (label[x] == 0)
/* 180 */       label[x] = (this.next_label++);
/* 181 */     return label[x];
/*     */   }
/*     */   
/*     */ 
/*     */   public void constructBinaryImages(int robotsLabel)
/*     */   {
/* 187 */     this.BinaryRobot = deepCopyMatrix(this.label2d);
/* 188 */     this.BinaryNonRobot = deepCopyMatrix(this.label2d);
/*     */     
/* 190 */     for (int i = 0; i < this.rows; i++) {
/* 191 */       for (int j = 0; j < this.cols; j++) {
/* 192 */         if (this.label2d[i][j] == robotsLabel) {
/* 193 */           this.BinaryRobot[i][j] = 1;
/* 194 */           this.BinaryNonRobot[i][j] = 0;
/* 195 */         } else if (this.label2d[i][j] != 0) {
/* 196 */           this.BinaryRobot[i][j] = 0;
/* 197 */           this.BinaryNonRobot[i][j] = 1;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private int[][] deepCopyMatrix(int[][] input)
/*     */   {
/* 205 */     if (input == null)
/* 206 */       return (int[][])null;
/* 207 */     int[][] result = new int[input.length][];
/* 208 */     for (int r = 0; r < input.length; r++) {
/* 209 */       result[r] = ((int[])input[r].clone());
/*     */     }
/* 211 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public float[][] NormalizedEuclideanDistanceBinary(boolean RobotR)
/*     */   {
/* 221 */     float[][] Region = new float[this.rows][this.cols];
/*     */     
/* 223 */     float[] f = new float[Math.max(this.rows, this.cols)];
/* 224 */     float[] d = new float[f.length];
/* 225 */     int[] v = new int[f.length];
/* 226 */     float[] z = new float[f.length + 1];
/*     */     
/* 228 */     for (int x = 0; x < this.cols; x++) {
/* 229 */       for (int y = 0; y < this.rows; y++) {
/* 230 */         if (RobotR) {
/* 231 */           f[y] = (this.BinaryRobot[y][x] == 0 ? Float.MAX_VALUE : 0.0F);
/*     */         } else {
/* 233 */           f[y] = (this.BinaryNonRobot[y][x] == 0 ? Float.MAX_VALUE : 0.0F);
/*     */         }
/*     */       }
/* 236 */       DT1D(f, d, v, z);
/* 237 */       for (int y = 0; y < this.rows; y++) {
/* 238 */         Region[y][x] = d[y];
/*     */       }
/*     */     }
/*     */     
/* 242 */     float maxV = 0.0F;float minV = Float.MAX_VALUE;
/* 243 */     for (int y = 0; y < this.rows; y++) {
/* 244 */       DT1D(getVector(Region, y), d, v, z);
/*     */       
/* 246 */       for (int x = 0; x < this.cols; x++) {
/* 247 */         Region[y][x] = ((float)Math.sqrt(d[x]));
/* 248 */         if (maxV < Region[y][x]) maxV = Region[y][x];
/* 249 */         if (minV > Region[y][x]) { minV = Region[y][x];
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 255 */     for (int y = 0; y < this.rows; y++) {
/* 256 */       for (int x = 0; x < this.cols; x++) {
/* 257 */         if (RobotR) {
/* 258 */           Region[y][x] = ((Region[y][x] - minV) * (1.0F / (maxV - minV)) + 1.0F);
/*     */         } else {
/* 260 */           Region[y][x] = ((Region[y][x] - minV) * (1.0F / (maxV - minV)));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 265 */     return Region;
/*     */   }
/*     */   
/*     */   private void DT1D(float[] f, float[] d, int[] v, float[] z) {
/* 269 */     int k = 0;
/* 270 */     v[0] = 0;
/* 271 */     z[0] = -3.4028235E38F;
/* 272 */     z[1] = Float.MAX_VALUE;
/*     */     
/* 274 */     for (int q = 1; q < f.length; q++) {
/* 275 */       float s = (f[q] + q * q - (f[v[k]] + v[k] * v[k])) / (2 * q - 2 * v[k]);
/*     */       
/* 277 */       while (s <= z[k]) {
/* 278 */         k--;
/* 279 */         s = (f[q] + q * q - (f[v[k]] + v[k] * v[k])) / (2 * q - 2 * v[k]);
/*     */       }
/* 281 */       k++;
/* 282 */       v[k] = q;
/* 283 */       z[k] = s;
/* 284 */       z[(k + 1)] = Float.MAX_VALUE;
/*     */     }
/*     */     
/* 287 */     k = 0;
/* 288 */     for (int q = 0; q < f.length; q++) {
/* 289 */       while (z[(k + 1)] < q) { k++;
/*     */       }
/* 291 */       d[q] = ((q - v[k]) * (q - v[k]) + f[v[k]]);
/*     */     }
/*     */   }
/*     */   
/*     */   private float[] getVector(float[][] A, int row) {
/* 296 */     float[] ret = new float[this.cols];
/*     */     
/* 298 */     for (int i = 0; i < this.cols; i++) { ret[i] = A[row][i];
/*     */     }
/* 300 */     return ret;
/*     */   }
/*     */ }


/* Location:              D:\New folder\SOP\DARP\DARP.jar!\ConnectComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */