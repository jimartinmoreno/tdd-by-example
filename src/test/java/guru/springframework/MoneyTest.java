package guru.springframework;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MoneyTest {

    @Test
    void testMultiplicationDollar() {
        Money five = Money.dollar(5);
        assertEquals(Money.dollar(10), five.times(2));
        assertEquals(Money.dollar(15), five.times(3));
    }

    @Test
    void testEqualityDollar() {
        assertEquals(Money.dollar(5), Money.dollar(5));
        assertNotEquals(Money.dollar(5), Money.dollar(8));
    }

    @Test
    void testMultiplicationFranc() {
        Money five = Money.franc(5);
        assertEquals(Money.franc(10), five.times(2));
        assertEquals(Money.franc(15), five.times(3));
    }

    @Test
    void testEqualityFranc() {
        assertEquals(Money.franc(5), Money.franc(5));
        assertNotEquals(Money.franc(5), Money.franc(8));
    }

    @Test
    void testDollarNotEqualsFranc() {
        assertNotEquals(Money.franc(5), Money.dollar(5));
    }

    @Test
    void testCurrency() {
        assertEquals(Currency.USD, Money.dollar(1).currency());
        assertEquals(Currency.CHF, Money.franc(1).currency());
    }

    @Test
    void testSimpleAddition() {
        Bank bank = new Bank();
        Money five = Money.dollar(5);
        Expression sum = five.plus(five);
        assertEquals(Money.dollar(10), bank.reduce(sum, Currency.USD));
    }

    @Test
    void testPlusReturnsSum() {
        Money five = Money.dollar(5);
        Money four =  Money.dollar(4);
        Sum sum = (Sum) five.plus(four);
        assertEquals(five, sum.augmend());
        assertEquals(four, sum.addmend());
        assertEquals(Money.dollar(9), new Bank().reduce(sum, Currency.USD));
    }

    @Test
    void testReduceSum() {
        Bank bank = new Bank();
        Expression sum = new Sum(Money.dollar(3), Money.dollar(4));
        Money result = bank.reduce(sum, Currency.USD);
        assertEquals(Money.dollar(7), result);
    }

    @Test
    void testReduceMoney() {
        Bank bank = new Bank();
        bank.addRate(Currency.USD, Currency.CHF, 0.5);

        Money result = bank.reduce(Money.dollar(1), Currency.USD);
        assertEquals(Money.dollar(1), result);
        result = bank.reduce(Money.dollar(1), Currency.CHF);
        assertEquals(Money.franc(2), result);
    }

    @Test
    void testReduceMoneyDifferentCurrency() {
        Bank bank = new Bank();
        bank.addRate(Currency.CHF, Currency.USD, 2.0);
        Money result = bank.reduce(Money.franc(2), Currency.USD);
        assertEquals(Money.dollar(1), result);
    }

    @Test
    void testIdentityRate() {
        assertEquals(1, new Bank().rate(Currency.USD, Currency.USD));
        assertEquals(1, new Bank().rate(Currency.CHF, Currency.CHF));
    }

    @Test
    void testMixedAddition() {
        Bank bank = new Bank();
        bank.addRate(Currency.CHF, Currency.USD, 2.0);

        Expression fiveBucks = Money.dollar(5);
        Expression tenFrancs = Money.franc(10);
        Expression sum = new Sum(fiveBucks, tenFrancs);

        Money result = bank.reduce(sum, Currency.USD);
        assertEquals(result, Money.dollar(10));
    }

    @Test
    void testSumPlusMoney() {
        Bank bank = new Bank();
        bank.addRate(Currency.CHF, Currency.USD, 2);


        Expression fiveBucks = Money.dollar(5);
        Expression tenFrancs = Money.franc(10);
        Expression sum = new Sum(fiveBucks, tenFrancs).plus(fiveBucks);

        Money result = bank.reduce(sum, Currency.USD);
        assertEquals(Money.dollar(15), result);

        Money result1 = new Sum(result, tenFrancs).reduce(bank, Currency.USD);
        Money result2 = bank.reduce(new Sum(result, tenFrancs), Currency.USD);
        assertEquals(result1, result2);
    }

    @Test
    void testSumTimes() {
        Expression fiveBucks = Money.dollar(5);
        Expression tenFrancs = Money.franc(10);
        Bank bank = new Bank();
        bank.addRate(Currency.CHF, Currency.USD, 2);
        Expression sum = new Sum(fiveBucks, tenFrancs).times(2);
        Money result = bank.reduce(sum, Currency.USD);
        assertEquals(Money.dollar(20), result);
    }

    @Test
    void testMoneyTimes() {
        Expression fiveBucks = Money.dollar(5);
        Bank bank = new Bank();
        Expression sum = fiveBucks.times(4);
        Money result = bank.reduce(sum, Currency.USD);

        assertEquals(Money.dollar(20), result);
        assertEquals(Money.dollar(20), sum);
    }

}