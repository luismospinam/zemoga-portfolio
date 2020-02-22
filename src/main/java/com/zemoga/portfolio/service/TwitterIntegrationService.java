package com.zemoga.portfolio.service;

import java.util.List;

public interface TwitterIntegrationService {
    List<String> getLastTweetsFromUser(String user);
}
