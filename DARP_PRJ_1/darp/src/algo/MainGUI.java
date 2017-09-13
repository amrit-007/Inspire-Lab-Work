package algo;


	 import java.awt.*;
     import java.awt.event.*;
     import java.util.*;
     import javax.swing.*;
     import javax.swing.event.ChangeEvent;
     import javax.swing.event.ChangeListener;
 
			public class MainGUI
			{
        	 private int c1=0;
        	 private ArrayList<Color> colorArr;
			 public Thread t1;	//Thread for all the robots to start moving
			 public static int rangeView=2; //Range of robot sensing		
			 public JSlider RangeSlider; //SpeedSlider for range selection
			 public static int robos=0; //Total number of robots in the field
			 public static int[][] visitedGrid;//Grid to represent discovered terrain
			 public static ArrayList<Robot> Robots; //List for Robots
			 public ArrayList<JPanel> selectedPanel; //Temporary assignment of goals to each robot
			 /*
			  * Image Icons for start run and stop buttons
			  * 
			  * */
			 private final ImageIcon run_Hover = new ImageIcon("F:\\DARP_PRJ_1\\darp\\src\\resources\\run_Hover.png");
			 private final ImageIcon run_Default = new ImageIcon("F:\\DARP_PRJ_1\\darp\\src\\resources\\run_Default.png");
			 private final ImageIcon run_Pressed = new ImageIcon("F:\\DARP_PRJ_1\\darp\\src\\resources\\run_Pressed.png");
			 private final ImageIcon abort_Hover = new ImageIcon("F:\\DARP_PRJ_1\\darp\\src\\resources\\abort_Hover.jpg");
			 private final ImageIcon abort_Default = new ImageIcon("F:\\DARP_PRJ_1\\darp\\src\\resources\\abort_Default.png");
			 private final ImageIcon abort_Pressed = new ImageIcon("F:\\DARP_PRJ_1\\darp\\src\\resources\\abort_Pressed.png");
			 private final ImageIcon start_Default = new ImageIcon("F:\\DARP_PRJ_1\\darp\\src\\resources\\start3.png");
			 private final ImageIcon start_Pressed = new ImageIcon("F:\\DARP_PRJ_1\\darp\\src\\resources\\start2.png");

			 private JFrame mainFrame; // The Frame of panels
			 private JLabel RangeLabel;
			 private JLabel SpeedLabel;
			 private JPanel UserInputPanel; 
			 private JPanel ConsolePanel;
			 private JPanel RightPanel;
			 private JComboBox<Integer> textboxRows;// Selection of Rows in Grid
			 private JComboBox<Integer> textboxCols;// Selection of Cols in Grid
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
               
			 public JSlider SpeedSlider; // To Regulate the speed of the robot
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

               MainGUI() // Constructor
               {
            	   colorArr=new ArrayList<>();
            	   Robots=new ArrayList<>();
            	   selectedPanel=new ArrayList<>();
			   this.mainFrame = new JFrame("Multi-Robot Path Planning for unknown terrain");
			   this.UserInputPanel = new JPanel();
               this.UserInputPanel.setLayout(new java.awt.FlowLayout(0));
			   this.SpeedSlider=new JSlider(JSlider.HORIZONTAL, 10, 100, 10);
			   this.RangeSlider=new JSlider(JSlider.HORIZONTAL,2,10,2);
			   this.RangeSlider.addChangeListener(new RangeSliderListener());
			   this.SpeedSlider.addChangeListener(new SpeedSliderListener());
		       this.ObstaclesButton = new JRadioButton("Obstacle");
		       this.ObstaclesButton.setBackground(Color.white);
		       this.EmptyButton = new JRadioButton("Unoccupied Cell");
		       this.EmptyButton.setBackground(Color.white);
		       this.RobotButton = new JRadioButton("Robot");
		       this.RobotButton.setBackground(Color.white);
			   this.RobotGoalButton = new JRadioButton("Robot Goal");
		       this.RobotGoalButton.setBackground(Color.white);
		       this.RangeLabel=new JLabel("Range");
		       this.SpeedLabel=new JLabel("Speed");
		       this.CurrentIDXAdd = -1;
		       this.axisScale = 400;
		       this.ColorGrid = null;
				Integer[] choices=new Integer[105];
				for(int i=1;i<=100;i++)choices[i-1]=(i);
		       this.textboxRows = new JComboBox<>(choices);
		       this.textboxRows.setSelectedIndex(9);
		       this.textboxCols = new JComboBox<>(choices);
		       this.textboxCols.setSelectedIndex(9);
		       DefineRightPanel();
                 
		       this.mainFrame.getContentPane().add("East", this.RightPanel);
		       this.mainFrame.setDefaultCloseOperation(3);
		       this.mainFrame.pack();
		       this.mainFrame.setLocationByPlatform(true);
		       this.mainFrame.setSize(1250, 840);
                 
		       this.mainFrame.setVisible(true);
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
               private void DefineRobotsObstacles()
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
		       this.Items.add(this.ObstaclesButton);
		       this.Items.add(this.EmptyButton);
		       this.Items.add(this.RobotButton);
			   this.Items.add(this.RobotGoalButton);
				
                 
             
		       this.ObstaclesButton.setSelected(true);
		       this.CurrentIDXAdd = 1;
		       this.CurrColor = Color.BLACK;
		       appendToPane("Click to add an obstacle\n\n", Color.WHITE);
                 
             
		       RadioAreaButtons.setLayout(new javax.swing.BoxLayout(RadioAreaButtons, 1));
		       RadioAreaButtons.add(this.ObstaclesButton, "West");
		       RadioAreaButtons.add(this.EmptyButton, "West");
		       RadioAreaButtons.add(this.RobotButton, "West");
		       RadioAreaButtons.add(this.RobotGoalButton, "West");
				
		       JPanel SliderPanel = new JPanel();
		       SliderPanel.setBackground(Color.WHITE);
		       SliderPanel.add(this.SpeedLabel);
			   SliderPanel.add(this.SpeedSlider,"WEST");
			   JPanel SliderPanel2 = new JPanel();
			   SliderPanel2.add(this.RangeLabel);
		       SliderPanel2.setBackground(Color.WHITE);
			   SliderPanel2.add(this.RangeSlider,"WEST");
                 
                 
		       this.SuperRadio = new JPanel();
		       this.SuperRadio.setPreferredSize(new Dimension(265, 400));
		       this.SuperRadio.setBackground(Color.WHITE);
		       this.SuperRadio.add(RadioAreaButtons);
				this.SuperRadio.add(SliderPanel);
				this.SuperRadio.add(SliderPanel2);
		       this.SuperRadio.setBorder(BorderFactory.createTitledBorder("Elements"));
                 
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
               private class RangeSliderListener implements ChangeListener{
            	   private RangeSliderListener(){}

				@Override
				public void stateChanged(ChangeEvent arg0) {
					// TODO Auto-generated method stub
					   MainGUI.this.appendToPane(RangeSlider.getValue()+"\n", Color.cyan);
					   rangeView=RangeSlider.getValue();
				}
            	   
               }
               //Change Listener for SpeedSlider
               private class SpeedSliderListener implements ChangeListener{
            	   private SpeedSliderListener(){}

				@Override
				public void stateChanged(ChangeEvent arg0) {
					   MainGUI.this.appendToPane(SpeedSlider.getValue()+"\n", Color.white);
					   timer=10000/SpeedSlider.getValue();
					   }
               }
               
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
										Thread.sleep(timer);
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
					MainGUI.rows=textboxRows.getSelectedIndex()+1;
					MainGUI.cols=textboxCols.getSelectedIndex()+1;
		             MainGUI.this.appendToPane("The grid [" + MainGUI.rows + "," + MainGUI.cols + "] has been created\n\n", Color.WHITE);
		             MainGUI.this.appendToPane("Define the Robots initial positions along with the fixed obstacles\n\n", Color.WHITE);
		             MainGUI.this.DefineRobotsObstacles();
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
                 GridPane()
                 {
		         setLayout(new java.awt.GridLayout(MainGUI.rows + 1, MainGUI.cols + 1));
		         setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
                   
		         for (int i = 0; i < MainGUI.rows + 1; i++) {
		           for (int j = -1; j < MainGUI.cols; j++) {
		             JPanel pan = new JPanel();
                       
		             pan.setEnabled(true);
		             pan.setBackground(Color.WHITE);
                       
		             pan.setPreferredSize(new Dimension(getWidth(), getHeight()));
		             if ((i < MainGUI.rows) && (j >= 0)) {
//		               pan.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
//		               pan.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
		         repaint();

	}
                 GridPane(int image[][],ArrayList<Robot> r){
	
    
    setLayout(new java.awt.GridLayout(MainGUI.rows + 1, MainGUI.cols + 1));
    setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
      
    for (int i = 0; i < MainGUI.rows + 1; i++) {
      for (int j = -1; j < MainGUI.cols; j++) {
        JPanel pan = new JPanel();
          
        pan.setEnabled(true);
        pan.setBackground(Color.GRAY);
        pan.setPreferredSize(new Dimension(getWidth(), getHeight()));	
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
//            pan.setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
        }
          

        add(pan);
        }
      }
    repaint();
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