# UserStory :
Feature: Customer can input user and phone to sign-up

  Scenario Outline: Customer is able to sign-up
    Given Prepare consumer listen Topic = CREATE_NEW_USER_TOPIC
    And Clear User information in User Service by Phone = <phone>
    When User signup with Name = <name> Phone = <phone>
    Then TestCase <Testcase>: I expect response message contain Phone = <phone> Name = <name> Status = <userStatus>
    And I expect a new message Kafka topic CREATE_NEW_USER_TOPIC Phone = <phone> Name = <name> Status = <userStatus>

    Examples:
      | Testcase | name      | phone      | userStatus |
      | 1        | Doai Tran | 0906973152 | OPEN       |

