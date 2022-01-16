# REST API for a chat app
# Goals
- [ ] Authentication with Access/Refresh tokens
- [ ] Service layer and repository pattern
- [ ] Redis and PostgreSQL repositories
- [ ] Custom errors
- [ ] Validation
- [ ] Chatting via websockets
- [ ] Logging
- [ ] Tests
- [ ] Configuration
- [ ] Docker


# Endpoints
**/auth**
- **/login** - Authenticates the user. Receives email and password in json, returns access/refresh token pair
- **/register** - Registers the user. Receives user info in json, returns access/refresh token
- **/logout** - Invalidates the access token.
- **/refresh** - Refreshes the access/refresh token pair. Receives bearer refresh token, returns access/refresh token pair.

**/users**
- **/me** - Returns requesting user's info. Receives bearer access token, returns user info.

**/categories**
- **/** - Lists all categories.
- **/{id}** - Returns specified category.