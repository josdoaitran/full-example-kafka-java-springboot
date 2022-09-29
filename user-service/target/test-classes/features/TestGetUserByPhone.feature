## UserStory :
#Feature: Customer can input user and phone to get account info
#  @UserConsumer
#  Scenario Outline: Customer is able to get correct information by phone
#    Given Clear User information in User Service by Phone '<phone>'
#    And UserID has Name "<Name>" Phone "<phone>" and Status "<status>" in Signup User Service
#    When Request to get User information by Phone "<phone>"
#    Then I expect API get User by Phone "<phone>" will return Name "<name>" and Status "<status>"
#
#    Examples:
#      | Testcase | Name       | phone        | status |
#      | 1        | Doai Tran  | +27906973152 | OPEN       |
#      | 2        | Titi Taka  | +27906973153   | PENDING    |
#      | 3        | Roger Titi | +27906973156   | BLOCKED    |
#
#  Scenario: Creating a new user should publish a message to the topic
#    When A user with id="user-1" and name "Max Mustermann" is created
#    Then The user with id="user-1" and name "Max Mustermann" should be written on the topic
