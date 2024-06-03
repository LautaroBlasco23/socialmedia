# Social-Media

I've developed this app to practice my skills as backend developer.

In this app you can create and personalize your user. Also you can create and edit posts or comments, and you can
like other's posts or comments.

You can follow the profiles you want and see their new posts on your home page.

# Technologies

* Java (Spring boot / Gradle)
* PostgreSQL (user database)
* MongoDB (posts and comments databases)
* Jwt and Bcrypt (user's auth)
* React (Frontend)

## Scripts

Inside */utils* folder you can run this script to start your postgresql's Docker database.

```
make database-up
```

To run the App's backend without Docker

```
gradle bootRun
```

To run the app with Docker.

```
// TODO
```

# Entities

## Entities Description

**userAuth** -> responsable por la autenticación del usuario, usado por spring security.

**userProfile** -> Información no sensible del usuario, contiene informacion visible en el perfil del usuario.

**Post** -> posteos generados por el usuario, visibles para todo el mundo.

**Comments** -> comentarios sobre los posteos, o sobre otros comentarios.

## Entities data

UserAuth

* **id** | Long
* **email** | String(Unique)
* **password** | String
* **roles** | String
* **userProfile** | UserProfile/foreign_key
* **isAccountNonExpired** | boolean
* **isAccountNonLocked** | boolean
* **isCredentialsNonExpired** | boolean
* **isEnabled** | boolean

UserProfile

* **id** | Long
* **firstname** | String
* **lastname** | String

Post

* **id** | Long
* **userId** | Long
* **text** | String
* **listOfComments** | Set(Comment)
* **listOfLikes** | Set(UserProfile)

Comment

* **id** | Long
* **userId** | Long
* **text** | Long
* **listOfReplies** | Set(Comment)
* **listOfLikes** | Set(UserProfile)

# API Endpoints

All under */api* path.

## Auth 

all under */auth* path.

* **POST /login** -> put your credentials, and it will generate a jwt for you.
* **POST /register** -> store your user in database.
* **PUT /changepassword** -> change your password.
* **POST /logout** -> invalidate your jwt.
* **POST /refreshtoken** -> get new jwt from your jwt's refresh token.
* **DELETE /delete/me** -> delete my auth and profile.

## Profile

all under */profiles* path.

* **GET "/"**  -> Get all profiles.
* **GET "/me"** -> Get your profile (based on your token).
* **PUT "/modify"** -> Modify your profile (based on your token).

## Post

all under */posts* path.

* **GET "/user/{userId}"** -> Get all posts from specific user.
* **GET "/{postId}"** -> Get specific post.
* **POST "/"** -> Create new post.
* **PUT "/{postId}"** -> Modify post.
* **DELETE "/{postId}"** -> Delete post.

## Comment

all under */comments* path.

* **GET "/post/{postId}"** -> Get all comments from post.
* **GET "/user/{userId}"** -> Get all comments from specific user.
* **PUT "/modify/{commentId}"** -> Modify Comment.
* **DELETE "/delete/{commentId}"** -> Delete comment.
