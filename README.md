# SalonTime (Kotlin + Spring Boot)

Aplicação backend do projeto SalonTime, desenvolvida em Kotlin com Spring Boot, responsável por gestão de agendamentos, serviços, usuários, cupons e integrações (e-mail, dashboards, etc.). Este repositório contém o código do servidor, testes e scripts de banco de dados.

## Visão Geral
 - **Linguagem:** Kotlin
 - **Framework:** Spring Boot
 - **Build:** Maven (mvnw incluso)
 - **Banco de Dados:** Configurável via `application.properties` e scripts SQL em `src/main/kotlin/sptech/salonTime/bd`
 - **Estrutura principal:** controladores (REST), serviços, repositórios, DTOs e mapeadores

## Requisitos
 - Java 17+ (JDK)
 - Maven (não é obrigatório, o wrapper `mvnw` já está no projeto)
 - Banco de dados compatível com os scripts SQL fornecidos (ex.: PostgreSQL ou MySQL; ajuste conforme sua configuração)

## Configuração
1. Configure o banco de dados:
	 - Scripts em [https://github.com/Grupo1-Semestre3/salontime-banco-dados]
2. Ajuste propriedades em `salonTime/src/main/resources/application.properties`:
	 - Exemplo (adapte conforme seu banco):
		 ```properties
		 spring.datasource.url=jdbc:postgresql://localhost:5432/salontime
		 spring.datasource.username=seu_usuario
		 spring.datasource.password=sua_senha
		 spring.jpa.hibernate.ddl-auto=none
		 spring.jpa.show-sql=true
		 spring.mail.host=smtp.seuprovedor.com
		 spring.mail.port=587
		 spring.mail.username=seu_email
		 spring.mail.password=sua_senha_email
		 spring.mail.properties.mail.smtp.auth=true
		 spring.mail.properties.mail.smtp.starttls.enable=true
		 ```

## Como Rodar
Dentro da pasta `salonTime/`:

```zsh
# Build completo
./mvnw clean package

# Executar a aplicação (Spring Boot)
./mvnw spring-boot:run

# Alternativa: executar o JAR gerado
java -jar target/salonTime-*.jar
```

Por padrão, o Spring Boot sobe em `http://localhost:8080`.

## Estrutura de Pastas
```
salonTime/
	pom.xml
	src/
		main/
			kotlin/sptech/salonTime/
				SalonTimeApplication.kt
				bd/
					criacao_bd.sql
					inserts.sql
				config/
					CorsConfig.kt
					MailConfig.kt
				controller/
					AgendamentoController.kt
					AiController.kt
					AvaliacaoController.kt
					CupomConfiguracaoController.kt
					CupomController.kt
					CupomDestinadoController.kt
					DashboardController.kt
					DescCancelamentoController.kt
					FuncionamentoController.kt
					FuncionarioCompetenciaController.kt
					HorarioExcecaoController.kt
					InfoSalaoController.kt
					PagamentoController.kt
					ServicoController.kt
					TipoUsuarioController.kt
					UsuariosController.kt
				dto/ ...
				entidade/ ...
				exception/ ...
				mapper/ ...
				repository/ ...
				service/ ...
			resources/
				application.properties
		test/
			kotlin/sptech/salonTime/
				Projeto04ApplicationTests.kt
				controller/ ...
				service/ ...
```

## Endpoints e APIs
Os controladores em `controller/` expõem as rotas principais. Exemplos comuns:
 - `AgendamentoController`: operações de agendamentos (listar/criar/cancelar)
 - `ServicoController`: listagem e gestão de serviços
 - `UsuariosController`: cadastro, autenticação e perfil de usuários
 - `DashboardController`: métricas e gráficos
 - `CupomController` e relacionados: cupons e regras
 - `InfoSalaoController`: informações gerais do salão

Se houver Swagger/OpenAPI configurado, estará disponível em `/swagger-ui` ou `/api-docs` (ajuste conforme sua configuração). Caso não esteja ativo, considere adicionar a dependência `springdoc-openapi` para documentação automática.

## Variáveis e Perfis
 - Propriedades principais em `application.properties`.
 - Para perfis (ex.: `dev`, `prod`), use `application-dev.properties` e rode com:
	 ```zsh
	 ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
	 ```

## Testes
Execute os testes com:
```zsh
./mvnw test
```
Resultados podem ser consultados em relatórios gerados (ex.: `Test Results - All_in_salonTime.html`).

## Desenvolvimento
 - CORS: configurado em `config/CorsConfig.kt`.
 - E-mail: configurado em `config/MailConfig.kt`.
 - DTOs e mapeamentos: `dto/` e `mapper/`.
 - Repositórios: `repository/` (Spring Data).
 - Serviços: `service/` com regras de negócio.

## Licença
Projeto acadêmico do Grupo 1 (3º semestre). Uso conforme diretrizes do curso.
.
