# Sistema de Votação

## Descrição

API REST desenvolvida em Java utilizando Spring Boot, com foco na gestão de sessões de votação em cooperativas. A API permite o cadastro de pautas, abertura de sessões de votação, recepção de votos dos associados e contabilização dos resultados.

---

## Funcionalidades Principais

- **Cadastro de Pautas**: Crie pautas para serem votadas pelos associados.
- **Sessões de Votação**: Abra sessões de votação com um tempo personalizado ou use o padrão de 1 minuto.
- **Registro de Votos**: Associe votos ("Sim" ou "Não") a pautas, garantindo que cada associado vote apenas uma vez por pauta.
- **Resultados**: Consulte o resultado das votações.
- **Validação de CPF**: Integração com serviço de validação de CPF para garantir que apenas associados válidos possam votar.

---

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.4.3**
- **Spring Data JPA**
- **Flyway para migrações de banco de dados**
- **PostgreSQL (Banco de dados)**
- **Swagger para documentação da API**
- **MapStruct para mapeamento de DTOs**
- **Lombok para simplificação do código**
- **JUnit e Spring Test para testes automatizados**
- **Containerização (Docker e Docker Compose)**
---

## Como Executar o Projeto

### Pré-requisitos

- **Java 21**: Instale o JDK 21.
- **Docker**: Instale o Docker e o Docker Compose (opcional, mas recomendado).
- **Maven**: Instale o Maven ou use o Maven Wrapper (`mvnw`) incluído no projeto.

---

### 1. Clonando o Repositório

```bash
git clone https://github.com/yuri-italo/votacao.git
cd votacao
```

---

### 2. Executando com Docker Compose (Recomendado)

O Docker Compose facilita a execução da aplicação e do banco de dados PostgreSQL em containers.

1. Certifique-se de que o Docker está instalado e em execução.
2. Execute o seguinte comando na raiz do projeto:

   ```bash
   docker-compose up --build
   ```

3. A aplicação estará disponível em `http://localhost:8080`.

---

### 3. Executando Localmente (Sem Docker)

1. Certifique-se de que o PostgreSQL está instalado e em execução.
2. Configure as credenciais do banco de dados no arquivo `application.yml`.
3. Execute o projeto com o Maven Wrapper:

   ```bash
   ./mvnw spring-boot:run
   ```

4. A aplicação estará disponível em `http://localhost:8080`.

---

### 4. Acessando a Documentação da API

A API está documentada com Swagger (OpenAPI 3.1.0). Após iniciar a aplicação, acesse:

```
http://localhost:8080/swagger-ui/index.html
```

Aqui você pode explorar todos os endpoints, testar requisições e ver exemplos de payloads.

---

### 5. Executando Testes Automatizados

Para rodar os testes unitários e de integração, execute:

```bash
./mvnw test
```

---

## Endpoints da API

A API está organizada em torno dos seguintes recursos:

### **1. Pautas**
- **POST `/api/v1/pauta`**: Cria uma nova pauta.
- **GET `/api/v1/pauta/{id}`**: Retorna os detalhes de uma pauta específica.
- **DELETE `/api/v1/pauta/{id}`**: Remove uma pauta pelo seu ID.
- **GET `/api/v1/resultado/{pautaId}`**: Retorna o resultado da votação de uma pauta.

### **2. Sessões de Votação**
- **POST `/api/v1/sessao`**: Abre uma nova sessão de votação para uma pauta.
- **GET `/api/v1/sessao/{id}`**: Retorna os detalhes de uma sessão de votação.

### **3. Votos**
- **POST `/api/v1/voto`**: Registra o voto de um associado em uma pauta.

### **4. Associados**
- **POST `/api/v1/associado`**: Cadastra um novo associado.
- **GET `/api/v1/associado/{id}`**: Retorna os detalhes de um associado.
- **DELETE `/api/v1/associado/{id}`**: Remove um associado pelo seu ID.

---

## Exemplos de Uso

### Criar uma Pauta
**Requisição**:
```bash
POST /api/v1/pauta
Content-Type: application/json

{
  "nome": "Nova Pauta",
  "descricao": "Descrição da pauta"
}
```

**Resposta**:
```json
{
  "id": 1,
  "nome": "Nova Pauta",
  "descricao": "Descrição da pauta"
}
```

### Abrir uma Sessão de Votação
**Requisição**:
```bash
POST /api/v1/sessao
Content-Type: application/json

{
  "pautaId": 1,
  "dataInicio": "2025-01-01T10:00:00Z",
  "dataFim": "2025-01-01T11:00:00Z"
}
```

**Resposta**:
```json
{
  "id": 1,
  "dataInicio": "2025-01-01T10:00:00Z",
  "dataFim": "2025-01-01T11:00:00Z",
  "status": "ABERTA"
}
```

### Registrar um Voto
**Requisição**:
```bash
POST /api/v1/voto
Content-Type: application/json

{
  "escolha": "SIM",
  "pautaId": 1,
  "associadoId": 1
}
```

**Resposta**:
```json
{
  "id": 1,
  "pauta": "Nova Pauta",
  "nomeAssociado": "João Silva"
}
```

---

## Funcionalidades Adicionais

- **Validação de CPF**: A API valida se o CPF do associado é válido antes de registrar o voto.
- **Versionamento**: A API está versionada usando o padrão `/api/v1/...`.

---