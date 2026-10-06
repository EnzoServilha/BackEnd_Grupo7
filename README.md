# BackEnd_Grupo7
BackEnd do projeto de extensão

## Docker com H2 (testes e demonstração)

Execute na raiz do projeto:

```powershell
docker build -t backend-grupo7 -f Docker/Dockerfile .
docker run -d --name backend-grupo7 -p 8080:8080 -e SPRING_PROFILES_ACTIVE=h2 backend-grupo7
docker logs -f backend-grupo7
```

A API fica disponível em `http://localhost:8080`; Swagger em
`http://localhost:8080/swagger-ui/index.html`.

- O perfil `h2` cria um banco em memória e carrega os dados de demonstração de `data.sql`.
- Os dados são perdidos e recriados ao reiniciar a aplicação. Não use esse perfil em produção.
- O console H2 fica desabilitado. Não é necessário instalar MySQL para esse perfil.
- Sem `SPRING_PROFILES_ACTIVE=h2`, a configuração padrão usa MySQL e as variáveis `DB_*`.
- `-f Docker/Dockerfile .` é usado apenas no build, não no comando de execução.
- Para ver todos os containers, use `docker ps -a`. Para reiniciar o existente, use
  `docker start backend-grupo7` em vez de criar outro com o mesmo nome.