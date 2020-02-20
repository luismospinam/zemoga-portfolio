package com.zemoga.portfolio.service;

import com.zemoga.portfolio.Exception.CustomPortfolioException;
import com.zemoga.portfolio.model.Portfolio;
import com.zemoga.portfolio.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    private PortfolioRepository portfolioRepository;

    @Autowired
    public PortfolioServiceImpl(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    @Override
    public Portfolio findPortfolioById(Integer id) {
        if (id == null || id <= 0) {
            throw new CustomPortfolioException("The portfolio Id can't be null and must be greater than 0.");
        }
        return portfolioRepository.findById(id)
                .orElseThrow(() -> new CustomPortfolioException("There was not portfolio found with id: " + id));
    }

}
