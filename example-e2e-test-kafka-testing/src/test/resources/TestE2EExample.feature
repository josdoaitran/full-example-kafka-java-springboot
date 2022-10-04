Feature: Customer can input user and phone to sign-up

  Scenario Outline: Customer is able to sign-up
    Given I prepare a listener topic CREATE_NEW_USER_TOPIC
    Given Clear User information in User Service by Phone = <phone>

    When User signup with Name = <name> Phone = <phone>
    Then I expect response message contain StatusCode = 201 Phone = <phone> Name = <name> Status = <oldUserStatus>
    And I expect a new message Kafka topic CREATE_NEW_USER_TOPIC Phone = <phone> Name = <name> Status = <oldUserStatus>
    #
    When User get user information by Phone = <phone>
    Then I expect response message contain StatusCode = 200 Phone = <phone> Name = <name> Status = <newUserStatus>

    And I prepare a listener topic UPDATE_USER_INFO_TOPIC
    And I expect a new message Kafka topic UPDATE_USER_INFO_TOPIC Phone = <phone> Name = <name> Status = <newUserStatus>

    Then Validate User info in User-Service correct Phone = <phone> Name = <name> Status = <newUserStatus>
    And Validate User info in Fraud-Check-Service correct Phone = <phone> Name = <name> Status = <newUserStatus>

    Examples:
      | name      | phone      | oldUserStatus | newUserStatus |
      | Doai Tran | 0906973151 | OPEN          | PENDING       |
      | Doai Tran | 0908973151 | OPEN          | BLOCKED       |