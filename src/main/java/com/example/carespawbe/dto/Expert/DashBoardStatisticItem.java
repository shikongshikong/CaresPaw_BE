package com.example.carespawbe.dto.Expert;

import lombok.Setter;

@Setter
//public class DashBoardStatisticItem {
//    private long currentAmount;
//    private double growthRate;
//
//    public DashBoardStatisticItem(long currentAmount, double growthRate) {
//        this.currentAmount = currentAmount;
//        this.growthRate = growthRate;
//    }
//}
public class DashBoardStatisticItem<T extends Number> {
    private T currentAmount;
    private double growthRate;

    // Factory method tiện lợi
    public static <T extends Number> DashBoardStatisticItem<T> of(T currentAmount, double growthRate) {
        return new DashBoardStatisticItem<>(currentAmount, growthRate);
    }

    // Constructors, Getters, Setters
    public DashBoardStatisticItem(T currentAmount, double growthRate) {
        this.currentAmount = currentAmount;
        this.growthRate = growthRate;
    }
    // Getters and Setters omitted for brevity
}