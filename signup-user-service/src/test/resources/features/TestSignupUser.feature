# UserStory :
Feature: Customer can input user and phone to get account info

  Scenario Outline: Customer is able to get correct
    Given UserID has name <Name> phone <Phone> and status <UserStatus> in Signup User Service
    Then TestCase <Testcase>: I expect API get User by phone <Phone> will return name <Name> and status <UserStatus>
#    And TestCase <Testcase>: I expect a new message Kafka topic <Topic> phone <Phone> username <Name> status <UserStatus>

    Examples:
      | Testcase | Name       | Phone      | UserStatus |Topic |
      | 1        | Doai Tran  | 0906973152 |OPEN       | CREATE_NEW_USER_TOPIC|
      | 2        | Titi Taka  | 0906973153 |PENDING    | CREATE_NEW_USER_TOPIC|
      | 3        | Roger Titi | 0906973156 |BLOCKED    | CREATE_NEW_USER_TOPIC |
