Como executar o projecto

1)Preparar os run configurations dos três programas

1.1)--- Para o MsgFile ---
Program arguments:
<serverAddress> <username> <userPassword> <truststoreLocation>
exemplo para a truststore fornecido na pasta keystore:
127.0.0.1:23456 fernando pass .\keystore\myClient.keyStore

VM arguments:
-Djava.security.manager -Djava.security.policy=client.policy



1.2)--- Para o MsgFileServer ---
Program arguments:
<port> <keystoreLocation> <keystorePassword> <secKeyAlias> <secKeyPassword> <privPubAlias> <privPubPassword>
exemplo para a keystore fornecido na pasta keystore:
23456 .\keystore\myServer.keyStore batata secKey batata keyRSA batata

VM arguments:
-Djava.security.manager -Djava.security.policy=server.policy



1.3) --- Para o UserManager ---
Program arguments:
<keystoreLocation> <keystorePassword> <secKeyAlias> <secKeyPassword> <privPubAlias> <privPubPassword>
exemplo para a keystore fornecido na pasta keystore:
.\keystore\myServer.keyStore batata secKey batata keyRSA batata

VM arguments:
-Djava.security.manager -Djava.security.policy=userManager.policy


2)Correr o UserManager (gerenciador de users, somente este programa consegue criar e remover users)
2.1) criar os users desejados

3)Correr o MsgFileServer (server)

4)Correr o MsgFile (client)
4.1)passar nos args o user e pass que foram usados no passo 2.1

5)Usar o MsgFile para introduzir os comandos, usar "help"
para ver lista de comandos existentes no programa
