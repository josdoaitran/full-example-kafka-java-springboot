## UserStory :
#Feature: Customer can input user and phone to sign-up
#
#  @UserConsumer
#  Scenario: Customer is able to sign-up
#    Given Clear User information in User Service by Phone '+27906973152'
#    When User signup with Name "Doai Tran" Phone "+27906973152"
#    Then I expect response message contain Phone "+27906973152" Name "Doai Tran" Status "OPEN"
#    And I expect a new message Kafka topic "CREATE_NEW_USER_TOPIC" Phone "<+27906973152" Name "Doai Tran" Status "OPEN"
#
