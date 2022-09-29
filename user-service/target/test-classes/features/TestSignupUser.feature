# UserStory :
Feature: Customer can input user and phone to sign-up

  Scenario Outline: Customer is able to sign-up
    Given Clear User information in User Service by phone <Phone>
    When User signup with name <Name> phone <Phone>
    Then TestCase <Testcase>: I expect response message contain phone <Phone> name <Name> status <UserStatus>
    And I expect a new message Kafka topic CREATE_NEW_USER_TOPIC phone <Phone> name <Name> status <UserStatus>

    Examples:
      | Testcase | Name       | Phone      | UserStatus |
      | 1        | Doai Tran  | 0906973152 |OPEN       |

