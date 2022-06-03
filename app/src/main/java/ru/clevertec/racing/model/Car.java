package ru.clevertec.racing.model;

import java.sql.Time;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Car implements Callable<Car> {
    private final Integer speed;
    private final Integer numberCar;
    private Double time;
    private final Phaser phaser;
    private Integer lengthRoute;
    private static final AtomicInteger aCount = new AtomicInteger(0);

    public Car(Integer speed, Integer lengthRoute, Phaser phaser) {
        this.speed = speed;
        this.lengthRoute = lengthRoute;
        this.phaser = phaser;
        this.numberCar = aCount.incrementAndGet();
        this.time = lengthRoute * 1.00 / this.getSpeed();
        phaser.register();
    }

    public Integer getSpeed() {
        return this.speed;
    }

    public Integer getNumberCar() {
        return this.numberCar;
    }

    public Double getTime() {
        return this.time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Болид №" + numberCar;
    }

    @Override
    public Car call() {
        System.out.printf("%s на старте! %n", this);
        var lastLength = lengthRoute % speed;
        phaser.arriveAndAwaitAdvance();
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
        System.out.printf("Финишировал: %s время %.2f %n", this, time);
        return this;
    }
}
