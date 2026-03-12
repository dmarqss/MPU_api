# 🚀 MPU API – Backend com Java & Spring Boot

Este projeto é uma **API REST em desenvolvimento**, criada com foco em **estudos, portfólio e boas práticas de backend**, utilizando Java e Spring Boot.

O objetivo não é apenas entregar funcionalidades, mas **aplicar conceitos de arquitetura, regras de domínio, mensageria e organização de código**, simulando cenários reais de negócio.

---

# 🧠 Objetivos do projeto

* Consolidar conhecimentos em **Java + Spring Boot**
* Aplicar **Clean Code** e separação de responsabilidades
* Trabalhar com **fluxos reais de negócio** (usuários, produtos, pedidos e pagamentos)
* Implementar **arquitetura orientada a eventos**
* Criar um projeto evolutivo para **portfólio profissional**

---

# 🛠️ Tecnologias utilizadas

* **Java 17**
* **Spring Boot 3**
* **Spring Security + JWT**
* **Spring Data JPA (Hibernate)**
* **PostgreSQL**
* **RabbitMQ**
* **JUnit 5**
* **Mockito**
* **Swagger / OpenAPI**
* **Maven**

---

# 📦 Funcionalidades implementadas

## 🔐 Autenticação e Segurança

* Autenticação com **JWT**
* Controle de acesso com **Spring Security**
* Endpoints protegidos por perfil
* Fluxo de **recuperação de senha**

---

## 👤 Usuários

* Cadastro de usuários
* Autenticação/login
* Associação de roles
* Solicitação de recuperação de senha

---

## 📦 Produtos

* **CRUD completo de produtos**
* Controle de estoque (`amount`)
* Identificação por **barcode (único)**

---

## 🧾 Pedidos (Orders)

* Criação de pedidos com múltiplos produtos
* Estrutura baseada em **Order** e **OrderItem**
* Cálculo automático do **valor total do pedido**
* Associação do pedido ao **usuário autenticado**

### Fluxo de status do pedido

Os pedidos seguem um **ciclo de vida de status**, simulando o fluxo real de uma compra:

```
PENDING → PAID → COMPLETED
       ↘
        CANCELED
```

Esse fluxo permite aplicar **regras de negócio claras na camada de serviço**, garantindo consistência na evolução do pedido.

---

# 📧 Sistema de Emails Assíncronos

A aplicação utiliza **RabbitMQ** para implementar um sistema de envio de emails **assíncrono e orientado a eventos**.

### Eventos implementados

* Solicitação de **reset de senha**
* **Confirmação de pagamento**
* **Falha no pagamento**

### Arquitetura de mensageria

* Exchanges, queues e bindings no **RabbitMQ**
* Comunicação baseada em **eventos**
* Serialização de mensagens em **JSON com Jackson**
* Uso de **DTOs de evento** para evitar envio de entidades JPA
* **Rabbit listeners** responsáveis por consumir eventos e enviar emails

### Benefícios

* Envio de email ocorre **fora da thread principal da requisição**
* Melhor **performance**
* Maior **escalabilidade**
* **Desacoplamento** entre regras de negócio e infraestrutura de email

---

# 🧪 Testes automatizados

Foram implementados **testes unitários utilizando JUnit 5 e Mockito** para validar as principais regras de negócio da aplicação.

### Módulos cobertos

* **User Service**
* **Product Service**
* **Payment Service**

### Abordagem dos testes

* Mock de dependências utilizando **Mockito**
* Validação de **cenários de sucesso**
* Testes para **lançamento de exceções**
* Organização seguindo o padrão **Given / When / Then**

Esses testes aumentam a confiabilidade do sistema e facilitam futuras refatorações.

---

# 🧱 Modelagem principal

Principais entidades do sistema:

* **User** → realiza pedidos
* **Order** → representa o pedido
* **OrderItem** → itens do pedido
* **Product** → produtos disponíveis
* **Payment** → processamento do pagamento

Os relacionamentos seguem boas práticas de banco de dados, utilizando **IDs internos como chaves estrangeiras**.

---

# 📚 Documentação da API

A API conta com **Swagger UI**, permitindo testar os endpoints diretamente pelo navegador.

Após subir a aplicação:

```
http://localhost:8080/swagger-ui.html
```

---

# ▶️ Como executar o projeto

## Pré-requisitos

* Java 17+
* Maven
* PostgreSQL
* RabbitMQ

---

## Passos

### 1. Clonar o repositório

```bash
git clone https://github.com/seu-usuario/seu-repositorio.git
```

---

### 2. Configurar o banco de dados no `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mpu_api
spring.datasource.username=postgres
spring.datasource.password=postgres
```

---

### 3. Configurar RabbitMQ

Exemplo de configuração:

```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

---

### 4. Executar a aplicação

```bash
mvn spring-boot:run
```

---

# 🧪 Testando a API

Os endpoints podem ser testados via:

* **Swagger UI**
* **Postman**
* **Insomnia**

Para rodar os testes unitários:

```bash
mvn test
```

---

# 🚧 Próximos passos

Melhorias planejadas para o projeto:

* Implementar **gateway de pagamento simulado**
* Adicionar **testes de integração**
* Melhorar **tratamento global de exceções**
* Aumentar **cobertura de testes**
* Implementar **cache (Redis)**
* Adicionar **monitoramento e métricas**

---

# 🤝 Contribuições e feedback

Este é um projeto de estudo, então **feedbacks, sugestões e boas práticas são muito bem-vindos**.

Se quiser trocar ideias ou contribuir, fique à vontade!

---

# 👨‍💻 Autor

**Davi Huffenbaecher**

Projeto desenvolvido para **aprendizado e portfólio profissional**, com foco em **Java, Spring Boot e arquiteturas backend escaláveis**.
