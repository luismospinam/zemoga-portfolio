package com.zemoga.portfolio.service;

import com.zemoga.portfolio.Exception.CustomPortfolioException;
import com.zemoga.portfolio.Exception.PortfolioNotFoundException;
import com.zemoga.portfolio.TestHelper;
import com.zemoga.portfolio.model.Portfolio;
import com.zemoga.portfolio.repository.PortfolioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceImplTest {

    @Spy
    @InjectMocks
    private PortfolioServiceImpl portfolioService;

    @Mock
    private PortfolioRepository portfolioRepository;


    @Test
    void findPortfolioByIdNullShouldFail() {
        CustomPortfolioException expectedException = Assertions.assertThrows(CustomPortfolioException.class,
                () -> portfolioService.findPortfolioById(null),
                "Portfolio search with Null Id should have thrown an exception");

        Assertions.assertNotNull(expectedException.getMessage(), "Exception message should NOT be null");
    }

    @Test
    void findPortfolioByIdNotValidNumberShouldFail() {
        CustomPortfolioException expectedException = Assertions.assertThrows(CustomPortfolioException.class,
                () -> portfolioService.findPortfolioById("not-valid-number"),
                "Portfolio search with not numeric Id should have thrown an exception");

        Assertions.assertNotNull(expectedException.getMessage(), "Exception message should NOT be null");
    }

    @Test
    void findPortfolioByIdZeroShouldFail() {
        CustomPortfolioException expectedException = Assertions.assertThrows(CustomPortfolioException.class,
                () -> portfolioService.findPortfolioById("0"),
                "Portfolio search with Id as 0 should have thrown an exception");

        Assertions.assertNotNull(expectedException.getMessage(), "Exception message should NOT be null");
    }

    @Test
    void findPortfolioByIdNegativeShouldFail() {
        CustomPortfolioException expectedException = Assertions.assertThrows(CustomPortfolioException.class,
                () -> portfolioService.findPortfolioById("-1"),
                "Portfolio search with negative Id should have thrown an exception");

        Assertions.assertNotNull(expectedException.getMessage(), "Exception message should NOT be null");
    }

    @Test
    void findPortfolioByIdNotExistingShouldFail() {
        String id = "1";
        Mockito.when(portfolioRepository.findById(id)).thenReturn(Optional.empty());

        PortfolioNotFoundException expectedException = Assertions.assertThrows(PortfolioNotFoundException.class,
                () -> portfolioService.findPortfolioById(id),
                "Portfolio search with not existing Id should have thrown an exception");

        Assertions.assertNotNull(expectedException.getMessage(), "Exception message should NOT be null");
    }

    @Test
    void findPortfolioByIdSucceed() {
        String id = "1";
        Portfolio expectedPortfolio = TestHelper.createDummyPortfolio(id);
        Mockito.when(portfolioRepository.findById(id)).thenReturn(Optional.of(expectedPortfolio));

        Portfolio response = portfolioService.findPortfolioById(id);

        Assertions.assertNotNull(response, "A valid and existing portfolio id should not return null.");
        Assertions.assertAll(
                () -> Assertions.assertEquals(response.getIdPortfolio(), response.getIdPortfolio()),
                () -> Assertions.assertEquals(response.getTwitterUserName(), response.getTwitterUserName()),
                () -> Assertions.assertEquals(response.getImageUrl(), response.getImageUrl()),
                () -> Assertions.assertEquals(response.getDescription(), response.getDescription()),
                () -> Assertions.assertEquals(response.getTitle(), response.getTitle())
        );
    }

    @Test
    void updatePortfolioShouldValidateIdExistCallingFindMethod() {
        String id = "1";
        Portfolio portfolio = TestHelper.createDummyPortfolio(id);
        Mockito.when(portfolioRepository.findById(id)).thenReturn(Optional.of(portfolio));

        portfolioService.updatePortfolio(portfolio, id);

        Mockito.verify(portfolioService, Mockito.times(1)).findPortfolioById(id);
    }

    @Test
    void updatePortfolioWithNullIdShouldFail() {
        String id = null;
        Portfolio portfolio = TestHelper.createDummyPortfolio(id);

        CustomPortfolioException expectedException = Assertions.assertThrows(CustomPortfolioException.class,
                () -> portfolioService.updatePortfolio(portfolio, id),
                "Portfolio update with null Id should have thrown an exception");

        Assertions.assertNotNull(expectedException.getMessage(), "Exception message should NOT be null");
        Mockito.verify(portfolioService, Mockito.times(0)).findPortfolioById(id);
    }

    @Test
    void updatePortfolioWithNullPortfolioShouldFail() {
        String id = "1";
        Portfolio portfolio = null;

        CustomPortfolioException expectedException = Assertions.assertThrows(CustomPortfolioException.class,
                () -> portfolioService.updatePortfolio(portfolio, id),
                "Portfolio update with null Id should have thrown an exception");

        Assertions.assertNotNull(expectedException.getMessage(), "Exception message should NOT be null");
        Mockito.verify(portfolioService, Mockito.times(0)).findPortfolioById(id);
    }

    @Test
    void updatePortfolioWithExistingIdSucceed() {
        String id = "1";
        Portfolio portfolio = TestHelper.createDummyPortfolio(id);
        Mockito.when(portfolioRepository.findById(id)).thenReturn(Optional.of(portfolio));
        Mockito.when(portfolioRepository.save(portfolio)).thenReturn(portfolio);

        portfolioService.updatePortfolio(portfolio, id);

        Mockito.verify(portfolioService, Mockito.times(1)).findPortfolioById(id);
        Mockito.verify(portfolioRepository, Mockito.times(1)).save(portfolio);
    }
}