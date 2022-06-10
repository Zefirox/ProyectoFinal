CREATE TABLE UserApp(
                        email varchar PRIMARY KEY,
                        password varchar,
                        nombre varchar,
                        role varchar DEFAULT null
);

CREATE TABLE Collection (
                            id SERIAL PRIMARY KEY,
                            nombre VARCHAR,
                            description varchar,
                            category varchar,
                            userApp varchar,
                            FOREIGN KEY (userApp)
                                references UserApp (email)
);

CREATE TABLE WalletHistory(
                              id SERIAL PRIMARY KEY,
                              userApp varchar,
                              FOREIGN KEY (userApp)
                                  references UserApp (email),
                              type varchar,
                              fcoins float,
                              registeredAt timestamp

);

CREATE TABLE Art(
                    id SERIAL PRIMARY KEY,
                    nombre varchar,
                    price float,
                    imagePath varchar,
                    forSale boolean,
                    collection int,
                    FOREIGN KEY (collection)
                        references Collection (id)

);

CREATE TABLE Ownership(
                          id serial PRIMARY KEY,
                          art int,
                          FOREIGN KEY (art)
                              references Art (id),
                          userApp varchar,
                          FOREIGN KEY (userApp)
                              references UserApp (email),
                          registeredAt timestamp
);

CREATE TABLE Likes (
                       id SERIAL PRIMARY KEY,
                       art int,
                       userApp varchar,
                       FOREIGN KEY (art)
                           references Art (id),
                       FOREIGN KEY (userApp)
                           references UserApp (email),
                       registeredAt timestamp

);