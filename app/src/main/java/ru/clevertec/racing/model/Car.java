package ru.clevertec.racing.model;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;

public class Car implements Callable<Car> {
    public static final AtomicInteger winner = new AtomicInteger(0);
    private final Integer speed;
    private final Integer numberCar;
    private final Phaser phaser;
    private Integer lengthRoute;
    private static final AtomicInteger aCount = new AtomicInteger(0);

    public Car(Integer speed, Integer lengthRoute, Phaser phaser) {
        this.speed = speed;
        this.lengthRoute = lengthRoute;
        this.phaser = phaser;
        this.numberCar = aCount.incrementAndGet();
        phaser.register();
    }

    public Integer getSpeed() {
        return this.speed;
    }

    public Integer getNumberCar() {
        return this.numberCar;
    }

    private void setWinner(int numberCar) {
        winner.compareAndSet(0, numberCar);
    }

    @Override
    public String toString() {
        return "Болид №" + numberCar;
    }

    @Override
    public Car call() {
        Random random = new Random();
        try {
            Thread.sleep(random.nextInt(10, 2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("%s на старте! %n", this);
        phaser.arriveAndAwaitAdvance();
        var lastLength = lengthRoute % speed;
        phaser.arriveAndAwaitAdvance();
        var time = lengthRoute * 1.0 / speed;
        while (lengthRoute > lastLength) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lengthRoute -= speed;
            System.out.printf("%s до финиша %s м.%n", this, (lengthRoute >= 0 ? lengthRoute : 0));
        }
        try {
            Thread.sleep((long) ((lastLength * 1.0 / speed) * 1000L));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setWinner(numberCar);
        System.out.printf("Финишировал: %s время %.2f %n", this, time);
        phaser.arriveAndDeregister();
        return this;
    }
}
