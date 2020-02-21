package com.zemoga.portfolio.repository;

import com.zemoga.portfolio.model.Portfolio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends CrudRepository<Portfolio, String> {
}
