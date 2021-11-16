package guru.springframework;

public record Sum(Expression augmend, Expression addmend) implements Expression {

    public Money reduce(Bank bank, Currency to) {
        double amount = augmend.reduce(bank, to).amount + addmend.reduce(bank, to).amount;
        return new Money(amount, to);
    }

    @Override
    public Expression plus(Expression addend) {
        return new Sum(this, addend);
    }

    public Expression times(int times) {
        return new Sum(augmend.times(times), addmend.times(times));
    }
}