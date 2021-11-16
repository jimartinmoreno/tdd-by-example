package guru.springframework;

import java.util.Objects;

public record Money(double amount, Currency currency) implements Expression {

    public static Money dollar(int amount){
        return new Money(amount, Currency.USD);
    }

    public static Money franc(int amount){
        return new Money(amount, Currency.CHF);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        Money money = (Money) o;

        if (amount != money.amount) return false;
        return Objects.equals(currency.toString(), money.currency.toString());
    }

    @Override
    public Sum plus(Expression addend) {
        return new Sum(this, addend);
    }

    @Override
    public Money reduce(Bank bank, Currency to) {
        double rate = bank.rate(this.currency, to);
        double totalAmount = this.amount / rate;
        return new Money(totalAmount, to);
    }

    @Override
    public Expression times(int multiplier) {
        return new Money(this.amount * multiplier, this.currency);
    }
}
