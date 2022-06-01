package ru.clevertec.racing.model;

import java.util.concurrent.atomic.AtomicInteger;

public class Car {
    private Integer speed;
    private String name;

    private static final AtomicInteger aCount = new AtomicInteger(0);

    public Car(Integer speed) {
        this.speed = speed;
        this.name = String.valueOf(aCount.incrementAndGet());
    }
    public Integer getSpeed() {
        return this.speed;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "Болид №" + name.toString();
    }
}
