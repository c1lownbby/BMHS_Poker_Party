package players;

import game.HandRanks;
import game.Player;

public class SanfordPlayer extends Player {
    public SanfordPlayer(String name) {
        super(name);
    }

    @Override
    protected void takePlayerTurn() {
        if (shouldFold()) {
            fold();
        } else if (shouldCheck()) {
            check();
        } else if (shouldCall()) {
            call();
        } else if (shouldRaise()) {
            shouldRaise();
        } else if (shouldAllIn()) {
            allIn();
        }
    }

    @Override
    protected boolean shouldFold() {
        //only fold with bad hand a high bets
        return getGameState().getTableBet() > getBank() * 0.1;
    }

    @Override
    protected boolean shouldCheck() {
        //always check if possible
        return !getGameState().isActiveBet();
    }

    @Override
    protected boolean shouldCall() {
        //call with mediocre hand and worth less than .5 of bank
        //call with a good hand and the call is worth less than .75 of bank
        //call with royal flush or straight flush no matter what
        return getGameState().isActiveBet() && getGameState().getTableBet() < getBank() * 0.03;
    }

    @Override
    protected boolean shouldRaise() {
        //raise for pairs, straights, flush, 3/4 of a kind, full house
        //1 and 2 pair = raise .10 of you bank
        //3 of a kind or straight = raise by .25 of your bank
        // flush and full house = raise by .5 of your bank
        // 4 of a kind or straight flush = raise by .75 of your bank;
        boolean hasPair = evaluatePlayerHand().getValue() >= HandRanks.PAIR.getValue();
        boolean hasTwoPair = evaluatePlayerHand().getValue() >= HandRanks.TWO_PAIR.getValue();
        boolean hasThreeOfAKind = evaluatePlayerHand().getValue() >= HandRanks.THREE_OF_A_KIND.getValue();
        boolean straight = evaluatePlayerHand().getValue() >= HandRanks.STRAIGHT.getValue();
        boolean flush = evaluatePlayerHand().getValue() >= HandRanks.FLUSH.getValue();
        boolean fourOfAKind = evaluatePlayerHand().getValue() >= HandRanks.FOUR_OF_A_KIND.getValue();
        boolean straightFlush = evaluatePlayerHand().getValue() >= HandRanks.STRAIGHT_FLUSH.getValue();

        boolean betIsSmallPercentageOfBank = getGameState().getTableBet() < getBank() * 0.05;
        return betIsSmallPercentageOfBank;
    }

    @Override
    protected boolean shouldAllIn() {
        //all in only with royal flush
        return evaluatePlayerHand().getValue() >= HandRanks.FULL_HOUSE.getValue();
    }
}
