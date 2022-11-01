Feature: Form Authentication

  @ui-testing @form-authentication
  Scenario: Login success and Logout
    Given [ui] form-authentication page is opened
    And [ui] set username to 'tomsmith'
    And [ui] set password to 'SuperSecretPassword!'
    And [ui] click on login button
    And [ui] verify 'logged-in' url contains:'/secure'
    Then [ui] verify 'login' message:'You logged into a secure area!'
    And [ui] click on logout button
    And [ui] verify 'logged-in' url contains:'/login'
    Then [ui] verify 'logout' message:'You logged out of the secure area!'

  @ui-testing @form-authentication
  Scenario Outline: Unsuccessful login
    Given [ui] form-authentication page is opened
    And [ui] set username to '<username>'
    And [ui] set password to '<password>'
    And [ui] click on login button
    Then [ui] verify 'login' message:'<message>'
    And [ui] verify 'page' url contains:'/login'

    Examples:
    |username  |password   |message                           |
    |tomsmith  |badpassword|Your password is invalid!         |
    |tomsmith2 |SuperSecretPassword!|Your username is invalid!|



