package com.dellemc.rawdata.analyze;

public class FakeAnalyzer implements Analyzer{
    @Override
    public void analyze(String message) {
        System.out.println("received message: " + message);
    }
}
