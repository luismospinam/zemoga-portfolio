package com.zemoga.portfolio.service;


import com.zemoga.portfolio.model.Portfolio;

public interface PortfolioService {

    Portfolio findPortfolioById(Integer id);

    Portfolio updatePortfolio(Portfolio portfolio, Integer id);

}
