import java.util.*;

public class Cards {

    public static List<String> gameColors (){
        ArrayList<String> colors = new ArrayList<String>();
        colors.add("blue");
        colors.add("red");
        colors.add("yellow");
        colors.add("green");

        return colors;
    }

    public static List<String> wild (){
        ArrayList<String> wildCards = new ArrayList<String>();
        wildCards.add("wild draw4");
        wildCards.add("wild card");

        return wildCards;
    }

    public static List<String> cardAction(){
        ArrayList<String> actions = new ArrayList<String>();
        actions.add("skip");
        actions.add("draw2");
        actions.add("reverse");

        return actions;
    }

    public  static List <String> BuildCards(){
//        all the game cards
        ArrayList<String> cards = new ArrayList<String>();

        ArrayList<Integer> nums = new ArrayList<Integer>();
//        nums for cards
        for (int i =0; i < 10; i++){
            nums.add(i);
        }
        for (int i =1; i < 10; i++){
            nums.add(i);
        }

//        card colors
        List <String> colors = gameColors();

//        wild cards
        List <String> wildCards = wild();

//        action cards
        List <String> actions = cardAction();

        for (String color : colors){
            for (int num: nums){
                cards.add(color+ " " + num);
            }
            for (int i = 0; i < 2; i++) {
                for (String action : actions) {
                    cards.add(color + " " + action);
                }
            }
        }

        for (int i=0; i< 4; i++){
            for (String wild: wildCards){
                cards.add(wild);
            }
        }
        return cards;
    }

    public  static List <String> shuffledCards(List <String> cards){
          Collections.shuffle(cards);
          return cards;
        }


    public  static String firstCardPutDown(List<String> shuffledCards){

        while (true){
            String card = shuffledCards.get(0);
            if (!card.contains("wild")){
                shuffledCards.remove(0);
                return card;
            }
        }
    }

    public static int addCardToDiscardPile (List <String> discardPile, String card, int turnIdx, LinkedHashMap<String, List<String>> playerDict, List <String> playerNames, List <String> shuffledCards){
        System.out.println(card + " put down.");
        int newTurnIdx = turnIdx;
        discardPile.add(card);
        if (card.contains("wild")){
            String playerName = playerNames.get(turnIdx);
            List<String> playerHand = playerDict.get(playerName);
            String color = newColor(playerName,playerHand);
            discardPile.add(color);
        }
        if (card.contains("draw")|| card.contains("skip") || card.contains("reverse")){
            GamePlay.displayActionCardMessage(card);
            newTurnIdx = GamePlay.doAction(card, turnIdx, playerDict, playerNames, shuffledCards, discardPile);

        }

        return  newTurnIdx;
    }

    public  static void showLastCardPutDown (List <String> discardPile){
        int length = discardPile.size();
        length --;
        String lastCardDiscarded = discardPile.get(length);
        String message = "Last Card Put Down Is: "+ lastCardDiscarded;
        displayMessage("=", message);
    }

    public static boolean isCardPlayable(String card, String lastCardDiscarded){
        String [] lastCardDiscardedArray = lastCardDiscarded.split(" ");

        if (lastCardDiscardedArray.length == 1){
            if (card.contains(lastCardDiscardedArray[0])){
                return true;
            }
        } else if (card.contains("wild")) {
            return true;
        } else if (card.contains(lastCardDiscardedArray[0])) {
            return true;
        } else if (card.contains(lastCardDiscardedArray[1])) {
            return true;
        }
        return false;

    }

    public  static List<String> cardsPlayable (List <String> playerCards, String lastCardDiscarded){
        ArrayList <String> playable = new ArrayList<>();
        for (String card : playerCards){
            if (isCardPlayable(card, lastCardDiscarded)){
                playable.add(card);
            }
        }
        return playable;
    }

    public static  void showCards (List <String> playerCards){
        int idx = 0;
        System.out.println("Here are all Your Cards");
        for (String card : playerCards){
            idx++;
            System.out.println(idx + " " + card);
        }
        System.out.println();
    }

    public  static int playAfterDraw(String card, String lastCardDiscarded, String name, List <String> playableCards, List<String> hand, List <String> discardPile, int turnIdx, LinkedHashMap<String, List<String>> playerDict, List <String> playerNames, List <String> shuffledCards){
        int turn = turnIdx ;
        if (isCardPlayable(card, lastCardDiscarded)){
            playableCards.add(card);
            if (name.contains("Human")){
                String choice = playOrKeep(card);
                if (choice.contains("play")){
                    Players.chooseCardToPlay(playableCards);
                    turn = Players.addToDiscardPileRemoveFromHand(hand, card, discardPile, turnIdx, playerDict, playerNames, shuffledCards);
                }
            }else {
                turn = Players.addToDiscardPileRemoveFromHand(hand, card, discardPile, turnIdx, playerDict, playerNames, shuffledCards);
            }
        }
        return turn;
    }

    public  static String playOrKeep(String card){
        while (true){
            Scanner scanner = new Scanner(System.in);
            System.out.println();
            System.out.println("You picked up "+ card.toUpperCase() + "!!!!!!");
            System.out.println("Would you like to keep or play card");
            System.out.println("Enter play or keep");
            String choice = scanner.nextLine().strip().toLowerCase();
            if (choice.contains("keep") || choice.contains("play")){
                return choice;
            }
        }
    }

    public  static String getRandomColor(List<String> colors){
        int length = colors.size();
        length--;
        Random random = new Random();
        int idx = random.nextInt(length);
        String color = colors.get(idx);
        return  color;
    }

    public static String chooseNewColor(List<String> colors){
        while (true){
            System.out.println();
            System.out.println("Pick a new color for the game");
            int idx = 0;
            for (String color: colors){
                idx++;
                System.out.println(idx + " "+ color);
            }
            System.out.println("Enter 1 - 4: ");
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine().strip();
            if (choice.length() == 1 && Character.isDigit(choice.charAt(0))) {
                int pick = Integer.parseInt(choice);
                if (pick >= 1 && pick <= 4) {
                    pick--;
                    String newColor = colors.get(pick);
                    return newColor;
                }
            }
        }
    }

    public static String newColor(String playerName, List <String> playerHand){
        List<String> colors = gameColors();
        String newGameColor = "";

        if (playerName.contains("Computer")){
            ArrayList <String> colorsInHand = new ArrayList<String>();
            for (String card : playerHand){
                String [] cardArray = card.split(" ");
                if (colors.contains(cardArray[0]) && !colorsInHand.contains(cardArray[0])){
                    colorsInHand.add(cardArray[0]);
                }
            }

            if (colorsInHand.size() == 0){
                newGameColor = getRandomColor(colors);
            }else {
                newGameColor = getRandomColor(colorsInHand);
            }

        }else {
            newGameColor = chooseNewColor(colors);
        }
        String colorMessage = "Game Color Changed to "+ newGameColor;
        displayMessage("+", colorMessage);
        return  newGameColor;
    }

    public static void displayMessage(String symbol, String message){
        System.out.println();
        String boarder = symbol.repeat(50);
        System.out.println(boarder);
        System.out.println(symbol.repeat(2) + "   " + message +"  "+ symbol.repeat(2));
        System.out.println(boarder);
    }

    public static void showComputerCards(List <String> hand){
        System.out.println("Here are the computer's Cards");
        int length = hand.size();
        length++;
        for (int i = 1; i < length ; i++)
            System.out.println(i + " UNO");
    }

    }

