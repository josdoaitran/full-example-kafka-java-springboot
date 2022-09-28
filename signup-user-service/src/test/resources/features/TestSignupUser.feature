Feature: Customer is able to get Available to Spend

  Scenario Outline: Customer is able to get correct
    Given UserID has name <Name> phone <Phone> and status <UserStatus> in Signup User Service
    Then TestCase <Testcase>: I expect API get User by phone <Phone> will return name <Name> and status <UserStatus>

    Examples:
      | Testcase | Name       | Phone      | UserStatus |
      | 1        | Doai Tran  | 0906973152 |OPEN       |
      | 2        | Titi Taka  | 0906973153 |PENDING    |
      | 3        | Roger Titi | 0906973156 |BLOCKED    |
