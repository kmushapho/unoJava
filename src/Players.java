import java.util.*;

public class Players {

    public static  List <String> playerNames(){
        ArrayList<String> names = new ArrayList<String>();
        names.add("Human");
        names.add("Computer");
        return names;
    }

    public  static List <List> firstCardDeal(List <String> shuffledCards){
        ArrayList <List> allCardPlayers = new ArrayList<>();

        for (int i = 0; i < 2; i++){
            ArrayList <String> playerCards = new ArrayList<>();
            for (int j = 0; j < 7; j++){
                String card = shuffledCards.get(0);
                playerCards.add(card);
                shuffledCards.remove(0);
            }
            allCardPlayers.add(playerCards);
        }
        return  allCardPlayers;
    }

    public static LinkedHashMap playerDict( List <String> names, List <List> playerCards) {
        LinkedHashMap dict = new LinkedHashMap<>();
        for (int i = 0; i < 2; i++) {
            dict.put(names.get(i), playerCards.get(i));
        }
        return dict;
    }


    public static int computerPlay(List<String> computerHand, List <String> discardPile, String lastCardDiscarded, List <String> shuffledCards, LinkedHashMap<String, List<String>> playerDict, List <String> playerNames, int turnIdx){
        List <String> playableCards = Cards.cardsPlayable(computerHand,lastCardDiscarded );
        Cards.showComputerCards(computerHand);
        try {
            System.out.println();
            System.out.println("Computer is thinking........");
            Thread.sleep(2000);
        }catch (InterruptedException e) {
            System.out.println("Thread was interrupted.");
        }
        int turn = turnIdx;
        if (playableCards.size() == 0){
            String card = GamePlay.draw(computerHand, 1, shuffledCards, "Computer", discardPile);
            turn = Cards.playAfterDraw(card, lastCardDiscarded, "Computer", playableCards, computerHand, discardPile, turnIdx, playerDict, playerNames, shuffledCards);

        }
        else {
            Random random = new Random();
            int length = playableCards.size();
            int idx = random.nextInt(length--);
            String card = playableCards.get(idx);
            turn = addToDiscardPileRemoveFromHand(computerHand, card, discardPile, turnIdx, playerDict, playerNames, shuffledCards);
        }
        return turn;
    }

    public static int humanPlay(List<String> humanHand,List<String> discardPile, String lastCardDiscarded, List <String> shuffledCards, LinkedHashMap<String, List<String>> playerDict, List <String> playerNames, int turnIdx){
        List <String> playableCards = Cards.cardsPlayable(humanHand, lastCardDiscarded);
        Cards.showCards(humanHand);
        int turn = turnIdx;
        if (playableCards.size() == 0){
            System.out.println("You have no playable cards. You will draw 1 card.");
            String card = GamePlay.draw(humanHand, 1, shuffledCards, "You", discardPile);
            turn = Cards.playAfterDraw(card, lastCardDiscarded, "Human", playableCards, humanHand, discardPile, turnIdx, playerDict, playerNames, shuffledCards);
        }else {
            String card = chooseCardToPlay(playableCards);
            turn = addToDiscardPileRemoveFromHand(humanHand, card, discardPile, turnIdx, playerDict, playerNames, shuffledCards);
        }
        return turn;
    }

    public static int addToDiscardPileRemoveFromHand(List <String> hand, String card, List <String> discardPile, int turnIdx, LinkedHashMap<String, List<String>> playerDict, List <String> playerNames, List <String> shuffledCards){
        int turn = Cards.addCardToDiscardPile(discardPile, card, turnIdx, playerDict, playerNames, shuffledCards);
        hand.remove(card);
        return turn;
    }

    public static String chooseCardToPlay (List <String> playableCards){
        int length = playableCards.size();
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println();
            System.out.println("Here are Your Playable Cards: ");
            int idx = 0;
            for (String i: playableCards){
                idx++;
                System.out.println(idx + " " +i);
            }
            System.out.println("Choose a card to play: ");
            String pick = scanner.nextLine().strip();

            if (pick.length() == 1 && Character.isDigit(pick.charAt(0))){
                int card_idx = Integer.parseInt(pick);
                if (card_idx >= 1 && card_idx <= length){
                    card_idx--;
                    String card = playableCards.get(card_idx);
                    return card;
                }

            }
        }
    }

}
