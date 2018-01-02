import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Dimension;
import java.util.Random;


public class Minesweeper extends JFrame implements ActionListener {

    /* variables to set up game */
    private int rows = 10; //Can be changed by user
    private int columns = 10; //Can be changed by user
    private int mines = 10; //Can be changed by user

    Timer timer;

    /*time and mine label*/
    private JLabel txtTime; //time output
    private JLabel txtMinesLeft; //mines output

    /*Menu options*/
    private JMenuItem NewGame; //Menu option to start a new game
    private JMenuItem GridSize; //Menu option to set grid size
    private JMenuItem Quit; //Menu option to quit the application
    private JMenuItem Difficulty; //Menu option to change difficulty
    private JMenuItem Instruction; //Menu option to show instructions
    private JMenuItem About;


    private int ExplodeMines = mines; //Variable to count the mines that exploded
    private int time = 0; //initialize time
    private Board [][]buttons = new Board[rows][columns]; //2d button array for grid
    private JPanel GameBoard; //Main board
    private int ButtonsLeft = rows * columns - ExplodeMines;
    boolean gameStarted = false;
    boolean gameFinished = false;


    public Minesweeper(){

        //start application in center of screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

        //set layout of the Pane
        Container contentPane = getContentPane();
        getContentPane().setLayout( new BorderLayout() );

        //Set title for application
        this.setTitle( "Minesweeper" );

        //create a panel for new game button and game timer
        JPanel GamePanel = new JPanel( new GridLayout( 1, 3, 5, 3) );

        //Button to start a new game, align in center
        JButton btnStart = new JButton(new ImageIcon ("Images/smiley.png"));
        btnStart.setHorizontalAlignment( JLabel.CENTER );
        btnStart.setPreferredSize( new Dimension( 25, 25 ) );
        btnStart.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                timer.stop();
               resetGame();

               Board(rows, columns);



                if (ButtonsLeft == 0){
                    btnStart.setIcon (new ImageIcon("Images/cool2.jpg"));

                }
                return;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                btnStart.setIcon(new ImageIcon("Images/oface.jpg"));

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                btnStart.setIcon(new ImageIcon("Images/smiley.png"));

            }



        });

        // JLabel to show mines
        JLabel lblMinesLeft = new JLabel("Mines:");
        lblMinesLeft.setHorizontalAlignment( JLabel.CENTER );
        GamePanel.add( lblMinesLeft );

        // JLabel that shows number of mines left on a field
        txtMinesLeft = new JLabel( "" + ExplodeMines );
        txtMinesLeft.setHorizontalAlignment( JLabel.LEFT );
        txtMinesLeft.setForeground( Color.red );

        //add the mine label to the panel
        GamePanel.add( txtMinesLeft );

        //add the start button to the panel
        GamePanel.add(btnStart);

        //Label called "Time", aligned to the right
        JLabel lblTime = new JLabel( "Time:" );
        lblTime.setHorizontalAlignment( JLabel.RIGHT );

        //add the time label to the panel
        GamePanel.add( lblTime);

        // JLabel that shows elapsed time
        txtTime = new JLabel( "0" );
        txtTime.setForeground( Color.blue );
        txtTime.setFont( new Font( "DialogInput", Font.BOLD, 18 ) );

        // add the elapsed time label to the panel
        GamePanel.add( txtTime );

        //add the panel to the frame
        contentPane.add(GamePanel, BorderLayout.NORTH );

        //create timer object to measure time
        timer = new javax.swing.Timer(1000, this);

        //Create the game board
        Board(rows, columns);

    }

    private JMenuBar Menu()
    {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu( "Game" );
        NewGame = new JMenuItem( "New Game" );
        NewGame.addActionListener( this );
        GridSize = new JMenuItem( "Grid Size" );
        GridSize.addActionListener( this );
        Difficulty= new JMenuItem( "Difficulty" );
        Difficulty.addActionListener(this);
        Quit = new JMenuItem( "Quit" );
        Quit.addActionListener( this );

        menuGame.add( NewGame);
        menuGame.add( GridSize );
        menuGame.add(Difficulty);
        menuGame.add( Quit);

        JMenu menuHelp = new JMenu( "Help" );
        Instruction = new JMenuItem( "Instructions" );
        Instruction.addActionListener( this );
        About = new JMenuItem( "About" );
        About.addActionListener( this );
        menuHelp.add( Instruction );
        menuHelp.add( About );

        menuBar.add( menuGame );
        menuBar.add( menuHelp );

        return menuBar;
    }

    //Create the board filled with buttons of type Board
    private void Board(int rows, int columns){

        setSize( (int)(21 * columns) - 11 , (int)(21 * rows) + 83 );


        GameBoard = new JPanel( new GridLayout( rows, columns, 1, 1 ) );

        int i, j;

       // from left-to-right across the top first, then drop down to the next row.
        for( i = 0 ; i < rows ; i++ )
            for( j = 0 ; j < columns ; j++)
            {
                buttons[i][j] = new Board(i, j);
                GameBoard.add(buttons[i][j]);
                buttons[i][j].addMouseListener(new MouseAdapter() {
                                                   @Override
                                                   public void mouseClicked(MouseEvent e) {
                                                       super.mouseClicked(e);
                                                       Board bg = (Board) e.getSource();

                                                       int mouseClick = e.getButton();
                                                       //if clicked field is mine and is not flagged -- end the game
                                                       // if clicked not clicked or flagged -- if zero neighbors
                                                       // if left mouse button was clicked
                                                       if (mouseClick == 1 && !gameFinished) {
                                                           // if in the clicked field is mine and it is not flagged end game
                                                           if (bg.isMine() && bg.ReturnFlag() != 1) {
                                                               bg.setSelected(false);
                                                               endGame( true );
                                                               return;
                                                           }

                                                           // if we click the first square after starting the game
                                                           if (!gameStarted) {
                                                               randomMines();
                                                               markNeighbors();
                                                               gameStarted = true;
                                                               timer.start();
                                                           }

                                                           // not clicked yet and not flagged square is clicked
                                                           if (!bg.IsPressed() && bg.ReturnFlag() != 1) {
                                                               if (bg.getNeighors() == 0)
                                                                   showZeros(bg);
                                                               else
                                                                   ButtonsLeft--;
                                                           }
                                                           // notify a square that it was clicked
                                                           bg.leftClick();
                                                       }

                                                       // if right mouse button was clicked
                                                       else if (mouseClick == 3) {
                                                           ExplodeMines += bg.Boardrightclick();
                                                       }
                                                       txtMinesLeft.setText( "" + ExplodeMines );

                                                   }
                                                   });


            }


        getContentPane().add(GameBoard, BorderLayout.CENTER );

    }

    private void endGame(boolean end){

        for( int i = 0 ; i < rows ; i++ )
            for( int j = 0 ; j < columns ; j++ ){
                if (buttons[i][j].isMine()){
                    buttons[i][j].setIcon(new ImageIcon("Images/bomb.jpg"));
                    buttons[i][i].setDisabledIcon(new ImageIcon("Images/bomb.jpg"));
                    timer.stop();
                }
                else
                    buttons[i][j].setEnabled( false );


            }

    }

    private void showZeros(Board theBoard )
    {
        int posX = theBoard.getXpos();
        int posY = theBoard.getYpos();

        checkForZeros( posX, posY );
        return;
    }

    private void checkForZeros( int x, int y )
    {
        if( x < rows && y < columns && x >= 0 && y >= 0 && !buttons[x][y].IsPressed() )
        {
            buttons[x][y].leftClick();
            ButtonsLeft--;

            if( buttons[x][y].getNeighors() == 0 )
            {
                checkForZeros( x - 1, y - 1 );
                checkForZeros( x	, y - 1 );
                checkForZeros( x + 1, y - 1 );
                checkForZeros( x - 1, y		);
                checkForZeros( x + 1, y		);
                checkForZeros( x - 1, y + 1 );
                checkForZeros( x	, y + 1 );
                checkForZeros( x + 1, y + 1 );
            }
        }
    }

    private void randomMines(){


        Random rInt = new Random();
        int rCol, rRow;
        int i, j;

        // place mines in random order
        for( i = 0 ; i < mines ; i++ )
        {
            rCol = rInt.nextInt( rows );
            rRow = rInt.nextInt( columns );
            // don't put more than one mine on one square
            if( !buttons[ rCol ][ rRow ].isMine() && !buttons[ rCol ][ rRow ].isSelected() )
                buttons[ rCol ][ rRow ].setMine();
            else
                i--;
        }

    }

    private void markNeighbors(){
        // mark number of neighboring mines on each square
        int i, j;
        for( i = 0 ; i < rows ; i++ )
            for( j = 0 ; j < columns ; j++)
            {
                if( buttons[i][j].isMine() )
                {
                    if( i - 1 >= 0 && j - 1 >= 0 )	// upper left square
                        buttons[i - 1][j - 1].addNeighbors( 1 );
                    if( i - 1 >= 0 && j >= 0 )	// upper middle square
                        buttons[i - 1][j].addNeighbors( 1 );
                    if( i - 1 >= 0 && j + 1 < columns )	// upper right square
                        buttons[i - 1][j + 1].addNeighbors( 1 );
                    if( i >= 0 && j - 1 >= 0 )	// middle left square
                        buttons[i][j - 1].addNeighbors( 1 );
                    if( i >= 0 && j + 1 < columns )	// middle right square
                        buttons[i][j + 1].addNeighbors( 1 );
                    if( i + 1 < rows && j - 1 >= 0 )	// lower left square
                        buttons[i + 1][j - 1].addNeighbors( 1 );
                    if( i + 1 < rows && j >= 0 )	// lower middle square
                        buttons[i + 1][j].addNeighbors( 1 );
                    if( i + 1 < rows && j + 1 < columns )	// lower left square
                        buttons[i + 1][j + 1].addNeighbors( 1 );
                }
            }
    }


    private void resetGame()

    {

        getContentPane().remove(GameBoard);

        for( int i = 0 ; i < rows ; i++ )
            for( int j = 0 ; j < columns ; j++)
            {

                buttons[i][j].reset();
            }

        // reset all the flags and the timer
        time = 0;
        txtTime.setText( "" + time );
        ExplodeMines = mines;
        ButtonsLeft = rows * columns - ExplodeMines;



    }

    public void actionPerformed( ActionEvent e ){

        Object UsrSource = e.getSource();
        String[] options = {"Beginner", "Intermediate", "Expert"};
        String Instructions = "You are presented with a board of squares. Some squares contain mines (bombs), others don't. If you click on a square containing a bomb, you lose. \n If you manage to click all the squares (without clicking on any bombs) you win.\n" +
                "Clicking a square which doesn't have a bomb reveals the number of neighbouring squares containing bombs.\n Use this information plus some guess work to avoid the bombs.\n" +
                "To open a square, point at the square and click on it. To mark a square you think is a bomb, point and right-click.";
        String AboutMe = " Created by Jake Davis \n Date 10/11/2017";
        String[] Difficulty2 = {"Game difficulty has been set to Beginner.\nGrid Size is 5x5 with 5 bombs", "Game difficulty has been set to Intermediate.\nGrid Size is 8x8 with 15 bombs", "Game difficulty has been set to Expert.\nGrid Size is 10x10 with 30 bombs" };


        if(UsrSource == About){
            JOptionPane.showMessageDialog(About, AboutMe, "About", JOptionPane.INFORMATION_MESSAGE);
        }

        else if (UsrSource == Instruction){
            JOptionPane.showMessageDialog(Instruction, Instructions, "Instructions", JOptionPane.INFORMATION_MESSAGE );
        }

        else if( UsrSource == Quit ) {
            System.exit(0);
        }


        //change the rows/columns depending on the input
        else if(UsrSource == Difficulty ){

           int x = JOptionPane.showOptionDialog(Difficulty, "Please Select a Difficulty", "Difficulty", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

           if (x == 0){
               JOptionPane.showMessageDialog(Difficulty, Difficulty2[0], "Beginner", JOptionPane.INFORMATION_MESSAGE );

                resetGame();
               rows = 5;
               columns = 5;
               mines = 5;
               txtMinesLeft.setText(""+ mines);
               ExplodeMines = mines;
               Board(rows, columns);

           }
           else if (x == 1){
               JOptionPane.showMessageDialog(Difficulty, Difficulty2[1], "Intermediate", JOptionPane.INFORMATION_MESSAGE );
               resetGame();
               rows = 8;
               columns = 8;
               mines = 15;
               txtMinesLeft.setText(""+ mines);
               ExplodeMines = mines;
               Board(rows, columns);

           }
           else if (x == 2){
               JOptionPane.showMessageDialog(Difficulty, Difficulty2[2], "Expert", JOptionPane.INFORMATION_MESSAGE );

                resetGame();
               rows = 10;
               columns = 10;
               mines = 30;
               txtMinesLeft.setText(""+ mines);
               ExplodeMines = mines;
               Board(rows, columns);

           }
        }

        else if(UsrSource == NewGame){

            resetGame();
            timer.stop();
            Board(rows, columns);
            return;

        }


        //Change the rows/columns specified by user
        else if(UsrSource == GridSize){

          // resetGame();

          String inputrows =  JOptionPane.showInputDialog(GridSize, "Enter the number of rows:");
          rows  = Integer.valueOf(inputrows);

          String inputcolumns = JOptionPane.showInputDialog(GridSize, "Enter the number of columns:" );
          columns = Integer.valueOf(inputcolumns);

          String inputmines = JOptionPane.showInputDialog(GridSize, "Enter the number of mines:" );
          mines = Integer.valueOf(inputcolumns);

          Board(rows, columns);

        }

        //increases clock by one
        else if( UsrSource == timer )
        {
            time++;
            txtTime.setText( "" + time );

        }
    }


    public static void main(String args[]) {
        Minesweeper mine = new Minesweeper();
        mine.setSize(300, 300);
        mine.setJMenuBar(mine.Menu());
        mine.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mine.setVisible(true);
        mine.setResizable(true);
    }
}
