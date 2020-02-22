package com.zemoga.portfolio.service;

import com.zemoga.portfolio.Exception.CustomPortfolioException;
import com.zemoga.portfolio.Exception.PortfolioNotFoundException;
import com.zemoga.portfolio.model.Portfolio;
import com.zemoga.portfolio.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortfolioServiceImpl implements PortfolioService {


    private TwitterIntegrationService twitterIntegrationService;
    private PortfolioRepository portfolioRepository;

    @Autowired
    public PortfolioServiceImpl(PortfolioRepository portfolioRepository, TwitterIntegrationService twitterIntegrationService) {
        this.portfolioRepository = portfolioRepository;
        this.twitterIntegrationService = twitterIntegrationService;
    }

    @Override
    public Portfolio findPortfolioById(String id) {
        return findPortfolioById(id, true);
    }


    protected Portfolio findPortfolioById(String id, boolean shouldIntegrateWithTwitter) {
        if (id == null || !stringIsValidInteger(id) || Integer.valueOf(id) <= 0) {
            throw new CustomPortfolioException("The portfolio Id can't be null and must be a valid Number greater than 0.");
        }

        var portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new PortfolioNotFoundException("There was not portfolio found with id: " + id));

        if (shouldIntegrateWithTwitter) {
            portfolio.setTweets(twitterIntegrationService.getLastTweetsFromUser(portfolio.getTwitterUserName()));
        }

        return portfolio;
    }

    @Override
    public Portfolio updatePortfolio(Portfolio portfolio, String id) {
        if (id == null) {
            throw new CustomPortfolioException("An Id must be provided as a path parameter in the URL.");
        } else if (portfolio == null) {
            throw new CustomPortfolioException("When updating a portfolio the body needs to be included with the Id at least.");
        }

        portfolio.setIdPortfolio(id);
        findPortfolioById(portfolio.getIdPortfolio(), false);

        portfolio = portfolioRepository.save(portfolio);

        return portfolio;
    }

    private boolean stringIsValidInteger(String number) {
        try {
            Integer.valueOf(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
