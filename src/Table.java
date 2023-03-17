public class Table {

    //for rows and columns
    private int x, y;

    //value of square on table
    private double prob;

    //for table limit
    private boolean limit;

    //gets square value in the table
    public double getProb() {
        return prob;
    }

    //sets probability
    public void setProb(double value) {
        this.prob = value;
    }

    //gets the columns
    public int gety() {
        return y;
    }

    //sets the column
    public void sety(int y) {
        this.y = y;
    }

    //gets the row
    public int getx() {
        return x;
    }

    //sets the row
    public void setx(int x) {
        this.x = x;
    }

    //gets the limit
    public boolean getlimit() {
        return limit;
    }
    //sets the limit
    public void setlimit(boolean x) {
        this.limit = x;
    }

    //constructor
    Table(){

        //sets prob to 0
        setProb(0);
        //table limit is initially false
        setlimit(false);
    }
}
