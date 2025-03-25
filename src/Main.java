import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        create the cards
        List <String> cards = Cards.BuildCards();
//        shuffle the cards
        List <String> shuffledCards = Cards.shuffledCards(cards);
//        get the players names
        List <String> names = Players.playerNames();
//        deal the player's first cards
        List <List> firstDeal = Players.firstCardDeal(shuffledCards);
//        linkedHashMap to handle the player's names and their cards
        LinkedHashMap<String, List<String>> playerCardsDict = Players.playerDict(names, firstDeal);
//        discard pile
        ArrayList<String> discardPile = new ArrayList<String>();
//        get the first card put down
        String firstCard = Cards.firstCardPutDown(shuffledCards);
//        add first card to discard pile and initialize turn index
        int turnIdx = Cards.addCardToDiscardPile(discardPile, firstCard, 2, playerCardsDict, names, shuffledCards);
//        play game
        GamePlay.playGame(shuffledCards, playerCardsDict, discardPile, turnIdx, names);



    }
}