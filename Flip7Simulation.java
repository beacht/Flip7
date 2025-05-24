// Flip7 is a card game where you draw numbered cards and tally their values.
// You "bust" if you draw duplicates, and win the round if you draw 7 unique cards.
// This simulation excludes special cards and focuses only on number cards (0 to 12).
// If you draw 7 unique cards, your score is the sum of those cards plus a 15-point bonus.
// Instead of drawing on your turn, you can choose to "bank" early, accepting the sum of cards drawn so far.

public class Flip7Simulation {

  // Defining the count of each card in the deck
  static final java.util.Map<Integer, Integer> numberCardCounts = new java.util.HashMap<>() {{
    put(0, 1); put(1, 1); put(2, 2); put(3, 3); put(4, 4); put(5, 5);
    put(6, 6); put(7, 7); put(8, 8); put(9, 9); put(10, 10); put(11, 11); put(12, 12);
  }};

  // Build an unshuffled deck of cards based on the counts
  static java.util.List<Integer> createDeck() {
    java.util.List<Integer> deck = new java.util.ArrayList<>();
    for (int num = 0; num <= 12; num++) {
      for (int i = 0; i < numberCardCounts.get(num); i++) {
        deck.add(num);
      }
    }
    return deck;
  }

  // Randomly shuffle the given deck
  static void shuffle(java.util.List<Integer> deck) {
    java.util.Collections.shuffle(deck);
  }

  // Simulates drawing cards until a duplicate or 7 unique cards are drawn. Returns how many cards were drawn (not including the busting or 7th card).
  static int simulateUniqueFlips() {
    java.util.List<Integer> deck = createDeck();
    shuffle(deck);
    java.util.Set<Integer> seen = new java.util.HashSet<>();
    int count = 0;

    for (int card : deck) {
      if (seen.contains(card)) return count; // Bust. Return the count of cards we had BEFORE drawing this one.
      seen.add(card); // Not a bust, so add to our hand.
      if (seen.size() == 7) return 6; // If this card is our 7th unique, return 6 (because count before drawing here has to have been 6).
      count++; // If we haven't busted and haven't drawn 7 unique, put it in our hand and draw again.
    }

    return count;
  }

  // Simulates drawing cards until a duplicate or 7 unique cards are drawn. Returns the score of drawn cards (not including the busting or 7th card).
  static int simulateScoreBeforeDuplicate() {
    java.util.List<Integer> deck = createDeck();
    shuffle(deck);
    java.util.Set<Integer> seen = new java.util.HashSet<>();
    int score = 0;

    for (int card : deck) {
      if (seen.contains(card)) return score; // Bust. Return the score of cards we had BEFORE drawing this one.
      seen.add(card); // Not a bust, so add to our hand.
      if (seen.size() == 7) return score; // If this card is our 7th unique, return the score we had BEFORE drawing this one.
      score += card; // If we haven't busted and haven't drawn 7 unique, put it in our hand and draw again.
    }

    return score;
  }

  // Simulates banking after drawing N cards, or busting or hitting 7 unique. Returns the score you would actually get.
  static int simulateBankingAfterN(int n) {
  java.util.List<Integer> deck = createDeck();
  shuffle(deck);
  java.util.Set<Integer> seen = new java.util.HashSet<>();
  int score = 0;

  for (int card : deck) {
    if (seen.size() == n) return score; // If we've drawn n cards, bank.
    if (seen.contains(card)) return 0; // Bust (the other functions return score because they're checking score BEFORE bust, but this function returns the actual score you'd get).
    seen.add(card); // This card is unique, so put it in our hand.
    score += card;
    if (seen.size() == 7) return score + 15; // If this card is our 7th unique, return our score for the round (sum of cards, including this one, plus 15).
  }

  return score;
}

  // Runs the given simulation function multiple times and returns the average result
  static double average(java.util.function.Supplier<Integer> simFn, int trials) {
    long total = 0;
    for (int i = 0; i < trials; i++) {
      total += simFn.get();
    }
    return (double) total / trials;
  }

  public static void main(String[] args) {
    final int trials = 10000;
    System.out.println("Average hand size BEFORE busting or 7 unique: " + average(Flip7Simulation::simulateUniqueFlips, trials));
    System.out.println("Average score BEFORE busting or 7 unique: " + average(Flip7Simulation::simulateScoreBeforeDuplicate, trials));

    for (int i = 1; i <= 6; i++) {
      final int flips = i;
      System.out.println("Average score when planning to bank after " + flips + " card(s) drawn: " + average(() -> simulateBankingAfterN(flips), trials));
    }
  }
}
