package algo;
import pathfinding.*;
import pathfinding.Map;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
//import java.lang.Math;
//import javax.swing.event.ChangeEvent;
//import javax.swing.event.ChangeListener;
 
			public class MainGUI
			{
        	 private int c1=0;
        	 private static int no_chromosomes = 10; //stores number of chromosomes in each generation
        	 private static int no_groups_for_generations_k = 2;
        	 private static int no_generations = 50;
        	 //private static int no_tasks = 7; //stores the number of tasks in the problem
        	 private ArrayList<Color> colorArr;
			 public Thread t1;	//Thread for all the robots to start moving
			 public static int rangeView=100; //Range of robot sensing		
			 //public JSlider RangeSlider; //SpeedSlider for range selection
			 public static int robos=0; //Total number of robots in the field
			 public static int[][] visitedGrid;//Grid to represent discovered terrain
			 public static ArrayList<Robot> Robots; //List for Robots
			 public ArrayList<JPanel> selectedPanel; //Temporary assignment of goals to each robot
			 public ArrayList<pair> listoftasks; //to store all tasks of the problem
			 public ArrayList<Integer> costoftasks; //to store cost of service of each task 
			 public static Integer numberoftasks; //to store the number of total tasks
			 public static Integer numberofrobots;
			 public ArrayList<pair> listofrobots; //to store all robots in the problem with their initial positions
			 public static int[][][] travelcosts_ijk; //cost matrix for going from ith task to kth task by kth robot

			 public static Integer[][] chromosomes; //to store the population at each generation
			 public static Integer[][] gene_partition_current_gen = new Integer[no_chromosomes][2]; //to store the gene apportions of curren generation
			 
			 public static Integer[][] best_chromosomes;
			 public static Integer[][] gene_partition_best_chromosomes = new Integer[no_generations][2];
			 public static Integer[] fitness_function_value_best_chromosomes = new Integer[no_generations];

			 public static Integer[] temp_chromosome;
			 public static Integer[] global_best_soln;

			 public static Integer[] fitness_function_value;
			 public Map<ExampleNode> myMap;
			 /*
			  * Image Icons for start run and stop buttons
			  * 
			  * */
			 private final ImageIcon run_Hover = new ImageIcon("C:\\Users\\Amrit\\Documents\\Eclipse\\darp_mycopy\\src\\resources\\run_Hover.png");
			 private final ImageIcon run_Default = new ImageIcon("C:\\Users\\Amrit\\Documents\\Eclipse\\darp_mycopy\\src\\resources\\run_Default.png");
			 private final ImageIcon run_Pressed = new ImageIcon("C:\\Users\\Amrit\\Documents\\Eclipse\\darp_mycopy\\src\\resources\\run_Pressed.png");
			 private final ImageIcon abort_Hover = new ImageIcon("C:\\Users\\Amrit\\Documents\\Eclipse\\darp_mycopy\\src\\resources\\abort_Hover.jpg");
			 private final ImageIcon abort_Default = new ImageIcon("C:\\Users\\Amrit\\Documents\\Eclipse\\darp_mycopy\\src\\resources\\abort_Default.png");
			 private final ImageIcon abort_Pressed = new ImageIcon("C:\\Users\\Amrit\\Documents\\Eclipse\\darp_mycopy\\src\\resources\\abort_Pressed.png");
			 private final ImageIcon start_Default = new ImageIcon("C:\\Users\\Amrit\\Documents\\Eclipse\\darp_mycopy\\src\\resources\\start3.png");
			 private final ImageIcon start_Pressed = new ImageIcon("C:\\Users\\Amrit\\Documents\\Eclipse\\darp_mycopy\\src\\resources\\start2.png");

			 private JFrame mainFrame; // The Frame of panels
			 //private JLabel RangeLabel;
			 //private JLabel SpeedLabel;
			 private JPanel UserInputPanel; 
			 private JPanel ConsolePanel;
			 private JPanel RightPanel;
			 //private JComboBox<Integer> textboxRows;// Selection of Rows in Grid
			 //private JComboBox<Integer> textboxCols;// Selection of Cols in Grid
               private JTextField textboxRows;
               private JTextField textboxCols;
               private JLabel Title; // Title of the GUI
               private javax.swing.ButtonGroup Items; // All the RadioButtons
			   private Color CurrColor = Color.MAGENTA; // Current Color to be painted
			 /*
			  * Four selection Radio Buttons for obstacles, unoccupied cell, Robot and Goals
			  * */
               private JRadioButton ObstaclesButton;
               private JRadioButton EmptyButton;
               private JRadioButton RobotButton;
               private JRadioButton RobotGoalButton;
               private JCheckBox SwitchGrid; //checkbox for switching gridlines on/off

               private JButton AbortDARP; // To stop the thread of moving robots
               private JButton startExp; // To Evaluate the path
			   private JButton simMotion;	// To start the motion
               private javax.swing.JTextPane consoleToPrint; //The Black Console showing various outputs
               private MainGUI.GridPane ColorGrid; //The ColorGrid of panels
               public static int[][] EnvironmentGrid; // The Matrix for storing robots, obstacles and Goal Locations
               private JPanel SuperRadio; //Panel for the buttons
               public static int rows; //Rows in Grid
		       public static int cols; //Columns in Grid 
		       private int CurrentIDXAdd; //Whether Robot or obstacle or Goal is being added  
		       private int axisScale; // Resizing the user panel according to number of rows and cols
               public static int timer=500; // The time interval between successive robot motion i.e. speed regulation 
			   public Robot m; // Robot object for utility
               //public int gridflag;
			   //public JSlider SpeedSlider; // To Regulate the speed of the robot
               private int[][] ByteImage; // Utility matrix to retain the value of environment Grid
               
               
               //This method generates a random color
                public static int random255(){
				ArrayList<Integer> a1=new ArrayList<>();
				for(int i=0;i<256;i++){
					a1.add(i);
				}
				Collections.shuffle(a1);
				return a1.get(0);
				}

				//to generate initial population
                public void generate_initial_chromosomes() 
                {
                		for(int i = 0; i<no_chromosomes ; i++)
                		{
                			for (int j = 0; j < numberoftasks; j++) 
                			{
                				chromosomes[i][j] = j+1;
                			}
     				    Collections.shuffle(Arrays.asList(chromosomes[i]));
     				    //System.out.println(Arrays.toString(chromosomes[i]));
     				    //System.out.printf("here%d\n",fitness_function_value[i]);
                		}
                }

                public void generate_initial_chromosomes(Integer[] b,int n) 
                {

                		//System.out.println(Arrays.toString(b));   
                		best_chromosomes[n-1] = b;
                		chromosomes[0] = b;
                		for(int i = 1; i<no_chromosomes ; i++)
                		{
                			for (int j = 0; j < numberoftasks; j++) 
                			{
                				chromosomes[i][j] = j+1;
                			}
     				    Collections.shuffle(Arrays.asList(chromosomes[i]));
     				    //System.out.println(Arrays.toString(chromosomes[i]));
     				    //System.out.printf("here%d\n",fitness_function_value[i]);
                		}
                }		

                //function to generate geneapportions to divide the chromosome into numberofrobots segments 
                public void generate_initial_gene_partitions() 
                {
                	int first = 0,second=0;
                	double val;
                	int flag;
                	Random r = new Random();
                	for(int i=0;i<no_chromosomes;i++)
                	{
                		flag = 0;
                		while(flag==0)
                		{
	                		val = r.nextGaussian() * numberoftasks;
	                		if(val >=1 && val < numberoftasks)
	                		{	
	                			first = (int) val;
	                			flag = 1;
	                		}	
                		}

                		flag=0;
                		while(flag==0)
                		{
	                		val = r.nextGaussian() * numberoftasks;
	                		if((int) val == first)
	                			continue;
	                		
	                		if(val >=1 && val < numberoftasks && (val != first))
	                		{
	                			
	                				//flag=0;

	                			second = (int) val;
	                			flag = 1;
                			}
                		}		
                		if(first < second)
                		{
                			gene_partition_current_gen[i][0] = first;
                			gene_partition_current_gen[i][1] = second;
                		}
                		else
                		{
                			gene_partition_current_gen[i][1] = first;
                			gene_partition_current_gen[i][0] = second;
                		}		
                		//System.out.printf("%d %d\n",gene_partition_current_gen[i][0],gene_partition_current_gen[i][1]);
                	}
				}

				public void generate_gene_partitions(double std_dev, double[] mean)
				{
					int first = 0,second=0;
                	double val;
                	int flag;
                	Random r = new Random();
                	for(int i=0;i<no_chromosomes;i++)
                	{
                		flag = 0;
                		while(flag==0)
                		{
	                		val = (r.nextGaussian() * std_dev) + mean[0];
	                		if(val >=1 && val < numberoftasks)
	                		{	
	                			first = (int) val;
	                			flag = 1;
	                		}	
                		}

                		flag=0;
                		while(flag==0)
                		{
	                		val = (r.nextGaussian() * std_dev) + mean[1];
	                		if((int) val == first)
	                			continue;
	                		
	                		if(val >=1 && val < numberoftasks && (val != first))
	                		{
	                			
	                				//flag=0;

	                			second = (int) val;
	                			flag = 1;
                			}
                		}		
                		if(first < second)
                		{
                			gene_partition_current_gen[i][0] = first;
                			gene_partition_current_gen[i][1] = second;
                		}
                		else
                		{
                			gene_partition_current_gen[i][1] = first;
                			gene_partition_current_gen[i][0] = second;
                		}		
                		//System.out.printf("%d %d\n",gene_partition_current_gen[i][0],gene_partition_current_gen[i][1]);
                	}
				}

				public void calculate_fitness_function()
				{
					int i,j;
					int temp;
					for(i=0;i<no_chromosomes;i++)
					{
						//loop for first robot
						fitness_function_value[i]=0; //make it zero before starting to calculate total cost
						temp =0;
						for(j=0;j<gene_partition_current_gen[i][0];j++)
						{
							//to add cost of robot travelling to first task from its start position
							if(j==0)
								temp += travelcosts_ijk[0][chromosomes[i][j]][0];
							//adding costs from a task to another
							if(j>0)
								temp += travelcosts_ijk[chromosomes[i][j-1]][chromosomes[i][j]][0];
							//adding the returning cost from last task to its start position
							if(j==(gene_partition_current_gen[i][0]-1))
								temp+= travelcosts_ijk[chromosomes[i][j]][0][0];


						}
						fitness_function_value[i] += temp;
						temp = 0;

						//loop for second robot
						for(;j<gene_partition_current_gen[i][1];j++)
						{
							if(j==gene_partition_current_gen[i][0])
								temp += travelcosts_ijk[0][chromosomes[i][j]][1];

							if(j>gene_partition_current_gen[i][0])
								temp += travelcosts_ijk[chromosomes[i][j-1]][chromosomes[i][j]][1];

							if(j==(gene_partition_current_gen[i][1]-1))
								temp += travelcosts_ijk[chromosomes[i][j]][0][1];
						}	
						fitness_function_value[i] += temp;
						temp = 0;
						//loop for third robot	
						for(;j<numberoftasks;j++)
						{
							if(j==gene_partition_current_gen[i][1])
								temp += travelcosts_ijk[0][chromosomes[i][j]][2];

							if(j>gene_partition_current_gen[i][1])
								temp += travelcosts_ijk[chromosomes[i][j-1]][chromosomes[i][j]][2];

							if(j==(numberoftasks-1))
								temp += travelcosts_ijk[chromosomes[i][j]][0][2];
						}
						fitness_function_value[i] += temp;
						//System.out.printf("%d\n",fitness_function_value[i]);
					} 
				}


				public int calculate_fitness_function_chromosome(Integer[] tc,int p1,int p2)
				{
					int j;
					int temp;
					int value=0;
					//for(i=0;i<no_chromosomes;i++)
					//{
						//loop for first robot
						value=0; //make it zero before starting to calculate total cost
						temp=0;
						for(j=0;j<p1;j++)
						{
							//to add cost of robot travelling to first task from its start position
							if(j==0)
								temp += travelcosts_ijk[0][tc[j]][0];
							//adding costs from a task to another
							if(j>0)
								temp += travelcosts_ijk[tc[j-1]][tc[j]][0];
							//adding the returning cost from last task to its start position
							if(j==(p1-1))
								temp+= travelcosts_ijk[tc[j]][0][0];


						}
						value += temp;
						temp = 0;

						//loop for second robot
						for(;j<p2;j++)
						{
							if(j==p1)
								temp += travelcosts_ijk[0][tc[j]][1];

							if(j>p1)
								temp += travelcosts_ijk[tc[j-1]][tc[j]][1];

							if(j==(p2-1))
								temp += travelcosts_ijk[tc[j]][0][1];
						}	
						value += temp;
						temp = 0;
						//loop for third robot	
						for(;j<numberoftasks;j++)
						{
							if(j==p2)
								temp += travelcosts_ijk[0][tc[j]][2];

							if(j>p2)
								temp += travelcosts_ijk[tc[j-1]][tc[j]][2];

							if(j==(numberoftasks-1))
								temp += travelcosts_ijk[tc[j]][0][2];
						}
						value += temp;

						return (value);
						//System.out.printf("%d\n",value);
					//} 
				}

				public void swap_mutation(Integer[] ch)
				{
					int flag=0;
					double val;
					int first =0,second=0,temp=0;
					Random r = new Random();
					//flag = 0;
                		while(flag==0)
                		{
	                		val = r.nextGaussian() * numberoftasks;
	                		if(val >=0 && val < numberoftasks)
	                		{	
	                			first = (int) val;
	                			flag = 1;
	                		}	
                		}

                		flag=0;
                		while(flag==0)
                		{
	                		val = r.nextGaussian() * numberoftasks;
	                		if((int) val == first)
	                			continue;
	                		
	                		if(val >=0 && val < numberoftasks && (val != first))
	                		{
	                			//flag=0;
	                			second = (int) val;
	                			flag = 1;
                			}
                		}
                		//System.out.println(Arrays.toString(ch));
                		temp = ch[first];
                		ch[first] = ch[second];
                		ch[second] = temp;

                		//System.out.printf("%d %d\n",first,second);
                		
                		//System.out.println(Arrays.toString(ch));
				}

				public void inversion_mutation(Integer[] ch)
				{
					int flag=0,first =0,second=0,temp=0,times=0;
					double val;
					Random r = new Random();
					//flag = 0;
                		while(flag==0)
                		{
	                		val = r.nextGaussian() * numberoftasks;
	                		if(val >=0 && val < numberoftasks)
	                		{	
	                			first = (int) val;
	                			flag = 1;
	                		}	
                		}

                		flag=0;
                		while(flag==0)
                		{
	                		val = r.nextGaussian() * numberoftasks;
	                		if((int) val == first)
	                			continue;
	                		
	                		if(val >=0 && val < numberoftasks && (val != first))
	                		{
	                			//flag=0;
	                			second = (int) val;
	                			flag = 1;
                			}
                		}
                		//System.out.println(Arrays.toString(ch));
                		//System.out.printf("%d %d\n",first,second);
                		if(first > second)
                		{
                			temp = first;
                			first = second;
                			second = temp;
                		}
                		//System.out.printf("%d %d\n",first,second);
                		times = (second-first) / 2;
                		
                		if((second-first)%2 != 0)
                			times++;


                		//System.out.printf("%d \n",times);
                		//i = first;
                		//j = second;
                		while(times>0)
                		{
                			temp = ch[first];
                			ch[first] = ch[second];
                			ch[second] = temp;
                			first++;
                			second--;
                			times--;
                		}

                		
                		
                		//System.out.println(Arrays.toString(ch));
				}

				public void insertion_mutation(Integer[] ch)
				{
					int flag=0,first=0,second=0,temp=0,i;
					double val;
					Random r = new Random();
					//flag = 0;
                		while(flag==0)
                		{
	                		val = r.nextGaussian() * numberoftasks;
	                		if(val >=0 && val < numberoftasks)
	                		{	
	                			first = (int) val;
	                			flag = 1;
	                		}	
                		}

                		flag=0;
                		while(flag==0)
                		{
	                		val = r.nextGaussian() * numberoftasks;
	                		if((int) val == first)
	                			continue;
	                		
	                		if(val >=0 && val < numberoftasks && (val != first))
	                		{
	                			//flag=0;
	                			second = (int) val;
	                			flag = 1;
                			}
                		}

                	if(first > second)
                	{
                		temp = first;
                		first = second;
                		second = temp;
                	}
                	//System.out.printf("%d %d\n",first,second);
                	//System.out.println(Arrays.toString(ch));		
					for(i = second-1; i >= first ; i--)
					{       
						temp = ch[i+1];
						ch[i+1] = ch[i];
						ch[i] = temp; 
					}
					//System.out.println(Arrays.toString(ch));
				}

				public void displacement_mutation(Integer[] ch)
				{
					int flag=0,first=0,second=0,target=0,temp=0,i,no_iterations=0;
					double val;
					Random r = new Random();
					//flag = 0;
                		while(flag==0)
                		{
	                		val = r.nextGaussian() * numberoftasks;
	                		if(val >=0 && val < numberoftasks)
	                		{	
	                			first = (int) val;
	                			flag = 1;
	                		}	
                		}

                		flag=0;
                		while(flag==0)
                		{
	                		val = r.nextGaussian() * numberoftasks;
	                		if((int) val == first)
	                			continue;
	                		
	                		if(val >=0 && val < numberoftasks && (val != first))
	                		{
	                			//flag=0;
	                			second = (int) val;
	                			flag = 1;
                			}
                		}

                	if(first > second)
                	{
                		temp = first;
                		first = second;
                		second = temp;
                	}

                	flag=0;
                	while(flag==0)
                	{	
	                	val = r.nextGaussian() * first;    
		                		
		                if(val >=0)
		                {
		                	target = (int) val;
		                	flag=1;
	                	}
	                }	

                	//System.out.printf("%d %d %d\n",first,second,target);
                	//System.out.println(Arrays.toString(ch));	
	                
	                while(no_iterations <= second-first)
	                {	
	                	i = first+no_iterations-1;
						for(; i >= (target+no_iterations); i--)
						{       
							temp = ch[i+1];
							ch[i+1] = ch[i];
							ch[i] = temp; 
						}
						no_iterations++;
					}	
					//System.out.println(Arrays.toString(ch));
				}

				public int best_chromosome()
				{
					int i,min_index = 0;
					//System.out.printf("%d %d\n",fitness_function_value[0],min_index);
					for(i=1;i<no_chromosomes;i++)
					{
						//System.out.printf("%d %d\n",fitness_function_value[i],min_index);
						//System.out.println(min_index);
						if(fitness_function_value[i] < fitness_function_value[min_index])
							min_index = i;
					}
					//System.out.println(min_index);
					
					return (min_index);
				}

				public void set_global_best_chromosome()
				{
					int i,min_index = 0;
					//System.out.printf("%d %d\n",fitness_function_value[0],min_index);
					for(i=1;i<no_chromosomes;i++)
					{
						//System.out.printf("%d %d\n",fitness_function_value[i],min_index);
						//System.out.println(min_index);
						if(fitness_function_value_best_chromosomes[i] < fitness_function_value_best_chromosomes[min_index])
							min_index = i;
					}
					//System.out.println(min_index);
					for(i=0;i<numberoftasks;i++)
						global_best_soln[i] = best_chromosomes[min_index][i];

					System.out.printf("%d the gene apportions are - %d %d",fitness_function_value_best_chromosomes[min_index],gene_partition_best_chromosomes[min_index][0],gene_partition_best_chromosomes[min_index][1]);
					System.out.println(Arrays.toString(global_best_soln));
					//return (min_index);
				}


				
				public void generate_generations()
				{
					//first generation is the initial one
					//population size = number of chromosomes in each gen = 10
					// number of groups we want to divide into for applying operations = k = 2
					// (10/5*2) = 1 (one best chromosome is reproduced without change in each generation)
					int best_index=0,i,j;
					//Integer[] temp_best = new Integer[numberoftasks];
					int generation_number = 1;
					double[] mean = new double[2];
					mean[0] = mean[1] = 0.0;
					double std_dev = 0.03*numberoftasks;

					while(generation_number <= no_generations)
					{	

						best_index = best_chromosome(); //compute the best chromosome index in each population
						//best_chromosomes[generation_number-1] = chromosomes[best_index]; //store the best in the best chromosome pool, one from each gen

						//System.out.printf("%d \n",fitness_function_value[best_index]);
						//System.out.println(Arrays.toString(best_chromosomes[generation_number-1]));
						
						//calculating mean for each new generation
						//System.out.printf("\n%d partition of best index %d\n",gene_partition_current_gen[best_index][0],gene_partition_current_gen[best_index][1]);
						/*if(generation_number==1)
						{	
							mean[0] = gene_partition_current_gen[best_index][0];
							mean[1] = gene_partition_current_gen[best_index][1];
						}*/	
						//else
						//{
							mean[0] *= generation_number-1;
							mean[1] *= generation_number-1;
							mean[0] += gene_partition_current_gen[best_index][0]; 
							mean[0] /= generation_number;
							mean[1] += gene_partition_current_gen[best_index][1];
							mean[1] /= generation_number;
						//}
						//System.out.printf("\n%f %f\n",mean[0],mean[1]);	
								

						//four mutation techniques applied to best in current population
						//System.out.println(Arrays.toString(chromosomes[best_index]));	
						swap_mutation(chromosomes[best_index]);
						//System.out.println(Arrays.toString(chromosomes[best_index]));
						insertion_mutation(chromosomes[best_index]);
						//System.out.println(Arrays.toString(chromosomes[best_index]));
						inversion_mutation(chromosomes[best_index]);
						//System.out.println(Arrays.toString(chromosomes[best_index]));
						displacement_mutation(chromosomes[best_index]);
						//System.out.println(Arrays.toString(chromosomes[best_index]));

						for(j=0;j<numberoftasks;j++)
							best_chromosomes[(generation_number-1)][j] = chromosomes[best_index][j];

						gene_partition_best_chromosomes[generation_number-1][0] = gene_partition_current_gen[best_index][0];
						gene_partition_best_chromosomes[generation_number-1][1] = gene_partition_current_gen[best_index][1];
						fitness_function_value_best_chromosomes[generation_number-1] = fitness_function_value[best_index];
						//best_chromosomes[(generation_number-1)] = chromosomes[best_index]; 
						//System.out.printf("bestchromosome gen-1 %d %d",generation_number-1,fitness_function_value[best_index]);
						//System.out.println(Arrays.toString(best_chromosomes[generation_number-1]));

						//temp_best = chromosomes[best_index];
					/*	for(j=0;j<numberoftasks;j++)
							temp_best[j] = chromosomes[best_index][j];*/

						//System.out.printf("\ntempbest");
						//System.out.println(Arrays.toString(temp_best));

						generate_initial_chromosomes(best_chromosomes[(generation_number-1)],generation_number);
						//System.out.printf("\n");



						generate_gene_partitions(std_dev,mean);



						/*System.out.printf("\ntempbest");
						System.out.println(Arrays.toString(temp_best));
						System.out.printf("\n");*/
						//chromosomes[0] = temp_best; //retain the best from last generation as the 0th chromosome in the new generation
						//System.out.println(Arrays.toString(chromosomes[0]));
						calculate_fitness_function();

						generation_number++;
					}
					//System.out.printf("\n");
					/*for(i=0;i<no_generations;i++)
					{
						//System.out.printf("bestchromosome gen-1 %d",fitness_function_value[best_index]);
						//System.out.println(Arrays.toString(best_chromosomes[i]));
						System.out.printf("\n %d partitions - %d %d\n",fitness_function_value_best_chromosomes[i],gene_partition_best_chromosomes[i][0],gene_partition_best_chromosomes[i][1]);
					}*/
				}

				public void print_best_chromosomes()
				{
					int i;
					double av_ff_value=0;
					for(i=0;i<no_generations;i++)
					{
						av_ff_value += fitness_function_value_best_chromosomes[i];
						System.out.printf("%d %d the gene apportions are - %d %d",i+1,fitness_function_value_best_chromosomes[i],gene_partition_best_chromosomes[i][0],gene_partition_best_chromosomes[i][1]);
						System.out.println(Arrays.toString(best_chromosomes[i]));
					}
					av_ff_value /= no_generations;
					System.out.printf("\n average ff value: %f\n",av_ff_value);
				}


				public void lsearch_2nearest_neighbours()
				{
					int i,j,k;
					int temp_index_value;
					int temp_ff_value=0;
					//Integer[] temp = new Integer[numberoftasks];
					for(i=0;i<no_generations;i++)
					{
						for(k=0;k<numberoftasks;k++)
							temp_chromosome[k] = best_chromosomes[i][k];

						temp_ff_value = fitness_function_value_best_chromosomes[i];

						for(j=0;j<(gene_partition_best_chromosomes[i][0]-1);j++)
						{
							temp_index_value = temp_chromosome[j];
							temp_chromosome[j] = temp_chromosome[j+1];
							temp_chromosome[j+1] = temp_index_value;

							temp_ff_value = calculate_fitness_function_chromosome(temp_chromosome,gene_partition_best_chromosomes[i][0],gene_partition_best_chromosomes[i][1]);

							if(temp_ff_value < fitness_function_value_best_chromosomes[i])
							{
								for(k=0;k<numberoftasks;k++)
									best_chromosomes[i][k] = temp_chromosome[k];

								fitness_function_value_best_chromosomes[i] = temp_ff_value;
							}
							else
							{
								temp_index_value = temp_chromosome[j];
								temp_chromosome[j] = temp_chromosome[j+1];
								temp_chromosome[j+1] = temp_index_value;
							}	
						}
						

						//loop for second robot
						for(j=gene_partition_best_chromosomes[i][0];j<(gene_partition_best_chromosomes[i][1]-1);j++)
						{
							temp_index_value = temp_chromosome[j];
							temp_chromosome[j] = temp_chromosome[j+1];
							temp_chromosome[j+1] = temp_index_value;

							temp_ff_value = calculate_fitness_function_chromosome(temp_chromosome,gene_partition_best_chromosomes[i][0],gene_partition_best_chromosomes[i][1]);

							if(temp_ff_value < fitness_function_value_best_chromosomes[i])
							{
								for(k=0;k<numberoftasks;k++)
									best_chromosomes[i][k] = temp_chromosome[k];

								fitness_function_value_best_chromosomes[i] = temp_ff_value;
							}
							else
							{
								temp_index_value = temp_chromosome[j];
								temp_chromosome[j] = temp_chromosome[j+1];
								temp_chromosome[j+1] = temp_index_value;
							}	
						}	
						
						//loop for third robot	
						for(j=gene_partition_best_chromosomes[i][1];j<(numberoftasks-1);j++)
						{
							temp_index_value = temp_chromosome[j];
							temp_chromosome[j] = temp_chromosome[j+1];
							temp_chromosome[j+1] = temp_index_value;

							temp_ff_value = calculate_fitness_function_chromosome(temp_chromosome,gene_partition_best_chromosomes[i][0],gene_partition_best_chromosomes[i][1]);

							if(temp_ff_value < fitness_function_value_best_chromosomes[i])
							{
								for(k=0;k<numberoftasks;k++)
									best_chromosomes[i][k] = temp_chromosome[k];

								fitness_function_value_best_chromosomes[i] = temp_ff_value;
							}
							else
							{
								temp_index_value = temp_chromosome[j];
								temp_chromosome[j] = temp_chromosome[j+1];
								temp_chromosome[j+1] = temp_index_value;
							}	
						}
					}	
				}


                
               @SuppressWarnings("static-access")
			MainGUI() // Constructor
               {
            	   
            	   colorArr=new ArrayList<>();
            	   Robots=new ArrayList<>();
            	   selectedPanel=new ArrayList<>();
			   this.mainFrame = new JFrame("Multi-Robot Path Planning for unknown terrain");
			   this.UserInputPanel = new JPanel();
               this.UserInputPanel.setLayout(new java.awt.FlowLayout(0));
			   //this.SpeedSlider=new JSlider(JSlider.HORIZONTAL, 10, 100, 10);
			   //this.RangeSlider=new JSlider(JSlider.HORIZONTAL,2,10,2);
			   //this.RangeSlider.addChangeListener(new RangeSliderListener());
			   //this.SpeedSlider.addChangeListener(new SpeedSliderListener());
		       this.SwitchGrid = new JCheckBox("Gridlines");
		       this.SwitchGrid.setBackground(Color.white);
		       this.ObstaclesButton = new JRadioButton("Obstacle");
		       this.ObstaclesButton.setBackground(Color.white);
		       this.EmptyButton = new JRadioButton("Unoccupied Cell");
		       this.EmptyButton.setBackground(Color.white);
		       this.RobotButton = new JRadioButton("Robot");
		       this.RobotButton.setBackground(Color.white);
			   this.RobotGoalButton = new JRadioButton("Robot Goal");
		       this.RobotGoalButton.setBackground(Color.white);
		       //this.RangeLabel=new JLabel("Range");
		       //this.SpeedLabel=new JLabel("Speed");
		       this.CurrentIDXAdd = -1;
		       this.axisScale = 400;
		       this.ColorGrid = null;
		       this.textboxRows = new JTextField(3);
		       this.textboxCols = new JTextField(3);
	
		       this.numberoftasks = 0;
		       this.numberofrobots=0;
		       this.costoftasks = new ArrayList<Integer>();
		       this.listoftasks = new ArrayList<pair>();
		       this.listofrobots = new ArrayList<pair>();
		       this.fitness_function_value = new Integer[no_chromosomes];

		       //this.gridflag = 0;
		       //textboxRows.setLayout(0,0,40,20);
		       //textboxRows.setLayout(0,30,40,20);
				//Integer[] choices=new Integer[105];
				//for(int i=1;i<=100;i++)choices[i-1]=(i);
		       //this.textboxRows = new JComboBox<>(choices);
		       //this.textboxRows.setSelectedIndex(9);
		       //this.textboxCols = new JComboBox<>(choices);
		       //this.textboxCols.setSelectedIndex(9);
		       DefineRightPanel();
                 
		       this.mainFrame.getContentPane().add("East", this.RightPanel);
		       this.mainFrame.setDefaultCloseOperation(3);
		       this.mainFrame.pack();
		       this.mainFrame.setLocationByPlatform(true);
		       this.mainFrame.setSize(1250, 840);
                 
		       this.mainFrame.setVisible(true);
               }


               public void build_map_for_cost_calculation()
               {
               		myMap = new Map<ExampleNode>(MainGUI.rows, MainGUI.cols, new ExampleFactory());
       	 			//List<ExampleNode> path = myMap.findPath(0, 0, 2, 2);
           		}

              

               public void ReadTextFile() throws IOException 
				{
				    File inFile = new File ("C:\\Users\\Amrit\\Documents\\Eclipse\\darp_mycopy\\src\\resources\\input.txt");

				    Scanner sc = new Scanner (inFile);
				    String[] temp;
				    int a,b,c,d,i,j;
				    pair p;
				    while (sc.hasNextLine())
				    {
				      String line = sc.nextLine();
				      temp = line.split(" ");
				      if(temp[0].equals("o"))
				      {
				    	a =  Integer.parseInt(temp[1]);
				    	b =  Integer.parseInt(temp[2]);	
				    	c =  Integer.parseInt(temp[3]);
				    	d =  Integer.parseInt(temp[4]);				    	
				    	for(i=a;i<=b;i++)
				    	{
				    		for(j=c;j<=d;j++)
				    		{
				    			MainGUI.EnvironmentGrid[i][j] = 1;
				    			myMap.setWalkable(i,j,false);
				    		}
				    	}
				      }
				      else if(temp[0].equals("r"))
				      {
				    	a =  Integer.parseInt(temp[1]);
					    b =  Integer.parseInt(temp[2]);
					    p = new pair(a,b);
					    MainGUI.EnvironmentGrid[a][b] = 2;
					    MainGUI.numberofrobots++;
					    listofrobots.add(p);
					    //myMap.setWalkable(a,b,false);
				      }
				      else if(temp[0].equals("t"))
				      {
				    	a =  Integer.parseInt(temp[1]);
					    b =  Integer.parseInt(temp[2]);
					    p = new pair(a,b); 
					    MainGUI.EnvironmentGrid[a][b] = 3;
					    MainGUI.numberoftasks++;
					    costoftasks.add(1);
					    listoftasks.add(p);
				      }
				      
				    }
				    sc.close();
				    
				    
				   /* for(i=0;i<numberofrobots;i++)
				    {
				    	
				    	System.out.println(listofrobots.get(i));
				    }*/
				    
				}
               
                              
               //function to compute cost for travelling from ith to jth task by kth robot
     			public void cost_calc()
     			{
     				int i,j,k;
     				travelcosts_ijk = new int[numberoftasks+1][numberoftasks+1][numberofrobots];
            	    //we fill the matrix with zeros and use it as a check to not compute costs twice between any two locations
					for(k=0;k<numberofrobots;k++)
				    {
				    	for(i=0;i<=numberoftasks;i++)
				    	{
				    		for(j=0;j<=numberoftasks;j++)
				    		{
				    			travelcosts_ijk[i][j][k] = 0;
				    		}
				    	}
				    }			            	
				    List<ExampleNode> path; //temp file to compute costs
				    	//System.out.printf("%d %d\n",numberofrobots,numberoftasks);
				    	//System.out.printf("%d %d\n",listofrobots.get(k).first,listofrobots.get(k).second);
				    	//i=0;
				    for(k=0;k<numberofrobots;k++)
				    {	
				    	for(i=0;i<=numberoftasks;i++)
				    	{
				    		//j=0;
				    		for(j=0;j<=numberoftasks;j++)
				    		{

				    			if(i==j)
			 	    				continue;

			 	    			/*if(!(travelcosts_ijk[i][j][k]==0))
			 	    				continue;*/

			 	    			if(i>0 && j>0)
			 	    			{
			 	    				path = myMap.findPath(listoftasks.get(i-1).first,listoftasks.get(i-1).second,listoftasks.get(j-1).first,listoftasks.get(j-1).second);
			 	    				travelcosts_ijk[i][j][k] = path.size();
			 	    				travelcosts_ijk[j][i][k] = path.size();
			 	    			}
			 	    			else if(i==0)
			 	    			{
			 	    				path = myMap.findPath(listofrobots.get(k).first,listofrobots.get(k).second,listoftasks.get(j-1).first,listoftasks.get(j-1).second);
			 	    				//System.out.printf("%d %d\n",listofrobots.get(k).first,listofrobots.get(k).second);
			 	    				travelcosts_ijk[i][j][k] = path.size();
			 	    				travelcosts_ijk[j][i][k] = path.size();
			 	    			}	

				    		}	
				    	}
				    }	
 	    
 	    			//printing the costs to check
				    /*for(k=0;k<numberofrobots;k++)
				    {
				    	for(i=0;i<=numberoftasks;i++)
				    	{
				    		for(j=0;j<=numberoftasks;j++)
				    		{
				    			System.out.printf("%d ",travelcosts_ijk[i][j][k]);
				    		}
				    		System.out.printf("\n");
				    	}
				    	System.out.printf("\n\n");
				    }
				    System.out.printf("\n");*/	
     			}

             
               private void DefineRightPanel() //Defines the Right Panel with details
               {
		       this.RightPanel = new JPanel();
		       this.RightPanel.setPreferredSize(new Dimension(295, 800));
		       this.RightPanel.setLayout(new java.awt.GridBagLayout());
		       java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		       DefineGridDimensions();
		       DefineConsole();
		       gbc.gridx = (gbc.gridy = 0);
		       gbc.gridwidth = (gbc.gridheight = 1);
		       gbc.fill = 1;
		       gbc.anchor = 18;
		       gbc.weightx = (gbc.weighty = 70.0D);
		       this.RightPanel.add(this.UserInputPanel, gbc);
                 
		       gbc.gridy = 1;
		       gbc.weightx = (gbc.weighty = 30.0D);
		       gbc.insets = new java.awt.Insets(2, 2, 2, 2);
		       this.RightPanel.add(this.ConsolePanel, gbc);
               }
               
               private void DefineGridDimensions() // Creating initial panels
               {
		       this.UserInputPanel.setBackground(Color.white);
                 
		       this.Title = new JLabel("Operational Environment Size");
		       this.Title.setFont(new Font("serif", 1, 18));
                 
		       JLabel textRows = new JLabel("#Rows:  2x");
                 
		       JLabel textCols = new JLabel("   #Cols:  2x");
                 
		       JButton submitRowsCols = new JButton("Submit");
		       submitRowsCols.addActionListener(new MainGUI.submitRowsColsListener());
                 
		       JPanel RowsPane = new JPanel();
		       RowsPane.setBackground(Color.white);
		       RowsPane.add(textRows);
		       RowsPane.add(this.textboxRows);
		       JPanel ColsPane = new JPanel();
		       ColsPane.setBackground(Color.white);
		       ColsPane.add(textCols);
		       ColsPane.add(this.textboxCols);
		       JPanel SubmitPane = new JPanel();
		       SubmitPane.setBackground(Color.white);
		       SubmitPane.add(submitRowsCols);
                 
		       JPanel FirstOption = new JPanel();
		       FirstOption.setBackground(Color.white);
		       FirstOption.add(new javax.swing.JSeparator());
		       FirstOption.setLayout(new javax.swing.BoxLayout(FirstOption, 1));
		       FirstOption.add(new JLabel("Create an empty grid with dimensions:"));
		       FirstOption.add(RowsPane);
		       FirstOption.add(ColsPane);
		       FirstOption.add(SubmitPane);

		       this.UserInputPanel.add(this.Title);
		       this.UserInputPanel.add(FirstOption);

               }
             
               private void DefineConsole() // Creating the output console on the gui
               {
		       this.ConsolePanel = new JPanel();
			   ConsolePanel.setPreferredSize(new Dimension(275,250));
		       this.ConsolePanel.setBackground(Color.white);
		       this.ConsolePanel.setLayout(new javax.swing.BoxLayout(this.ConsolePanel, 1));
		       this.consoleToPrint = new javax.swing.JTextPane();
                 
             
		       JLabel ConsoleTitle = new JLabel("Console");
		       ConsoleTitle.setFont(new Font("serif", 1, 18));
		       this.ConsolePanel.add(ConsoleTitle);
		       this.ConsolePanel.add(this.consoleToPrint);
                 
		       this.consoleToPrint.setBackground(Color.black);
		       this.consoleToPrint.setEditable(false);
                 
		       javax.swing.JScrollPane scrollConsole = new javax.swing.JScrollPane(this.consoleToPrint, 22, 32);
                 
             
		       this.ConsolePanel.add(scrollConsole);
               }
               
               
               /*
                * Utility method to show the contents of EnvironmentGrid on the Console Pane
                * */
               
               void showEnvironmentGrid(){
					for(int i=0;i<rows;i++){
						StringBuilder sb=new StringBuilder();
						for(int j=0;j<cols;j++){
							sb.append(""+EnvironmentGrid[i][j]+" ");
						}
						appendToPane(sb.toString()+"\n",Color.WHITE);
					}
					appendToPane("-----------\n\n",Color.white);
				}
               
               // Utility method to show the EnvironmentGrid on the Standard Output
               public static void showEnvironmentGrid2(){
            	   for(int i=0;i<rows;i++){
            		   for(int j=0;j<cols;j++){
            			   System.out.print(""+EnvironmentGrid[i][j]+" ");
            		   }
            		   System.out.println();
            	   }
               }
             /*
              * The Core GUI defining method:
              * Sets the radio buttons and start stop run buttons
              * Action Listener for start
              * 
              * */
               private void DefineRobotsObstacles() throws IOException
               {
		       this.UserInputPanel.removeAll();
		       this.UserInputPanel.setBackground(Color.white);
                 

		         MainGUI.EnvironmentGrid = new int[rows][cols];
		         MainGUI.visitedGrid = new int[rows][cols];
		         this.ColorGrid = new MainGUI.GridPane();
                 
		       this.mainFrame.getContentPane().add("Center", this.ColorGrid);
                 
		       this.Title = new JLabel("Obstacles - Robots Locations");
		       this.Title.setFont(new Font("serif", 1, 18));
                 
             
		       JPanel RadioAreaButtons = new JPanel();
		       RadioAreaButtons.setBackground(Color.WHITE);
		       this.Items = new javax.swing.ButtonGroup();
		       this.Items.add(this.SwitchGrid);
		       this.Items.add(this.ObstaclesButton);
		       this.Items.add(this.EmptyButton);
		       this.Items.add(this.RobotButton);
			   this.Items.add(this.RobotGoalButton);
				
                 
             
		       this.ObstaclesButton.setSelected(true);
		       this.CurrentIDXAdd = 1;
		       this.CurrColor = Color.BLACK;

		       appendToPane("Click to add an obstacle\n\n", Color.WHITE);
                 
             
		       RadioAreaButtons.setLayout(new javax.swing.BoxLayout(RadioAreaButtons, 1));
		       RadioAreaButtons.add(this.SwitchGrid, "West");
		       RadioAreaButtons.add(this.ObstaclesButton, "West");
		       RadioAreaButtons.add(this.EmptyButton, "West");
		       RadioAreaButtons.add(this.RobotButton, "West");
		       RadioAreaButtons.add(this.RobotGoalButton, "West");
				
		       //JPanel SliderPanel = new JPanel();
		       //SliderPanel.setBackground(Color.WHITE);
		       //SliderPanel.add(this.SpeedLabel);
			   //SliderPanel.add(this.SpeedSlider,"WEST");
			   //JPanel SliderPanel2 = new JPanel();
			   //SliderPanel2.add(this.RangeLabel);
		      // SliderPanel2.setBackground(Color.WHITE);
			  // SliderPanel2.add(this.RangeSlider,"WEST");
                 
                 
		       this.SuperRadio = new JPanel();
		       this.SuperRadio.setPreferredSize(new Dimension(265, 400));
		       this.SuperRadio.setBackground(Color.WHITE);
		       this.SuperRadio.add(RadioAreaButtons);
				//this.SuperRadio.add(SliderPanel);
				//this.SuperRadio.add(SliderPanel2);
		       this.SuperRadio.setBorder(BorderFactory.createTitledBorder("Elements"));
               
               //this.SwitchGrid.addItemListener(new MainGUI.CheckBoxListener());
               this.SwitchGrid.setSize(100,100);  
		       this.ObstaclesButton.addActionListener(new MainGUI.CurrentComponentToAdd());
		       this.EmptyButton.addActionListener(new MainGUI.CurrentComponentToAdd());
		       this.RobotButton.addActionListener(new MainGUI.CurrentComponentToAdd());
		       this.RobotGoalButton.addActionListener(new MainGUI.CurrentComponentToAdd());

		       this.startExp = new JButton(this.run_Default);
		       this.startExp.addActionListener(new MainGUI.StartDARP());
		       this.startExp.addMouseListener(new MainGUI.RunButton());
		       this.startExp.setBorderPainted(false);
		       this.startExp.setFocusPainted(false);
		       this.startExp.setContentAreaFilled(false);

		       this.simMotion = new JButton(this.start_Default);
		       this.simMotion.addMouseListener(new MainGUI.StartButton());
		       this.simMotion.setBorderPainted(false);
		       this.simMotion.setFocusPainted(false);
		       this.simMotion.setContentAreaFilled(false);
		       
		       //----Action Listener for simMotion. Starts a thread that changes the environment grid periodically and robot position
		       this.simMotion.addActionListener(new MainGUI.simMotionListener());
                 
		       this.AbortDARP = new JButton(this.abort_Default);
		       this.AbortDARP.addActionListener(new MainGUI.AbortDARPListener());
		       this.AbortDARP.addMouseListener(new MainGUI.AbortButton());
		       this.AbortDARP.setBorderPainted(false);
		       this.AbortDARP.setFocusPainted(false);
		       this.AbortDARP.setContentAreaFilled(false);
                 
		       RadioAreaButtons.add(this.startExp);
		       RadioAreaButtons.add(this.simMotion);
		       RadioAreaButtons.add(this.AbortDARP);

		       JButton ReturnButton = new JButton("Return");
		       ReturnButton.addActionListener(new MainGUI.ReturnToTheInitialGUI());
                 
                 
		       this.UserInputPanel.add(ReturnButton);
		       this.UserInputPanel.add(this.Title);
		       this.UserInputPanel.add(this.SuperRadio);
                 
               }
               
               // Utility method to add text to the console on gui
               private void appendToPane(String msg, Color c)
               {
		       javax.swing.text.StyleContext sc = javax.swing.text.StyleContext.getDefaultStyleContext();
		       javax.swing.text.AttributeSet aset = sc.addAttribute(javax.swing.text.SimpleAttributeSet.EMPTY, javax.swing.text.StyleConstants.Foreground, c);
                 
		       aset = sc.addAttribute(aset, javax.swing.text.StyleConstants.FontFamily, "Lucida Console");
		       aset = sc.addAttribute(aset, javax.swing.text.StyleConstants.Alignment, Integer.valueOf(3));
		       this.consoleToPrint.setEditable(true);
		       int len = this.consoleToPrint.getDocument().getLength();
		       this.consoleToPrint.setCaretPosition(len);
		       this.consoleToPrint.setCharacterAttributes(aset, false);
		       this.consoleToPrint.replaceSelection(msg);
		       this.consoleToPrint.setEditable(false);
               }


               //Change Listener for RangeSlider
               /*private class RangeSliderListener implements ChangeListener{
            	   private RangeSliderListener(){}

				@Override
				public void stateChanged(ChangeEvent arg0) {
					// TODO Auto-generated method stub
					   MainGUI.this.appendToPane(RangeSlider.getValue()+"\n", Color.cyan);
					   rangeView=RangeSlider.getValue();
				}
            	   
               }*/
               //Change Listener for SpeedSlider
               /*private class SpeedSliderListener implements ChangeListener{
            	   private SpeedSliderListener(){}

				@Override
				public void stateChanged(ChangeEvent arg0) {
					   MainGUI.this.appendToPane(SpeedSlider.getValue()+"\n", Color.white);
					   timer=10000/SpeedSlider.getValue();
					   }
               }*/
               
            // Action Listener for simMotion Button
               private class simMotionListener implements ActionListener {
                 private simMotionListener() {}
                 
		       public void actionPerformed(ActionEvent event) {
			        t1=new Thread(new Runnable(){
						@Override
						public void run() {
							// TODO Auto-generated method stub
							int len=Collections.max(Robots, new Comparator<Robot>(){
								@Override
								public int compare(Robot e1, Robot e2) {
									// TODO Auto-generated method stub
									return e1.path.size()-e2.path.size();
								}}).path.size();
							
							//this loop is for coloring visited squares when simulation of the path is going on (while robot is moving)
					  		for(int i=0;i<len;i++){
					  			for(int j=0;j<Robots.size();j++){
						  			if(i+1<Robots.get(j).path.size()){
						  				EnvironmentGrid[Robots.get(j).path.get(i+1).first][Robots.get(j).path.get(i+1).second]=2;
						  				Robots.get(j).RoboLoc.first=Robots.get(j).path.get(i+1).first;
						  				Robots.get(j).RoboLoc.second=Robots.get(j).path.get(i+1).second;
							  			if(i<Robots.get(j).path.size())EnvironmentGrid[Robots.get(j).path.get(i).first][Robots.get(j).path.get(i).second]=0;
						  			}
						  		}
						  		ByteImage=EnvironmentGrid;
						           MainGUI.this.mainFrame.remove(MainGUI.this.ColorGrid);
						           MainGUI.this.ColorGrid = new MainGUI.GridPane(ByteImage,Robots);
						           MainGUI.this.mainFrame.getContentPane().add("Center", MainGUI.this.ColorGrid);
						           MainGUI.this.mainFrame.setVisible(true);
						           MainGUI.this.mainFrame.repaint();
									try {
										Thread.sleep(100);
									} catch (Exception e) {
										e.printStackTrace();
									}
					  		}
						}
			        	
			        });
			       t1.start();
		       }
               }
               
               // Action Listener for Return Button
               private class ReturnToTheInitialGUI implements ActionListener {
                 private ReturnToTheInitialGUI() {}
                 
		       public void actionPerformed(ActionEvent event) { Object[] options = { "Yes, I want to start over", "Cancel" };
                   String msgToDISP;
		         if (event.getActionCommand().equals("Clear All")) { msgToDISP = "All the data will be discarded.\n Do you really want to continue?";
                   } else {
		           msgToDISP = "Any Robot/Obstacles initialization progress will be lost.\n Do you really want to continue?";
                   }
                   
		         int n = javax.swing.JOptionPane.showOptionDialog(MainGUI.this.mainFrame, msgToDISP, "WARNING", 0, 2, null, options, options[1]);
		         if (n == 0) {
		        	 MainGUI.this.mainFrame.dispose();
		        	 new MainGUI();
                   }
                 }
               }
               
               /*private class CheckBoxListener implements ItemListener {
	               	
	               	public void itemStateChanged(ItemEvent e1)
	               	{
	               		if(e1.getSource()==SwitchGrid)
	               		{
	               			if(e1.getStateChange()==ItemEvent.SELECTED)
	               			{	
	               				MainGUI.this.gridflag = 1;
	               				MainGUI.this.appendToPane("Gridlines ON\n\n", Color.WHITE);
	               				//MainGUI.this.mainFrame.repaint();
	               			}	
	               			if(e1.getStateChange()==ItemEvent.DESELECTED)
	               			{	
	               				MainGUI.this.gridflag = 0;
	               				MainGUI.this.appendToPane("Gridlines OFF\n\n", Color.WHITE);
	               				//MainGUI.this.mainFrame.repaint();
	               			}	
	               		}



	               		
	               	}
               	}	*/
               // Action Listener on changing the Radio Buttons
               private class CurrentComponentToAdd implements ActionListener { 
            	   
            	   private CurrentComponentToAdd() {}
                 
		       public void actionPerformed(ActionEvent event) { if ((event.getActionCommand().equals("Robot")) && (MainGUI.this.CurrentIDXAdd != 2)) {
		           MainGUI.this.CurrColor = Color.BLUE;
		           MainGUI.this.CurrentIDXAdd = 2;
		           MainGUI.this.appendToPane("Click to add a robot\n\n", Color.WHITE);
   		           MainGUI.this.appendToPane("Or click on a robot to assign the goals\n\n", Color.WHITE);

		         } else if ((event.getActionCommand().equals("Obstacle")) && (MainGUI.this.CurrentIDXAdd != 1)) {
		           MainGUI.this.CurrColor = Color.BLACK;
		           MainGUI.this.CurrentIDXAdd = 1;
		           MainGUI.this.appendToPane("Click to add an obstacle\n\n", Color.WHITE);
		         } else if ((event.getActionCommand().equals("Unoccupied Cell")) && (MainGUI.this.CurrentIDXAdd != 0)) {
		           MainGUI.this.CurrColor = Color.WHITE;
		           MainGUI.this.CurrentIDXAdd = 0;
		           MainGUI.this.appendToPane("Click to reset to an unoccupied cell\n\n", Color.WHITE);
		         } else if ((event.getActionCommand().equals("Robot Goal")) && (MainGUI.this.CurrentIDXAdd != 3)) {
		           MainGUI.this.CurrColor = Color.GREEN;
		           MainGUI.this.CurrentIDXAdd = 3;
		           MainGUI.this.appendToPane("Click to reset the goal \n\n", Color.WHITE);
                   }

		         MainGUI.this.mainFrame.repaint();
                 }
               }
               
               // Submit button Listener of the initial gui to take rows and cols input from user
               private class submitRowsColsListener implements ActionListener {
                 private submitRowsColsListener() {}
                 
                 public void actionPerformed(ActionEvent event) {
		         if(true)  
		            {
					MainGUI.rows=Integer.parseInt(textboxRows.getText());
					MainGUI.cols=Integer.parseInt(textboxCols.getText());
					build_map_for_cost_calculation(); //initiate map for cost calculations
		             MainGUI.this.appendToPane("The grid [" + MainGUI.rows + "," + MainGUI.cols + "] has been created\n\n", Color.WHITE);
		             MainGUI.this.appendToPane("Define the Robots initial positions along with the fixed obstacles\n\n", Color.WHITE);
		             try {
						MainGUI.this.DefineRobotsObstacles();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                   } 
		         MainGUI.this.mainFrame.setVisible(true);
		         MainGUI.this.mainFrame.repaint();
                 }
               }
               
               // Action Listener for Abort Button
               private class AbortDARPListener implements ActionListener {
                 private AbortDARPListener() {}
                 
                 @SuppressWarnings("deprecation")
				public void actionPerformed(ActionEvent event) {
                	 t1.stop();
                 }
               }
               
             
             
             // Mouse Listener for Abort Button
               private class AbortButton
                 implements java.awt.event.MouseListener
               {
                 private AbortButton() {}
                 
             
             
             
                 public void mouseEntered(MouseEvent e)
                 {
		         MainGUI.this.AbortDARP.setIcon(MainGUI.this.abort_Hover);
                 }
                 
                 public void mouseClicked(MouseEvent e) {
		         MainGUI.this.AbortDARP.setIcon(MainGUI.this.abort_Pressed);
                 }
                 
                 public void mouseReleased(MouseEvent e) {
		         MainGUI.this.AbortDARP.setIcon(MainGUI.this.abort_Hover);
                 }
                 
                 public void mouseExited(MouseEvent e) {
		         MainGUI.this.AbortDARP.setIcon(MainGUI.this.abort_Default);
                 }
                 
                 public void mousePressed(MouseEvent e) {
		         MainGUI.this.AbortDARP.setIcon(MainGUI.this.abort_Pressed);
                 }
               }
               
               // Mouse Listener for Start Button
               private class StartButton
               implements java.awt.event.MouseListener
             {
               private StartButton() {}
           
               public void mouseEntered(MouseEvent e)
               {
		         MainGUI.this.simMotion.setIcon(MainGUI.this.start_Default);
               }
               
               public void mouseClicked(MouseEvent e) {
		         MainGUI.this.simMotion.setIcon(MainGUI.this.start_Pressed);
               }
               
               public void mouseReleased(MouseEvent e) {
		         MainGUI.this.simMotion.setIcon(MainGUI.this.start_Default);
               }
               
               public void mouseExited(MouseEvent e) {
		         MainGUI.this.simMotion.setIcon(MainGUI.this.start_Default);
               }
               
               public void mousePressed(MouseEvent e) {
		         MainGUI.this.simMotion.setIcon(MainGUI.this.start_Pressed);
               }
             }
               
               
               
               //Mouse Listener for Run Button
               private class RunButton implements java.awt.event.MouseListener
               {
                 private RunButton() {}
                 
                 public void mouseEntered(MouseEvent e) {
		         MainGUI.this.startExp.setIcon(MainGUI.this.run_Hover);
                 }
                 
                 public void mouseClicked(MouseEvent e) {
		         MainGUI.this.startExp.setIcon(MainGUI.this.run_Pressed);
                 }
                 
                 public void mouseReleased(MouseEvent e) {
		         MainGUI.this.startExp.setIcon(MainGUI.this.run_Hover);
                 }
                 
                 public void mouseExited(MouseEvent e) {
		         MainGUI.this.startExp.setIcon(MainGUI.this.run_Default);
                 }
                 
                 public void mousePressed(MouseEvent e) {
		         MainGUI.this.startExp.setIcon(MainGUI.this.run_Pressed);
                 }
               }

               //Action Listener for Run Button
               private class StartDARP implements ActionListener
               {
                 private StartDARP() {}
                 
                 public void actionPerformed(ActionEvent event)
                 	{ 
                	 // Calculating the path of robot
				for(Robot m1:Robots){
					robos++;
					m1.RoboId=10000+robos;
					m1.setPath();
//					appendToPane("\nRoboLoc : : "+m1.RoboLoc,Color.cyan);
//					appendToPane("\nGoalnRobot : : :"+m1.goalnrobot,Color.cyan);
//					appendToPane("\nObs : : : "+Robot.countObs2,Color.cyan);
//					appendToPane("\nObs Coord : : : "+Robot.ObsArray,Color.cyan);
//					appendToPane("\nPath : : : "+m1.path,Color.cyan);
				}
				colorArr.add(Color.BLACK);
				colorArr.add(Color.WHITE);
				colorArr.add(Color.BLUE);
				colorArr.add(Color.GREEN);
				colorArr.add(Color.YELLOW);
				c1=15+robos;
				while(colorArr.size()<c1){
					Color color1=new Color(random255(),random255(),random255());
					if(!colorArr.contains(color1))colorArr.add(color1);
				}

				ByteImage=EnvironmentGrid;
		           MainGUI.this.mainFrame.remove(MainGUI.this.ColorGrid);
		           MainGUI.this.ColorGrid = new MainGUI.GridPane(ByteImage,robos);
		           MainGUI.this.mainFrame.getContentPane().add("Center", MainGUI.this.ColorGrid);
		           MainGUI.this.mainFrame.setVisible(true);
		           MainGUI.this.mainFrame.repaint();	
				   showEnvironmentGrid();
					}
               }
               
               
               private class GridPane // This class is used to create the GUI according to the environment grid
                 extends JPanel
               {
				private static final long serialVersionUID = 1L;
				boolean enable = true;
				int i,j;
                 GridPane() throws IOException
                 {
		         setLayout(new java.awt.GridLayout(MainGUI.rows + 1, MainGUI.cols + 1));
		         setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
                   
		         for (i = 0; i < MainGUI.rows + 1; i++) {
		           for (j = -1; j < MainGUI.cols; j++) {
		             JPanel pan = new JPanel();
                       
		             pan.setEnabled(true);
		             pan.setBackground(Color.WHITE);
                       
		             pan.setPreferredSize(new Dimension(getWidth(), getHeight()));
		             if ((i < MainGUI.rows) && (j >= 0)) {
		               
		               if(MainGUI.rows < 60 || MainGUI.cols < 60) 	 
		               		pan.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		               
		               pan.addMouseListener(new MainGUI.BoxListener());
		               pan.setName(i + " " + j);
                       }
		             else if ((i == MainGUI.rows) && (j < 0)) { pan.add(new JLabel(" "));
		             } else if (i == MainGUI.rows) {
		               JLabel axisLabel = new JLabel(Integer.toString(j));
		               axisLabel.setFont(new Font("serif", 1, MainGUI.this.axisScale / MainGUI.cols));
		               pan.add(axisLabel);
             
                       }
		             else if (j < 0) {
		               JLabel axisLabel = new JLabel(Integer.toString(i));
		               axisLabel.setFont(new Font("serif", 1, MainGUI.this.axisScale / MainGUI.rows));
		               pan.add(axisLabel);
                       }
                       
             
		             add(pan);
                     }
                   }
                   
             		ReadTextFile();
             
            	    chromosomes = new Integer[no_chromosomes][numberoftasks];
            	    best_chromosomes = new Integer[no_generations][numberoftasks];
            	    temp_chromosome = new Integer[numberoftasks];
            	    global_best_soln = new Integer[numberoftasks];
            	    //intial generation functions
            	    //int times = 10;
            	    //while(times>0)
            	    //{
	            	    generate_initial_chromosomes();
	            	    generate_initial_gene_partitions();
	            	    cost_calc(); //one time thing
	            	    calculate_fitness_function();
	            	    generate_generations();
	            	    //print_best_chromosomes();
	            	    lsearch_2nearest_neighbours();
	            	    //System.out.printf("\nafter local search\n");
	            	    //print_best_chromosomes();

	            	    set_global_best_chromosome();

	            	//    times--;
	            	//}    
            	            	
		         repaint();
                 }
              
              

                 GridPane(int image[][],int robotNo){
		         setLayout(new java.awt.GridLayout(MainGUI.rows + 1, MainGUI.cols + 1));
		         setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
                   
		         for (int i = 0; i < MainGUI.rows + 1; i++) {
		           for (int j = -1; j < MainGUI.cols; j++) {
		             JPanel pan = new JPanel();
                       
		             pan.setEnabled(true);
		             pan.setBackground(Color.WHITE);
		             pan.setPreferredSize(new Dimension(getWidth(), getHeight()));
		             if ((i < MainGUI.rows) && (j >= 0))
                       {		            	 if(image[i][j]==9999){
		            		 pan.setBackground(Color.yellow);
		            	 }
		               if (image[i][j] == 0) {
		                 pan.setBackground(Color.WHITE);
		                 MainGUI.EnvironmentGrid[i][j] = 0;
                         } else if (image[i][j] == 1){
		                 pan.setBackground(Color.BLACK);
		                 MainGUI.EnvironmentGrid[i][j] = 1;
                         }
                         else if (image[i][j] == 2){
		                 pan.setBackground(Color.BLUE);
		                 pan.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		                 MainGUI.EnvironmentGrid[i][j] = 2;
                         }
                         else if (image[i][j] == 3){
		                 pan.setBackground(Color.GREEN);
		                 pan.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		                 MainGUI.EnvironmentGrid[i][j] = 3;
                         }else if (image[i][j] > 10000){
		                 pan.setBackground(colorArr.get(image[i][j]-10000+10));
		                 MainGUI.EnvironmentGrid[i][j] = image[i][j];
                         }

		               if(MainGUI.rows < 60 || MainGUI.cols < 60) 	 
		               		pan.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		               
		               pan.addMouseListener(new MainGUI.BoxListener());
		               pan.setName(i + " " + j);
                       }
		             else if ((i == MainGUI.rows) && (j < 0)) { pan.add(new JLabel(" "));
		             } 
		             else if (i == MainGUI.rows) {
		               JLabel axisLabel = new JLabel(Integer.toString(j));
		               axisLabel.setFont(new Font("serif", 1, MainGUI.this.axisScale / MainGUI.cols));
		               pan.add(axisLabel);
             
                       }
		             else if (j < 0) {
		               JLabel axisLabel = new JLabel(Integer.toString(i));
		               axisLabel.setFont(new Font("serif", 1, MainGUI.this.axisScale / MainGUI.rows));
		               pan.add(axisLabel);
                       }
		             add(pan);
                     }
                   }
		         repaint();

	}
                 GridPane(int image[][],ArrayList<Robot> r){
	
    
    setLayout(new java.awt.GridLayout(MainGUI.rows + 1, MainGUI.cols + 1));
    setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    int flag;  // to print axis labels only once and not for (number of robots time)
    for (int i = 0; i < MainGUI.rows + 1; i++) {
      for (int j = -1; j < MainGUI.cols; j++) {
        JPanel pan = new JPanel();  
        pan.setEnabled(true);
        pan.setBackground(Color.GRAY);
        pan.setPreferredSize(new Dimension(getWidth(), getHeight()));
        flag = 1;	
        for(Robot r1:r){
        	pair p=r1.RoboLoc;
            if ((i < MainGUI.rows) && (j >= 0)&&(((i<p.first+rangeView&&i>p.first-rangeView)&&(j<p.second+rangeView&&j>p.second-rangeView))||(visitedGrid[i][j]==1)))
            {
            	if(image[i][j]==9999){
            		pan.setBackground(Color.yellow);
            	}
            if (image[i][j] == 0) {
              pan.setBackground(Color.WHITE);
              MainGUI.EnvironmentGrid[i][j] = 0;
              } else if (image[i][j] == 1){
              pan.setBackground(Color.BLACK);
              MainGUI.EnvironmentGrid[i][j] = 1;
              }
              else if (image[i][j] == 2){
              pan.setBackground(Color.BLUE);
              pan.setBorder(BorderFactory.createLineBorder(Color.BLACK));
              MainGUI.EnvironmentGrid[i][j] = 2;
              }
              else if (image[i][j] == 3){
              pan.setBackground(Color.GREEN);
              pan.setBorder(BorderFactory.createLineBorder(Color.BLACK));
              MainGUI.EnvironmentGrid[i][j] = 3;
              }else if (image[i][j] > 10000){
              pan.setBackground(colorArr.get(image[i][j]-10000+10));
              MainGUI.EnvironmentGrid[i][j] = image[i][j];
              }
            visitedGrid[i][j]=1;

            if(MainGUI.rows < 60 || MainGUI.cols < 60) 	 
		        pan.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            
            pan.addMouseListener(new MainGUI.BoxListener());
            pan.setName(i + " " + j);
            }
          else if ((i == MainGUI.rows) && (j < 0)) { pan.add(new JLabel(" "));
          } 
          else if (i == MainGUI.rows && flag ==1) {
            JLabel axisLabel = new JLabel(Integer.toString(j));
            axisLabel.setFont(new Font("serif", 1, MainGUI.this.axisScale / MainGUI.cols));
            pan.add(axisLabel);
            flag=0;
            }
          else if (j < 0 && flag==1) {
            JLabel axisLabel = new JLabel(Integer.toString(i));
            axisLabel.setFont(new Font("serif", 1, MainGUI.this.axisScale / MainGUI.rows));
            pan.add(axisLabel);
            flag=0;
            }
        }

          

        add(pan);
        }
      }
    //repaint();
}
               }
               
			public Robot getRobot(pair p){ // Returns robot at particular coordinate
				for(int i=0;i<Robots.size();i++){
					if(Robots.get(i).RoboLoc.first==p.first&&Robots.get(i).RoboLoc.second==p.second)return Robots.get(i);
				}
				return null;
			}
			
			/*
			 * This class changes the color of each panel in gui based on selection
			 * It also initializes corresponding environment grid value
			 * 
			 * */
			
			
               private class BoxListener extends java.awt.event.MouseAdapter 
               {
                 private BoxListener() {}
                 
                 public void mousePressed(MouseEvent me) {
		         if ((MainGUI.this.CurrentIDXAdd >= 0) && (MainGUI.this.ColorGrid.enable)) {
		           JPanel clickedBox = (JPanel)me.getSource();
				   if(MainGUI.this.CurrentIDXAdd==2){
					   if(clickedBox.getBackground()==MainGUI.this.CurrColor){
						   clickedBox.setBackground(Color.GRAY);
						   MainGUI.this.appendToPane("\n\nClick on the goals to assign to the robot : : ",Color.WHITE);
		           	String[] iAndJ = clickedBox.getName().split("\\s+");
					pair p=new pair(Integer.parseInt(iAndJ[0]),Integer.parseInt(iAndJ[1]));
					m=MainGUI.this.getRobot(p);	
					if(m==null){
						m=new Robot(p);
						Robots.add(m);
						}
										
					   }
					   else if(clickedBox.getBackground()==Color.GRAY){
						   clickedBox.setBackground(MainGUI.this.CurrColor);
						   for(JPanel p:selectedPanel){
							   p.setBackground(Color.GREEN);
						   }
						   selectedPanel=new ArrayList<>();
						   for(pair p:m.Goals)
						   MainGUI.this.appendToPane(p+" ", Color.CYAN);
					   }
					   else if(clickedBox.getBackground()==Color.GREEN){
						   clickedBox.setBackground(Color.cyan);
						   MainGUI.this.appendToPane("\n\n Click on the goals to add more and robot to assign to it",Color.WHITE);
						   selectedPanel.add(clickedBox);
						   String[] iAndJ = clickedBox.getName().split("\\s+");
							pair p=new pair(Integer.parseInt(iAndJ[0]),Integer.parseInt(iAndJ[1]));
						   m.addGoal(p);
					   }
					   else{
		           clickedBox.setBackground(MainGUI.this.CurrColor);
		           String[] iAndJ = clickedBox.getName().split("\\s+");
		           MainGUI.EnvironmentGrid[Integer.parseInt(iAndJ[0])][Integer.parseInt(iAndJ[1])] = MainGUI.this.CurrentIDXAdd;
					   }
				   }
				   else{
		           clickedBox.setBackground(MainGUI.this.CurrColor);
		           String[] iAndJ = clickedBox.getName().split("\\s+");
		           MainGUI.EnvironmentGrid[Integer.parseInt(iAndJ[0])][Integer.parseInt(iAndJ[1])] = MainGUI.this.CurrentIDXAdd;
				   }
                   }
                 }
                 
                 public void mouseEntered(MouseEvent me) {
		         if ((me.getModifiers() == 16) && (MainGUI.this.CurrentIDXAdd != 2)&& (MainGUI.this.CurrentIDXAdd != 3) && 
		           (MainGUI.this.CurrentIDXAdd >= 0) && (MainGUI.this.ColorGrid.enable)) {
		           JPanel clickedBox = (JPanel)me.getSource();
				   clickedBox.setBackground(MainGUI.this.CurrColor);
				   String[] iAndJ = clickedBox.getName().split("\\s+");
				   MainGUI.EnvironmentGrid[Integer.parseInt(iAndJ[0])][Integer.parseInt(iAndJ[1])] = MainGUI.this.CurrentIDXAdd;
                   }
                 }
               }
               




               public static void main(String[] arg)
               {
            	   new MainGUI();
               }
             }