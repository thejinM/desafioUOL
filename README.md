# desafioUOL
Primeiro desafio UOL.

O programa:
 Esse é um sistema básico de gerenciamento de uma loja virtual, desenvolvido em Java, que permite interações tanto para clientes quanto para vendedores. O sistema possibilita a manipulação de produtos no estoque, a criação de carrinhos de compras e a finalização de transações. Apesar de funcional, ainda apresenta alguns problemas em determinadas funcionalidades.
 Quando o programa é iniciado, o usuário deve escolher entre os dois perfis disponíveis: vendedor ou cliente. Após digitar a opção desejada, o sistema direciona para o menu correspondente.
 No caso do cliente, o programa solicita que o usuário insira seu nome, o que não interfere no programa. Em seguida, o sistema cria um carrinho para este cliente, permitindo que ele visualize os produtos disponíveis no estoque, adicione itens ao carrinho, atualize ou remova produtos e, por fim, finalize a compra. Durante a finalização, as quantidades de produtos no estoque são automaticamente ajustadas.
 Já para o vendedor, o sistema oferece opções de gerenciamento do estoque. O vendedor pode adicionar novos produtos, atualizar informações  de produtos existentes, remover itens ou listar todos os produtos disponíveis no sistema.

Estrutura do Repositório: 
 * src/application: Contém o arquivo principal que executa o programa.  
 * src/db: Contém a classe responsável pela configuração e gerenciamento da conexão com o banco de dados, incluindo os dados do meu banco de dados logo no início do arquivo, e também o arquivo com os comandos SQL usados para criar as tabelas necessárias no banco de dados.  
 * src/entities: Contém as classes principais do sistema, como carrinho e estoque.  
 * src/teste: Contém as exceções das classes.
 * DAO: Contém as classes que fazem o acesso ao banco de dados.  
 * lib: Contém o driver do banco de dados.

Bugs:
 Embora o programa esteja rodando, algumas falhas continuam acontecendo na classe responsável pelo gerenciamento do carrinho. O método de atualizar a quantidade de produtos no carrinho apresenta erros, assim como o método de listar os produtos, que pode não exibir os dados corretamente, pegando dados do estoque no lugar dos dados do carrinho. 
 Além disso, o método de remover produtos do carrinho também parece não estar atualizando corretamente os valores. Fiquei preso nesses erros por alguns dias e talvez possa ter misturado um pouco as lógicas do carrinho e estoque, deixando o carrinho bagunçado. Apesar disso, o estoque continua funcionando corretamente.

Repositório GitHub:
 Tive alguns problemas com o meu repositório original. No domingo à noite, foi necessário recriar o repositório mas por sorte, algumas versões anteriores do programa estavam salvas, permitindo que os commits fossem feitos no novo repositório, garantindo pelo menos um pouco a preservação do histórico de desenvolvimento.

Jean Michel Mallmann