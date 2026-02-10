# 🚀 MPU API – Backend com Java & Spring Boot

Este projeto é uma **API REST em desenvolvimento**, criada com foco em **estudos, portfólio e boas práticas de backend**, utilizando Java e Spring Boot.

O objetivo não é apenas entregar funcionalidades, mas **aplicar conceitos de arquitetura, regras de domínio e organização de código**, simulando cenários reais de negócio.

---

## 🧠 Objetivos do projeto

* Consolidar conhecimentos em **Java + Spring Boot**
* Aplicar **Clean Code** e separação de responsabilidades
* Trabalhar com **fluxos reais de negócio** (usuários, produtos e pedidos)
* Criar um projeto evolutivo para **portfólio profissional**

---

## 🛠️ Tecnologias utilizadas

* **Java 17**
* **Spring Boot 3**
* **Spring Security + JWT**
* **Spring Data JPA (Hibernate)**
* **PostgreSQL**
* **Swagger / OpenAPI**
* **Maven**

---

## 📦 Funcionalidades implementadas

### 🔐 Autenticação e Segurança

* Autenticação com **JWT**
* Controle de acesso com **Spring Security**
* Endpoints protegidos por perfil

### 👤 Usuários

* Cadastro de usuários
* Autenticação/login
* Associação de roles

### 📦 Produtos

* CRUD de produtos
* Controle de estoque (`amount`)
* Identificação por **barcode (único)**

### 🧾 Pedidos (Orders)

* Criação de pedidos com múltiplos produtos
* Estrutura baseada em **Order** e **OrderItem**
* Cálculo de valor total do pedido
* Associação do pedido ao usuário autenticado

> 🔄 O fluxo de status do pedido (ex: `PENDING → PAID`) está em fase de implementação.

---

## 🧱 Modelagem principal

* **User** → realiza pedidos
* **Order** → representa o pedido
* **OrderItem** → itens do pedido
* **Product** → produtos disponíveis

Relacionamentos seguem boas práticas de banco de dados, utilizando **IDs internos** como chaves estrangeiras.

---

## 📚 Documentação da API

A API conta com **Swagger UI**, permitindo testar os endpoints diretamente pelo navegador.

Após subir a aplicação:

```
http://localhost:8080/swagger-ui.html
```

---

## ▶️ Como executar o projeto

### Pré-requisitos

* Java 17+
* Maven
* PostgreSQL

### Passos

1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/seu-repositorio.git
```

2. Configure o banco de dados no `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mpu_api
spring.datasource.username=postgres
spring.datasource.password=postgres
```

3. Execute a aplicação

```bash
mvn spring-boot:run
```

---

## 🧪 Testes

Os endpoints podem ser testados via:

* Swagger UI
* Postman / Insomnia

---

## 🚧 Próximos passos

* Implementar fluxo completo de status do pedido (`PENDING → PAID / CANCELED`)
* Criar relatórios de vendas
* Melhorar tratamento de exceções
* Adicionar testes unitários
* Simular integração com gateway de pagamento

---

## 🤝 Contribuições e feedback

Este é um projeto de estudo, então **feedbacks, sugestões e boas práticas são muito bem-vindos**.

Se quiser trocar ideias ou contribuir, fique à vontade!

---

## 👨‍💻 Autor

**Davi Huffenbaecher**
Projeto desenvolvido para fins de aprendizado e portfólio.
