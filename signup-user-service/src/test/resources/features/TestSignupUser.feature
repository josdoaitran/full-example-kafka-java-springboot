Feature: Customer is able to get Available to Spend

  Scenario Outline: Customer is able to get correct
    Given UserID <userid> has name <name> phone <phone> and status <userstatus> in Signup User Service
    Then TestCase <testcase>: I expect API get UserID <userid> will return name <name> phone <phone> and status <userstatus>

    Examples:
      | testcase | userid  | name    | phone   | userstatus |
      | 1        | 15000.0 | 2999.0  | 0.0     | 100000.0
      | 2        | 500.0   | 2999.0  | 0.0     | 100000.0
      | 3        | 10000.0 | 23000.0 | 10000.0 | 100000.0
