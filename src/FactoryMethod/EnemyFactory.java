package FactoryMethod;


/**  Creator abstract – definește Factory Method-ul. */
public abstract class EnemyFactory {
    /**  Factory Method: returnează un obiect care implementează Enemy. */
    public abstract Enemy createEnemy();
}