import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

class UserDashBoard {
    /**
     * score !It will store current Score of the user  at each Iteration.
     **/
    private int score;
    /**
     * prev_value !It will store prev_value  of dice  at each Iteration.
     **/
    private int prev_value;
    /**
     * rank !It will store rank  of user   at each Iteration.
     **/
    private int rank;
    /**
     * skip !This flag will be used to check whether user needs to be  skipped or not!
     **/
    private boolean skip;
    /**
     * done This flag will be used to check whether user is reached target or not!
     **/
    private boolean done;

    /**
     * initialising default values
     */
    UserDashBoard() {
        this.score = 0;
        this.prev_value = 0;
        this.rank = 0;
        this.skip = false;
        this.done = false;
    }

    int getScore() {
        return score;
    }

    int getPrev_value() {
        return prev_value;
    }

    int getRank() {
        return rank;
    }

    boolean getSkip() {
        return skip;
    }

    boolean getDone() {
        return done;
    }

    void setScore(int score) {
        this.score = score;
    }

    void setDone(boolean done) {
        this.done = done;
    }

    void setPrev_value(int prev_value) {
        this.prev_value = prev_value;
    }

    void setRank(int rank) {
        this.rank = rank;
    }

    void setSkip(boolean skip) {
        this.skip = skip;

    }

}

public class GameOfDice {
    /**
     * @param playerDashBoard
     * @return sorting  HashMap based on score
     */
    public static Map<String, UserDashBoard> sortByScore(Map<String, UserDashBoard> playerDashBoard) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, UserDashBoard>> list = new LinkedList<>(playerDashBoard.entrySet());

        // Sort the list based on score
        Collections.sort(list, new Comparator<Map.Entry<String, UserDashBoard>>() {
            public int compare(Map.Entry<String, UserDashBoard> o1, Map.Entry<String, UserDashBoard> o2) {
                int intObj1 = (int) o1.getValue().getScore();
                int intObj2 = (int) o2.getValue().getScore();
                // Get the difference
                int difference = intObj1 - intObj2;
                if (difference == 0) {
                    // Both are equal
                    return 0;
                } else if (difference < 0) {
                    // obj1 < obj2
                    return 1;
                } else {
                    // obj1 > obj2
                    return -1;
                }
            }
        });

        // put data from sorted list to hashmap
        Map<String, UserDashBoard> temp = new LinkedHashMap<String, UserDashBoard>();
        for (Map.Entry<String, UserDashBoard> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static void main(String args[]) {

        /**Number of Players N
         Target is M
         Random arrangement of players N
         dice 1 to 6
         if one get 6 he will get another chance
         if one get 2 1's in line then he will be skipped for nextChance
         Name of the players should be player1 to playerN
         Player-3 its your turn ...press r to take the action / roll the dice
         maintain score board
         print score board with ranks after each roll
         print message with appropriate
         if user completes the game then print message with rank
         **/
        Scanner s = new Scanner(System.in);
        /**
         * make sure you passed  two string values in  CL arguments other wise you will get errors
         * first one should be number of players :n
         * second one should be target :m
         **/
        final int n = Integer.parseInt(args[0]);
        final int m = Integer.parseInt(args[1]);
        Random rand = new Random();
        final List<String> listOfPlayers = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            final String str = "player-" + "" + i;
            listOfPlayers.add(str);
        }
        //This would shuffle the list i.e we will get  random for every iteration
        Collections.shuffle(listOfPlayers);
        //we are using LinkedHashMap ! so it will maintain the inserted Order
        Map<String, UserDashBoard> playerDashBoard = new LinkedHashMap<>();
        for (String str : listOfPlayers) {
            playerDashBoard.put(str, new UserDashBoard());
        }
        int rank = 1;
        //to  count number of 6's
        int count = 0;
        //This flag is used to check whether all players are reached target or not
        boolean allDone = false;
        while (!allDone) {
            Set<Map.Entry<String, UserDashBoard>> set = playerDashBoard.entrySet();
            for (Map.Entry<String, UserDashBoard> stringUserDashBoardEntry : set) {
                //Start of every iteration make number of 6's as zero
                count = 0;
                //if the player is already won then player will be skipped !
                if (playerDashBoard.get(stringUserDashBoardEntry.getKey()).getDone()) {
                    continue;
                }
                do {
                    if (!playerDashBoard.get(stringUserDashBoardEntry.getKey()).getSkip() || rank == n) {

                        if (count > 0) {
                            System.out.println(stringUserDashBoardEntry.getKey() + " you get one more chance as you "
                                    + "rolled 6 earlier");
                        } else {
                            System.out.println(stringUserDashBoardEntry.getKey() + " its your turn.Roll the dice");
                        }
                        //Here it will wait until player roll the dice.press any button to roll the dice ! instance"r"
                        final String str = s.next();
                        UserDashBoard userDashBoard = playerDashBoard.get(stringUserDashBoardEntry.getKey());
                        //rand.nextInt(6) produces random values 0 to 5 .
                        int diceValue = 1 + rand.nextInt(6);
                        int previous_diceValue = userDashBoard.getPrev_value();
                        //System.out.println("current value is :" + diceValue);
                        //if current value and previous  value is equal to one then it will skip the next iteration
                        if (diceValue == 1 && previous_diceValue == 1) {
                            userDashBoard.setSkip(true);
                        }
                        final int score = diceValue + userDashBoard.getScore();
                        //if he/she reach target then it will print his/her rank  and exit  the loop
                        if (score >= m) {
                            System.out.println(stringUserDashBoardEntry.getKey() + " your rank is :" + rank);
                            userDashBoard.setRank(rank);
                            userDashBoard.setDone(true);
                            userDashBoard.setScore(score);
                            ++rank;
                            playerDashBoard.put(stringUserDashBoardEntry.getKey(), userDashBoard);
                            break;
                        }
                        userDashBoard.setScore(score);
                        userDashBoard.setPrev_value(diceValue);
                        playerDashBoard.put(stringUserDashBoardEntry.getKey(), userDashBoard);
                        count++;
                    } else {
                        System.out.println(stringUserDashBoardEntry.getKey() + " you are not eligible to roll this time"
                                + " as you rolled 1 twice consecutively");
                        UserDashBoard userDashBoard = playerDashBoard.get(stringUserDashBoardEntry.getKey());
                        //Making prev_value as zero and skip value as false as they are eligible for next iteration
                        userDashBoard.setPrev_value(0);
                        userDashBoard.setSkip(false);
                        playerDashBoard.put(stringUserDashBoardEntry.getKey(), userDashBoard);
                    }
                } while (playerDashBoard.get(stringUserDashBoardEntry.getKey()).getPrev_value() == 6);
            }

            Map<String, UserDashBoard> sortedMap = sortByScore(playerDashBoard);
            int rankForIteration = rank;
            String previous_key = "";
            Set<Map.Entry<String, UserDashBoard>> set1 = sortedMap.entrySet();
            for (Map.Entry<String, UserDashBoard> stringUserDashBoardEntry : set1) {
                //System.out.println("score of a player is :" + stringUserDashBoardEntry.getKey());
                if (!playerDashBoard.get(stringUserDashBoardEntry.getKey()).getDone()) {
                    UserDashBoard userDashBoard = playerDashBoard.get(stringUserDashBoardEntry.getKey());
                    if (previous_key.isEmpty()) {
                        userDashBoard.setRank(rankForIteration);
                    } else if (playerDashBoard.get(previous_key).getScore() == userDashBoard.getScore()) {
                        userDashBoard.setRank(playerDashBoard.get(previous_key).getRank());
                    } else {
                        userDashBoard.setRank(playerDashBoard.get(previous_key).getRank() + 1);
                    }
                    previous_key = stringUserDashBoardEntry.getKey();
                }

            }

            Set<Map.Entry<String, UserDashBoard>> set3 = playerDashBoard.entrySet();
            allDone = true;
            for (Map.Entry<String, UserDashBoard> stringUserDashBoardEntry : set3) {
                System.out.print(stringUserDashBoardEntry.getKey() + "  ");
                System.out.print("Score :" + stringUserDashBoardEntry.getValue().getScore() + "  ");
                System.out.println("Rank :" + stringUserDashBoardEntry.getValue().getRank());
                if (stringUserDashBoardEntry.getValue().getDone() == false) {
                    allDone = false;
                }
            }

        }

    }
}
