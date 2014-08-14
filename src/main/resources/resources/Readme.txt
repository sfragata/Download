Download

Aplica��o feita 100% em java que serve para efetuar o download de um ou mais arquivos.
Possui interface amig�vel, bastando poucas a��es para come�ar a baixar os arquivos.


Criando o arquivo de links: 
O arquivo de links pode ser criado pela interface, inserindo os links e diret�rios na lista e depois exportando, ou criando um arquivo texto no seguinte formato: 

[nome-do-diret�rio] - caso queira inserir um diret�rio, este deve vir entre colchetes ([]). 
links - um ou mais links. 

Vers�es: 

1.0 � 1.9 : 
	Inclus�o da altera��o do L&F. Adicionado novo l&F - kunststoff (http://www.incors.org). 
	Inclus�o do verificador de links, alterando o status para quebrado se o link n�o estiver dispon�vel. 
	Inserido o teste de mem�ria que a partir de um limiar, executa o GC. 
	Inclus�o do menu pop-up para permitir que insira, altere ou remova um link. 
	Inserida uma op��o para o arquivo de links ser exportado. 
	Os links que estiverem com erro, isto �, quebrados ser�o exportados com um serquilha (#) na frente para indicar que ele � um link quebrado no momento de abrir o arquivo. 
	Acrescentado o repetidor de links para facilitar a inser��o de links seq�enciais. 
	Adicionado o Getlinks para obter dinamicamente os links a partir de outro link. 
	Inclu�do a op��o pop-up de alterar o status do link, que pode ser: 
	BAIXADO  
	N�O-BAIXADO  
	QUEBRADO  
	DIRET�RIO  


2.0:(07/11/2002) 
	Altera��o da thread do garbage collector para um Timer. 
	Os links j� baixados ser�o exportados com um asterisco (*) na frente para indicar que ele j� foi baixado no momento da importa��o. 

2.1:(20/11/2002) 
	Inclus�o no sobre de 3 propriedades da jvm: 
	java.vm.vendor 
	java.vm.version 
	java.vm.name 
	Inclus�o do MemoryMonitor, que mostra o gr�fico do heap usado pela JVM. 
	Corre��o de formato e de precis�o nos campos de percentagem. 

2.2:(27/11/2002) 
	Inclus�o da propriedade na linha da comando para desativar a thread e o log para o GC, a propriedade � log.gc=false. 
	Ex.: java -Dlog.gc=false ...
	O arquivo de gc agora � montado com o nome da vers�o do java ( java.vm.version ). 
	Amplia��o da janela de Sobre... e mudan�a do tipo da fonte para Courier. 

2.3:(28/11/2002) 
	Altera��o de alguns nomes de menus. 
	Altera��o, inclus�o e remo��o de alguns hot-keys e mnemonics. 

2.4:(03/12/2002) 
	Inclus�o de efeito de clique nos bot�es. 
	Altera��o do deste arquivo para html. 
	Retirada das 3 propriedades da jvm especificadas na vers�o 2.1, por causa da altera��o deste documento para html. 

2.5:(05/12/2002) 
	Inclus�o de mnemonics nas op��es para inclus�o ou altera��o. 
	Retirada de espa�os em branco que forem inclu�dos nos campos de entrada de dados. 
	Resolver problema da lista �s vezes n�o aparecer. 

2.6:(16/12/2002) 
	Inclus�o do TabbedPane no sobre para que seja dividida a descri��o do programa das informa��es de sistema. 
	Inclus�o do acesso a hiperlinks no sobre. 
	Retorno das 3 propriedades da jvm e mais a indica��o se o log de GC est� ativado ou n�o. 

2.7:(20/12/2002) 
	Altera��o no formato do sobre na aba de informa��es. 
	Retirada da propriedade que exibia se o log de GC estava ativado ou n�o. 
	Inclus�o de outras propriedades da jvm como: 
	java.runtime.name. 
	java.vm.info. 

2.8:(22/12/2002) 
	Retirada do arquivo de log e da thread de GC que a depender de um limiar executava o System.gc(). 
	Inclus�o de uma barra de status que possui o link selecionado da lista e um Progress Bar indicando o uso e o total do heap da JVM.

2.9:(28/12/2002) 
	Arruma��o da barra de status para que todas as mensagens sejam inseridas nela. 
	Altera��o do �cone informando se estava conectado ou n�o. 
	Inclus�o de �cones correspondentes aos Look & Feel. 
	A janela agora � redimension�vel. 
	Inclus�o de ToolTip na imagem.

3.0:(05/05/2003) 
	Ap�s muitos meses sem altera��o(problemas no computador), foi feita uma total remodelagem. 

	Inclus�o de "abas" separando as urls das threads e logs. . 
	Os downloads s�o agora efetuados por 4 threads. 
	Na aba threads � mostrada o progresso do arquivo, s�o 4 progress bars. 
	Na aba logs s�o exibidos os logs de sa�da e de erro, s�o s�o mais gerados os arquivos de out.log e err.log.. 

3.1:(07/05/2003) 
	Retirada a imagem que informava que estava conectado, passando a ter uma imagem para cada thread . 
	Retidada a op��o de Parar, que servia para parar de efetuar o download, no lugar disso basta dar um clique duplo no �cone acima 
	descrito que ele ir� parar o download da thread clicada. 
	Quando � selecionado um arquivo atrav�s do menu Abrir, o caminho completo do arquivo � exibido na barra de t�tulo. 
	Ap�s usar a op��o Abrir, na pr�xima vez que esta op��o for usada ser� exibido o �ltimo diret�rio utilizado, assim como a 
	op��o Procurar. 
	Se um arquivo estiver aberto, quando solicitar a op��o Salvar, ir� para o diret�rio do arquivo aberto. 

3.2:(20/05/2003) 
	Inclus�o do popup Selecionar Tudo que seleciona todos os links exibidos. 
	Corre��o de problema quando removia um link e a sele��o sumia. Agora quando um �tem � removido, 
	a sele��o ir� para o pr�ximo link, se o link removido for o �ltimo, a sele��o ir� para o anterior. 

3.3:(27/08/2003) 
	Inclus�o de internacionaliza��o de mensagens. 
	Quando clica na imagem para desconectar a thread � encerrada e n�o mais espera o �ltimo link ser baixado. 

3.4:(05/09/2003) 
	Altera��o para inclus�o de �cones no lugar de cores na lista

3.5:(05/01/2004) 
	Altera��o para inclus�o da aba de configura��es de proxy, para o caso da internet estar sob um firewall. 

3.6:(09/02/2004) 
	Altera��o feita nos dialogs de inclus�o e altera��o para que quando uma op��o seja selecionada, 
	a imagem mude. 

3.7:(30/09/2004)
	Corre��o de textos. 
	Os links podem ser inseridos em uma determinada posi��o. 
	Inclus�o da aba de propriedades do sistema  
	
3.8:(01/11/2004)
	Quando a aplica��o for fechada, se existir algum link, ser� perguntado que deseja salvar os links. 
	Caso um arquivo de links j� esteja aberto, o arquivo ser� salvo sem perguntar nada. 

3.9:(18/07/2005)
	Refactoring interno, n�o afeta o usu�rio

Desenvolvido por Silvio Fragata da Silva.
