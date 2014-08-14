Download

Aplicação feita 100% em java que serve para efetuar o download de um ou mais arquivos.
Possui interface amigável, bastando poucas ações para começar a baixar os arquivos.


Criando o arquivo de links: 
O arquivo de links pode ser criado pela interface, inserindo os links e diretórios na lista e depois exportando, ou criando um arquivo texto no seguinte formato: 

[nome-do-diretório] - caso queira inserir um diretório, este deve vir entre colchetes ([]). 
links - um ou mais links. 

Versões: 

1.0 à 1.9 : 
	Inclusão da alteração do L&F. Adicionado novo l&F - kunststoff (http://www.incors.org). 
	Inclusão do verificador de links, alterando o status para quebrado se o link não estiver disponível. 
	Inserido o teste de memória que a partir de um limiar, executa o GC. 
	Inclusão do menu pop-up para permitir que insira, altere ou remova um link. 
	Inserida uma opção para o arquivo de links ser exportado. 
	Os links que estiverem com erro, isto é, quebrados serão exportados com um serquilha (#) na frente para indicar que ele é um link quebrado no momento de abrir o arquivo. 
	Acrescentado o repetidor de links para facilitar a inserção de links seqüenciais. 
	Adicionado o Getlinks para obter dinamicamente os links a partir de outro link. 
	Incluído a opção pop-up de alterar o status do link, que pode ser: 
	BAIXADO  
	NÃO-BAIXADO  
	QUEBRADO  
	DIRETÓRIO  


2.0:(07/11/2002) 
	Alteração da thread do garbage collector para um Timer. 
	Os links já baixados serão exportados com um asterisco (*) na frente para indicar que ele já foi baixado no momento da importação. 

2.1:(20/11/2002) 
	Inclusão no sobre de 3 propriedades da jvm: 
	java.vm.vendor 
	java.vm.version 
	java.vm.name 
	Inclusão do MemoryMonitor, que mostra o gráfico do heap usado pela JVM. 
	Correção de formato e de precisão nos campos de percentagem. 

2.2:(27/11/2002) 
	Inclusão da propriedade na linha da comando para desativar a thread e o log para o GC, a propriedade é log.gc=false. 
	Ex.: java -Dlog.gc=false ...
	O arquivo de gc agora é montado com o nome da versão do java ( java.vm.version ). 
	Ampliação da janela de Sobre... e mudança do tipo da fonte para Courier. 

2.3:(28/11/2002) 
	Alteração de alguns nomes de menus. 
	Alteração, inclusão e remoção de alguns hot-keys e mnemonics. 

2.4:(03/12/2002) 
	Inclusão de efeito de clique nos botões. 
	Alteração do deste arquivo para html. 
	Retirada das 3 propriedades da jvm especificadas na versão 2.1, por causa da alteração deste documento para html. 

2.5:(05/12/2002) 
	Inclusão de mnemonics nas opções para inclusão ou alteração. 
	Retirada de espaços em branco que forem incluídos nos campos de entrada de dados. 
	Resolver problema da lista às vezes não aparecer. 

2.6:(16/12/2002) 
	Inclusão do TabbedPane no sobre para que seja dividida a descrição do programa das informações de sistema. 
	Inclusão do acesso a hiperlinks no sobre. 
	Retorno das 3 propriedades da jvm e mais a indicação se o log de GC está ativado ou não. 

2.7:(20/12/2002) 
	Alteração no formato do sobre na aba de informações. 
	Retirada da propriedade que exibia se o log de GC estava ativado ou não. 
	Inclusão de outras propriedades da jvm como: 
	java.runtime.name. 
	java.vm.info. 

2.8:(22/12/2002) 
	Retirada do arquivo de log e da thread de GC que a depender de um limiar executava o System.gc(). 
	Inclusão de uma barra de status que possui o link selecionado da lista e um Progress Bar indicando o uso e o total do heap da JVM.

2.9:(28/12/2002) 
	Arrumação da barra de status para que todas as mensagens sejam inseridas nela. 
	Alteração do ícone informando se estava conectado ou não. 
	Inclusão de ícones correspondentes aos Look & Feel. 
	A janela agora é redimensionável. 
	Inclusão de ToolTip na imagem.

3.0:(05/05/2003) 
	Após muitos meses sem alteração(problemas no computador), foi feita uma total remodelagem. 

	Inclusão de "abas" separando as urls das threads e logs. . 
	Os downloads são agora efetuados por 4 threads. 
	Na aba threads é mostrada o progresso do arquivo, são 4 progress bars. 
	Na aba logs são exibidos os logs de saída e de erro, são são mais gerados os arquivos de out.log e err.log.. 

3.1:(07/05/2003) 
	Retirada a imagem que informava que estava conectado, passando a ter uma imagem para cada thread . 
	Retidada a opção de Parar, que servia para parar de efetuar o download, no lugar disso basta dar um clique duplo no ícone acima 
	descrito que ele irá parar o download da thread clicada. 
	Quando é selecionado um arquivo através do menu Abrir, o caminho completo do arquivo é exibido na barra de título. 
	Após usar a opção Abrir, na próxima vez que esta opção for usada será exibido o último diretório utilizado, assim como a 
	opção Procurar. 
	Se um arquivo estiver aberto, quando solicitar a opção Salvar, irá para o diretório do arquivo aberto. 

3.2:(20/05/2003) 
	Inclusão do popup Selecionar Tudo que seleciona todos os links exibidos. 
	Correção de problema quando removia um link e a seleção sumia. Agora quando um ítem é removido, 
	a seleção irá para o próximo link, se o link removido for o último, a seleção irá para o anterior. 

3.3:(27/08/2003) 
	Inclusão de internacionalização de mensagens. 
	Quando clica na imagem para desconectar a thread é encerrada e não mais espera o último link ser baixado. 

3.4:(05/09/2003) 
	Alteração para inclusão de ícones no lugar de cores na lista

3.5:(05/01/2004) 
	Alteração para inclusão da aba de configurações de proxy, para o caso da internet estar sob um firewall. 

3.6:(09/02/2004) 
	Alteração feita nos dialogs de inclusão e alteração para que quando uma opção seja selecionada, 
	a imagem mude. 

3.7:(30/09/2004)
	Correção de textos. 
	Os links podem ser inseridos em uma determinada posição. 
	Inclusão da aba de propriedades do sistema  
	
3.8:(01/11/2004)
	Quando a aplicação for fechada, se existir algum link, será perguntado que deseja salvar os links. 
	Caso um arquivo de links já esteja aberto, o arquivo será salvo sem perguntar nada. 

3.9:(18/07/2005)
	Refactoring interno, não afeta o usuário

Desenvolvido por Silvio Fragata da Silva.
