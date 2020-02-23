function loadPortfolioContent(isInfo) {
    const id = getIdFromUrl();
    callPortfolioEndpoint(id, isInfo);
}

function getIdFromUrl() {
    const urlParameters = new URLSearchParams(window.location.search);
    return urlParameters.get('id');
}

function callPortfolioEndpoint(id, isInfo) {
      const response = fetch('user_info/' + id).then(function(response) {
          if (response.ok) {
            response.json().then(function(jsonResponse) {
                  if (isInfo)
                    fillPortfolioDataInfoHtml(jsonResponse);
                  else
                    fillPortfolioDataUpdateHtml(jsonResponse)
            })
          } else  {
            response.json().then(function(jsonResponse) {
                alert("Error: " + jsonResponse.Error + " Message: " + jsonResponse.message
                    + " Status: " +  jsonResponse.status + " Timestamp: " + jsonResponse.timestamp);
                document.getElementById("updateButton").style.display = "none";
            })
          }
       })
}

function fillPortfolioDataInfoHtml(portfolioJson) {
    document.getElementById("image").src= portfolioJson.imageUrl;
    document.getElementById('username').innerHTML =  portfolioJson.twitterUserName;
    document.getElementById('description').innerHTML =  portfolioJson.description;
    document.getElementById('title').innerHTML =  portfolioJson.title;

    if (portfolioJson.tweets.length > 0) {
      var count = 1;
      var tweetsHtml = '';
      portfolioJson.tweets.forEach(element => tweetsHtml += count++ + ". " + element + "<br>")
      document.getElementById('tweets').innerHTML =  tweetsHtml;
    } else {
       document.getElementById('tweets').innerHTML =  "";
    }
}

function fillPortfolioDataUpdateHtml(portfolioJson) {
    document.getElementById("idportfolio").value = portfolioJson.idPortfolio;
    document.getElementById("image").value = portfolioJson.imageUrl;
    document.getElementById('username').value =  portfolioJson.twitterUserName;
    document.getElementById('description').value =  portfolioJson.description;
    document.getElementById('title').value =  portfolioJson.title;
}

function updatePortfolioData() {
    var portfolio = new Object();

    portfolio.idPortfolio = document.getElementById("idportfolio").value;
    portfolio.imageUrl = document.getElementById("image").value;
    portfolio.twitterUserName = document.getElementById('username').value;
    portfolio.description = document.getElementById('description').value;
    portfolio.title = document.getElementById('title').value;

    const portfolioJson = JSON.stringify(portfolio);

    const postUpdates = async () => {
      const response = await fetch('modify_user_info/' + portfolio.idPortfolio, {
        method: 'POST',
        body: portfolioJson,
        headers: {
          'Accept': 'application/json, text/plain, */*',
          'Content-Type': 'application/json'
        }
      });
      const responseJson = await response.json();
      if (responseJson.Error === undefined && responseJson.error === undefined) {
        location.href = "portfolio-info.html?id=" + portfolio.idPortfolio;
      } else {
        alert("Error: " + responseJson.Error + " Message: " + responseJson.message
                    + " Status: " +  responseJson.status + " Timestamp: " + responseJson.timestamp);
      }

    }
    postUpdates();
}