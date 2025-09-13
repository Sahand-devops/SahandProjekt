package com.friends.controllers;
/**
 * Abstrakt basklass för att hantera minnesoperationer (M) på kalkylatorn.
 * Denna klass tillhandahåller ett delat statiskt minne för alla instanser och deklarerar metoder för att hantera minnesrelaterade operationer.
 */
public abstract class MemoryControl {
    protected static double memory = 0;
    public static double getMemory() {
        return memory;
    }

    public abstract void handleMemoryOperation(double value);

    public static class MemoryAdd extends MemoryControl {
        @Override
        public void handleMemoryOperation(double value) {
            memory += value;
        }
    }

    public static class MemorySubtract extends MemoryControl {
        @Override
        public void handleMemoryOperation(double value) {
            memory -= value;
        }
    }

    public static class MemoryClear extends MemoryControl {
        @Override
        public void handleMemoryOperation(double value) {
            memory = 0;
        }
    }


    // NEW: Static helper methods to simplify calls from ButtonHandler
    public static void addMemory(double value) {
        new MemoryAdd().handleMemoryOperation(value);
    }

    public static void subtractMemory(double value) {
        new MemorySubtract().handleMemoryOperation(value);
    }

    public static void clearMemory() {
        new MemoryClear().handleMemoryOperation(0);
    }
}