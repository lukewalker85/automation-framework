Feature: Inventory page interactions

    Scenario: Product is displayed
        Given the user is logged in 
        Then a product is displayed

    Scenario Outline: Sorting products
        Given the user is logged in
        When the filter is changed to '<sort_option>'
        Then the first item is '<first_item>' and the last item is '<last_item>'

        Examples: 
            | sort_option | first_item | last_item |
            | Name (A to Z) | Sauce Labs Backpack | Test.allTheThings() T-Shirt (Red) |
            | Name (Z to A) | Test.allTheThings() T-Shirt (Red) | Sauce Labs Backpack |
            | Price (low to high) | Sauce Labs Onesie | Sauce Labs Fleece Jacket |
            | Price (high to low) | Sauce Labs Fleece Jacket | Sauce Labs Onesie |

    Scenario: Cart badge is updated
        Given the user is logged in
        When an item is added to the Cart
        Then the cart badge displays the number of items in it
