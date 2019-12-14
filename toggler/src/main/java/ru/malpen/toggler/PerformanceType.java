package ru.malpen.toggler;

public enum PerformanceType {
    Screen(0),
    Case(1);

    private final int number;

    PerformanceType(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
