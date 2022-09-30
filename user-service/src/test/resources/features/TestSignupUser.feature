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
      | 10        | Doai Tran | 0906973151 | OPEN       |

  Scenario Outline: Test User Service consume message from Fraud Service
    Given Clear User information in User Service by Phone = <phone>
    And UserID = <id> has Name = <name> Phone = <phone> and Status = <olduserStatus> in User Service

    And Prepare producer message Topic = UPDATE_USER_INFO_TOPIC
    When There is a new Update User Status message to Topic UPDATE_USER_INFO_TOPIC Id = <id> Phone = <phone> Name = <name> Status = <newStatus> from Fraud Service

    And I expect User Service consume update user message Id = <id> Phone = <phone> Name = <name> Status = <newStatus>
    When Request to get User information by Phone = <phone>
    Then TestCase <Testcase>: I expect API get User by Phone = <phone> will return Name = <name> and Status = <newStatus>

    Examples:
      | Testcase | id | name      | phone      | olduserStatus | newStatus |
      | 1        | 1  | Doai Tran | 0906973152 | OPEN          | PENDING   |