# Gerenciador de arquivos distribuídos

Desenvolvimento de gerenciador de arquivos em um ambiente distribuído. O gerenciador deve atuar como um gerenciador de arquivos convencional, limitadando suas ações em:

- Listar os arquivos da pasta atual;
- Criar novo arquivo: essa opção será simulada pela importação de um arquivo externo (de uma pasta local de seu HD) para o gerenciador de arquivos distribuídos;
- Renomear arquivo, e;
- Remover arquivo.

Os arquivos serão distribuídos entre os elementos (nós) ativos do sistema (não serão particionados).

##### O gerenciador será composto por dois elementos:
1. Aplicação: fornece a interface com o usuário, recebendo comandos e exibindo resultados (os comandos executam as ações listas acima), a solicitação é repassada ao middleware local para processamento;
2. Middleware: recebe comandos locais da aplicação, interagem com outros middlewares para processar requisição do usuário.

##### Requisitos para implementação do middleware:
- Exclusão mútua: ao selecionar um arquivo para remover ou renomear, outros acessos devem ser bloqueados;
- Distribuição homogênea de carga: volume de dados (arquivos) entre os elementos do sistema deve ser equilibrado
- Tolerância a falhas: implementar tolerância a falhas através de redundância (uma cópia extra). Implementar a detecção de elemento com falha;
- Considere os hosts não estando em rede local, ou seja, não há comunicação broadcast ou multicast entre o grupo de hosts;

##### Considere inicialmente a pasta vazia (sem arquivos) e os testes serão executados com 4 hosts.

##### Para compilar o projeto
1. javac *.java (nas 4 pastas)

##### Para rodar o projeto
1. java Server
2. java Client
3. Repetir o processo nas outras pastas
