rm -R servidor1/*.class
rm -R servidor2/*.class
rm -R servidor3/*.class
rm -R servidor4/*.class

javac servidor1/*.java
javac servidor2/*.java
javac servidor3/*.java
javac servidor4/*.java

echo "- Trabalhar como uma pasta só
- Tratar erro de duplicidade de arquivos
- Consertar bug de comunicação server-server
- Balanceamento funcionando bem em qualquer cliente
- Renomear arquivo de qualquer servidor
- Excluir arquivo de qualquer servidor
- Listar a pasta de todos os servidores
- FEITO trocar setNewFile pra pegar arquivo de fora e trazer para a pasta com menor tamanho
- Arquivo de backup em um outro servidor (final -bkp por exemplo, ele nao deve ser listado"

pause