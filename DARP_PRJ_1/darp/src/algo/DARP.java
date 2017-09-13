package algo;
/*     */ /*     */ import java.util.ArrayList;
/*     */ 
/*     */ public class DARP
/*     */ {
/*     */   private double variateWeight;
/*     */   private double randomLevel;
/*     */   private int rows;
/*     */   private int cols;
/*     */   private int nr;
/*     */   private int ob;
/*     */   private int maxIter;
/*     */   private int[][] GridEnv;
/*     */   private ArrayList<Integer[]> RobotsInit;
/*     */   private ArrayList<int[][]> BWlist;
/*     */   private int[][] A;
/*     */   private boolean[][] robotBinary;
/*     */   private int[] ArrayOfElements;
/*     */   private boolean[] ConnectedRobotRegions;
/*     */   private boolean success;
/*     */   private ArrayList<boolean[][]> BinrayRobotRegions;
/*     */   private int maxCellsAss;
/*     */   private int minCellsAss;
/*     */   private double elapsedTime;
/*     */   private int discr;
/*     */   private boolean canceled;
/*     */   private boolean UseImportance;
/*     */   
/*     */   public DARP(int r, int c, int[][] src, int iters, double vWeight, double rLevel, int discr, boolean imp)
/*     */   {
/*  31 */     this.rows = r;
/*  32 */     this.cols = c;
/*  33 */     this.GridEnv = deepCopyMatrix(src);
/*  34 */     this.nr = 0;
/*  35 */     this.ob = 0;
/*  36 */     this.maxIter = iters;
/*  37 */     this.RobotsInit = new ArrayList();
/*  38 */     this.A = new int[this.rows][this.cols];
/*  39 */     this.robotBinary = new boolean[this.rows][this.cols];
/*  40 */     this.variateWeight = vWeight;
/*  41 */     this.randomLevel = rLevel;
/*  42 */     this.discr = discr;
/*  43 */     this.canceled = false;
/*  44 */     this.UseImportance = imp;
/*  45 */     defineRobotsObstacles();
/*     */   }
/*     */   
/*     */ 
/*     */   public void constructAssignmentM()
/*     */   {
/*  51 */     long startTime = System.nanoTime();
/*     */     
/*  53 */     int NoTiles = this.rows * this.cols;
/*     */     
/*     */ 
/*  56 */     double fairDivision = 1.0D / this.nr;
/*  57 */     int effectiveSize = NoTiles - this.nr - this.ob;
/*  58 */     int termThr; if (effectiveSize % this.nr != 0) termThr = 1; else {
/*  59 */       termThr = 0;
/*     */     }
/*  61 */     ArrayList<double[][]> AllDistances = new ArrayList();
/*  62 */     ArrayList<double[][]> TilesImportance = new ArrayList();
/*     */     
/*  64 */     for (int r = 0; r < this.nr; r++) {
/*  65 */       AllDistances.add(new double[this.rows][this.cols]);
/*  66 */       TilesImportance.add(new double[this.rows][this.cols]);
/*     */     }
/*     */     
/*     */ 
/*  70 */     double[] MaximumDist = new double[this.nr];
/*  71 */     double[] MaximumImportance = new double[this.nr];
/*  72 */     double[] MinimumImportance = new double[this.nr];
/*  73 */     for (int r = 0; r < this.nr; r++) { MinimumImportance[r] = Double.MAX_VALUE;
/*     */     }
/*  75 */     float[][] ONES2D = new float[this.rows][this.cols];
/*     */     
/*  77 */     for (int i = 0; i < this.rows; i++) {
/*  78 */       for (int j = 0; j < this.cols; j++) {
/*  79 */         double tempSum = 0.0D;
/*  80 */         for (int r = 0; r < this.nr; r++) {
/*  81 */           ((double[][])AllDistances.get(r))[i][j] = EuclideanDis((Integer[])this.RobotsInit.get(r), new Integer[] { Integer.valueOf(i), Integer.valueOf(j) });
/*  82 */           if (((double[][])AllDistances.get(r))[i][j] > MaximumDist[r]) MaximumDist[r] = ((double[][])AllDistances.get(r))[i][j];
/*  83 */           tempSum += ((double[][])AllDistances.get(r))[i][j];
/*     */         }
/*  85 */         for (int r = 0; r < this.nr; r++) {
/*  86 */           ((double[][])TilesImportance.get(r))[i][j] = (1.0D / (tempSum - ((double[][])AllDistances.get(r))[i][j]));
/*  87 */           if (((double[][])TilesImportance.get(r))[i][j] > MaximumImportance[r]) MaximumImportance[r] = ((double[][])TilesImportance.get(r))[i][j];
/*  88 */           if (((double[][])TilesImportance.get(r))[i][j] < MinimumImportance[r]) MinimumImportance[r] = ((double[][])TilesImportance.get(r))[i][j];
/*     */         }
/*  90 */         ONES2D[i][j] = 1.0F;
/*     */       }
/*     */     }
/*     */     
/*  94 */     this.success = false;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  99 */     ArrayList<double[][]> MetricMatrix = deepCopyListMatrix(AllDistances);
/*     */     
/* 101 */     double[][] criterionMatrix = new double[this.rows][this.cols];
/*     */     
/* 103 */     while ((termThr <= this.discr) && (!this.success) && (!this.canceled))
/*     */     {
/* 105 */       double downThres = (NoTiles - termThr * (this.nr - 1)) / (NoTiles * this.nr);
/* 106 */       double upperThres = (NoTiles + termThr) / (NoTiles * this.nr);
/*     */       
/* 108 */       this.success = true;
/*     */       
/* 110 */       int iter = 0;
/* 111 */       while ((iter <= this.maxIter) && (!this.canceled)) {
/* 112 */         assign(MetricMatrix);
/*     */         
/* 114 */         ArrayList<float[][]> ConnectedMultiplierList = new ArrayList();
/* 115 */         double[] plainErrors = new double[this.nr];
/* 116 */         double[] divFairError = new double[this.nr];
/*     */         
/*     */ 
/* 119 */         for (int r = 0; r < this.nr; r++) {
/* 120 */           float[][] ConnectedMultiplier = deepCopyMatrix(ONES2D);
/* 121 */           this.ConnectedRobotRegions[r] = true;
/*     */           
/* 123 */           ConnectComponent cc = new ConnectComponent();
/* 124 */           int[][] Ilabel = cc.compactLabeling((int[][])this.BWlist.get(r), new java.awt.Dimension(this.cols, this.rows), true);
/* 125 */           if (cc.getMaxLabel() > 1) {
/* 126 */             this.ConnectedRobotRegions[r] = false;
/*     */             
/*     */ 
/* 129 */             cc.constructBinaryImages(Ilabel[((Integer[])this.RobotsInit.get(r))[0].intValue()][((Integer[])this.RobotsInit.get(r))[1].intValue()]);
/*     */             
/*     */ 
/* 132 */             ConnectedMultiplier = CalcConnectedMultiplier(cc.NormalizedEuclideanDistanceBinary(true), cc
/* 133 */               .NormalizedEuclideanDistanceBinary(false));
/*     */           }
/*     */           
/* 136 */           ConnectedMultiplierList.add(r, ConnectedMultiplier);
/*     */           
/*     */ 
/* 139 */           plainErrors[r] = (this.ArrayOfElements[r] / effectiveSize);
/*     */           
/*     */ 
/* 142 */           if (plainErrors[r] < downThres) { divFairError[r] = (downThres - plainErrors[r]);
/* 143 */           } else if (plainErrors[r] > upperThres) { divFairError[r] = (upperThres - plainErrors[r]);
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 149 */         if (isThisAGoalState(termThr)) {
/*     */           break;
/*     */         }
/*     */         
/* 153 */         double TotalNegPerc = 0.0D;double totalNegPlainErrors = 0.0D;
/* 154 */         double[] correctionMult = new double[this.nr];
/*     */         
/* 156 */         for (int r = 0; r < this.nr; r++) {
/* 157 */           if (divFairError[r] < 0.0D) {
/* 158 */             TotalNegPerc += Math.abs(divFairError[r]);
/* 159 */             totalNegPlainErrors += plainErrors[r];
/*     */           }
/* 161 */           correctionMult[r] = 1.0D;
/*     */         }
/*     */         
/*     */ 
/* 165 */         for (int r = 0; r < this.nr; r++) {
/* 166 */           if (totalNegPlainErrors != 0.0D)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 171 */             if (divFairError[r] < 0.0D) {
/* 172 */               correctionMult[r] = (1.0D + plainErrors[r] / totalNegPlainErrors * (TotalNegPerc / 2.0D));
/*     */             }
/*     */             else {
/* 175 */               correctionMult[r] = (1.0D - plainErrors[r] / totalNegPlainErrors * (TotalNegPerc / 2.0D));
/*     */             }
/*     */             
/* 178 */             criterionMatrix = calculateCriterionMatrix((double[][])TilesImportance.get(r), MinimumImportance[r], MaximumImportance[r], correctionMult[r], divFairError[r] < 0.0D);
/*     */           }
/* 180 */           MetricMatrix.set(r, FinalUpdateOnMetricMatrix(criterionMatrix, generateRandomMatrix(), (double[][])MetricMatrix.get(r), (float[][])ConnectedMultiplierList.get(r)));
/*     */         }
/*     */         
/* 183 */         iter++;
/*     */       }
/*     */       
/* 186 */       if (iter >= this.maxIter) {
/* 187 */         this.maxIter /= 2;
/* 188 */         this.success = false;
/* 189 */         termThr++;
/*     */       }
/*     */     }
/*     */     
/* 193 */     this.elapsedTime = ((System.nanoTime() - startTime) / Math.pow(10.0D, 9.0D));
/* 194 */     calculateRobotBinaryArrays();
/*     */   }
/*     */   
/*     */   private void calculateRobotBinaryArrays()
/*     */   {
/* 199 */     this.BinrayRobotRegions = new ArrayList();
/* 200 */     for (int r = 0; r < this.nr; r++) this.BinrayRobotRegions.add(new boolean[this.rows][this.cols]);
/* 201 */     for (int i = 0; i < this.rows; i++) {
/* 202 */       for (int j = 0; j < this.cols; j++) {
/* 203 */         if (this.A[i][j] < this.nr) {
/* 204 */           ((boolean[][])this.BinrayRobotRegions.get(this.A[i][j]))[i][j] = true;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private double[][] FinalUpdateOnMetricMatrix(double[][] CM, double[][] RM, double[][] curentONe, float[][] CC)
/*     */   {
/* 212 */     double[][] MMnew = new double[this.rows][this.cols];
/*     */     
/* 214 */     for (int i = 0; i < this.rows; i++) {
/* 215 */       for (int j = 0; j < this.cols; j++) {
/* 216 */         MMnew[i][j] = (curentONe[i][j] * CM[i][j] * RM[i][j] * CC[i][j]);
/*     */       }
/*     */     }
/*     */     
/* 220 */     return MMnew;
/*     */   }
/*     */   
/*     */ 
/*     */   private double[][] generateRandomMatrix()
/*     */   {
/* 226 */     double[][] RandomMa = new double[this.rows][this.cols];
/* 227 */     java.util.Random randomno = new java.util.Random();
/*     */     
/* 229 */     for (int i = 0; i < this.rows; i++) {
/* 230 */       for (int j = 0; j < this.cols; j++) {
/* 231 */         RandomMa[i][j] = (2.0D * this.randomLevel * randomno.nextDouble() + 1.0D - this.randomLevel);
/*     */       }
/*     */     }
/*     */     
/* 235 */     return RandomMa;
/*     */   }
/*     */   
/*     */   private double[][] calculateCriterionMatrix(double[][] TilesImp, double minImp, double maxImp, double corMult, boolean SmallerThan0) {
/* 239 */     double[][] retrunCriter = new double[this.rows][this.cols];
/*     */     
/* 241 */     for (int i = 0; i < this.rows; i++) {
/* 242 */       for (int j = 0; j < this.cols; j++) {
/* 243 */         if (this.UseImportance) {
/* 244 */           if (SmallerThan0) {
/* 245 */             retrunCriter[i][j] = ((TilesImp[i][j] - minImp) * ((corMult - 1.0D) / (maxImp - minImp)) + 1.0D);
/*     */           } else {
/* 247 */             retrunCriter[i][j] = ((TilesImp[i][j] - minImp) * ((1.0D - corMult) / (maxImp - minImp)) + corMult);
/*     */           }
/*     */         } else {
/* 250 */           retrunCriter[i][j] = corMult;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 255 */     return retrunCriter;
/*     */   }
/*     */   
/*     */   private boolean isThisAGoalState(int thres)
/*     */   {
/* 260 */     this.maxCellsAss = 0;
/* 261 */     this.minCellsAss = Integer.MAX_VALUE;
/*     */     
/*     */ 
/* 264 */     for (int r = 0; r < this.nr; r++) {
/* 265 */       if (this.maxCellsAss < this.ArrayOfElements[r]) {
/* 266 */         this.maxCellsAss = this.ArrayOfElements[r];
/*     */       }
/* 268 */       if (this.minCellsAss > this.ArrayOfElements[r]) {
/* 269 */         this.minCellsAss = this.ArrayOfElements[r];
/*     */       }
/*     */       
/* 272 */       if (this.ConnectedRobotRegions[r] == false) { return false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 284 */     return this.maxCellsAss - this.minCellsAss <= thres;
/*     */   }
/*     */   
/*     */ 
/*     */   private float[][] CalcConnectedMultiplier(float[][] dist1, float[][] dist2)
/*     */   {
/* 290 */     float[][] returnM = new float[this.rows][this.cols];
/* 291 */     float MaxV = 0.0F;
/* 292 */     float MinV = Float.MAX_VALUE;
/* 293 */     for (int i = 0; i < this.rows; i++) {
/* 294 */       for (int j = 0; j < this.cols; j++) {
/* 295 */         dist1[i][j] -= dist2[i][j];
/* 296 */         if (MaxV < returnM[i][j]) MaxV = returnM[i][j];
/* 297 */         if (MinV > returnM[i][j]) { MinV = returnM[i][j];
/*     */         }
/*     */       }
/*     */     }
/* 301 */     for (int i = 0; i < this.rows; i++) {
/* 302 */       for (int j = 0; j < this.cols; j++) {
/* 303 */         returnM[i][j] = ((returnM[i][j] - MinV) * (2.0F * (float)this.variateWeight / (MaxV - MinV)) + (1.0F - (float)this.variateWeight));
/*     */       }
/*     */     }
/*     */     
/* 307 */     return returnM;
/*     */   }
/*     */   
/*     */   private void assign(ArrayList<double[][]> Q)
/*     */   {
/* 312 */     this.BWlist = new ArrayList();
/* 313 */     for (int r = 0; r < this.nr; r++) {
/* 314 */       this.BWlist.add(new int[this.rows][this.cols]);
/* 315 */       ((int[][])this.BWlist.get(r))[((Integer[])this.RobotsInit.get(r))[0].intValue()][((Integer[])this.RobotsInit.get(r))[1].intValue()] = 1;
/*     */     }
/*     */     
/* 318 */     this.ArrayOfElements = new int[this.nr];
/* 319 */     for (int i = 0; i < this.rows; i++) {
/* 320 */       for (int j = 0; j < this.cols; j++) {
/* 321 */         if (this.GridEnv[i][j] == -1) {
/* 322 */           double minV = ((double[][])Q.get(0))[i][j];
/* 323 */           int indMin = 0;
/* 324 */           for (int r = 1; r < this.nr; r++) {
/* 325 */             if (((double[][])Q.get(r))[i][j] < minV) {
/* 326 */               minV = ((double[][])Q.get(r))[i][j];
/* 327 */               indMin = r;
/*     */             }
/*     */           }
/* 330 */           this.A[i][j] = indMin;
/* 331 */           ((int[][])this.BWlist.get(indMin))[i][j] = 1;
/* 332 */           this.ArrayOfElements[indMin] += 1;
/* 333 */         } else if (this.GridEnv[i][j] == -2) { this.A[i][j] = this.nr;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private double EuclideanDis(double[] a, double[] b)
/*     */   {
/* 342 */     int vecSize = a.length;
/* 343 */     double d = 0.0D;
/* 344 */     for (int i = 0; i < vecSize; i++) d += Math.pow(a[i] - b[i], 2.0D);
/* 345 */     return Math.sqrt(d);
/*     */   }
/*     */   
/*     */   private double EuclideanDis(Integer[] a, Integer[] b) {
/* 349 */     int vecSize = a.length;
/* 350 */     double d = 0.0D;
/* 351 */     for (int i = 0; i < vecSize; i++) d += Math.pow(a[i].intValue() - b[i].intValue(), 2.0D);
/* 352 */     return Math.sqrt(d);
/*     */   }
/*     */   
/*     */   private int[][] deepCopyMatrix(int[][] input) {
/* 356 */     if (input == null)
/* 357 */       return (int[][])null;
/* 358 */     int[][] result = new int[input.length][];
/* 359 */     for (int r = 0; r < input.length; r++) {
/* 360 */       result[r] = ((int[])input[r].clone());
/*     */     }
/* 362 */     return result;
/*     */   }
/*     */   
/*     */   private ArrayList<double[][]> deepCopyListMatrix(ArrayList<double[][]> input) {
/* 366 */     if (input == null)
/* 367 */       return null;
/* 368 */     ArrayList<double[][]> result = new ArrayList();
/* 369 */     for (int r = 0; r < input.size(); r++) {
/* 370 */       result.add(deepCopyMatrix((double[][])input.get(r)));
/*     */     }
/* 372 */     return result;
/*     */   }
/*     */   
/*     */   private double[][] deepCopyMatrix(double[][] input) {
/* 376 */     if (input == null)
/* 377 */       return (double[][])null;
/* 378 */     double[][] result = new double[input.length][];
/* 379 */     for (int r = 0; r < input.length; r++) {
/* 380 */       result[r] = ((double[])input[r].clone());
/*     */     }
/* 382 */     return result;
/*     */   }
/*     */   
/*     */   private int[] deepCopyMatrix(int[] input) {
/* 386 */     if (input == null)
/* 387 */       return null;
/* 388 */     int[] result = (int[])input.clone();
/* 389 */     return result;
/*     */   }
/*     */   
/*     */   private double[] deepCopyMatrix(double[] input) {
/* 393 */     if (input == null)
/* 394 */       return null;
/* 395 */     double[] result = (double[])input.clone();
/* 396 */     return result;
/*     */   }
/*     */   
/*     */   private float[][] deepCopyMatrix(float[][] input) {
/* 400 */     if (input == null)
/* 401 */       return (float[][])null;
/* 402 */     float[][] result = new float[input.length][];
/* 403 */     for (int r = 0; r < input.length; r++) {
/* 404 */       result[r] = ((float[])input[r].clone());
/*     */     }
/* 406 */     return result;
/*     */   }
/*     */   
/*     */   private void defineRobotsObstacles()
/*     */   {
/* 411 */     for (int i = 0; i < this.rows; i++) {
/* 412 */       for (int j = 0; j < this.cols; j++) {
/* 413 */         if (this.GridEnv[i][j] == 2) {
/* 414 */           this.robotBinary[i][j] = true;
/* 415 */           this.RobotsInit.add(new Integer[] { Integer.valueOf(i), Integer.valueOf(j) });
/* 416 */           this.GridEnv[i][j] = this.nr;
/* 417 */           this.A[i][j] = this.nr;
/* 418 */           this.nr += 1;
/*     */         }
/* 420 */         else if (this.GridEnv[i][j] == 1) {
/* 421 */           this.ob += 1;
/* 422 */           this.GridEnv[i][j] = -2;
/* 423 */         } else { this.GridEnv[i][j] = -1;
/*     */         }
/*     */       }
/*     */     }
/* 427 */     this.ConnectedRobotRegions = new boolean[this.nr];
/*     */   }
/*     */   
/*     */   private void printMatrix(int[][] M) {
/* 431 */     int r = M.length;
/* 432 */     int c = M[0].length;
/*     */     
/* 434 */     System.out.println();
/* 435 */     for (int i = 0; i < r; i++) {
/* 436 */       for (int j = 0; j < c; j++) {
/* 437 */         System.out.print(M[i][j] + " ");
/*     */       }
/* 439 */       System.out.println();
/*     */     }
/*     */   }
/*     */   
/*     */   private void printMatrix(float[][] M) {
/* 444 */     int r = M.length;
/* 445 */     int c = M[0].length;
/*     */     
/* 447 */     System.out.println();
/* 448 */     for (int i = 0; i < r; i++) {
/* 449 */       for (int j = 0; j < c; j++) {
/* 450 */         System.out.print(M[i][j] + " ");
/*     */       }
/* 452 */       System.out.println();
/*     */     }
/*     */   }
/*     */   
/*     */   private void printMatrix(double[][] M) {
/* 457 */     int r = M.length;
/* 458 */     int c = M[0].length;
/*     */     
/* 460 */     System.out.println();
/* 461 */     for (int i = 0; i < r; i++) {
/* 462 */       for (int j = 0; j < c; j++) {
/* 463 */         System.out.print(M[i][j] + " ");
/*     */       }
/* 465 */       System.out.println();
/*     */     }
/*     */   }
/*     */   
/* 469 */   public boolean getSuccess() { return this.success; }
/* 470 */   public int getNr() { return this.nr; }
/* 471 */   public int getNumOB() { return this.ob; }
/* 472 */   public int[][] getAssignmentMatrix() { return this.A; }
/* 473 */   public boolean[][] getRobotBinary() { return this.robotBinary; }
/* 474 */   public ArrayList<boolean[][]> getBinrayRobotRegions() { return this.BinrayRobotRegions; }
/* 475 */   public ArrayList<Integer[]> getRobotsInit() { return this.RobotsInit; }
/* 476 */   public int getEffectiveSize() { return 4 * (this.rows * this.cols - this.ob); }
/* 477 */   public int getMaxCellsAss() { return 4 * (this.maxCellsAss + 1); }
/* 478 */   public int getMinCellsAss() { return 4 * (this.minCellsAss + 1); }
/* 479 */   public double getElapsedTime() { return this.elapsedTime; }
/* 480 */   public int getDiscr() { return this.discr; }
/* 481 */   public int getMaxIter() { return this.maxIter; }
/* 482 */   public void setCanceled(boolean c) { this.canceled = c; }
/* 483 */   public int getAchievedDiscr() { return this.maxCellsAss - this.minCellsAss; }
/*     */ }


/* Location:              D:\New folder\SOP\DARP\DARP.jar!\DARP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */