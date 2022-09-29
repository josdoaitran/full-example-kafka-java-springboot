# UserStory :
Feature: Customer can input user and phone to get account info

  Scenario Outline: Customer is able to get correct information by phone
    Given Clear User information in User Service by phone <Phone>
    And UserID has name <Name> phone <Phone> and status <UserStatus> in Signup User Service
    When  Request to get User information by phone <Phone>
    Then TestCase <Testcase>: I expect API get User by phone <Phone> will return name <Name> and status <UserStatus>

    Examples:
      | Testcase | Name       | Phone      | UserStatus |
      | 1        | Doai Tran  | 0906973152 |OPEN       |
      | 2        | Titi Taka  | 0906973153 |PENDING    |
      | 3        | Roger Titi | 0906973156 |BLOCKED    |
