package guru.springframework;

public interface Expression {
    Money reduce(Bank bank, Currency to);

    Sum plus(Expression fiveBucks);

    Expression times(int multiplier);
}
