package ru.clevertec.racing.model;

import org.checkerframework.checker.units.qual.A;

import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;

public class Car implements Runnable {
    private Integer speed;
    private Integer numberCar;
    private Double time;
    private Phaser phaser;
    private Integer lengthRoute;

    public static final AtomicInteger winner = new AtomicInteger(0);

    private static final AtomicInteger aCount = new AtomicInteger(0);

    public Car(Integer speed, Integer lengthRoute, Phaser phaser) {
        this.speed = speed;
        this.lengthRoute = lengthRoute;
        this.phaser = phaser;
        this.numberCar = aCount.incrementAndGet();
        this.time = 0d;
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

    public void setWinner() {
        winner.compareAndSet(0, numberCar);
    }

    public AtomicInteger getWinner() {
        return this.winner;
    }

    @Override
    public String toString() {
        return "Болид №" + numberCar;
    }

    @Override
    public void run() {
        System.out.println(this + " на старте! " + this.getSpeed());
        phaser.arriveAndAwaitAdvance();
        this.setTime(Double.valueOf(lengthRoute) / Double.valueOf(this.getSpeed()));
        while (lengthRoute > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            lengthRoute -= this.getSpeed();
            System.out.println(this + " до финиша " + (lengthRoute >= 0 ? lengthRoute : 0) + " м.");
        }
        setWinner();
        System.out.printf("Финишировал: %s время %.2f\n", this, this.getTime());
    }
}
