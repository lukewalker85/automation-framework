package com.automation.testdata;

public record LoginScenario(String name, String username, String password, String expectedError) {
    @Override
    public String toString() {
        return name;
    }
}

