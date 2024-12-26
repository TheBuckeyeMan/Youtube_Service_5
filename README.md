# Youtube-Service-5
Youtube Service to host application to take created video and post to the Youtube API

# Manual Get Refresh Token
This step is required untill we create a solution to get a new refresh token for oAuth authentication each time we activate the service. 

1. Uncomment RefreshToken.java

2. In serviceTrigger.java, Uncomment the obvject import of RefreshToken

3. Add RefreshToken to the constructors of ServiceTrigger

4. Uncomment step 3 to get the refresh token

5. Comment out the Token properties import and step 4 and 5 to make sure we dont try and post

6. Makwe the build and run the docker container

7. follow the steps in the terminal to get the new refresh token. 

8. Update the Secrets value to the new token value 

9. Your new access token should work for a period of atleast a week. 

