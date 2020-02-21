package com.zemoga.portfolio;

import com.zemoga.portfolio.model.Portfolio;

public class TestHelper {

    public static Portfolio createDummyPortfolio(String id) {
        return new Portfolio(id, "testUser", "testImage", "testDescription", "testTitle");
    }
}
