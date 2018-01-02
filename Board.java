import javax.swing.*;
import java.awt.*;


public class Board extends JToggleButton {
    int x;
    int y;
    int flag = 0;
    int neighborMines = 0;
    boolean mine = false;
    boolean Pressed = false;

    public Board(int newX, int newY) {

        this.setPreferredSize(new Dimension(10, 10));
        this.setMargin(new Insets(1, 1, 1, 1));
        this.setFocusPainted(false);
        x = newX;
        y = newY;
    }

    public int Boardrightclick() {

        if (flag == 0) {
            flag = 1;
            setIcon(new ImageIcon("C:\\Users\\Jake\\IdeaProjects\\Minesweep\\src\\Images\\flag.png"));
            setDisabledIcon(new ImageIcon("C:\\Users\\Jake\\IdeaProjects\\Minesweep\\src\\Images\\flag.png"));
            setEnabled(false);
            return -1;
        } else if (flag == 1) {
            setIcon(null);
            flag = 0;
            return 1;
        }
        return 0;

    }

    public int getNeighors() {
        return neighborMines;
    }

    public int getXpos() {
        return x;
    }

    public int getYpos() {
        return y;
    }

    public int ReturnFlag() {
        return flag;
    }

    public void reset() {
        setText("");
        flag = 0;
        setEnabled(true);
        this.setSelected(false);
        setIcon(null);
    }

    public void setMine() {
        mine = true;
    }

    public void addNeighbors(int number) {
        neighborMines += number;
    }

    public boolean isMine() {
        return mine;
    }

    public void setNeighbors(int newNeighbors) {
        neighborMines = newNeighbors;

    }

    public boolean IsPressed() {
        return Pressed;
    }

   public void setPressed() {
        this.setEnabled(false);
    }


    public void leftClick() {
        switch (neighborMines) {
            case 1:
                this.setForeground(Color.BLUE);
                break;

            case 2:
                this.setForeground(Color.GREEN);
                break;

            case 3:
                this.setForeground(Color.RED);
                break;

            case 4:
                this.setForeground(Color.YELLOW);
                break;

            case 5:
                this.setForeground(Color.ORANGE);
                break;


            case 6:
                this.setForeground(Color.CYAN);
                break;

            case 7:
                this.setForeground(Color.PINK);
                break;

            case 8:
                this.setForeground(Color.MAGENTA);
                break;
            default:
                break;
        }


        // if flag is set cannot click that square

        if( Pressed )
        {
            setSelected( true );
            return;
        }

        // if flag is set cannot click that square
        if( flag == 1)
            setSelected( false );
        else
        {
            setSelected( true );
                Pressed = true;

            if( neighborMines > 0 )
                setText( "" + neighborMines );
        }


    }
}



