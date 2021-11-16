package guru.springframework;

import java.util.HashMap;
import java.util.Map;

public class Bank {

    Map<Pair, Double> conversionRates = new HashMap<>();

    public Money reduce(Expression source, Currency currency) {
        return source.reduce(this, currency);
    }

    public void addRate(Currency from, Currency to, double conversionRate) {
        conversionRates.put(new Pair(from, to), conversionRate);
    }

    public double rate(Currency from, Currency to) {
        if (from.equals(to)){
            return 1;
        }
        return conversionRates.get(new Pair(from, to));
    }
}