package ua.pti.myatm;

import ua.pti.myatm.Exceptions.NoCardInsertedException;
import ua.pti.myatm.Exceptions.NotEnoughMoneyInATMException;
import ua.pti.myatm.Exceptions.NotEnoughMoneyInAccountException;

public class ATM {
    private double moneyInATM;
    //Если в банкомат не вставлена, то значение поля card равно null
    private Card card;
        
    //Можно задавать количество денег в банкомате 
    ATM(double moneyInATM){
        if(moneyInATM < 0)
            throw new IllegalArgumentException();
        this.moneyInATM = moneyInATM;
    }

    // Возвращает каоличестов денег в банкомате
    public double getMoneyInATM() {
         return this.moneyInATM;
    }
        
    //С вызова данного метода начинается работа с картой
    //Метод принимает карту и пин-код, проверяет пин-код карты и не заблокирована ли она
    //Если неправильный пин-код или карточка заблокирована, возвращаем false. При этом, вызов всех последующих методов у ATM с данной картой должен генерировать исключение NoCardInserted
    public boolean validateCard(Card card, int pinCode){
        if(card == null)
            throw new IllegalArgumentException();
        if(!card.checkPin(pinCode) || card.isBlocked())
             return false;
        this.card = card;
        return true;
    }
    
    //Возвращает сколько денег есть на счету
    public double checkBalance(){
        this.isCardCorrect();
        return card.getAccount().getBalance();
    }
    
    //Метод для снятия указанной суммы
    //Метод возвращает сумму, которая у клиента осталась на счету после снятия
    //Кроме проверки счета, метод так же должен проверять достаточно ли денег в самом банкомате
    //Если недостаточно денег на счете, то должно генерироваться исключение NotEnoughMoneyInAccount 
    //Если недостаточно денег в банкомате, то должно генерироваться исключение NotEnoughMoneyInATM 
    //При успешном снятии денег, указанная сумма должна списываться со счета, и в банкомате должно уменьшаться количество денег
    public double getCash(double amount){
         isCardCorrect();
         isEnoughMoneyInAccount(amount);
         isEnoughMoneyInATM(amount);
         this.moneyInATM -= card.getAccount().withdrow(amount);
         return card.getAccount().getBalance();
    }
  
    //Проверяет или в поле card значение не null
    public void isCardCorrect(){
        if(card == null)
            throw new NoCardInsertedException();
    }
    
    //Проверяет или достаточно денег для снятия на карте
    public void isEnoughMoneyInAccount(double amount){
        if(amount > card.getAccount().getBalance())
            throw new NotEnoughMoneyInAccountException();
    }
    
    //Проверяет или достаточно денег для снятия в ATM
    public void isEnoughMoneyInATM(double amount){
        if(amount > this.getMoneyInATM())
            throw new NotEnoughMoneyInATMException();
    }
}
