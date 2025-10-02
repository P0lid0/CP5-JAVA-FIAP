# Biblioteca Comunitária (Spring Boot)

Aplicação web simples para gerenciar **livros**, **usuários** e **empréstimos**.

## Como rodar no IntelliJ
1. Abra o IntelliJ IDEA e escolha **Open**.
2. Selecione a pasta `biblioteca-spring-boot`.
3. Aguarde o Maven baixar as dependências.
4. Rode a classe `BibliotecaApplication` ou use `mvn spring-boot:run`.

A aplicação sobe em `http://localhost:8080`.

### H2 Console
- Acesse `http://localhost:8080/h2`
- JDBC URL: `jdbc:h2:mem:biblioteca` — usuário: `sa` — senha: *vazia*.

## Funcionalidades
- Livros: cadastrar (título obrigatório), editar e excluir. Começam como **DISPONIVEL**.
- Usuários: cadastrar (e-mail válido) e listar.
- Empréstimos: selecionar **usuário** e **livro disponível**, informar datas.
  - Validação: data prevista **deve ser posterior** à data de retirada.
  - Ao registrar, o livro passa para **EMPRESTADO**.
  - Ao devolver, o livro volta para **DISPONIVEL**.
- Listas:
  - Todos os livros e apenas disponíveis.
  - Empréstimos ativos (sem data de devolução).

> Projeto feito propositalmente **simples e direto** para fins didáticos.
