Feature: Checkout Page Interactions

    @smoke
    Scenario: Successful order journey
        Given the user is logged in
        When the product name and price are stored
        And the add to cart button is clicked for 'Sauce Labs Backpack'
        And the cart icon is clicked
        And the checkout button is clicked
        And the shipping details are entered
        And the continue button is clicked
        And the product name and price should match on checkout
        And the finish button is clicked
        Then they should be presented with the order complete message

    @regression
    Scenario: Missing shipping information
        Given the user is logged in
        When the add to cart button is clicked for 'Sauce Labs Backpack'
        And the cart icon is clicked
        And the checkout button is clicked
        And the continue button is clicked
        Then they should see a first name is required error message
