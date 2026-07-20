Feature: Cart Page interactions

    @regression
    Scenario: Add item to cart
        Given the user is logged in
        When the add to cart button is clicked for 'Sauce Labs Backpack'
        And the cart icon is clicked
        Then the item should be displayed on the cart page

    @regression
    Scenario: Remove item from cart
        Given the user is logged in
        When the add to cart button is clicked for 'Sauce Labs Backpack'
        And the cart icon is clicked
        And the remove button is clicked
        Then the item should be removed from the cart page

    @regression
    Scenario: Verify item count
        Given the user is logged in
        When the add to cart button is clicked for 'Sauce Labs Backpack'
        And the add to cart button is clicked for 'Sauce Labs Bike Light'
        And the cart icon is clicked
        Then there should be two items on the cart page