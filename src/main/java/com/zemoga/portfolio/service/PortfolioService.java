package com.zemoga.portfolio.service;


import com.zemoga.portfolio.model.Portfolio;

public interface PortfolioService {

    Portfolio findPortfolioById(String id);

    Portfolio updatePortfolio(Portfolio portfolio, String id);

}
