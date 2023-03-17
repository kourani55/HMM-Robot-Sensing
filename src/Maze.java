import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Maze {

    //function to perform filtering after evidence is observed
    public static double filteringAfterEvidence(Table[][] table, char W, char N, char E, char S, int x, int y) {

        double total = 1.0;

        if (!table[x][y].getlimit()) {
            /*
            if west equal to zero, is an obstacle
            if not equal to zero, is an obstacle
             */
            total *= (y - 1 >= 0) && !table[x][y - 1].getlimit() ? W == '0' ? 0.95 : 0.05 : W != '0' ? 0.85 : 0.15;
        }
        else {
            return 0.0;
        }

       /*
       if north is equal to zero, is an obstacle
       if not equal to zero, is an obstacle
       */
        total *= (x - 1 >= 0) && !table[x - 1][y].getlimit() ? N == '0' ? 0.95 : 0.05 : N == '0' ? 0.15 : 0.85;


        /*
       if east is equal to zero, is an obstacle
       if not equal to zero, is an obstacle
       */
        total *= (y + 1 > 6) || table[x][y + 1].getlimit() ? E == '0' ? 0.15 : 0.85 : E == '0' ? 0.95 : 0.05;

          /*
       if south is equal to zero, is an obstacle
       if not equal to zero, is an obstacle
       */
        total *= (x + 1 > 5) || table[x + 1][y].getlimit() ? S == '0' ? 0.15 : 0.85 : S == '0' ? 0.95 : 0.05;

        return total;
    }

    //function for moving with implemented direction
    public static void movingAction(Table[][] table, char dir) {

        //if North or West, predicts the probability
        switch (dir) {
            case 'N' -> {

                double[][] tempTable;
                tempTable = new double[6][7];

                //for loop to traverse table
                for (int x = 0; x < table.length; x++) {
                    for (int y = 0; y < table[x].length; y++) {

                        double prob = 0.0;

                        //probability of an obstacle being on the left side of the robot
                        prob += (y - 1 >= 0) && !table[x][y - 1].getlimit() ? (table[x][y - 1].getProb() * 0.1) : (table[x][y].getProb() * 0.1);

                        //probability of an obstacle being on the right side of the robot
                        prob += (y + 1 <= 6) && !table[x][y + 1].getlimit() ? (table[x][y + 1].getProb() * 0.1) : (table[x][y].getProb() * 0.1);

                        //probability of an obstacle being underneath the robot
                        if ((x + 1 > 5) || table[x + 1][y].getlimit()) {
                        } else {
                            prob += (table[x + 1][y].getProb() * 0.8);
                        }
                        //probability of an obstacle being on top of the robot
                        if ((x - 1 >= 0) && !table[x - 1][y].getlimit()) {
                        } else {
                            prob += (table[x][y].getProb() * 0.8);
                        }

                        //new probability in table
                        tempTable[x][y] = prob;

                        //sets prob to 0 to restart loop
                        prob = 0.0;
                    }
                }
                //updates data to table
                updateTable(tempTable, table);
            }
            case 'W' -> {

                double[][] tempTable;
                tempTable = new double[6][7];

                //for loop to traverse table
                for (int x = 0; x < table.length; x++) {
                    for (int y = 0; y < table[x].length; y++) {

                        double prob = 0.0;

                        //probability of an obstacle being on the top of the robot
                        prob += (x - 1 >= 0) && !table[x - 1][y].getlimit() ? (table[x - 1][y].getProb() * 0.1) : (table[x][y].getProb() * 0.1);

                        //probability of an obstacle being on the bottom of the robot
                        prob += (x + 1 <= 5) && !table[x + 1][y].getlimit() ? (table[x + 1][y].getProb() * 0.1) : (table[x][y].getProb() * 0.1);

                        //probability of an obstacle being on the right of the robot
                        if ((y + 1 > 6) || table[x][y + 1].getlimit()) {
                        } else {
                            prob += (table[x][y + 1].getProb() * 0.8);
                        }

                        //probability of an obstacle being on the left of the robot
                        if ((y - 1 >= 0) && !table[x][y - 1].getlimit()) {
                        } else {
                            prob += (table[x][y].getProb() * 0.8);
                        }

                        //new probability in table
                        tempTable[x][y] = prob;

                        //sets prob to 0 to restart loop
                        prob = 0;
                    }
                }

                //updates values in table
                updateTable(tempTable, table);
            }
        }

        //prints action table
        System.out.println("Prediction after Action " + dir);
        displayTable(table);
    }

    //function to sense using evidence
    public static void sensingAction(Table[][] table, char W, char N, char E, char S) {

        int x = 0;
        double sum = 0.0;

        //while loop that changes probability to decimal and then applies to index if true
        while (true) {
            if (x >= table.length) break;
            int y = 0;

            while (true) {
                if (y >= table[x].length) break;
                double prob = ((table[x][y].getProb() / 100) * filteringAfterEvidence(table, W, N, E, S, x, y));
                table[x][y].setProb(prob);
                y++;
            }
            x++;
        }

        //normalization to add updated values for the probabilities
        for (Table[] tables : table) {
            for (Table prob : tables) {
                sum += prob.getProb();
            }
        }
        //switches decimal to percent
        for (Table[] tables : table) {
            for (Table prob : tables) {
                prob.setProb((prob.getProb() / sum) * 100);
            }
        }

        //prints the probability table
        System.out.println("Filtering after Evidence [" + W + ", " + N + ", " + E + ", " + S + "]");
        displayTable(table);
    }

    //sets the values in the table
    //initial is set to 2.70
    public static void fillTable(Table[][] table) {

        int x = 0;

        //while loop to initialize table
        while (x < table.length) {

            int y = 0;
            //sets initial value to 2.70
            while (y < table[x].length) {
                table[x][y] = new Table();
                table[x][y].setx(x);
                table[x][y].sety(y);
                table[x][y].setProb(2.70);
                y++;
            }
            x++;
        }

        //fills the tables
        for (Table table1 : Arrays.asList(table[1][5], table[2][1], table[2][4], table[3][1], table[3][4])) {
            table1.setlimit(true);
        }
        //sets the values of the table to be 0
        for (Table table1 : Arrays.asList(table[1][5], table[2][1], table[2][4], table[3][1], table[3][4])) {
            table1.setProb(0.0);
        }
        //prints the initial table
        System.out.println("Initial Location Probabilities");
        displayTable(table);

    }

    //function to print table with formatted time, name and with set obstacles
    public static void displayTable(Table[][] table) {

        //data and time formatter
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        //prints table
        for (Table[] tables : table) {
            for (Table value : tables) {
                //prints obstacles
                if (!value.getlimit()) {
                    System.out.printf(" %5.2f ", value.getProb());
                } else {
                    System.out.printf(" %5s ", "####");
                }
            }
            System.out.println("\n");

        }
        //prints name and time
        System.out.println("Fatima Kourani, Ali Hazime " + dtf.format(now) + "\n");
    }

    //prints the updated values from the temp table to the table
    public static void updateTable(double tempTable[][], Table[][] table) {
        int x = 0;
        while (true) {
            //if true, break
            //if false, increment x
            if (x >= table.length) break;
            int y = 0;
            while (true) {
                if (y >= table[x].length) break;
                table[x][y].setProb(tempTable[x][y]);
                y++;
            }
            x++;
        }
    }

    public static void main(String[] args) {

        //creates new table
        //loads table with values
        Table[][] table;
        table = new Table[6][7];
        fillTable(table);

        //for each loop that senses and moves based on dir North or West
        for (char c : new char[]{'0', '1'}) {
            sensingAction(table, c, '0', '0', '0');
            movingAction(table, 'N');
        }
        for (char c : new char[]{'W', 'N'}) {
            sensingAction(table, '1', '0', '0', '0');
            movingAction(table, c);
        }
        sensingAction(table, '0', '0', '0', '0');
    }
}

