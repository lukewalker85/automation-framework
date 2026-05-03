Feature: User Login

    Scenario: Successful login
        Given the user is on the login page
        When they login with valid credentials
        Then they should be redirected to the inventory page

    Scenario: Failed login
        Given the user is on the login page
        When they login with locked out credentials
        Then they should see a locked out error message

    Scenario: Invalid password
        Given the user is on the login page
        When they login with invalid credentials
        Then they should see an invalid credentials error message

    Scenario: Logout
        Given the user is logged in
        When the logout sidebar link is clicked
        Then they should be on the login page

    Scenario: Direct URL access
        Given the user is on the login page
        When they navigate to the inventory URL
        Then they should be on the login page