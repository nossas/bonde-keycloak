# BONDE Auth (Keycloak)

Primeiro execute o banco de dados:

```sh
docker compose up -d postgres
```

Após o `postgres` iniciado, execute o keycloak:

```sh
docker compose up -d keycloak
```

Você pode acessar ao `keycloak` em [http://localhost:8181](http://localhost:8181) com usuário `admin` e senha `admin`.