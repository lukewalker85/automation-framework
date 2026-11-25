Feature: Accessibility checks

    @accessibility @regression
    Scenario: Login page accessibility
        Given the user is on the login page
        Then the login page should have no blocking accessibility violations

    @accessibility @regression
    Scenario: Inventory page accessibility
        Given the user is logged in
        Then the inventory page should have no blocking accessibility violations

    @accessibility @regression
    Scenario: Cart page accessibility
        Given the user is logged in
        When the add to cart button is clicked for 'Sauce Labs Backpack'
        And the cart icon is clicked
        Then the cart page should have no blocking accessibility violations

    @accessibility @regression
    Scenario: Checkout page accessibility
        Given the user is logged in
        When the add to cart button is clicked for 'Sauce Labs Backpack'
        And the cart icon is clicked
        And the checkout button is clicked
        Then the checkout page should have no blocking accessibility violations
