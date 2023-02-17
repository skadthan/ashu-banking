Feature: Check if new users can be registered

@Regression
Scenario Outline: Simple user registration
	Given User registered with details <firstName>, <lastName>, <phone>, <email>, <username> and <password>
	When User logged in after registration with <username> and <password> 
	Then Check if user details <firstName>, <lastName>, <phone>, <email>, <username> and <password> are properly stored in DB

	Examples:
		|firstName|lastName|phone     |email        |username|password|
		|Suresh     |Kadthan   |1111111111|skadthan@gmail.com|suresh    |suresh    |
		|Ashwath      |Kadthan   |2222222222|akadthan@gmail.com  |ashwath     |ashwath     |
		|John     |Doe   |3333333333|john@doe.com|john    |john    |
