# UserStory :
Feature: Customer can input user and phone to get account info

  Scenario Outline: Customer is able to get correct information by phone
    Given Clear User information in User Service by Phone = <phone>
    And UserID has Name = <name> Phone = <phone> and Status = <userStatus> in User Service
    When  Request to get User information by Phone = <phone>
    Then TestCase <Testcase>: I expect API get User by Phone = <phone> will return Name <name> and Status <userStatus>

    Examples:
      | Testcase | name       | phone      | userStatus |
      | 1        | Doai Tran  | 0906973152 |OPEN       |
      | 2        | Titi Taka  | 0906973153 |PENDING    |
      | 3        | Roger Titi | 0906973154 |BLOCKED    |
