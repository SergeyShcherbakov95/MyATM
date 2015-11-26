package ua.pti.myatm;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import ua.pti.myatm.Exceptions.NoCardInsertedException;
import ua.pti.myatm.Exceptions.NotEnoughMoneyInATMException;
import ua.pti.myatm.Exceptions.NotEnoughMoneyInAccountException;

/**
 *
 * @author andrii
 */
public class ATMTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructor_SetNegativeValueInMoneyInATM(){
        new ATM(-1);
    } 
    
    @Test
    public void constructor_SetCorrectCalueInMoneyInATM(){
        ATM actualATM = new ATM(1);
        assertEquals(1, actualATM.getMoneyInATM(), 0.001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void validateCard_CardIsNull(){
        new ATM(1).validateCard(null, 1111);
    }
    
    @Test
    public void validateCard_PinCodeIsIncorrect(){
        Card card = mock(Card.class);
        
        when(card.checkPin(1111)).thenReturn(Boolean.FALSE);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);

        assertFalse(new ATM(1).validateCard(card, 1111));
    }
    
    @Test
    public void validateCard_CardIsBlocked(){
        Card card = mock(Card.class);
        
        when(card.checkPin(1111)).thenReturn(Boolean.TRUE);
        when(card.isBlocked()).thenReturn(Boolean.TRUE);
        
        assertFalse(new ATM(1).validateCard(card, 1111));
    }
    
    @Test
    public void validateCard_Correct(){
        Card card = mock(Card.class);
        
        when(card.checkPin(1111)).thenReturn(Boolean.TRUE);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        
        assertTrue(new ATM(1).validateCard(card, 1111));
    }
    
    @Test(expected = NoCardInsertedException.class)
    public void checkBalance_CardIsNull(){
        ATM actualATM = new ATM(1);
        
        actualATM.checkBalance();
    }
    
    @Test
    public void checkBalance_Correct(){
        ATM actual = new ATM(1);
        
        Card card = mock(Card.class);
        Account account = mock(Account.class);
        
        when(card.checkPin(1111)).thenReturn(Boolean.TRUE);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(0.5);
        
        actual.validateCard(card, 1111);
        
        assertEquals(0.5, actual.checkBalance(), 0.001);
    }
    
    @Test(expected = NoCardInsertedException.class)
    public void isCardCorrect_CardIsNull(){
        new ATM(1).isCardCorrect();
    }
    
    @Test
    public void isCardCorrect_Correct(){
        Card card = mock(Card.class);
        
        when(card.checkPin(1111)).thenReturn(Boolean.TRUE);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        
        ATM actualATM = new ATM(1);
        
        actualATM.validateCard(card, 1111);
        
        actualATM.isCardCorrect();
    }
    
    @Test(expected = NotEnoughMoneyInAccountException.class)
    public void isEnoughMoneyInAccount_MoneyIsNotEnough(){
        ATM actualATM = new ATM(1);
        double amount = 1000;
        
        Card card = mock(Card.class);
        Account account = mock(Account.class);
        
        when(card.checkPin(1111)).thenReturn(Boolean.TRUE);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(999d);
        
        actualATM.validateCard(card, 1111);
        actualATM.isEnoughMoneyInAccount(amount);
    }
    
    @Test
    public void isEnoughMoneyInAccount_Correct(){
        ATM actualATM = new ATM(1);
        double amount = 1000;
        
        Card card = mock(Card.class);
        Account account = mock(Account.class);
        
        when(card.checkPin(1111)).thenReturn(Boolean.TRUE);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(1001d);
        
        actualATM.validateCard(card, 1111);
        actualATM.isEnoughMoneyInAccount(amount);
    }
    
    @Test(expected = NotEnoughMoneyInATMException.class)
    public void isEnoughMoneyInATM_MoneyIsNotEnough(){
        ATM actualATM = new ATM(999);
        double amount = 1000;
        
        actualATM.isEnoughMoneyInATM(amount);
    }
    
    @Test
    public void isEnoughMoneyInATM_Correct(){
        ATM actualATM = new ATM(1001);
        double amount = 1000;
        
        actualATM.isEnoughMoneyInATM(amount);
    }
    
    @Test(expected = NoCardInsertedException.class)
    public void getCash_CardIsNull(){
        ATM actualATM = new ATM(1000);
        double amount = 1000;
        
        actualATM.getCash(amount);
    }
    
    @Test(expected = NotEnoughMoneyInAccountException.class)
    public void getCash_MoneyIsNotEnoughInAccount(){
        ATM actualATM = new ATM(1000);
        double amount = 1000;
        
        Card card = mock(Card.class);
        Account account = mock(Account.class);
        
        when(card.checkPin(1111)).thenReturn(Boolean.TRUE);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(999d).thenReturn(0d);
        when(account.withdrow(amount)).thenReturn(1000d);
        
        actualATM.validateCard(card, 1111);
        actualATM.getCash(amount);
    }
    
    @Test(expected = NotEnoughMoneyInATMException.class)
    public void getCash_MoneyIsNotEnoughInATM(){
        ATM actualATM = new ATM(999);
        double amount = 1000;
        
        Card card = mock(Card.class);
        Account account = mock(Account.class);
        
        when(card.checkPin(1111)).thenReturn(Boolean.TRUE);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(1000d).thenReturn(0d);
        when(account.withdrow(amount)).thenReturn(1000d);
        
        actualATM.validateCard(card, 1111);
        actualATM.getCash(amount);
    }
     
    @Test
    public void getCash_Correct(){
        ATM actualATM = new ATM(1000);
        double amount = 1000;
        
        Card card = mock(Card.class);
        Account account = mock(Account.class);
        
        when(card.checkPin(1111)).thenReturn(Boolean.TRUE);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(1000d).thenReturn(0d);
        when(account.withdrow(amount)).thenReturn(1000d);
        
        actualATM.validateCard(card, 1111);
        assertEquals(0, actualATM.getCash(amount), 0.001);
    }
    
    @Test
    public void getCash_isMoneyInATMCorrectAfterTransaction(){
        ATM actualATM = new ATM(1500);
        double amount = 1000;
        
        Card card = mock(Card.class);
        Account account = mock(Account.class);
        
        when(card.checkPin(1111)).thenReturn(Boolean.TRUE);
        when(card.isBlocked()).thenReturn(Boolean.FALSE);
        when(card.getAccount()).thenReturn(account);
        when(account.getBalance()).thenReturn(1300d).thenReturn(300d);
        when(account.withdrow(amount)).thenReturn(1000d);
        
        actualATM.validateCard(card, 1111);
        actualATM.getCash(amount);
        
        assertEquals(500, actualATM.getMoneyInATM(), 0.001);
    }   
}
