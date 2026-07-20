Feature: Product Details page interactions

    @regression
    Scenario: Clicking product navigates to detail page
        Given the user is logged in
        When the product name and price are stored
        And a product is clicked
        Then they are on the details page for the clicked product

    @regression
    Scenario: Product name and price match between inventory and detail pages
        Given the user is logged in
        When the product name and price are stored
        And a product is clicked
        Then the product name and price match on details page

    @regression
    Scenario: The back button returns to the inventory page
        Given the user is logged in
        When a product is clicked
        And the back button is clicked
        Then they should be redirected to the inventory page