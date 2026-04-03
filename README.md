# Todo Cine Webflux

## What is Todo Cine

Todo Cine is a web app to manage movies from [TMDB](https://www.themoviedb.org/) 

## Code

Todo Cine Webflux is Webflux reactive version of [Todo Cine](https://github.com/abeltran10/todocine_backend) developed with Spring Webflux to make it reactive. It's NoSQL database with MongoDB.


application.properties loads properties from three files, one per environment (prod, dev, test) and Constants file is missing in order you can create it for your need


## Install

- Clone [Todo Cine Webflux](https://github.com/abeltran10/todo_cine_webflux) 
- Add application.properties and Constants.java files to project
- Execute [mvn clean install] command and deploy .jar file generated

## API Version: v1.0.0
API managed with Spring Boot, JWT security, and movie catalog with custom exception handling.

### Available authorizations
#### BearerAuth (HTTP, bearer)
Bearer format: JWT

---

### [POST] /usuarios
**Insert a new user**

#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [UsuarioReqDTO](#usuarioreqdto)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 201 | Created - User registered successfully. | **application/json**: [UsuarioDTO](#usuariodto)<br> |
| 400 | Invalid data. |  |
| 409 | Resource already exists. |  |

##### Security

| Security Schema | Scopes |
| --------------- | ------ |
| BearerAuth |  |

### [GET] /usuarios/{id}
**Get user by ID**

#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path | Unique user ID | Yes | long |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK - User found. | **application/json**: [UsuarioDTO](#usuariodto)<br> |
| 400 | Invalid data. |  |
| 403 | Access denied. |  |
| 404 | Not found. |  |

##### Security

| Security Schema | Scopes |
| --------------- | ------ |
| BearerAuth |  |

### [PUT] /usuarios/{id}
**Update user**

#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path | Unique user ID | Yes | long |

#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [UsuarioReqDTO](#usuarioreqdto)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK - User updated. | **application/json**: [UsuarioDTO](#usuariodto)<br> |
| 400 | Invalid data. |  |
| 403 | Access denied. |  |
| 404 | Not found. |  |

##### Security

| Security Schema | Scopes |
| --------------- | ------ |
| BearerAuth |  |

---

### [GET] /usuarios/{userId}/movies
**Get user movies (Paginated)**

#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| userId | path | User ID | Yes | long |
| vista | query | Filter by watched movies (true/false) | Yes | string |
| votada | query | Filter by voted movies | Yes | string |
| orderBy | query | Sorting field (e.g., popularity) | Yes | string |
| page | query | Requested page number | Yes | integer |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK - Paginated list retrieved. | **application/json**: [Paginator](#paginator)<br> |
| 400 | Invalid data. |  |
| 403 | Access denied. |  |

##### Security

| Security Schema | Scopes |
| --------------- | ------ |
| BearerAuth |  |

### [PUT] /usuarios/{userId}/movies/{movieId}
**Update movie status for a user**

#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| userId | path | User ID | Yes | long |
| movieId | path | Movie ID | Yes | long |

#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [UsuarioMovieDTO](#usuariomoviedto)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK - Movie status updated. | **application/json**: [MovieDetailDTO](#moviedetaildto)<br> |
| 400 | Invalid data. |  |
| 403 | Access denied. |  |
| 404 | Not found. |  |
| 502 | External server error. |  |

##### Security

| Security Schema | Scopes |
| --------------- | ------ |
| BearerAuth |  |

---

### [GET] /movies
**List movies with filters (Paginated)**

#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| name | query | Movie title | Yes | string |
| status | query | Status (Released, Post-Production...) | Yes | string |
| region | query | Region code (ES, US...) | Yes | string |
| page | query | Page number | Yes | integer |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | Filtered movie list. | **application/json**: [Paginator](#paginator)<br> |
| 400 | Invalid data. |  |
| 502 | External server error. |  |

##### Security

| Security Schema | Scopes |
| --------------- | ------ |
| BearerAuth |  |

### [GET] /movies/{id}
**Get full movie detail by ID**

#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path | Movie ID | Yes | long |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | Movie detail. | **application/json**: [MovieDetailDTO](#moviedetaildto)<br> |
| 400 | Invalid data. |  |
| 404 | Not found. |  |
| 502 | External server error. |  |

##### Security

| Security Schema | Scopes |
| --------------- | ------ |
| BearerAuth |  |

---

### [GET] /ganadores
**Get winners by award and year**

#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| premioId | query | Award ID | Yes | long |
| anyo | query | Award year | Yes | integer |
| pagina | query | Results page | Yes | integer |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | List of winners retrieved. | **application/json**: [Paginator](#paginator)<br> |
| 400 | Invalid data. |  |

##### Security

| Security Schema | Scopes |
| --------------- | ------ |
| BearerAuth |  |

### [POST] /ganadores
**Insert a new winner**

Requires ADMIN role.

#### Request Body

| Required | Schema |
| -------- | ------ |
|  Yes | **application/json**: [GanadorReqDTO](#ganadorreqdto)<br> |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 201 | Winner registered. | **application/json**: [GanadorDTO](#ganadordto)<br> |
| 400 | Invalid data. |  |
| 403 | Access denied. |  |
| 409 | Resource already exists. |  |
| 502 | External server error. |  |

##### Security

| Security Schema | Scopes |
| --------------- | ------ |
| BearerAuth |  |

---

### [GET] /premios
**Get awards**

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | List of awards. | **application/json**: [ [PremioDTO](#premiodto) ]<br> |
| 400 | Invalid data. |  |
| 403 | Access denied. |  |

##### Security

| Security Schema | Scopes |
| --------------- | ------ |
| BearerAuth |  |

---

### [GET] /premios/{id}
**Get specific award**

Get award by unique identifier

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK-Award. | **application/json**: [PremioDTO](#premiodto)<br> |
| 400 | Invalid data. |  |
| 403 | Access denied. |  |
| 404 | Not found. |  |

##### Security

| Security Schema | Scopes |
| --------------- | ------ |
| BearerAuth |  |

---

### [GET] /premios/{id}/categorias
**Get categories for a specific award**

Requires ADMIN role.

#### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ------ |
| id | path | Award ID (e.g., Oscars) | Yes | long |

#### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | List of categories. | **application/json**: [ [CategoriaDTO](#categoriadto) ]<br> |
| 400 | Invalid data. |  |
| 403 | Access denied. |  |

##### Security

| Security Schema | Scopes |
| --------------- | ------ |
| BearerAuth |  |

---
### Schemas

#### UsuarioReqDTO

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | long | Unique identifier | No |
| username | string | Access username | Yes |
| password | string (string) | Access password | Yes |
| rol | string | Assigned role (USER/ADMIN) | No |

#### UsuarioDTO

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | long | Unique identifier | No |
| username | string | Access username | Yes |
| rol | string | Assigned role (USER/ADMIN) | Yes |

#### Paginator

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| page | integer | Current page | Yes |
| results | [  ] | List of results | No |
| total_pages | integer | Total pages | Yes |
| total_results | integer | Total records | Yes |

#### MovieDetailDTO

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | long | Movie ID | Yes |
| original_title | string | Original title | Yes |
| title | string | Translated title | Yes |
| poster_path | string | Poster image path | Yes |
| overview | string | Summary or synopsis | No |
| release_date | string | Release date | Yes |
| popularity | double | Popularity index | Yes |
| vote_count | integer | Total external votes | Yes |
| vote_average | double | Average external score | Yes |
| original_language | string | Original language | Yes |
| favoritos | boolean | Is marked as favorite by user | No |
| voto | double | User's personal vote | No |
| vista | boolean | Already watched by user | No |
| total_votos_TC | integer | Total votes in Todo Cine | Yes |
| votos_media_TC | double | Average score in Todo Cine | Yes |
| genres | [ [GenreDTO](#genredto) ] | Genre list | Yes |
| videos | [ [VideoDTO](#videodto) ] | Trailers or video list | No |

#### GenreDTO

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | string | Genre ID | Yes |
| name | string | Genre name | No |

#### VideoDTO

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | string | Video identifier | Yes |
| name | string | Video name | Yes |
| key | string | Platform key (e.g., YouTube ID) | Yes |
| site | string | Hosting site | Yes |
| type | string | Video type (Trailer, Teaser) | Yes |

#### UsuarioMovieDTO

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| usuarioId | long | User ID | Yes |
| movieId | long | Movie ID | Yes |
| favoritos | boolean | Favorite status | Yes |
| vista | boolean | Watch status | Yes |
| voto | double | Personal score | No |

#### MovieDTO

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | long | Unique ID | Yes |
| original_title | string | Original title | Yes |
| title | string | Title | Yes |
| poster_path | string | Image path | Yes |
| overview | string | Synopsis | No |
| release_date | string | Release date | Yes |
| popularity | double | Popularity | No |
| vote_count | integer | Total votes | No |
| vote_average | double | Average score | No |
| original_language | string | Language | No |
| genres | [ [GenreDTO](#genredto) ] | Associated genres | No |
| videos | [ [VideoDTO](#videodto) ] | Associated videos | No |
| total_votos_TC | integer | Local votes | No |
| votos_media_TC | double | Local average | No |

#### GanadorReqDTO

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| premioId | long | Award ID | Yes |
| categoriaId | long | Category ID | Yes |
| anyo | integer | Gala year | Yes |
| movieId | long | Movie ID | Yes |

#### GanadorDTO

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| premioId | long | Award ID | Yes |
| premio | string | Award name | No |
| categoriaId | long | Category ID | Yes |
| categoria | string | Category name | No |
| anyo | integer | Gala year | Yes |
| movieId | long | Movie ID | Yes |
| original_title | string | Original title | No |
| title | string | Title | No |
| poster_path | string | Poster image | No |
| overview | string | Synopsis | No |
| release_date | string | Release date | No |

#### CategoriaDTO

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | long | Unique category ID | Yes |
| nombre | string | Category description | Yes |

#### PremioDTO

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| id | long | Unique award ID | Yes |
| titulo | string | Award name | Yes |
| anyos | [ integer ] | Award years | No |


## Database schema

### Movies 

```json
{
  "_schema": "MovieDocument",
  "version": "1.0",
  "collection": "movies",
  "properties": {
    "_id": {
      "bsonType": "long",
      "description": "Unique identifier for the movie (TMDB ID)"
    },
    "originalTitle": {
      "bsonType": "string",
      "description": "The title of the movie in its original language"
    },
    "title": {
      "bsonType": "string",
      "description": "The localized or translated title of the movie"
    },
    "posterPath": {
      "bsonType": "string",
      "description": "Relative URL path to the movie poster image",
      "pattern": "^/.*\\.(jpg|png)$"
    },
    "overview": {
      "bsonType": "string",
      "description": "A brief plot summary or description of the movie"
    },
    "releaseDate": {
      "bsonType": "string",
      "format": "date",
      "description": "The official theatrical release date (ISO 8601)"
    },
    "popularity": {
      "bsonType": "double",
      "description": "Popularity score generated by the external API"
    },
    "voteCount": {
      "bsonType": "int",
      "description": "Total number of votes received from the external API"
    },
    "voteAverage": {
      "bsonType": "double",
      "description": "Average rating score from the external API"
    },
    "originalLanguage": {
      "bsonType": "string",
      "maxLength": 2,
      "description": "ISO 639-1 code for the original language"
    },
    "totalVotosTC": {
      "bsonType": "int",
      "defaultValue": 0,
      "description": "Total number of internal platform user votes"
    },
    "votosMediaTC": {
      "bsonType": "double",
      "defaultValue": 0.0,
      "description": "Average rating based on internal platform users"
    },
    "premios": {
      "bsonType": "array",
      "description": "Embedded list of accolades and awards (1:N Denormalized Relation)",
      "items": {
        "bsonType": "object",
        "required": ["premioId", "categoriaId", "anyo"],
        "properties": {
          "premioId": {
            "bsonType": "long",
            "description": "Reference ID to the specific award ceremony"
          },
          "titulo": {
            "bsonType": "string",
            "description": "Name of the ceremony (e.g., Oscars, Goya, Cannes)"
          },
          "categoriaId": {
            "bsonType": "long",
            "description": "Reference ID to the specific award category"
          },
          "categoria": {
            "bsonType": "string",
            "description": "Name of the winning category (e.g., Best Picture)"
          },
          "anyo": {
            "bsonType": "int",
            "description": "The year the award was granted"
          }
        }
      }
    }
  }
}

```

### Categoria-Premio

```json


  "_schema": "AwardCategoryDocument",
  "version": "1.0",
  "collection": "categoria_premio",
  "properties": {
    "_id": {
      "bsonType": "objectId",
      "description": "Unique MongoDB internal identifier"
    },
    "premioId": {
      "bsonType": "long",
      "description": "Unique business ID for the award ceremony (referenced in movies.awards.awardId)"
    },
    "titulo": {
      "bsonType": "string",
      "description": "Name of the award ceremony (e.g., Goya, Oscars)"
    },
    "categorias": {
      "bsonType": "array",
      "description": "List of specific categories belonging to this award (1:N Relation)",
      "items": {
        "bsonType": "object",
        "required": ["id", "nombre"],
        "properties": {
          "id": {
            "bsonType": "long",
            "description": "Unique ID for the specific category within this award"
          },
          "nombre": {
            "bsonType": "string",
            "description": "Descriptive name of the category (e.g., Best Film, Best Director)"
          }
        }
      }
    }
  }
}

```
### Usuario

```json

{
  "_schema": "UserDocument",
  "version": "1.0",
  "collection": "usuarios",
  "properties": {
    "_id": {
      "bsonType": "objectId",
      "description": "Unique MongoDB internal identifier (Primary Key)"
    },
    "username": {
      "bsonType": "string",
      "description": "Unique login name for the user (should be indexed)"
    },
    "password": {
      "bsonType": "string",
      "description": "BCrypt hashed password string"
    },
    "accountNonExpired": {
      "bsonType": "bool",
      "description": "Spring Security flag: true if the account is still valid"
    },
    "accountNonLocked": {
      "bsonType": "bool",
      "description": "Spring Security flag: true if the account is not suspended"
    },
    "credentialsNonExpired": {
      "bsonType": "bool",
      "description": "Spring Security flag: true if the password hasn't expired"
    },
    "enabled": {
      "bsonType": "bool",
      "description": "Spring Security flag: true if the user is active"
    },
    "rol": {
      "bsonType": "string",
      "description": "Assigned security role (e.g., 'ADMIN', 'USER')"
    }
  }
}

```

### Usuario-Movie

```json

{
  "_schema": "UserMovieInteractionDocument",
  "version": "1.0",
  "collection": "usuario_movie",
  "properties": {
    "_id": {
      "bsonType": "string",
      "description": "Unique MongoDB internal identifier (String UUID or ObjectId)"
    },
    "usuarioId": {
      "bsonType": "string",
      "description": "Reference to the User ID (Indexed for fast lookups of user libraries)"
    },
    "movieId": {
      "bsonType": "long",
      "description": "Reference to the Movie ID (TMDB ID from the movies collection)"
    },
    "favoritos": {
      "bsonType": "string",
      "description": "Flag or category indicating if the movie is in the user's favorites list"
    },
    "vista": {
      "bsonType": "string",
      "description": "Status indicating if the movie has been watched (e.g., 'YES', 'NO', 'PENDING')"
    },
    "voto": {
      "bsonType": "double",
      "description": "Numerical score given by the user to this specific movie"
    }
  }
}

```


