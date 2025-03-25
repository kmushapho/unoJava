import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class GamePlay {
    public static String draw (List <String> playerCards, int numCardsToDraw, List <String> shuffledCards, String playerName, List <String> discardPile ){
        if (numCardsToDraw > numCardsToDraw){
            reshuffleCards(discardPile, shuffledCards);
        }

        for (int i = 0; i < numCardsToDraw; i ++){
            String card = shuffledCards.get(0);
            playerCards.add(card);
            shuffledCards.remove(0);
            if (numCardsToDraw == 1){
                return  card;
            }
        }
        String message = playerName + " picked up " + numCardsToDraw + " cards.";
        Cards.displayMessage("!", message);
        return  "";
    }


    public static String getActionMessage(String card){
        String message = "";
        if (card.contains("reverse")){
            message = "Game order has reversed the order of the game";
        } else if (card.contains("skip")) {
            message = "Next player turn has been skipped";
        } else if (card.contains("draw")) {
            if (card.contains("draw2")){
                message = "Next player has to draw TWO cards";
            } else if (card.contains("draw4")) {
                message = "Next player has to draw FOUR cards" ;
            }
        }
        return message;
    }

    public static void displayActionCardMessage(String card){
        String message = getActionMessage(card);
        Cards.displayMessage("#", message);
        try {
            Thread.sleep(1000);
        }catch (InterruptedException e) {
            System.out.println("Thread was interrupted.");
        }
    }

    public static int doAction(String card, int turnIdx, LinkedHashMap<String, List<String>> playerDict, List <String> playerNames, List <String> shuffledCards, List <String> discardPile){
        if (card.contains("reverse") || card.contains("skip")){
            turnIdx--;
        } else if (card.contains("draw")) {
            doDrawAction(turnIdx, card, playerDict, playerNames, shuffledCards, discardPile);
            turnIdx--;
        }

        return turnIdx;
    }

    public static void doDrawAction(int turnIdx, String card, LinkedHashMap<String, List<String>> playerDict, List <String> playerNames, List <String> shuffledCards, List<String> discardPile){
        int idx = 0;

        if (turnIdx == 0) {
            idx = 1;
        }

        String name = playerNames.get(idx);
        List <String> playerCards = playerDict.get(name);
        if (!name.contains("Computer")){
            name = "You";
        }
        if (card.contains("draw2")){
            draw(playerCards, 2, shuffledCards,name, discardPile );
        } else if (card.contains("draw4")) {
            draw(playerCards, 4, shuffledCards, name, discardPile);

        }
    }


    public static boolean isWinner(List <String> playerCards){
        return playerCards.size() == 0;
    }

    public static void shoutUno(List <String> playerCards, String playerName){
        if (playerCards.size() == 1){
            String message = playerName + " yelled UNO!";
            Cards.displayMessage("@", message);
        }
    }

    public static void reshuffleCards(List <String> discardPile, List <String> shuffledPile){
        int length = discardPile.size();
        length--;

        for (int i = 0; i < length; i++){
            String card = discardPile.get(i);
            if (card.split(" ").length > 1) {
                shuffledPile.add(card);
            }
                discardPile.remove(0);
        }
        Collections.shuffle(shuffledPile);
    }

    public static int controlTurnIdx(int turnIdx){
        if (turnIdx > 1){
            turnIdx = 0;
        }
        return turnIdx;
    }

    public static  void playGame(List <String> shuffledPile, LinkedHashMap<String, List<String>> playerDict, List<String> discardPile, int turnIdx, List <String> playerNames){

        while (true){

            turnIdx = controlTurnIdx(turnIdx);

//            get the last card discarded
            int discardPileLength = discardPile.size();
            discardPileLength--;
            String lastCardDiscarded = discardPile.get(discardPileLength);

//            show last card put down
            Cards.showLastCardPutDown(discardPile);

//            get current player name and their cards
            String playerName = playerNames.get(turnIdx);
            List <String> playerHand = playerDict.get(playerName);

//            game play
            if (playerName.contains("Computer")){
                turnIdx = Players.computerPlay(playerHand, discardPile, lastCardDiscarded, shuffledPile, playerDict, playerNames, turnIdx);
            }else {
                turnIdx = Players.humanPlay(playerHand, discardPile, lastCardDiscarded, shuffledPile, playerDict, playerNames, turnIdx);
            }
            System.out.println();

//            update the player dictionary
            playerDict.put(playerName, playerHand);

//            increment the turnIdx
            turnIdx++;

            if (playerName.contains("Human")){
                playerName = "You";
            }

//            shout uno if only one card left
            shoutUno(playerHand, playerName);

//            check if someone won
            if (isWinner(playerHand)){
                String message = playerName + " WON!!!!!!!!!";
                Cards.displayMessage("$", message);
                System.out.println("GAME OVER");
                break;
            }

//            reshuffle cards if the shuffled cards get finished before the game ends
            if (shuffledPile.size() == 0){
                reshuffleCards(discardPile, shuffledPile);
            }

        }

    }


}
