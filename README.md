# Book Catalog API
Uma API REST desenvolvida com Spring Boot para gerenciar um catálogo de livros.
Permite criar, listar, buscar, atualizar e excluir livros, garantindo regras como ISBN único e validações de dados.

## Tecnologias Utilizadas
- Java 17+
- Spring Boot 
- Spring Web 
- Spring Data JPA 
- H2 Database
- JUnit 5 e MockMvc
- Gradle

## Pré-requisitos
- Java 17+
- Gradle ou usar Gradle Wrapper

## Como instalar e executar
### 1. Clonar o repositório

```bash
git clone https://github.com/SEU_USUARIO/book-catalog.git
cd book-catalog
```

### 2. Executar o projeto
Usando o Gradle Wrapper

```bash
./gradlew bootRun    # Linux/Mac
gradlew bootRun      # Windows
```

### 3. Acessar a API
O servidor inicia por padrão em:

```bash
http://localhost:8080
```

## Executando os testes
Use: 
```bash
./gradlew test    # Linux/Mac
gradlew test      # Windows
```

Se tudo estiver correto, verá:
```
BUILD SUCCESSFUL
```

## Endpoints disponíveis
```
Books(/api/books)
```

| Método | Endpoint          | Descrição              |
| ------ | ----------------- | ---------------------- |
| POST   | `/api/books`      | Criar um livro         |
| GET    | `/api/books`      | Listar todos os livros |
| GET    | `/api/books/{id}` | Buscar livro por ID    |
| PUT    | `/api/books/{id}` | Atualizar livro        |
| DELETE | `/api/books/{id}` | Remover livro          |

## Exemplo de JSON

```bash
{
  "title": "Title",
  "author": "ISBN",
  "isbn": "xxxxxx",
  "year": 2008,
  "quantity": 5
}
```

## Regras de negócio
- ISBN é obrigatório e único
- Não é permitido alterar o ISBN durante update
- Quantidade deve ser >= 0

## Banco de Dados
A aplicação utiliza H2 em modo memória por padrão
Console disponível em:
``` 
http://localhost:8080/h2-console
```
Configuração default:
- JDBC URL: jdbc:h2:mem:booksdb
- User: sa
- Password: (vazio)